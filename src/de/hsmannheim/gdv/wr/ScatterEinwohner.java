package de.hsmannheim.gdv.wr;

import org.gicentre.utils.stat.XYChart;

import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;


public class ScatterEinwohner extends PApplet {

	// Simple scatterplot compating income and life expectancy.

	XYChart scatterplot;
	Table table;
	float[] einwohner;
	float[] radwegeLaenge;
	String[] quartiernamen;
	
	//koordinaten fuer hoverpoint
	float hoverX;
	float hoverY;
	
	//koordinaten fuer haver label
	float hoverLabelX = 0;
	float hoverLabelY = 0;
	String hoverLabel = "";

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


		 einwohner = new float[table.getRowCount()];
		 radwegeLaenge = new float[table.getRowCount()];
		 quartiernamen = new String[table.getRowCount()];
		int reihe = 0;
		for (TableRow row : table.rows()) {

			float einwohnerzahl = row.getFloat("einwohneranzahl");
			float radlaenge = row.getFloat("sum_streifen_wege");
			String quartiername = row.getString("Quartiername");
			
			einwohner[reihe] = einwohnerzahl;
			radwegeLaenge[reihe] = radlaenge;
			quartiernamen[reihe] = quartiername;
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
		
		//hover marker
		fill(0);
		ellipse(hoverX, hoverY, 10, 10);
		text(hoverLabel, hoverLabelX, hoverLabelY);
	
		
	}
	
	public void mousePressed() {
		PVector mousePoint = new PVector(mouseX, mouseY);
		
		for (int i=0; i<einwohner.length; i++) {
			PVector koordinate = new PVector(einwohner[i],radwegeLaenge[i]);
			PVector koordinateOnScreen = scatterplot.getDataToScreen(koordinate);
			if (dist(mouseX, mouseY, koordinateOnScreen.x, koordinateOnScreen.y) <= 5) {
				hoverX = koordinateOnScreen.x;
				hoverY = koordinateOnScreen.y;
				hoverLabelX = koordinateOnScreen.x;
				hoverLabelY = koordinateOnScreen.y;
				hoverLabel = quartiernamen[i];
			}
		}
		
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { ScatterEinwohner.class.getName() });
	}

}
