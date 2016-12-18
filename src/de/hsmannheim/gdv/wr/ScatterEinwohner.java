package de.hsmannheim.gdv.wr;

import org.gicentre.utils.stat.XYChart;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class ScatterEinwohner extends PApplet {

	// Simple scatterplot compating income and life expectancy.

	XYChart scatterplot;
	Table table;

	public void settings() {
		size(750, 400, P2D);
	}

	// Loads data into the chart and customises its appearance.
	public void setup() {

		textFont(createFont("Arial", 11), 11);

		// Both x and y data set here.
		scatterplot = new XYChart(this);

		// Load in data from a file
		table = loadTable("quartierdaten_formatiert.csv","header");


		float[] einwohner = new float[table.getRowCount()];
		float[] radwegeLaenge = new float[table.getRowCount()];
		int reihe = 0;
		for (TableRow row : table.rows()) {

			float einwohnerzahl = row.getFloat("einwohneranzahl");
			float radlaenge = row.getFloat("sum_streifen_wege");

			einwohner[reihe] = einwohnerzahl;
			radwegeLaenge[reihe] = radlaenge;
			reihe++;
		}

		scatterplot.setData(einwohner, radwegeLaenge);

		// Axis formatting and labels.
		scatterplot.showXAxis(true);
		scatterplot.showYAxis(true);
		scatterplot.setXFormat("$###,###");
		scatterplot.setXAxisLabel("Anzahl der Einwohner (Angabe in 1000)");
		scatterplot.setYAxisLabel("Länge der Radwege un Meter");

		// Symbol styles
		scatterplot.setPointColour(color(180, 50, 50, 200));
		scatterplot.setPointSize(7);
	}

	// Draws the scatterplot.
	public void draw() {
		background(255);
		scatterplot.draw(20, 20, width - 40, height - 40);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { ScatterEinwohner.class.getName() });
	}

}
