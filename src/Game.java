import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/* Unterschiedliche Schussarten;
 *
 * Kollision implementieren;
 *
 * Gegner implementieren;
 *  -Schiessen
 *  -Verfolgen
 *  -Sichtfeld
 */

public class Game
{
  // Attribute
  private Figur figur;
  
  private Kamera kamera;
  
  private ArrayList<Chunk> listChunks;
  private ArrayList<Asteroid> listAsteroids;
  private ArrayList<Schuss> listSchuesse;
  private ArrayList<Wurmloch> listWurmloecher;
  
  private boolean infos;
  
  private int timerCounterSchuss;
  private boolean hatGeschossen;
  
  private Color colorPoly;
  
  private static int MAXSHOOTDELAY = 180; // in ms
  
  // Konstruktor
  public Game()
  {
    this.infos = false;
    
    this.resetGame();
  }

  public void resetGame()
  {
    this.timerCounterSchuss = 0;
    this.hatGeschossen = false;
    
    this.colorPoly = Color.WHITE;
    
    this.figur = new Figur( (int) ( Chunk.WIDTH / 2 ), (int) ( Chunk.HEIGHT / 2 ));
    this.kamera = new Kamera();
    
    this.listChunks = new ArrayList<Chunk>();
    this.listAsteroids = new ArrayList<Asteroid>();
    this.listSchuesse = new ArrayList<Schuss>();
    this.listWurmloecher = new ArrayList<Wurmloch>();
    
    for ( int i = -2; i <= 2; i++ )
    {
      for ( int j = -2; j <= 2; j++ )
      {
        if (( i != 0 ) && ( j != 0 ))
        {
          this.addChunk( i, j );
        }
        else
        {
          this.listChunks.add( new Chunk( 0, 0 ));
        }
      }
    }
  }

  /*
   * Methoden
   */
  public void render( Graphics g )
  {
    BufferedImage img = new BufferedImage( SpaceSim.WIDTH, SpaceSim.HEIGHT, BufferedImage.TYPE_INT_RGB );
    Graphics imgG = img.getGraphics();
    
    // Hintergrund zeichnen
    this.renderHintergrund( imgG );
    
    // Wurmloecher zeichnen
    this.renderWurmloecher( imgG );
    
    // Asteroiden zeichnen
    this.renderAsteroids( imgG );
    
    // Schuesse zeichnen
    this.renderSchuesse( imgG );
    
    // Figur zeichnen
    this.renderFigur( imgG );
    
    // Infos zeigen
    if ( this.infos == true )
    {
      this.renderInfos( imgG );
    }
    
    g.drawImage( img, 0, 0, null );
  }
  
  public void updateLogik( int pX, int pY, boolean pE, boolean pSpace )
  {
    this.figur.bewegen( pX, pY );
    this.shoot( pSpace );
    
    // Check Chunks if exist
    this.checkChunks();
    
    // Check if Player is in Wormhole
    this.checkWurmloch();
    
    // Update Asteroids
    this.updateAsteroiden();
    
    // Update Schuesse
    this.updateSchuesse();
    
    // Update Wurmloecher
    for ( Wurmloch wl : this.listWurmloecher )
    {
      wl.update();
    }
    
    // Check Figurkollision
    this.checkKollision();
    
    this.kamera.setX( this.figur.getX() );
    this.kamera.setY( this.figur.getY() );
  }
  
  private void shoot( boolean pSpace )
  {
    if ( this.hatGeschossen == false )
    {
      if ( pSpace == true )
      {
        this.listSchuesse.add( new Schuss( (int) this.figur.getX(), (int) this.figur.getY(), this.figur.getAngle(), this.figur.getVel() ));
        this.hatGeschossen = true;
      }
    }
    else
    {
      if ( this.timerCounterSchuss > Game.MAXSHOOTDELAY )
      {
        this.timerCounterSchuss = 0;
        this.hatGeschossen = false;
      }
      else
      {
        this.timerCounterSchuss = this.timerCounterSchuss + SpaceSim.timerIntervall;
      }
    }
  }
  
  private void checkChunks()
  {
    for ( int i = -2; i <= 2; i++ )
    {
      for ( int j = -2; j <= 2; j++ )
      {
        int x = (int) ( Math.floor( this.figur.getX() / Chunk.WIDTH )) + i;
        int y = (int) ( Math.floor( this.figur.getY() / Chunk.HEIGHT )) + j;
        
        boolean exists = false;
        
        for ( int k = this.listChunks.size()-1; k >= 0; k-- )
        {
          Chunk c = this.listChunks.get( k );
          
          if (( c.getX() == x ) && ( c.getY() == y ))
          {
            exists = true;
          }
        }
        
        if ( exists == false )
        {
          this.addChunk( x, y );
        }
      }
    }
  }
  
  private void checkWurmloch()
  {
    int x = (int) this.figur.getX();
    int y = (int) this.figur.getY();
    
    if ( this.figur.getInWurmloch() == false )
    {
      for ( Wurmloch wl : this.listWurmloecher )
      {
        double x1 = x - wl.getX();
        double y1 = y - wl.getY();
        
        if ( Math.sqrt( x1*x1 + y1*y1 ) < Wurmloch.RADIUS / 2 )
        {
            Wurmloch wl2 = wl.getPartnerLoch();
            
            this.figur.setInWurmloch( true );
            this.figur.setX( wl2.getX() );
            this.figur.setY( wl2.getY() );
          
        }
      }
    }
    else
    {
      boolean inWurmloch = false;
      
      for ( Wurmloch wl : this.listWurmloecher )
      {
        double x1 = x - wl.getX();
        double y1 = y - wl.getY();
        
        if ( Math.sqrt( x1*x1 + y1*y1 ) < Wurmloch.RADIUS )
        {
          inWurmloch = true;
        }
      }
      
      this.figur.setInWurmloch( inWurmloch );
    }
  }
  
  private void updateAsteroiden()
  {
    for ( int i = -2; i <= 2; i++ )
    {
      for ( int j = -2; j <= 2; j++ )
      {
        int x = (int) ( Math.floor( this.figur.getX() / Chunk.WIDTH )) + i;
        int y = (int) ( Math.floor( this.figur.getY() / Chunk.HEIGHT )) + j;
        
        for ( Chunk c : this.listChunks )
        {
          if (( c.getX() == x ) && ( c.getY() == y ))
          {
            // Asteroiden updaten
            for ( Asteroid a : this.listAsteroids )
            {
              if (( (int) ( Math.floor( a.getX() / Chunk.WIDTH )) == x ) && ( (int) ( Math.floor( a.getY() / Chunk.HEIGHT )) == y ))
              {
                a.update();
                for ( Asteroid a2 : this.listAsteroids )
                {
                  if ( a2 != a )
                  {
                    int aX = (int) ( a2.getX() - a.getX() );
                    int aY = (int) ( a2.getY() - a.getY() );
                    
                    if ( aX*aX + aY*aY < ( a.getGroesse()+a2.getGroesse() )*( a.getGroesse()+a2.getGroesse() ))
                    {
                      double aVel = ( Math.abs( a.getXVel() ) + Math.abs( a.getYVel() )) * 2.0d/3.0d;
                      double a2Vel = ( Math.abs( a2.getXVel() ) + Math.abs( a2.getYVel() )) * 2.0d/3.0d;
                      
                      double aXVel = -Math.sin( Math.sqrt( aX*aX + aY*aY ) / aX ) * aVel;
                      double aYVel = -Math.cos( Math.sqrt( aX*aX + aY*aY ) / aY ) * aVel;
                      
                      double a2XVel = Math.sin( Math.sqrt( aX*aX + aY*aY ) / aX ) * a2Vel;
                      double a2YVel = Math.cos( Math.sqrt( aX*aX + aY*aY ) / aY ) * a2Vel;
                      
                      a.setXVel( aXVel*2.0d/3.0d - a2XVel/3.0d );
                      a.setYVel( aYVel*2.0d/3.0d - a2YVel/3.0d );
                      
                      a2.setXVel( a2XVel*2.0d/3.0d - aXVel/3.0d );
                      a2.setYVel( a2YVel*2.0d/3.0d - aYVel/3.0d );
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  private void updateSchuesse()
  {
    for ( int i = -2; i <= 2; i++ )
    {
      for ( int j = -2; j <= 2; j++ )
      {
        int x = (int) ( Math.floor( this.figur.getX() / Chunk.WIDTH )) + i;
        int y = (int) ( Math.floor( this.figur.getY() / Chunk.HEIGHT )) + j;
        
        
        for ( int k = this.listSchuesse.size()-1; k >= 0; k-- )
        {
          Schuss s = this.listSchuesse.get( k );
          
          if (( (int) ( Math.floor( s.getX() / Chunk.WIDTH )) == x ) && ( (int) ( Math.floor( s.getY() / Chunk.HEIGHT )) == y ))
          {
            s.update();
          }
          
          for ( Asteroid a : this.listAsteroids )
          {
            double ax = s.getX() - a.getX();
            double ay = s.getY() - a.getY();
      
            if ( ax*ax + ay*ay < a.getGroesse()*a.getGroesse() )
            {
              this.listSchuesse.remove( k );
            }
          }
        }
      }
    }
    
    for ( int h = this.listSchuesse.size()-1; h >= 0; h-- )
    {
      Schuss s = this.listSchuesse.get( h );
      
      boolean exists = false;
      
      for ( int i = -2; i <= 2; i++ )
      {
        for ( int j = -2; j <= 2; j++ )
        {
          int x = (int) ( this.figur.getX() / Chunk.WIDTH + i );
          int y = (int) ( this.figur.getY() / Chunk.WIDTH + j );
          
          if (( (int) ( Math.floor( s.getX() / Chunk.WIDTH )) == x ) && ( (int) ( Math.floor( s.getY() / Chunk.WIDTH )) == y ))
          {
            exists = true;
          }
        }
      }
      
      if ( exists == false )
      {
        this.listSchuesse.remove( h );
      }
    }
  }
  
  private void addChunk( int pX, int pY )
  {
    this.listChunks.add( new Chunk( pX, pY ));
    
    int zufallsWert = (int) ( Math.random() * 1000 + 1 );
    
    // Asteroid
    if ( zufallsWert <= 200 )
    {
      this.erstelleAsteroid( pX, pY );
    }
    
    // Asteroidenfeld
    if (( zufallsWert >= 100 ) && ( zufallsWert < 150 ))
    {
      int anzahlAsteroiden = (int) ( Math.random() * 21 + 10 );
      
      for ( int i = 0; i < anzahlAsteroiden; i++ )
      {
        
        this.erstelleAsteroid( pX, pY );
      }
    }
    
    // Wurmloch
    if (( zufallsWert >= 950 ) && ( zufallsWert < 1000 ))
    {
      int x = (int) ( Math.random() * Chunk.WIDTH ) + pX * Chunk.WIDTH;
      int y = (int) ( Math.random() * Chunk.HEIGHT ) + pY * Chunk.HEIGHT;
      
      int x2 = (int) ( Math.sqrt(( x - this.figur.getX() ) * ( x - this.figur.getX() )));
      int y2 = (int) ( Math.sqrt(( y - this.figur.getY() ) * ( y - this.figur.getY() )));
      
      if ( x2 < 0 )
      {
        x2 = x - (int) ( Math.random() * Chunk.WIDTH * 8 );
      }
      else
      {
        x2 = x + (int) ( Math.random() * Chunk.WIDTH * 8 );
      }
      
      if ( y2 < 0 )
      {
        y2 = y - (int) ( Math.random() * Chunk.WIDTH * 8 );
      }
      else
      {
        y2 = y + (int) ( Math.random() * Chunk.WIDTH * 8 );
      }
      
      int xC2 = (int) ( x2 / Chunk.WIDTH );
      int yC2 = (int) ( y2 / Chunk.WIDTH );
      
      boolean exists = false;
      
      for ( Chunk c : this.listChunks )
      {
        if (( c.getX() == xC2 ) && ( c.getY() == yC2 ))
        {
          exists = true;
        }
      }
      
      if ( exists == false )
      {
        this.listChunks.add( new Chunk( xC2, yC2 ));
        
        Wurmloch wl1 = new Wurmloch( x, y );
        Wurmloch wl2 = new Wurmloch( x2, y2 );
        
        wl1.setPartnerLoch( wl2 );
        wl2.setPartnerLoch( wl1 );
        
        this.listWurmloecher.add( wl1 );
        this.listWurmloecher.add( wl2 );
      }
    }
  }
  
  private void erstelleAsteroid( int pX, int pY )
  {
    boolean asteroidErstellt = false;
      
      while ( asteroidErstellt == false )
      { 
        int x = (int) ( Math.random() * Chunk.WIDTH ) + pX * Chunk.WIDTH;
        int y = (int) ( Math.random() * Chunk.HEIGHT ) + pY * Chunk.HEIGHT;
      
        int seitenanzahl = (int) ( Math.random() * 13 + 8 );
        int groesse = (int) ( Math.random() * 31 + 20 );
        int abweichung = (int) ( Math.random() * groesse * 0.4d );
        
        boolean funzt = true;
        
        for ( Asteroid a : this.listAsteroids )
        {
          if (( x-a.getX() )*( x-a.getX() ) + ( y-a.getY() )*( y-a.getY() ) < ( groesse + abweichung/2 + a.getGroesse() )*( groesse + abweichung/2 + a.getGroesse() ))
          {
            funzt = false;
          }
        }
        
        if ( funzt == true )
        {
          this.listAsteroids.add( new Asteroid( x, y, seitenanzahl, groesse, abweichung ));
          asteroidErstellt = true;
        }
      }
  }
  
  private void checkKollision()
  {
    int x = (int) this.figur.getX();
    int y = (int) this.figur.getY();
    
    for ( Asteroid a : this.listAsteroids )
    {
      int aX = (int) ( a.getX()-x );
      int aY = (int) ( a.getY()-y );
      
      double radius = this.figur.WIDTH + this.figur.HEIGHT;
      radius = radius/4.0d;
      radius = radius*0.8d;
      radius = radius + (double) a.getGroesse();
      
      if ( aX*aX + aY*aY < radius*radius )
      {
        double[] fVel1 = this.figur.getVel();
        
        double fVel = ( Math.abs( fVel1[ 0 ] ) + Math.abs( fVel1[ 1 ] )) * 2.0d/3.0d;
        double aVel = ( Math.abs( a.getXVel() ) + Math.abs( a.getYVel() )) * 2.0d/3.0d;;
        
        double fXVel = -Math.sin( Math.sqrt( aX*aX + aY*aY ) / aX ) * fVel;
        double fYVel = -Math.cos( Math.sqrt( aX*aX + aY*aY ) / aY ) * fVel;
        
        double aXVel = Math.sin( Math.sqrt( aX*aX + aY*aY ) / aX ) * aVel;
        double aYVel = Math.cos( Math.sqrt( aX*aX + aY*aY ) / aY ) * aVel;
        
//        this.figur.setX(( -Math.sin( aX/Math.sqrt( aX*aX + aY*aY ) ) * ( radius+3.5d ) + a.getX() ));
//        this.figur.setY(( -Math.cos( aY/Math.sqrt( aX*aX + aY*aY ) ) * ( radius+3.5d ) + a.getY() ));
        
        this.figur.setXVel( fXVel*2.0d/3.0d - aXVel/3.0d );
        this.figur.setYVel( fYVel*2.0d/3.0d - aYVel/3.0d );
        
        a.setXVel( aXVel*2.0d/3.0d - fXVel/3.0d );
        a.setYVel( aYVel*2.0d/3.0d - fYVel/3.0d );
      }
    }
  }
  
  public void changeInfoState()
  {
    this.infos = !this.infos;
  }
  
  /*
   * GRAFIK
   */
  private void renderHintergrund( Graphics g )
  {
    int kX = (int) this.kamera.getX();
    int kY = (int) this.kamera.getY();
    
    for ( Chunk c : this.listChunks )
    {
      int cX = c.getX() * Chunk.WIDTH;
      int cY = c.getY() * Chunk.HEIGHT;
      
      cX = cX - kX;
      cY = cY - kY;
      
      if (( cX < SpaceSim.WIDTH ) && ( cX > -Chunk.WIDTH ))
      {
        if (( cY < SpaceSim.HEIGHT ) && ( cY > -Chunk.HEIGHT ))
        {
          g.drawImage( c.getImage(), cX, cY, null );    
        }
      }
    }
  }
  
  private void renderInfos( Graphics g )
  {
    int genauigkeit = 10;
    
    Graphics2D g2D = (Graphics2D) g;
    
    g2D.setStroke( new BasicStroke( 1 ));
    
    g.setColor( Color.BLACK );
    g.fillRect( -1, -1, 150, 45 );
    
    // POSITION
    String text = "Pos: ";
    double var = (int) ( this.figur.getX() * genauigkeit );
  
    text = text + ( var / genauigkeit );
    var = (int) ( this.figur.getY() * genauigkeit );
    
    text = text + " | " + ( var / genauigkeit );
  
    g.setColor( Color.WHITE );
    g.drawString( text, 0, 10 );
    
    text = "Chunk: ";
    var = (int) ( Math.floor( this.figur.getX() / Chunk.WIDTH ));
    
    text = text + (int) var;
    var = (int) ( Math.floor( this.figur.getY() / Chunk.HEIGHT ));
    
    text = text + " | " + (int) var;
    
    g.drawString( text, 0, 20 );
    
    // ANGLE
    text = "Ang: ";
    var = (int) ( this.figur.getAngle() * genauigkeit );
    
    text = text + ( var / genauigkeit );
    
    g.drawString( text, 0, 30 );
    
    // VELOCITY
    text = "Vel: ";
    double[] vel = this.figur.getVel();
    
    var = (int) ( vel[ 0 ] * genauigkeit );
    text = text + ( var / genauigkeit );
    
    var = (int) ( vel[ 1 ] * genauigkeit );
    text = text + " | " + ( var / genauigkeit );
    
    g.drawString( text, 0, 40 );
  }
  
  private void renderSchuesse( Graphics g )
  {
    int kX = (int) this.kamera.getX();
    int kY = (int) this.kamera.getY();
    
    Graphics2D g2D = (Graphics2D) g;
    
    for ( Schuss s : this.listSchuesse )
    {
      int x = (int) ( s.getX() - kX );
      int y = (int) ( s.getY() - kY );
      
      if (( x >= 0 ) && ( x <= SpaceSim.WIDTH ))
      {
        if (( y >= 0 ) && ( y <= SpaceSim.HEIGHT ))
        {
          g.setColor( Color.WHITE );
          g.fillOval( x-2, y-2, 4, 4 );
        }
      }
    }
  }
  
  private void renderAsteroids( Graphics g )
  {
    int kX = (int) this.kamera.getX();
    int kY = (int) this.kamera.getY();
    
    Graphics2D g2D = (Graphics2D) g;
    
    for ( Asteroid a : this.listAsteroids )
    {
      int x = (int) ( a.getX() - kX );
      int y = (int) ( a.getY() - kY );
      
      if ((( x - a.getGroesse() ) >= 0 ) && (( x + a.getGroesse() ) <= SpaceSim.WIDTH ))
      {
        if ((( y - a.getGroesse() ) >= 0 ) && (( y + a.getGroesse() ) <= SpaceSim.HEIGHT ))
        {
          g2D.setStroke( new BasicStroke( 1 ) );
      
          AffineTransform aT = g2D.getTransform();
      
          g.translate( x, y );
          g2D.rotate( Math.toRadians( a.getAngle() ));
          g.setColor( Color.BLACK );
          g.fillPolygon( a.getPolygon() );
          
          g.setColor( Color.WHITE );
          g.drawPolygon( a.getPolygon() );
      
          g2D.setTransform( aT );
        }
      }
    }
  }
  
  private void renderWurmloecher( Graphics g )
  {
    int kX = (int) this.kamera.getX();
    int kY = (int) this.kamera.getY();
    
    Graphics2D g2D = (Graphics2D) g;
    
    for ( Wurmloch wl : this.listWurmloecher )
    {
      int x = (int) ( wl.getX() - kX );
      int y = (int) ( wl.getY() - kY );
      
      if (( x >= 0 ) && ( x <= SpaceSim.WIDTH ))
      {
        if (( y >= 0 ) && ( y <= SpaceSim.HEIGHT ))
        {
          double angle = wl.getAngle();
          
          g2D.setStroke( new BasicStroke( 1 ) );
          
          AffineTransform aT = g2D.getTransform();
          
          g2D.translate( x, y );
          g.setColor( Color.BLACK );
          g.fillOval( -Wurmloch.RADIUS, - Wurmloch.RADIUS, 3*Wurmloch.RADIUS, 3*Wurmloch.RADIUS);
          
          g2D.setTransform( aT );
          
          for ( int i = 1; i < 6; i++ )
          {
            g2D.translate( x, y );
            g2D.rotate( Math.toRadians( angle + 18.5d * i ));
            g.setColor( Color.WHITE );
            g.drawRect( -(int) ( Wurmloch.RADIUS / i ), -(int) ( Wurmloch.RADIUS / i ), (int) ( 2 * Wurmloch.RADIUS / i ), (int) ( 2 * Wurmloch.RADIUS / i ) );
            
            g2D.setTransform( aT );
            
            g2D.translate( x, y );
            g2D.rotate( Math.toRadians( angle + 18.5d * i + 45 ));
            g.setColor( Color.WHITE );
            g.drawRect( -(int) ( Wurmloch.RADIUS / i ), -(int) ( Wurmloch.RADIUS / i ), (int) ( 2 * Wurmloch.RADIUS / i ), (int) ( 2 * Wurmloch.RADIUS / i ) );
            
            g2D.setTransform( aT );
          }
        }
      }
    }
  }
  
  private void renderFigur( Graphics g )
  {
    int[][] points = this.figur.getPoints();
    
    int fX = (int) this.figur.getX();
    int fY = (int) this.figur.getY();
    
    int kX = (int) this.kamera.getX();
    int kY = (int) this.kamera.getY();
    
    Graphics2D g2D = (Graphics2D) g;
    
    AffineTransform aT = g2D.getTransform();
    
    g2D.translate( fX - kX, fY - kY );               
    g2D.rotate( Math.toRadians( this.figur.getAngle() ));
                                           
    g2D.setStroke( new BasicStroke( 5 ));
    g.setColor( this.colorPoly );
    g.drawPolyline( points[0], points[1], 3 );
    
    g2D.setTransform( aT );
  }
}