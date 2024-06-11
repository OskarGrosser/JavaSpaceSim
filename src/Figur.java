import java.awt.*;
import java.awt.geom.*;

public class Figur
{
  // Attribute
  public static int WIDTH = 24;
  public static int HEIGHT = 24;
  
  private int[][] points;
  
  private double xPos;
  private double yPos;
  
  private double xVel;
  private double yVel;
  
  private double angle;
  
  private boolean inWurmloch;
  
  private static double VELACC = 0.025d;
  private static double ANGLEACC = 3.0d;
  
  private static double MINVEL = 0.1d;
  private static double MAXVEL = 20.0d;
  
  // Konstruktor
  public Figur( int pX, int pY )
  {
    this.xPos = pX;
    this.yPos = pY;
    
    this.xVel = 0.0d;
    this.yVel = 0.0d;
    
    this.angle = 0.0d;
    
    this.inWurmloch = false;
    
    // Punkte des SpielerDreiecks
    this.points = new int[ 2 ][];
    this.points[ 0 ] = new int[ 3 ];
    this.points[ 1 ] = new int[ 3 ];
    
    this.points[ 0 ][ 0 ] = (int) ( Figur.WIDTH / 2 );
    this.points[ 0 ][ 1 ] = 0;
    this.points[ 0 ][ 2 ] = -(int) ( Figur.WIDTH / 2 );
    
    this.points[ 1 ][ 0 ] = Figur.HEIGHT;
    this.points[ 1 ][ 1 ] = 0;
    this.points[ 1 ][ 2 ] = Figur.HEIGHT;
  }
  
  /*
   * Methoden
   */
  public void bewegen( int pX, int pY )
  {
    // ROTATION
    this.angle = this.angle + this.ANGLEACC * pX;
    
    if ( this.angle >= 360 )
    {
      this.angle = this.angle - 360;
    }
    else if ( this.angle < 0 )
    {
      this.angle = this.angle + 360;
    }
    
    // VELOCITY 
    this.xVel = this.xVel + Math.sin( Math.toRadians( this.angle )) * this.VELACC * 2 * -pY;
    this.yVel = this.yVel + Math.cos( Math.toRadians( this.angle )) * this.VELACC * 2 * pY;
    
    if ( pY == 0 )
    {
      this.ifNoSpeeding();
    } 
    
    this.checkMaxVel();
    
    this.xPos = this.xPos + this.xVel;
    this.yPos = this.yPos + this.yVel;
    
    if (( this.xPos == 0 ) && ( this.yPos == 0 ))
    {
      this.xPos = 0.5d;
      this.yPos = 0.5d;
    }
  }                      
  
  private void ifNoSpeeding()
  {
    double vel = Math.abs( this.xVel ) + Math.abs( this.yVel ); 
    
    if ( vel > this.MINVEL )
    {
      this.xVel = this.xVel * 0.95d;
      this.yVel = this.yVel * 0.95d;
    } 
    else
    {
      this.xVel = 0.0d;
      this.yVel = 0.0d;
    } 
  }
  
  private void checkMaxVel()
  {
    if (( Math.abs( this.xVel ) + Math.abs( this.yVel )) > this.MAXVEL )
    {
      double max = Math.abs( this.xVel ) + Math.abs( this.yVel ); 
      
      double faktor = this.MAXVEL / max;
      
      this.xVel = this.xVel * faktor;
      this.yVel = this.yVel * faktor;
    } 
  }
  
  // Get/SetMethoden
  public void setInWurmloch( boolean pWert )
  {
    this.inWurmloch = pWert;
  }
  public void setX( double pWert )
  {
    this.xPos = pWert;
  }
  public void setY( double pWert )
  {
    this.yPos = pWert;
  }
  public void setXVel( double pWert )
  {
    this.xVel = pWert;
  }
  public void setYVel( double pWert )
  {
    this.yVel = pWert;
  }
   
  public double getX()
  {
    return this.xPos;
  }
  public double getY()
  {
    return this.yPos;
  }
  public int[][] getPoints()
  {
    return this.points;
  }
  public double getAngle()
  {
    return this.angle;
  }
  public double[] getVel()
  {
    double[] vel = new double[ 2 ];
    
    vel[ 0 ] = this.xVel;
    vel[ 1 ] = this.yVel;
    
    return vel;
  }
  public boolean getInWurmloch()
  {
    return this.inWurmloch;
  }
}