import GamePalitos.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends GamePalitosServerPOA {
  	private ArrayList<String> clientesNojogo = new ArrayList<String>();
    private ArrayList<String> vencedoresEmOrdem = new ArrayList<String>();
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
        int numeroClientes = clientes.size();
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
                    mensagemBroadCast("Aguardando mais 3 jogadores na sala...");
                    break;
                case 2:
                    mensagemBroadCast("Aguardando mais 2 jogadores na sala...");
                    break;
                case 3:
                    mensagemBroadCast("Aguardando mais 1 jogador na sala...");
                    break;
                case 4:
                    mensagemBroadCast("Finalmente 4 jogadores, o jogo será iniciado...");
                    mensagemBroadCast("Quantos palitos tem na sua mão direita?");
                    this.ultimoClienteJogada = nomeCliente;
                    init();
                    break;
            }
        }
    };

    public void mensagemBroadCast(String mensagem){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            cliente.getValue().novaMensagem(mensagem);
        }
    };

    public void pedePalitos(){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            cliente.getValue().pedePalitos();
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
            if(cliente.getKey() == this.clienteTurno){
                cliente.getValue().pedePalitos();
            }
        }
    };

    //recebe os palpites, caso seja o último começa a verificação, caso não
    //pede o palpite do próximo
    public void recebePalpite(String nomeCliente, int palpite){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if (cliente.getKey() == nomeCliente) {
                this.clientesPalpites.put(nomeCliente,palpite);
                //Se o cliente foi o último a dar palpite, começa a verificação dos palpites
                if(nomeCliente == this.ultimoClienteJogada){
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

    public void verificaPalpites(){
        mensagemBroadCast("O número total acumulado foi: " + this.somaPalitos);
        for (Map.Entry <String, Integer> palpiteRodada : clientesPalpites.entrySet()) {
            mensagemBroadCast("Palpite do jogador " + palpiteRodada.getKey() + " foi: " + palpiteRodada.getValue());
            if (palpiteRodada.getValue() == this.somaPalitos) {
                System.out.println("Palpite do cliente " + palpiteRodada.getKey() + " está correto");
                //Aqui eu retiro um palito do cliente
                //Verifico se alguém zerou os palitos
                //Se sim, remove do array de jogadores no jogo (se for o ultimo, atualiza o ultimo jogador, se for do turno também)
                //Se não, continua o jogo
            }else{
                System.out.println("Palpite do cliente " + palpiteRodada.getKey() + " está errado");
            }
        }
        System.out.println("Chegando aqui estou satisfeito até agora");
        System.out.println("Todos os jogadores deram os palpites de um por um pelo turno");
    };

    //altera o turno para o próximo da lista dos clientes que estão no jogo
    public void alteraTurno(){
        if(this.clienteTurno == this.ultimoClienteJogada){
            this.clienteTurno = clientesNojogo.get(0);
        }else{
            for(int i=0;i<=clientesNojogo.size();i++){
                if(this.clienteTurno == clientesNojogo.get(i)){
                    this.clienteTurno = clientesNojogo.get(i+1);
                    break;
                }
            }
        }
    };

    public void reiniciaValores(){
        this.somaPalitos = 0;
        clientesPalpites.clear();
    };

    //Pega a referência do cliente no sevidor de nomes e salva a referência num ArrayList
    public void referenciaCliente(String nomeCliente) throws Exception{
        org.omg.CORBA.Object clienteRef = this.naming.resolve(new NameComponent[]{new NameComponent(nomeCliente, "")});
        this.clientesRef.put(nomeCliente,GamePalitosClienteHelper.narrow(clienteRef));
    };

    public void init(){
        pedePalitos();
        pedePalpite(this.clienteTurno);
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
