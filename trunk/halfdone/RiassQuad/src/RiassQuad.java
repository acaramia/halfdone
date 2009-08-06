import java.util.Vector;

import processing.core.PApplet;

public class RiassQuad extends PApplet {

	public Contratti contratti = new Contratti();

	public void setup() {
		size(1024, 768);
		smooth();
		strokeWeight(2);

		contratti.setMax(650);
		contratti.addContratto(10);
		contratti.addContratto(100, 0, 65);
		contratti.addContratto(200, 100, 10);
		contratti.addContratto(650, 200, 10);
		contratti.addContratto(650, 100, 55);
	}

	public void draw() {
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
		int xo = 0;
		int yo = (int) (this.getHeight() * 0.7);// piazza fascia a 1/3 dal fondo
		int ym = this.getHeight();
		float sx = this.getWidth() / contratti.getSommaLaghezze();
		float sy = (this.getHeight() - yo)
				/ (float) contratti.getMassimaAltezza();
		Vector<Contratto> setContratti = contratti.getSet();
		for (int i = 0; i < setContratti.size(); i++) {
			Contratto ctr = setContratti.get(i);
			rect(xo, yo, ctr.getProp() * sx, (ctr.getMax() - ctr.getMin()) * sy);
			xo += ctr.getProp() * sx;
			xo += contratti.getSpaziaturaRettangoli() * sx;
		}
	}

	public void mousePressed() {
		//  
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
