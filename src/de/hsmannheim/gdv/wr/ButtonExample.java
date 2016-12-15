package de.hsmannheim.gdv.wr;

import processing.core.PApplet;

public class ButtonExample extends PApplet {

	public void settings() {
		size(800, 600, P2D);
	}
	
	
	int rectX, rectY;      // Position of square button
	int rectSize = 90;     // Diameter of rect
	int rectColor, baseColor;
	int rectHighlight;
	int currentColor;
	boolean rectOver = false;
	int strokeWeight;

	public void setup() {
	  rectColor = color(0);
	  rectHighlight = color(51);
	  baseColor = color(102);
	  currentColor = baseColor;
	  rectX = width/2-rectSize-10;
	  rectY = height/2-rectSize/2;
	  strokeWeight = 1;
	}

	public void draw() {
	  update(mouseX, mouseY);
	  background(currentColor);
	  
	  strokeWeight(strokeWeight);
	  stroke(0);
	  rect(rectX, rectY, rectSize, rectSize);
	  fill(213);
	  
	  //fuer hover
	   if (rectOver) {
	    fill(0);
	  }
	  
	}

	void update(int x, int y) {
	   if ( overRect(rectX, rectY, rectSize, rectSize) ) {
	    rectOver = true;
	  } else {
	     rectOver = false;
	  }
	}




	public void mousePressed() {

	  if (rectOver) {
	    strokeWeight = 4;
	  }
	}

	boolean overRect(int x, int y, int width, int height)  {
	  if (mouseX >= x && mouseX <= x+width && 
	      mouseY >= y && mouseY <= y+height) {
	    return true;
	  } else {
	    return false;
	  }
	}
	
	
	
	public static void main(String args[]) {
		PApplet.main(new String[] { ButtonExample.class.getName() });
	}
	
}
