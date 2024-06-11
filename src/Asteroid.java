import java.awt.*;

public class Asteroid
{
  // Attribute
  private double xPos;
  private double yPos;
  
  private Polygon polygon;
  
  private double angle;
  private double angleVel;
  
  private int groesseR;
  
  private double xVel;
  private double yVel;
  
  // Kosntruktor
  public Asteroid( int pX, int pY, int pSeitenanzahl, int pGroesse, double pAbweichung )
  {
    this.xPos = pX;
    this.yPos = pY;
    this.angle = 0.0d;
    
    this.groesseR = (int) ( pGroesse + pAbweichung * 0.5d );
    
    this.xVel = ( Math.random() - 0.5d ) * 0.5d;
    this.yVel = ( Math.random() - 0.5d ) * 0.5d;
    
    this.erstellePolygon( pSeitenanzahl, pGroesse, pAbweichung );
    
    this.angleVel = ( Math.random() - 0.5d );
  }
  
  /*
   * Methoden
   */
  private void erstellePolygon( int pSeitenanzahl, int pGroesse, double pAbweichung )
  {
    int[] xP = new int[ pSeitenanzahl + 1 ];
    int[] yP = new int[ pSeitenanzahl + 1 ];
    
    for ( double i = 0; i < pSeitenanzahl; i = i + 1 )
    {
      double abw = Math.random() * pAbweichung;
      xP[ (int) i ] = (int) ( Math.sin( Math.toRadians( 360 * ( i / pSeitenanzahl - 1 ))) * pGroesse + Math.sin( Math.toRadians( 360 * ( i / pSeitenanzahl - 1 ))) * abw );
      yP[ (int) i ] = (int) ( Math.cos( Math.toRadians( 360 * ( i / pSeitenanzahl - 1 ))) * pGroesse + Math.cos( Math.toRadians( 360 * ( i / pSeitenanzahl - 1 ))) * abw );
    }
    
    xP[ pSeitenanzahl ] = xP[ 0 ];
    yP[ pSeitenanzahl ] = yP[ 0 ];
    
    this.polygon = new Polygon( xP, yP, pSeitenanzahl + 1 );
  } 
  
  public void update()
  {
    this.angle = this.angle + this.angleVel;
    
    if ( this.angle > 360 )
    {
      this.angle = this.angle - 360;
    }
    else if ( this.angle < 0 )
    {
      this.angle = this.angle + 360;
    }
    
    this.xPos = this.xPos + this.xVel;
    this.yPos = this.yPos + this.yVel;
  }
  
  public void setAngleVel( double pWert )
  {
    this.angleVel = pWert;
  }
  
  public void addXVel( double pWert )
  {
    this.xVel = this.xVel + pWert;
  }
  public void addYVel( double pWert )
  {
    this.yVel = this.yVel + pWert;
  }
  
  public void setXVel( double pWert )
  {
    this.xVel = pWert;
  }
  public void setYVel( double pWert )
  {
    this.yVel = pWert;
  }
   
  public int getX()
  {
    return (int) this.xPos;
  }
  public int getY()
  {
    return (int) this.yPos;
  }
  public double getXVel()
  {
    return this.xVel;
  }
  public double getYVel()
  {
    return this.yVel;
  }
  public Polygon getPolygon()
  {
    return this.polygon;
  }
  public double getAngle()
  {
    return this.angle;
  }
  public int getGroesse()
  {
    return this.groesseR;
  }
}