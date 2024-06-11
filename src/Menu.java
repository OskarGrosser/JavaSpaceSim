import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Menu
{
  // Attribute
  private ArrayList<Taste> listTasten;
  
  private int zeiger;
  
  private int letzteBewegung;
  
  private BufferedImage img;
  private double angle;
  private static double ANGLEACC = 0.06125d;
  
  // Konstruktor
  public Menu()
  {
    this.zeiger = Integer.MAX_VALUE;
    this.letzteBewegung = Integer.MAX_VALUE;
    
    this.listTasten = new ArrayList<Taste>();
    
    // Hintergrundbild erstellen
    this.img = new BufferedImage( SpaceSim.WIDTH*2, SpaceSim.WIDTH*2, BufferedImage.TYPE_INT_RGB );
    Graphics g = this.img.getGraphics();
    g.setColor( Color.BLACK );
    g.fillRect( 0, 0, this.img.getWidth(), this.img.getHeight() );
    
    int anzahlDurchlaeufe = 5000;
    
    g.setColor( Color.WHITE );
    for ( int i =  0; i < anzahlDurchlaeufe; i++ )
    {
      int x = (int) ( Math.random() * this.img.getWidth() );
      int y = (int) ( Math.random() * this.img.getHeight() );
      
      g.fillRect( x, y, 2, 2 );
    }
    
    // Standard TastenVariablen
    int x = (int) ( SpaceSim.WIDTH / 32 );
    int w = (int) ( SpaceSim.WIDTH / 6 );
    int h = (int) ( SpaceSim.HEIGHT / 20 );
    
    this.listTasten.add( new Taste( x, (int) ( SpaceSim.HEIGHT / 10 ) - h, w, h, 1, "Return to Game", new Font( "font1", Font.BOLD, 12 )));
    this.listTasten.add( new Taste( x, (int) ( SpaceSim.HEIGHT / 10 ) * 2 - h, w, h, 2, "Toggle Info-Display", new Font( "font2", Font.BOLD, 12 )));
    this.listTasten.add( new Taste( x, (int) ( SpaceSim.HEIGHT / 10 ) * 3 - h, w, h, 3, "Reset Game", new Font( "font2", Font.BOLD, 12 )));
    
    this.listTasten.add( new Taste( x, (int) ( SpaceSim.HEIGHT / 10 ) * 4 - h, w, h, 4, "Exit", new Font( "font2", Font.BOLD, 12 )));
  }

  /*
   * Methoden
   */
  public void render( Graphics g )
  {
    // Hintergrund rendern
    Graphics2D g2D = (Graphics2D) g;
    
    AffineTransform aT = g2D.getTransform();
    
    g2D.translate( (int) ( SpaceSim.WIDTH / 2 ), (int) ( SpaceSim.HEIGHT / 2 ));
    g2D.rotate( Math.toRadians( this.angle ));
    
    g.drawImage( this.img, -(int) ( this.img.getWidth() / 2 ), -(int) ( this.img.getHeight() / 2 ), null );
    
    g2D.setTransform( aT );
    
    // Tasten rendern
    for ( Taste t : this.listTasten )
    {
      int[] tw = t.getTaste();
      
      if ( t.getID() == this.zeiger )
      {
        g.setColor( Color.WHITE );
        g.fillRect( tw[ 0 ], tw[ 1 ], tw[ 2 ], tw[ 3 ] );
        
        g.setColor( Color.BLACK );
        g.fillRect( tw[ 0 ]+5, tw[ 1 ]+5, tw[ 2 ]-10, tw[ 3 ]-10 );
      }
      else
      {
        g.setColor( Color.BLACK );
        g.fillRect( tw[ 0 ], tw[ 1 ], tw[ 2 ], tw[ 3 ] );
        
        g.setColor( Color.WHITE );
        g.drawRect( tw[ 0 ], tw[ 1 ], tw[ 2 ], tw[ 3 ] );
      }
      
      g.setColor( Color.WHITE );
      g.setFont( t.getFont() );
      g.drawString( t.getText(), tw[ 0 ] + 20, tw[ 1 ] + (int) (( tw[ 3 ] - t.getFont().getSize()-1 ) / 2 + t.getFont().getSize()-1 ));
    }
  }
  
  public void updateLogik( int pX, int pY, boolean pSpace )
  {
    this.angle = this.angle + this.ANGLEACC;
    if ( this.angle >= 360 )
    {
      this.angle = this.angle - 360;
    }
    
    if ( this.listTasten.isEmpty() == false )
    {
      if ( this.zeiger < 1 )
      {
        this.zeiger = this.listTasten.size();
      }
      else if ( this.zeiger > this.listTasten.size() )
      {
        this.zeiger = 1;
      }
    }
    
    if ( this.letzteBewegung != pY )
    {
      this.zeiger = this.zeiger + pY;
    }
    
    this.letzteBewegung = pY;
  }
  
  public int enter()
  {
    int id = this.zeiger;
    this.zeiger = Integer.MAX_VALUE;
    
    return id;
  }
}