import java.util.Vector;

import processing.core.PApplet;
import processing.core.PFont;

public class RiassQuad extends PApplet {

	public Contratti contratti = new Contratti();
	public Vector<Area> area=new Vector<Area>();  //aree in basso per l'elenco rettangoli
	public Vector<String> disegno=new Vector<String>();  //area disegno grafico reale, ordinata
	public String ctrSelected="";
	public String ctrMouse="";
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
		disegnaGrafico();
	}

	private void disegnaGrafico() {
		fill(255,255,255);
		Vector<Contratto> setContratti = contratti.getSet();
		int xo = 0;
		int yo = 0;
		int ym = (int) (this.getHeight()*0.7);
		float sx = this.getWidth() / 100;
		float sy = ( ym - yo)/ (float) contratti.getMassimaAltezza();
		
		contratti.azzeraProfilo();
		for (int i = 0; i < disegno.size(); i++) {
			String n=disegno.elementAt(i);
			Contratto ctr = contratti.getContrattoByName(n);
			xo=(int) (contratti.getMassimoProfilo(ctr)*sx);
			contratti.setProfilo(ctr);
			
			int lx=(int) (ctr.getProp() * sx);
			int ly=(int) ((ctr.getMax() - ctr.getMin()) * sy);
			int y=(int) (ym-ctr.getMax()*sy);			
			rect(xo,y,lx,ly);
			//xo+=lx;
		}
	}

	private void disegnaRettangoli() {
		area.clear();
		int xo = 0;
		int ym = this.getHeight();
		int yo = (int) (ym * 0.7);// piazza fascia a 1/3 dal fondo
		float sx = this.getWidth() / contratti.getSommaLarghezze();
		float sy = (this.getHeight() - yo)
				/ (float) contratti.getMassimaAltezza();
		Vector<Contratto> setContratti = contratti.getSet();
		for (int i = 0; i < setContratti.size(); i++) {
			Contratto ctr = setContratti.get(i);
			int lx=(int) (ctr.getProp() * sx);
			int ly=(int) ((ctr.getMax() - ctr.getMin()) * sy);
			int y=(int) (ym-ctr.getMax()*sy);
			area.add(new Area(xo,y,lx,ly,ctr.getName()));
			if(ctr.getName().equals(this.ctrSelected)){
				fill(0,255,0);
			} else {
				fill(255,255,255);
			}			
			rect(xo,y,lx,ly);
			xo += lx;
			xo += contratti.getSpaziaturaRettangoli() * sx;
			if(ctr.getName().equals(this.ctrMouse)){
				stampaLabel(this.ctrMouse,i);
			}
		}
	}

	// il paramentro i andra' tolto
	private void stampaLabel(String name,int i){
		Contratto c=contratti.getContrattoByName(ctrMouse);
		if(c!=null){
			fill(0,0,0);
			String s=c.toString(); 
			int tx=(int) (area.elementAt(i).x1+area.elementAt(i).x2/2-textWidth(s)/2);
			int ty=area.elementAt(i).y1+area.elementAt(i).y2/2+fontsize/2;
			text(s,tx,ty);
		}
	}
	
	public void mouseMoved(){
		int x=mouseX;
		int y=mouseY;
		//fill(0,0,255);
		for(int i=0;i<area.size();i++){
			if(area.elementAt(i).isInside(x,y)){				
				//rect(area.elementAt(i).x1,area.elementAt(i).y1,area.elementAt(i).x2,area.elementAt(i).y2);
				this.ctrMouse=area.elementAt(i).name;
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
				disegno.add(this.ctrSelected);
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
