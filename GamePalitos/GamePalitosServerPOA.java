package GamePalitos;


/**
* GamePalitos/GamePalitosServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Game.idl
* Terça-feira, 27 de Junho de 2017 09h17min28s BRT
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
    _methods.put ("pedePalitos", new java.lang.Integer (2));
    _methods.put ("pedePalpite", new java.lang.Integer (3));
    _methods.put ("verificaPalpites", new java.lang.Integer (4));
    _methods.put ("somaPalitos", new java.lang.Integer (5));
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

       case 2:  // GamePalitos/GamePalitosServer/pedePalitos
       {
         this.pedePalitos ();
         out = $rh.createReply();
         break;
       }

       case 3:  // GamePalitos/GamePalitosServer/pedePalpite
       {
         this.pedePalpite ();
         out = $rh.createReply();
         break;
       }

       case 4:  // GamePalitos/GamePalitosServer/verificaPalpites
       {
         this.verificaPalpites ();
         out = $rh.createReply();
         break;
       }

       case 5:  // GamePalitos/GamePalitosServer/somaPalitos
       {
         int palitos = in.read_long ();
         this.somaPalitos (palitos);
         out = $rh.createReply();
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
