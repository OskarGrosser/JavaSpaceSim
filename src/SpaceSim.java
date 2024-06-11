import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpaceSim
{
  // Attribute
  public static int WIDTH = 1280;
  public static int HEIGHT = 720;
  
  private JFrame frame;
  private ControllerPanel panel;
  
  public static int timerIntervall = 12;
  private Timer timer;
  
  public boolean wPressed;
  public boolean aPressed;
  public boolean sPressed;
  public boolean dPressed;
  public boolean ePressed;
  public boolean spacePressed;
  public boolean enterPressed;
  public boolean escapePressed;
  
  // Konstruktor
  public SpaceSim()
  {
    this.erstelleGUI();
    this.erstelleKeyBindings();
    this.erstelleTimer();
    
    this.panel.requestFocus();
  }
  
  public static void main( String[] args )
  {
    new SpaceSim();
  }
  /*
   * Methoden
   */
  public void updateLogik()
  {
    if ( this.escapePressed == true )
    {
      this.panel.changeState();
      this.escapePressed = false;
    }
    else
    {
      this.panel.updateLogik( this.vereinfacheX(), this.vereinfacheY(), this.ePressed, this.spacePressed, this.enterPressed );
      
      this.enterPressed = false;
    }
  } 
  
  private int vereinfacheX()
  {
    int ergebnis = 0;
    
    if ( this.aPressed == true )
    {
      ergebnis = ergebnis - 1;
    } 
    if ( this.dPressed == true )
    {
      ergebnis = ergebnis + 1;
    } 
      
    return ergebnis;
  }
  
  private int vereinfacheY()
  {
    int ergebnis = 0;
    
    if ( this.wPressed == true )
    {
      ergebnis = ergebnis - 1;
    } 
    if ( this.sPressed == true )
    {
      ergebnis = ergebnis + 1;
    } 
      
    return ergebnis;
  }
   
  private void erstelleTimer()
  {
    this.timer = new Timer( SpaceSim.timerIntervall, new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        updateLogik();
      }
    } );
    this.timer.setInitialDelay( SpaceSim.timerIntervall );
    this.timer.start();
  } 
   
  private void erstelleKeyBindings()
  {
    this.wPressed = false;
    this.aPressed = false;
    this.sPressed = false;
    this.dPressed = false;
    this.spacePressed = false;
    this.enterPressed = false;
    this.escapePressed = false;
    
    InputMap iM = this.panel.getInputMap();
    ActionMap aM = this.panel.getActionMap();
    
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_W, 0, false ), "wPress" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_A, 0, false ), "aPress" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_S, 0, false ), "sPress" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_D, 0, false ), "dPress" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_E, 0, false ), "ePress" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, 0, false ), "spacePress" );
    
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_W, 0, true ), "wRelease" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_A, 0, true ), "aRelease" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_S, 0, true ), "sRelease" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_D, 0, true ), "dRelease" );  
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_E, 0, true ), "eRelease" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0, true ), "enterRelease" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, 0, true ), "spaceRelease" );
    iM.put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0, true ), "escapeRelease" );
    
    
    aM.put( "wPress", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        wPressed = true;
      }
    } );
    aM.put( "aPress", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        aPressed = true;
      }
    } );
    aM.put( "sPress", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        sPressed = true;
      }
    } );
    aM.put( "dPress", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        dPressed = true;
      }
    } );
    aM.put( "ePress", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        ePressed = true;
      }
    } );
    aM.put( "spacePress", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        spacePressed = true;
      }
    } );
    
    
    aM.put( "wRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        wPressed = false;
      }
    } );
    aM.put( "aRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        aPressed = false;
      }
    } );
    aM.put( "sRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        sPressed = false;
      }
    } );
    aM.put( "dRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        dPressed = false;
      }
    } );
    aM.put( "eRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        ePressed = false;
      }
    } );
    aM.put( "enterRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        enterPressed = true;
      }
    } );
    aM.put( "spaceRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        spacePressed = false;
      }
    } );
    aM.put( "escapeRelease", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent a )
      {
        escapePressed = true;
      }
    } );
  } 
   
  private void erstelleGUI()
  {
    this.frame = new JFrame( "SpaceSim" );
    this.frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    this.frame.setSize( SpaceSim.WIDTH, SpaceSim.HEIGHT );
    this.frame.setLocationRelativeTo( null );
    this.frame.setResizable( false );
    
    this.panel = new ControllerPanel();
    this.panel.setBackground( Color.WHITE );
    this.frame.getContentPane().add( this.panel );
    
    this.frame.setVisible( true );
  }
}