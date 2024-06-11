import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControllerPanel extends JPanel
{
  // Attribute
  private Menu menu;
  private Game game;
  
  private enum STATE
  {
    GAME, MENU
  }
  
  private STATE state;
  
  // Konstruktor
  public ControllerPanel()
  {
    this.menu = new Menu();
    this.game = new Game();
    
    this.state = STATE.MENU;
  }
  
  /*
   * Methoden
   */
  public void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    
    if ( this.state == STATE.GAME )
    {
      this.game.render( g );
    } 
    else if ( this.state == STATE.MENU )
    {
      this.menu.render( g );
    }
  } 
   
  public void updateLogik( int pX, int pY, boolean pE, boolean pSpace, boolean pEnter )
  {
    if ( this.state == STATE.GAME )
    {
      this.game.updateLogik( pX, pY, pE, pSpace );
    } 
    else if ( this.state == STATE.MENU )
    {
      this.menu.updateLogik( pX, pY, pSpace );
    }
    
    if ( this.state == STATE.MENU )
    {
      if ( pEnter == true )
      {
        int id = this.menu.enter();
        
        if ( id == 1 )
        {
          this.changeState();
        }
        if ( id == 2 )
        {
          this.game.changeInfoState();
        }
        if ( id == 3 )
        {
          this.game.resetGame();
        }
        
        if ( id == 4 )
        {
          System.exit( 0 );
        }
      }
    }
    
    this.repaint();
  }
  
  public void changeState()
  {
    if ( this.state == STATE.GAME )
    {
      this.state = STATE.MENU;
    } 
    else if ( this.state == STATE.MENU )
    {
      this.state = STATE.GAME;
    }  
    
    System.out.println( "Changed state to " + this.state );
  }
}