import java.util.Vector;

import processing.core.PApplet;

public class RiassQuad extends PApplet {

	public Contratti c=new Contratti();
	
	
public void setup()
{
  size( 1024, 768 );
  smooth();
  strokeWeight( 2 );
  
  c.setMax(650);
  c.addContratto(10);
  c.addContratto(100,0,65);
  c.addContratto(200,100,10);
  c.addContratto(650,200,10);
  c.addContratto(650,100,55);
}

public void draw()
{
	Vector<Contratto> s=c.getSet();
	int xo=0;
	for(int i=0;i<s.size();i++){
		Contratto q=s.get(i);
		rect(xo,q.getMin(),q.getProp(),q.getMax()-q.getMin());
		xo+=q.getProp();
	}
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
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "RiassQuad" });
  }
}
