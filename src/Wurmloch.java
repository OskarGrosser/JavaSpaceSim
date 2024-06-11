public class Wurmloch
{
  // Attribute
  private int xPos;
  private int yPos;
  
  public static int RADIUS = 90;
  
  private Wurmloch partnerLoch;
  
  private double angle;
  private double angleVel;
  
  // Konstruktor
  public Wurmloch( int pX, int pY )
  {
    this.xPos = pX;
    this.yPos = pY;
    
    this.partnerLoch = null;
    
    this.angle = 0.0d;
    this.angleVel = Math.random() - 0.5d;
  }
  
  /*
   * Methoden
   */
  public void update()
  {
    this.angle = this.angle + this.angleVel;
    
    if ( this.angle >= 360 )
    {
      this.angle = this.angle - 360;
    }
    else if ( this.angle < 0 )
    {
      this.angle = this.angle + 360;
    }
  }
   
  public void setPartnerLoch( Wurmloch pLoch )
  {
    this.partnerLoch = pLoch;
  }
  
  public int getX()
  {
    return this.xPos;
  }
  public int getY()
  {
    return this.yPos;
  }
  public double getAngle()
  {
    return this.angle;
  }
  public Wurmloch getPartnerLoch()
  {
    return this.partnerLoch;
  }
}