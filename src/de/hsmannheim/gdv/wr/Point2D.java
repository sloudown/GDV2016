package de.hsmannheim.gdv.wr;

import processing.core.PApplet;

public class Point2D extends PApplet {

	float x, y;
	  
	  Point2D(float x_, float y_) {
	    x = x_;
	    y = y_;
	  }
	  
	  float x() {
	    return x;
	  }
	  
	  float y() {
	    return y;
	  }
	
}
