import java.awt.*;
import java.awt.image.*;

public class Chunk
{
  // Attribute
  public static int WIDTH = 1000;
  public static int HEIGHT = 1000;
  
  private int xPos;
  private int yPos;
  
  private BufferedImage img;
  
  // Konstruktor
  public Chunk( int pX, int pY )
  {
    this.xPos = pX;
    this.yPos = pY;
    
    this.erstelleImage();
  }
  
  /*
   * Methoden
   */
  private void erstelleImage()
  {
    this.img = new BufferedImage( Chunk.WIDTH, Chunk.HEIGHT, BufferedImage.TYPE_INT_RGB );
    
    int anzahlDurchlaeufe;
    
    if ( Chunk.WIDTH < Chunk.HEIGHT )
    {
      anzahlDurchlaeufe = (int) ( Chunk.WIDTH / 5 );
    }
    else
    {
      anzahlDurchlaeufe = (int) ( Chunk.HEIGHT / 5 );
    }
    
    double faktor = Math.random() / 10.0d ;
    
    anzahlDurchlaeufe = (int) ( anzahlDurchlaeufe * ( 1 + faktor ));
    
    Graphics g = this.img.getGraphics();
    
    g.setColor( Color.BLACK );
    g.fillRect( 0, 0, this.img.getWidth(), this.img.getHeight() );
    
    for ( int i = 0; i < anzahlDurchlaeufe; i++ )
    {
      int x = (int) ( Math.random() * this.img.getWidth() );
      int y = (int) ( Math.random() * this.img.getHeight() );
      
      g.setColor( Color.WHITE );
      g.drawRect( x, y, 1, 1 );
    }
  }
  
  public int getX()
  {
    return this.xPos;
  }
  public int getY()
  {
     return this.yPos;
  }
  public BufferedImage getImage()
  {
    return this.img;
  }
}