import java.util.Vector;

import processing.core.PApplet;
import processing.core.PFont;

public class RiassQuad extends PApplet {

	public Contratti contratti = new Contratti();
	public Vector<Area> area=new Vector<Area>();
	public String ctrSelected="";
	private PFont font;
	private int fontsize=16;

	public void setup() {
		size(1024, 768);
		smooth();
		strokeWeight(2);
		
		// Uncomment the following two lines to see the available fonts 
		//String[] fontList = PFont.list();
		//  println(fontList);
		font = createFont("Arial", fontsize); 
		textFont(font);
		
		contratti.setMax(650);
		contratti.addContratto(10,"c1");
		contratti.addContratto(100, 0, 65,"c2");
		contratti.addContratto(200, 100, 10,"c3");
		contratti.addContratto(650, 200, 10,"c4");
		contratti.addContratto(650, 100, 55,"c5");
	}

	public void draw() {
		background(192,192,192);		
		disegnaRettangoli();
		Vector<Contratto> setContratti = contratti.getSet();
		int xo = 0;
		for (int i = 0; i < setContratti.size(); i++) {
			Contratto ctr = setContratti.get(i);
			float sx = this.getWidth() / 100;
			// rect(xo,ctr.getMin(),ctr.getProp()*sx,ctr.getMax()-ctr.getMin());
			// xo+=q.getProp()*sx;
		}
	}

	private void disegnaRettangoli() {
		area.clear();
		int xo = 0;
		int yo = (int) (this.getHeight() * 0.7);// piazza fascia a 1/3 dal fondo
		int ym = this.getHeight();
		float sx = this.getWidth() / contratti.getSommaLarghezze();
		float sy = (this.getHeight() - yo)
				/ (float) contratti.getMassimaAltezza();
		Vector<Contratto> setContratti = contratti.getSet();
		for (int i = 0; i < setContratti.size(); i++) {
			Contratto ctr = setContratti.get(i);
			int lx=(int) (ctr.getProp() * sx);
			int ly=(int) ((ctr.getMax() - ctr.getMin()) * sy);
			if(ctr.getName().equals(this.ctrSelected)){
				fill(0,255,0);
			} else {
				fill(255,255,255);
			}
			rect(xo,yo,lx,ly);
			area.add(new Area(xo,yo,lx,ly,ctr.getName()));
			xo += ctr.getProp() * sx;
			xo += contratti.getSpaziaturaRettangoli() * sx;
		}
	}
	
	public void mouseMoved(){
		int x=mouseX;
		int y=mouseY;
		//fill(0,0,255);
		for(int i=0;i<area.size();i++){
			if(area.elementAt(i).isInside(x,y)){				
				//rect(area.elementAt(i).x1,area.elementAt(i).y1,area.elementAt(i).x2,area.elementAt(i).y2);
				Contratto c=contratti.getContrattoByName(area.elementAt(i).name);
				if(c!=null){
					fill(0,0,0);
					String s=c.toString(); 
					//stroke()
					x=(int) (area.elementAt(i).x1+area.elementAt(i).x2/2-textWidth(s)/2);
					y=area.elementAt(i).y1+area.elementAt(i).y2/2+fontsize/2;
					text(s,x,y);
				}
			}
		}		
	}

	public void mousePressed() {
		int x=mouseX;
		int y=mouseY;
		//fill(0,255,0);
		for(int i=0;i<area.size();i++){
			if(area.elementAt(i).isInside(x,y)){				
				//rect(area.elementAt(i).x1,area.elementAt(i).y1,area.elementAt(i).x2,area.elementAt(i).y2);
				this.ctrSelected=area.elementAt(i).name;
			}
		}
	}

	public void keyPressed() {
		if (key == 'i') {
			return;
		}
		if (key == '1') {
			return;
		}
		if (key == '2') {
			return;
		}
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--present", "--bgcolor=#666666",
				"--stop-color=#cccccc", "RiassQuad" });
	}
}
