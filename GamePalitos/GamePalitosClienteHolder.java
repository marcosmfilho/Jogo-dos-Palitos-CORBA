package GamePalitos;

/**
* GamePalitos/GamePalitosClienteHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Game.idl
* Quarta-feira, 28 de Junho de 2017 03h25min38s BRT
*/

public final class GamePalitosClienteHolder implements org.omg.CORBA.portable.Streamable
{
  public GamePalitos.GamePalitosCliente value = null;

  public GamePalitosClienteHolder ()
  {
  }

  public GamePalitosClienteHolder (GamePalitos.GamePalitosCliente initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = GamePalitos.GamePalitosClienteHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    GamePalitos.GamePalitosClienteHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return GamePalitos.GamePalitosClienteHelper.type ();
  }

}
