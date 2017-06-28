package GamePalitos;


/**
* GamePalitos/GamePalitosClienteHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Game.idl
* Quarta-feira, 28 de Junho de 2017 10h57min55s BRT
*/

abstract public class GamePalitosClienteHelper
{
  private static String  _id = "IDL:GamePalitos/GamePalitosCliente:1.0";

  public static void insert (org.omg.CORBA.Any a, GamePalitos.GamePalitosCliente that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static GamePalitos.GamePalitosCliente extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (GamePalitos.GamePalitosClienteHelper.id (), "GamePalitosCliente");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static GamePalitos.GamePalitosCliente read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_GamePalitosClienteStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, GamePalitos.GamePalitosCliente value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static GamePalitos.GamePalitosCliente narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof GamePalitos.GamePalitosCliente)
      return (GamePalitos.GamePalitosCliente)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      GamePalitos._GamePalitosClienteStub stub = new GamePalitos._GamePalitosClienteStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static GamePalitos.GamePalitosCliente unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof GamePalitos.GamePalitosCliente)
      return (GamePalitos.GamePalitosCliente)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      GamePalitos._GamePalitosClienteStub stub = new GamePalitos._GamePalitosClienteStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
