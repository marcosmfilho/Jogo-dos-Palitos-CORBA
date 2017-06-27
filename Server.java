import GamePalitos.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends GamePalitosServerPOA {
  	private ArrayList<String> clientes = new ArrayList<String>();
    private Map <String, GamePalitosCliente> clientesRef = new HashMap<String, GamePalitosCliente>();
    private Map <String, Integer> clientesPalpites = new HashMap<String, Integer>();
    private String nomeServidor;
    private NamingContext naming;
    private int maximoClientes = 4;
    private int somaPalitos = 0;
    private int controleEscolhaPalitos = 0;
    private String vencedor;
    private String perdedor;

    public Server(String args[]){
        initServer(args);
    }

    public void registraCliente(String nomeCliente){
        clientes.add(nomeCliente);
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
                    init();
                    break;
            }
        }
    };

    public void mensagemBroadCast(String mensagem){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            cliente.getValue().novaMensagem(mensagem);
        }
    }

    public void pedePalitos(){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            cliente.getValue().pedePalitos();
        }
    }

    public void somaPalitos(int palitos){
        this.controleEscolhaPalitos++;
        this.somaPalitos = this.somaPalitos + palitos;
        System.out.println("Número de palitos acumulados na rodada: " + this.somaPalitos);
    }

    public void pedePalpite(){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            cliente.getValue().pedePalitos();
        }
    }

    public void recebePalpite(String nomeCliente, int palpite){
        for (Map.Entry <String, GamePalitosCliente> cliente : clientesRef.entrySet()) {
            if (cliente.getKey() == nomeCliente) {
                this.clientesPalpites.put(nomeCliente,palpite);
            }
        }
    }

    public void verificaPalpites(){
        for (Map.Entry <String, Integer> palpiteRodada : clientesPalpites.entrySet()) {
            if (clientesPalpites.getValue() == this.somaPalitos) {
                System.out.println("Palpite do cliente " + clientesPalpites.getKey() + " está correto");
                //Aqui eu retiro um palito do cliente
                //Verifico se alguém zerou os palitos
                //Se sim, remove do array
            }else{
                System.out.println("Palpite do cliente " + clientesPalpites.getKey() + " está errado");
            }
        }
    }

    public void reiniciaValores(){
        this.somaPalitos = 0;
        clientesPalpites.clear();
    }

    //Pega a referência do cliente no sevidor de nomes e salva a referência num ArrayList
    public void referenciaCliente(String nomeCliente) throws Exception{
        org.omg.CORBA.Object clienteRef = this.naming.resolve(new NameComponent[]{new NameComponent(nomeCliente, "")});
        this.clientesRef.put(nomeCliente,GamePalitosClienteHelper.narrow(clienteRef));
    };

    public void init(){
        //  Cliente escolhe a quantidade de palitos
        //  Servidor calcula a soma
        //  Servidor pede os palpites
        //  Clientes mandam os palpites
        //  Verifica se o jogo acabou, ou seja, se só sobrou um jogador
        //  Se acabou, mostra o vencedor e o perdedor
        //  Se o jogo não acabou e algum cliente acertar seta os palitos dele com -1
        //  Volta para a escolha dos palitos
        while(clientes.size() != 1){
            pedePalitos();
            pedePalpite();
            System.out.println("Chegamos aqui depois dos palpites");
        }
    }

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
