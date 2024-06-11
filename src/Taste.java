import java.awt.*;

public class Taste
{
  // Attribute
  private int xPos;
  private int yPos;
  private int weite;
  private int hoehe;
  
  private int id;
  private String text;
  
  private Font font;
  
  // Konstruktor
  public Taste( int pX, int pY, int pW, int pH, int pID, String pText, Font pFont )
  {
    this.xPos = pX;
    this.yPos = pY;
    this.weite = pW;
    this.hoehe = pH;
    
    this.id = pID;
    this.text = pText;
    
    this.font = pFont;
  }
  
  /*
   * Methoden
   */
  public int[] getTaste()
  {
    int[] taste = new int[ 4 ];
    
    taste[ 0 ] = this.xPos;
    taste[ 1 ] = this.yPos;
    taste[ 2 ] = this.weite;
    taste[ 3 ] = this.hoehe;
    
    return taste;
  }
  public String getText()
  {
    return this.text;
  }
  public int getID()
  {
    return this.id;
  }
  public Font getFont()
  {
    return this.font;
  }
}