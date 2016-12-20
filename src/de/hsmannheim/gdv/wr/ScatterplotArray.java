package de.hsmannheim.gdv.wr;

import processing.core.PApplet;

public class ScatterplotArray extends PApplet {

	Plot plot;
	Bubble[] bubbles = new Bubble[40];

	int leftMargin = 20;
	int rightMargin = 20;
	int topMargin = 100;
	int bottomMargin = 100;

	int minRadius = 10;
	int maxRadius = 30;
	
	public void settings() {
		size(750, 400, P2D);
	}

	public void setup() {
	  smooth();
	  frameRate(30);
	  
	  // initialize plot
	  plot = new Plot(leftMargin, topMargin, width-rightMargin, height-topMargin, 235);
	  
	  // plot 5 bubbles
	  generateValues();
	}

	public void draw() {
	  background(255);
	  // display plot
	  plot.display();
	  
	  // display bubbles
	  for (int i = 0; i < bubbles.length; i++) {
	    bubbles[i].display();
	  }
	  
	  // display labels
	  for (int i = 0; i < bubbles.length; i++) {
	    bubbles[i].displayLabel();
	  }
	}

	public void keyPressed() {
	  generateValues();
	}

	void generateValues() {
	  for (int i = 0; i < bubbles.length; i++) {
	    // randomize x y coords
	    float x = random(maxRadius, plot.w() - maxRadius);
	    float y = random(maxRadius, plot.h() - maxRadius);
	    int color = color(random(255), random(255), random(255), 200);
	    float r = random(minRadius, maxRadius);
	    
	    bubbles[i] = new Bubble(x + plot.topLeft.x, y + plot.topLeft.y, r, color);
	  }
	}
	public static void main(String args[]) {
		PApplet.main(new String[] { ScatterplotArray.class.getName() });
	}
}
