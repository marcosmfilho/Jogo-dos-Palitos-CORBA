package GamePalitos;


/**
* GamePalitos/namesHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Game.idl
* Quarta-feira, 28 de Junho de 2017 01h59min42s BRT
*/

public final class namesHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public namesHolder ()
  {
  }

  public namesHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = GamePalitos.namesHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    GamePalitos.namesHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return GamePalitos.namesHelper.type ();
  }

}
