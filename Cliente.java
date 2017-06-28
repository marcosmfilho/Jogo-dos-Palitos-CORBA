import GamePalitos.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.util.Scanner;

public class Cliente extends GamePalitosClientePOA{

  private String nomeCliente;
  private int palitos;
  private int palpite;
  private int resultadoRodada;
  private GamePalitosServer server;
  private NamingContext naming;
  private POA rootPOA;
  private ORB orb;

  public Cliente(String args[]){
      initCliente(args);
  }

  // registrar referrência do objeto cliente no POA e no servidor de nomes
  public void novoCliente(String nome) throws Exception{
      this.nomeCliente = nome;
      this.palitos = 3;
      org.omg.CORBA.Object clienteRef = this.rootPOA.servant_to_reference(this);
      this.naming.rebind(new NameComponent[]{new NameComponent(nome, "")}, clienteRef);
  };

  // busca a referência do servidor no servidor de nomes
  public void buscaServidor(String nomeServidor) throws Exception{
      org.omg.CORBA.Object serverRef = this.naming.resolve(new NameComponent[]{new NameComponent(nomeServidor, "")});
      this.server = GamePalitosServerHelper.narrow(serverRef);
  };

  // Registra a referência do cliente no Servidor e inicia o ORB
  public void inicializaConexao() throws Exception{
      this.rootPOA.the_POAManager().activate();
      System.out.println("");
      System.out.println("***************************************");
      System.out.println("** CONEXÃO ESTABELECIDA COM SUCESSO ***");
      System.out.println("***************************************");
      System.out.println("");
      this.server.registraCliente(this.nomeCliente);
      this.orb.run();
  };

  public void novaMensagem(String mensagem){
      System.out.println(mensagem);
  };

  public void escolhePalitos(){
      Scanner entradaPalitos = new Scanner(System.in);
      int palitos = entradaPalitos.nextInt();
      if(palitos < 0 || palitos > this.palitos){
          System.out.println("Valor inválido, seu número de palitos é " + this.palitos);
          System.out.print("Escolha a quantidade de palitos novamente: ");
          escolhePalitos();
      }else{
          this.server.somaPalitos(palitos);
      }
  };

  public void removePalito(){
      this.palitos--;
  }

  public int getPalitos(){
      return this.palitos;
  }

  public void escolhePalpite(){
      System.out.println("");
      System.out.println("***************************************");
      System.out.println("*** QUAL SEU PALPITE PARA A RODADA? ***");
      System.out.println("***************************************");
      System.out.println("");
      Scanner entradaPalpite = new Scanner(System.in);
      int palpite = entradaPalpite.nextInt();
      this.server.recebePalpite(this.nomeCliente,palpite);
  };

  // inicializa o cliente e conecta ao servidor
  public void initCliente(String args[]){
      //Inicializa o ORB e crias as referências para o POA e o servidor de nomes
      try{
          this.orb = ORB.init(args,null);
          org.omg.CORBA.Object objPoa = orb.resolve_initial_references("RootPOA");
          this.rootPOA = POAHelper.narrow(objPoa);
          org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");
          this.naming = NamingContextHelper.narrow(obj);

          //Cria um novo cliente com dados de entrada
          Scanner entradaNome = new Scanner(System.in);
          System.out.println("Qual o seu nome?");
          String nomeEntrada = entradaNome.next();
          novoCliente(nomeEntrada);

          //Busca o servidor pelo nome
          Scanner entradaServidor = new Scanner(System.in);
          System.out.println("Qual o nome do servidor?");
          String nomeServidor = entradaServidor.next();
          buscaServidor(nomeServidor);
          inicializaConexao();

      } catch(Exception e) {
        e.printStackTrace();
      }
  };

  public static void main(String args[]){
      new Cliente(args);
  }

}
