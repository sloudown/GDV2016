package de.hsmannheim.gdv.wr;

import org.gicentre.utils.stat.*; // For chart classes.

public class ZuerichScatter extends PApplet{

	// Simple scatterplot compating income and life expectancy.

	XYChart scatterplot;

	// Loads data into the chart and customises its appearance.
	void setup() {
		size(500, 250);
		textFont(createFont("Arial", 11), 11);

		// Both x and y data set here.
		scatterplot = new XYChart(this);

		// Load in data from a file
		// (first line of file contains column headings).
		String[] data = loadStrings("ukIncomeAndLifeExp.csv"); //aendern!!!
		float[] income = new float[data.length - 1];
		float[] lifeExp = new float[data.length - 1];

		for (int i = 0; i < data.length - 1; i++) {
			String[] tokens = data[i + 1].split(",");
			income[i] = Float.parseFloat(tokens[1]);
			lifeExp[i] = Float.parseFloat(tokens[2]);
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
	void draw() {
		background(255);
		scatterplot.draw(20, 20, width - 40, height - 40);
	}
}