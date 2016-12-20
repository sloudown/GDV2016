package de.hsmannheim.gdv.wr;

import processing.core.PApplet;

public class Plot extends PApplet {

	 Point2D topLeft, bottomRight;
	  int color;
	 
	  Plot(int x1_, int y1_, int x2_, int y2_, int c_) {
	    topLeft = new Point2D(x1_, y1_);
	    bottomRight = new Point2D(x2_, y2_);
	    color = c_;
	  }
	  
	  void display() {
	    fill(235);
	    noStroke();
	    rectMode(CORNERS);
	    rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
	  }
	  
	  Point2D topLeft() {
	    return topLeft;
	  }
	  
	  Point2D bottomRight() {
	    return bottomRight;
	  }
	  
	  float w() {
	    return bottomRight.x - topLeft.x;
	  }
	  
	  float h() {
	    return bottomRight.y - topLeft.y;
	  }
	
}
