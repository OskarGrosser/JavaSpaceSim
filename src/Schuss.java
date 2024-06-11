public class Schuss
{
  // Attribute
  private static double VEL = 10.0d;
  
  private double xPos;
  private double yPos;
  
  private double xVel;
  private double yVel;
  
  // Konstruktor
  public Schuss( int pX, int pY, double pAngle, double[] pVel )
  {
    this.xPos = pX;
    this.yPos = pY;
    
    this.xVel = pVel[ 0 ];
    this.yVel = pVel[ 1 ];
    
    this.xVel = this.xVel + Math.sin( Math.toRadians( pAngle )) * this.VEL;
    this.yVel = this.yVel + Math.cos( Math.toRadians( pAngle )) * this.VEL * -1;
  }
  
  /*
   * Methoden
   */
  public void update()
  {
    this.xPos = this.xPos + this.xVel;
    this.yPos = this.yPos + this.yVel;
  }
  
  public double getX()
  {
    return this.xPos;
  }
  public double getY()
  {
    return this.yPos;
  }
}