package de.hsmannheim.gdv.wr;

import processing.core.PApplet;

public class Bubble extends PApplet {

	
		  float x, y, r;
		  int color;
		 
		  Bubble(float x_, float y_, float r_, int c_) {
		    x = x_;
		    y = y_;
		    r = r_;
		    color = c_;
		  }
		  
		  void display() {
		    
		    // check if mouseover
		    if (dist(mouseX, mouseY, x, y) <= r) {
		      fill(0);
		    } else {
		      fill(color);
		    }
		    
		    noStroke();
		    ellipse(x, y, r*2, r*2);
		  }
		  
		  void displayLabel() {
		    if (dist(mouseX, mouseY, x, y) <= r) {
		      String txt = "Value: " + (int)(r) + ", x: " + (int)(x) + ", y: " + (int)(y);
		      Label label = new Label(txt, mouseX, mouseY);
		    }
		  }
		
	
}
