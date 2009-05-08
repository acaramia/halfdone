import processing.core.*;

//import processing.phone.*;

public class Testo extends PMIDlet {

	//Phone p;

	PFont font;

	public void setup() {
		// p = new Phone(this);
		// p.fullscreen();

		font = loadFont(FACE_PROPORTIONAL, STYLE_PLAIN, SIZE_LARGE);
		textFont(font);
		textAlign(CENTER);
		fill(0);

		noLoop();
	}

	public void draw() {
		background(255);
		text("Key:\n" + key + "\n\nkeyCode:\n" + keyCode + "\n\nrawKeyCode:\n"
				+ rawKeyCode, 0, 0, width, height);
	}

	public void keyPressed() {
		redraw();
	}
}
