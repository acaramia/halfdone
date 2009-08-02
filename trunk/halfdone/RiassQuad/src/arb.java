import processing.core.PApplet;


public class arb extends PApplet {

// PROCESSING /////////////////////////////////////

public void setup()
{
  size( 1500, 1000 );
  smooth();
  frameRate( 24 );
  strokeWeight( 2 );
  ellipseMode( CENTER );       
}

public void draw()
{
 
}


public void mousePressed() {
//  
}

public void keyPressed()
{
  if ( key == 'i' )
  {
    return;
  }  
  if ( key == '1' )
  {
    return;
  }  
  if ( key == '2' )
  {
    return;
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "arb" });
  }
}
