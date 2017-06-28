import GamePalitos.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.util.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends GamePalitosServerPOA {
  	private ArrayList<String> clientesNojogo = new ArrayList<String>();
    private ArrayList<String> vencedoresEmOrdem = new ArrayList<String>();
    private ArrayList<Integer> palpitesDaRodada = new ArrayList<Integer>();
    private Map <String, GamePalitosCliente> clientesRef = new HashMap<String, GamePalitosCliente>();
    private Map <String, Integer> clientesPalpites = new HashMap<String, Integer>();
    private String nomeServidor;
    private NamingContext naming;
    private int maximoClientes = 4;
    private int somaPalitos = 0;
    private String clienteTurno;
    private String ultimoClienteJogada;
    private String vencedor;
    private String perdedor;

    public Server(String args[]){
        initServer(args);
    }

    public void registraCliente(String nomeCliente){
        clientesNojogo.add(nomeCliente);
        int numeroClientes = clientesNojogo.size();
        if(numeroClientes <= this.maximoClientes){
            try{
                referenciaCliente(nomeCliente);
            }catch (Exception e) {
                System.out.println("Erro : " + e) ;
                e.printStackTrace(System.out);
            }
            System.out.println("Clinte " + nomeCliente + " registrado com sucesso!");
            switch (numeroClientes) {
                case 1:
                    this.clienteTurno = nomeCliente;
                    // mensagemBroadCastNoJogo("Aguardando mais 3 jogadores na sala...");
                    break;
                case 2:
                    // mensagemBroadCastNoJogo("Aguardando mais 2 jogadores na sala...");
                    break;
                case 3:
                    // mensagemBroadCastNoJogo("Aguardando mais 1 jogador na sala...");
                    break;
                case 4:
                    mensagemBroadCastNoJogo("Finalmente 4 jogadores, o jogo será iniciado...");
                    this.ultimoClienteJogada = nomeCliente;
                    init();
                    break;
            }
        }
    };

    public boolean verificaEstaNoJogo(String nomeCliente){
        if(this.clientesNojogo.contains(nomeCliente)){
            return true;
        }else{
            return false;
        }
    };

    //boradcast para todos os jogadores no jogo
    public void mensagemBroadCastNoJogo(String mensagem){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if(verificaEstaNoJogo(cliente.getKey())){
                cliente.getValue().novaMensagem(mensagem);
            }
        }
    };

    //boradcast para todos os jogadores, que estejam ainda jogando ou não
    public void mensagemBroadCast(String mensagem){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            cliente.getValue().novaMensagem(mensagem);
        }
    };

    public void pedePalitos(){
        mensagemBroadCastNoJogo("");
        mensagemBroadCastNoJogo("***************************************");
        mensagemBroadCastNoJogo("***** ESCOLHA O NÚMERO DE PALITOS *****");
        mensagemBroadCastNoJogo("***************************************");
        mensagemBroadCastNoJogo("");
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if(verificaEstaNoJogo(cliente.getKey())){
                cliente.getValue().escolhePalitos();
            }
        }
    };

    //Quando o cliente fizer um palpite o servidor faz a soma acumulada
    //Lembrando que apenas farão os palpites os jogadores que estão no jogo
    public void somaPalitos(int palitos){
        this.somaPalitos = this.somaPalitos + palitos;
        System.out.println("Número de palitos acumulados na rodada: " + this.somaPalitos);
    };


    public void pedePalpite(String clienteTurno){
        System.out.println("Foi pedido o palpite do cliente " + clienteTurno);
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if(cliente.getKey().equals(this.clienteTurno)){
                if(verificaEstaNoJogo(cliente.getKey())){
                    cliente.getValue().novaMensagem("");
                    cliente.getValue().novaMensagem("***************************************");
                    cliente.getValue().novaMensagem("*** QUAL SEU PALPITE PARA A RODADA? ***");
                    cliente.getValue().novaMensagem("***************************************");
                    cliente.getValue().novaMensagem("");
                    cliente.getValue().escolhePalpite();
                }
            }
        }
    };

    //recebe os palpites, caso seja o último começa a verificação, caso não
    //pede o palpite do próximo
    public void recebePalpite(String nomeCliente, int palpite){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if (cliente.getKey().equals(nomeCliente.trim())) {
                this.clientesPalpites.put(nomeCliente,palpite);
                this.palpitesDaRodada.add(palpite);
                //Se o cliente foi o último a dar palpite, começa a verificação dos palpites
                if(nomeCliente.trim().equals(this.ultimoClienteJogada)){
                    alteraTurno();
                    verificaPalpites();
                    break;
                }else{
                    //Se não for último jogador altera o turno e pede um novo palpite
                    alteraTurno();
                    pedePalpite(this.clienteTurno);
                }
            }
        }
    };

    public boolean palpiteValido(int palpite){
        if(this.palpitesDaRodada.contains(palpite)){
            return false;
        }else{
            return true;
        }
    };

    public void verificaPalpites(){
        mensagemBroadCastNoJogo("");
        mensagemBroadCastNoJogo("***************************************");
        mensagemBroadCastNoJogo("****** ATUALIZAÇÃO DA ÚLTIMA RODADA****");
        mensagemBroadCastNoJogo("***************************************");
        mensagemBroadCastNoJogo("");
        mensagemBroadCastNoJogo("O número total acumulado foi: " + this.somaPalitos);
        for (Map.Entry <String, Integer> palpiteRodada : clientesPalpites.entrySet()) {
            if (palpiteRodada.getValue().equals(this.somaPalitos)) {
                mensagemBroadCastNoJogo("");
                mensagemBroadCastNoJogo("O jogador " + palpiteRodada.getKey() + " acertou o palpite e venceu a rodada (1 palito a menos)");
                mensagemBroadCastNoJogo("");
                System.out.println("Palpite do cliente " + palpiteRodada.getKey() + " está correto");
                retiraUmPalito(palpiteRodada.getKey());
                verificaVenceu(palpiteRodada.getKey());
            }else{
                System.out.println("Palpite do cliente " + palpiteRodada.getKey() + " está errado");
            }
            mensagemBroadCastNoJogo("Palpite do jogador " + palpiteRodada.getKey() + " foi: " + palpiteRodada.getValue() + " | Quantidade de palitos: " + buscaQuantidadePalitos(palpiteRodada.getKey()));
        }
        if(this.clientesNojogo.size() == 1){
            this.vencedoresEmOrdem.add(this.clientesNojogo.get(0));
            this.clientesNojogo.clear();
            lancaFim();
        }else{
            novaRodada();
        }
    };

    public void retiraUmPalito(String nomeCliente){
        System.out.println("Cliente acertou, vamos retirar um palito");
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if(cliente.getKey().equals(nomeCliente)){
                System.out.println("Removeu palito do " + nomeCliente);
                cliente.getValue().removePalito();
            }
        }
    }

    public int buscaQuantidadePalitos(String nomeCliente){
        int palitos = 3;
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if(cliente.getKey().equals(nomeCliente)){
                palitos = cliente.getValue().getPalitos();
                break;
            }
        }
        return palitos;
    }

    public void verificaVenceu(String nomeCliente){
        //aqui eu pego os palitos do cliente, se for 0
        //retira ele do array de jogadores em jogo
        //coloca ele no array de vencedores em ordem
        int numPalitos = 3;
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if(cliente.getKey().equals(nomeCliente)){
                numPalitos = cliente.getValue().getPalitos();
                break;
            }
        }

        if(numPalitos == 0){
            mensagemBroadCastNoJogo("-------");
            mensagemBroadCastNoJogo("ATENÇÃO: O jogador " + nomeCliente + " venceu e saiu do jogo");
            mensagemBroadCastNoJogo("-------");

            for(int i=0;i<=this.clientesNojogo.size();i++){
                if(this.clientesNojogo.get(i).equals(nomeCliente)){
                    if(this.clientesNojogo.size() > 1){
                        if(this.clienteTurno.equals(nomeCliente)){
                            this.clienteTurno = this.clientesNojogo.get(i+1);
                        }
                        if(this.ultimoClienteJogada.equals(nomeCliente)){
                            this.ultimoClienteJogada = this.clientesNojogo.get(this.clientesNojogo.size() - 1);
                        }
                    }
                    this.vencedoresEmOrdem.add(nomeCliente);
                    this.clientesNojogo.remove(i);
                    break;
                }
            }
        }
    }

    public void lancaFim(){
          mensagemBroadCast("");
          mensagemBroadCast("***************************************");
          mensagemBroadCast("************ FIM DE PARTIDA ***********");
          mensagemBroadCast("***************************************");
          mensagemBroadCast("");
          mensagemBroadCast("Colocação em ordem: ");
          mensagemBroadCast("1. " + this.vencedoresEmOrdem.get(0) + "(grande vencedor)");
          mensagemBroadCast("2. " + this.vencedoresEmOrdem.get(1));
          mensagemBroadCast("3. " + this.vencedoresEmOrdem.get(2));
          mensagemBroadCast("4. " + this.vencedoresEmOrdem.get(3) + "(grande perdedor)");
    }

    public void novaRodada(){
        mensagemBroadCastNoJogo("");
        mensagemBroadCastNoJogo("***************************************");
        mensagemBroadCastNoJogo("************* NOVA RODADA *************");
        mensagemBroadCastNoJogo("***************************************");
        mensagemBroadCastNoJogo("");
        reiniciaValores();
        init();
    }

    public void init(){
        pedePalitos();
        pedePalpite(this.clienteTurno);
    };

    //altera o turno para o próximo da lista dos clientes que estão no jogo
    public void alteraTurno(){
        if(this.clienteTurno == this.ultimoClienteJogada){
            this.clienteTurno = clientesNojogo.get(0);
        }else{
            for(int i=0;i<=clientesNojogo.size();i++){
                if(this.clienteTurno == clientesNojogo.get(i)){
                    this.clienteTurno = clientesNojogo.get(i+1);
                    System.out.println("chegou aqui, novo turno: " + this.clienteTurno);
                    break;
                }
            }
        }
    };

    public void reiniciaValores(){
        this.somaPalitos = 0;
        clientesPalpites.clear();
        this.palpitesDaRodada.clear();
    };

    //Pega a referência do cliente no sevidor de nomes e salva a referência num ArrayList
    public void referenciaCliente(String nomeCliente) throws Exception{
        org.omg.CORBA.Object clienteRef = this.naming.resolve(new NameComponent[]{new NameComponent(nomeCliente, "")});
        this.clientesRef.put(nomeCliente,GamePalitosClienteHelper.narrow(clienteRef));
    };


    public void initServer(String args[]){
        try{
            Scanner entrada = new Scanner(System.in);
            System.out.println("Escolha o nome do servidor:");
            this.nomeServidor = entrada.next();

            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objPoa = orb.resolve_initial_references("RootPOA");
            org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");

            POA rootPOA = POAHelper.narrow(objPoa);
            this.naming = NamingContextHelper.narrow(obj);

            org.omg.CORBA.Object objRef = rootPOA.servant_to_reference(this);
            this.naming.rebind(new NameComponent[]{new NameComponent(this.nomeServidor, "")}, objRef);

            rootPOA.the_POAManager().activate();
            System.out.println("Servidor " + this.nomeServidor + " rodando...");
            orb.run();
        }catch (Exception e) {
          	System.out.println("Erro : " + e) ;
        	  e.printStackTrace(System.out);
        }
    };

    public static void main(String args[]){
        new Server(args);
    }
}
