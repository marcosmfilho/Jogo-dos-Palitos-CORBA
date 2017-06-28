package GamePalitos;


/**
* GamePalitos/GamePalitosServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Game.idl
* Quarta-feira, 28 de Junho de 2017 03h25min38s BRT
*/

public abstract class GamePalitosServerPOA extends org.omg.PortableServer.Servant
 implements GamePalitos.GamePalitosServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("registraCliente", new java.lang.Integer (0));
    _methods.put ("mensagemBroadCast", new java.lang.Integer (1));
    _methods.put ("mensagemBroadCastNoJogo", new java.lang.Integer (2));
    _methods.put ("pedePalitos", new java.lang.Integer (3));
    _methods.put ("pedePalpite", new java.lang.Integer (4));
    _methods.put ("recebePalpite", new java.lang.Integer (5));
    _methods.put ("verificaPalpites", new java.lang.Integer (6));
    _methods.put ("somaPalitos", new java.lang.Integer (7));
    _methods.put ("alteraTurno", new java.lang.Integer (8));
    _methods.put ("reiniciaValores", new java.lang.Integer (9));
    _methods.put ("retiraUmPalito", new java.lang.Integer (10));
    _methods.put ("verificaVenceu", new java.lang.Integer (11));
    _methods.put ("buscaQuantidadePalitos", new java.lang.Integer (12));
    _methods.put ("verificaEstaNoJogo", new java.lang.Integer (13));
    _methods.put ("palpiteValido", new java.lang.Integer (14));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // GamePalitos/GamePalitosServer/registraCliente
       {
         String nomeCliente = in.read_string ();
         this.registraCliente (nomeCliente);
         out = $rh.createReply();
         break;
       }

       case 1:  // GamePalitos/GamePalitosServer/mensagemBroadCast
       {
         String mensagem = in.read_string ();
         this.mensagemBroadCast (mensagem);
         out = $rh.createReply();
         break;
       }

       case 2:  // GamePalitos/GamePalitosServer/mensagemBroadCastNoJogo
       {
         String mensagem = in.read_string ();
         this.mensagemBroadCastNoJogo (mensagem);
         out = $rh.createReply();
         break;
       }

       case 3:  // GamePalitos/GamePalitosServer/pedePalitos
       {
         this.pedePalitos ();
         out = $rh.createReply();
         break;
       }

       case 4:  // GamePalitos/GamePalitosServer/pedePalpite
       {
         String nomeCliente = in.read_string ();
         this.pedePalpite (nomeCliente);
         out = $rh.createReply();
         break;
       }

       case 5:  // GamePalitos/GamePalitosServer/recebePalpite
       {
         String nomeCliente = in.read_string ();
         int palitos = in.read_long ();
         this.recebePalpite (nomeCliente, palitos);
         out = $rh.createReply();
         break;
       }

       case 6:  // GamePalitos/GamePalitosServer/verificaPalpites
       {
         this.verificaPalpites ();
         out = $rh.createReply();
         break;
       }

       case 7:  // GamePalitos/GamePalitosServer/somaPalitos
       {
         int palitos = in.read_long ();
         this.somaPalitos (palitos);
         out = $rh.createReply();
         break;
       }

       case 8:  // GamePalitos/GamePalitosServer/alteraTurno
       {
         this.alteraTurno ();
         out = $rh.createReply();
         break;
       }

       case 9:  // GamePalitos/GamePalitosServer/reiniciaValores
       {
         this.reiniciaValores ();
         out = $rh.createReply();
         break;
       }

       case 10:  // GamePalitos/GamePalitosServer/retiraUmPalito
       {
         String nomeCliente = in.read_string ();
         this.retiraUmPalito (nomeCliente);
         out = $rh.createReply();
         break;
       }

       case 11:  // GamePalitos/GamePalitosServer/verificaVenceu
       {
         String nomeCliente = in.read_string ();
         this.verificaVenceu (nomeCliente);
         out = $rh.createReply();
         break;
       }

       case 12:  // GamePalitos/GamePalitosServer/buscaQuantidadePalitos
       {
         String nomeCliente = in.read_string ();
         int $result = (int)0;
         $result = this.buscaQuantidadePalitos (nomeCliente);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 13:  // GamePalitos/GamePalitosServer/verificaEstaNoJogo
       {
         String nomeCliente = in.read_string ();
         boolean $result = false;
         $result = this.verificaEstaNoJogo (nomeCliente);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 14:  // GamePalitos/GamePalitosServer/palpiteValido
       {
         int palpite = in.read_long ();
         boolean $result = false;
         $result = this.palpiteValido (palpite);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:GamePalitos/GamePalitosServer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public GamePalitosServer _this() 
  {
    return GamePalitosServerHelper.narrow(
    super._this_object());
  }

  public GamePalitosServer _this(org.omg.CORBA.ORB orb) 
  {
    return GamePalitosServerHelper.narrow(
    super._this_object(orb));
  }


} // class GamePalitosServerPOA
