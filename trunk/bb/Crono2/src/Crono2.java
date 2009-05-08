import processing.core.*;

public class Crono2 extends PMIDlet {// etch
// by Francis Li <http://www.francisli.com/>
//
// A mobile phone version of the classic Etch-A-Sketch toy. Press
// the "Shake" softkey to erase the screen. This simple example
// illustrates how softkey events are handled and how
// continuous key press input can be handled from within the
// draw() method
//
	int x, y;
	PFont font;

	public void setup() {
		softkey("Shake");

		x = width / 2;
		y = height / 2;

		//font = loadFont(FACE_PROPORTIONAL, STYLE_PLAIN, SIZE_LARGE);
		font = loadFont("data\\Arial-BoldMT-48");
		textFont(font);
		//textAlign(CENTER);
		
		framerate(15);
	}

	public void draw() {
		point(x, y);

		if (keyPressed) {
			text(String.valueOf(keyCode),10,10);
			switch (keyCode) {
			case UP:
				y = max(0, y - 1);
				break;
			case DOWN:
				y = min(height, y + 1);
				break;
			case LEFT:
				x = max(0, x - 1);
				break;
			case RIGHT:
				x = min(width, x + 1);
				break;
			}
		}
	}

	public void softkeyPressed(String label) {
		if (label.equals("Shake")) {
			background(200);
		}
	}

}