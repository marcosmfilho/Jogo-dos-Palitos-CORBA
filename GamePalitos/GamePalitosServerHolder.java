package GamePalitos;

/**
* GamePalitos/GamePalitosServerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Game.idl
* Terça-feira, 27 de Junho de 2017 09h17min28s BRT
*/

public final class GamePalitosServerHolder implements org.omg.CORBA.portable.Streamable
{
  public GamePalitos.GamePalitosServer value = null;

  public GamePalitosServerHolder ()
  {
  }

  public GamePalitosServerHolder (GamePalitos.GamePalitosServer initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = GamePalitos.GamePalitosServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    GamePalitos.GamePalitosServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return GamePalitos.GamePalitosServerHelper.type ();
  }

}
