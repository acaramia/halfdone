import processing.core.*;

public class shapeprimitives extends PMIDlet {

	public void setup() {// Shape Primitives
	// by REAS <http://reas.com>
	//
	// The basic shape primitive functions are triangle(), rect(), quad(),
	// and ellipse(). Squares are made with rect() and circles are made
	// with ellise(). Each of these functions requires a number of parameters
	// which determines their position and size.
	//
	// Created 19 January 2003

		background(0);
		noStroke();
		fill(226);
		triangle(10, 10, 10, 200, 45, 200);
		rect(45, 45, 35, 35);
		quad(105, 10, 120, 10, 120, 200, 80, 200);
		ellipse(140, 80, 40, 40);
		triangle(160, 10, 195, 200, 160, 50);

		noLoop();
	}
}