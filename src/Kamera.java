public class Kamera
{
  // Attribute
  private double xPos;
  private double yPos;
  
  // Konstruktor
  public Kamera()
  {
  }
  
  /*
   * Methoden
   */
  public void setX( double pWert )
  {
    this.xPos = pWert - ( SpaceSim.WIDTH / 2 );
  }
  public void setY( double pWert )
  {
    this.yPos = pWert - ( SpaceSim.HEIGHT / 2 );
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