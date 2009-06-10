import processing.core.*;
import processing.xml.*;

public class E247b extends PApplet {

	public void setup() {
		size(700, 700);
		noLoop();
	}

	public void draw() {
		float a = 1;
		float b = 0;
		background(0, 0, 0);
		float s = width / 4;
		translate(0, height);
		scale(s, -s);
		stroke(255, 255, 255);
		strokeWeight(1 / s);
		// noStroke();
		fill(0, 0, 255);
		// noFill();
		quad(a, b, 12,0,0);
	}

	void quad(float a, float b, int lev,int quadBot,int quadLeft) {
		if (lev > 0) {
			float w = b - a;
			float lato1 = (-w + (float) Math.sqrt(w * w + 4)) / 2;
			float lato2 = (-w - (float) Math.sqrt(w * w + 4)) / 2;
			float lato = lato1;
			if (lato < 0)
				lato = lato2;
			lato = lato - a;

			if((quadLeft==3)&&(quadBot==3)){
				fill(255, 0, 0);
				System.out.println(lato);
			} else {
				fill(0, 0, 255);
			}
			rect(a - 1, b, lato, lato);
			
			quad(a + lato, b, lev - 1,quadBot,quadLeft+1);// dx
			quad(a, b + lato, lev - 1,quadBot+1,quadLeft);// top
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "--bgcolor=#666666",
				"--stop-color=#cccccc", "E247b" });
	}
}
