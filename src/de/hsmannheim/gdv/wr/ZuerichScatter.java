package de.hsmannheim.gdv.wr;

import org.gicentre.utils.stat.*; // For chart classes.
import processing.core.PApplet;


public class ZuerichScatter extends PApplet {

	// Simple scatterplot compating income and life expectancy.

	XYChart scatterplot;
	public void settings() {
		  size(500, 250);
		}
	// Loads data into the chart and customises its appearance.
	public void setup() {
		
		textFont(createFont("Arial", 11), 11);

		// Both x and y data set here.
		scatterplot = new XYChart(this);

		// Load in data from a file
		// (first line of file contains column headings).
		String[] data = loadStrings("data/medeinkommenquartier.csv"); 
		float[] income = new float[data.length - 1];
		float[] lifeExp = new float[data.length - 1];

		for (int i = 1; i < data.length - 1; i++) {
			String[] tokens = data[i + 1].split(",");
			income[i] = Float.parseFloat(tokens[1]);
			lifeExp[i] = Float.parseFloat(tokens[5]);
		}
		scatterplot.setData(income, lifeExp);

		// Axis formatting and labels.
		scatterplot.showXAxis(true);
		scatterplot.showYAxis(true);
		scatterplot.setXFormat("$###,###");
		scatterplot.setXAxisLabel("\nAverage income per person " + "(inflation adjusted $US)");
		scatterplot.setYAxisLabel("Life expectancy at birth (years)\n");

		// Symbol styles
		scatterplot.setPointColour(color(180, 50, 50, 100));
		scatterplot.setPointSize(5);
	}

	// Draws the scatterplot.
	public void draw() {
		background(255);
		scatterplot.draw(20, 20, width - 40, height - 40);
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { ZuerichScatter.class.getName() });
	}
}