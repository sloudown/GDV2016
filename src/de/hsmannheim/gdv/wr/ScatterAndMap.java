package de.hsmannheim.gdv.wr;

import java.util.List;

import org.gicentre.utils.stat.XYChart;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.ui.BarScaleUI;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;

public class ScatterAndMap extends PApplet {

	// ==== Map ====
	UnfoldingMap map;
	UnfoldingMap smallMap;
	BarScaleUI barScale;
	int radwegColor = color(255, 0, 0);
	int radstreifenColor = color(0, 0, 153);
	String quartierLabel = "";

	Marker selectedDistrictMarker = null;

	// ==== Scatterplot =====
	XYChart scatterplot;
	Table table;
	float[] einwohner;
	float[] radwegeLaenge;
	String[] quartiernamen;

	// koordinaten fuer hoverpoint
	float hoverX;
	float hoverY;

	// koordinaten fuer hover label
	float hoverLabelX = 0;
	float hoverLabelY = 0;
	String hoverLabel = "";

	public void settings() {
		size(1400, 600, P2D);
	}

	// Loads data into the chart and customizes its appearance.
	public void setup() {

		// ==== MAP ====
		map = new UnfoldingMap(this, "map1", 0, 0, 800, 600);
		map.zoomAndPanTo(12, new Location(47.37174, 8.54226));
		MapUtils.createDefaultEventDispatcher(this, map);
		List<Feature> quartiere = GeoJSONReader.loadData(this, "data/statistischequartiere.json");
		List<Marker> quartierMarkers = MapUtils.createSimpleMarkers(quartiere);
		for (Marker m : quartierMarkers) {
			m.setColor(color(60, 150));
		}
		map.addMarkers(quartierMarkers);
		
		// ==== SMALL MAP ====
		smallMap = new UnfoldingMap(this, "smallMap", 820, 420, 200, 200);
		smallMap.zoomAndPanTo(12, new Location(47.37174, 8.54226));

		// Radwege einzeichnen
		List<Feature> radwege = GeoJSONReader.loadData(this, "radwege_koordinaten.geojson");
		List<Marker> radwegMarkers = MapUtils.createSimpleMarkers(radwege);
		smallMap.addMarkers(radwegMarkers);

		for (Marker marker : radwegMarkers) {
			marker.setColor(radwegColor);
			marker.setStrokeWeight(1);
			marker.setStrokeColor(color(0, 0, 0, 0));
		}

		// Radstreifen einzeichnen
		List<Feature> radstreifen = GeoJSONReader.loadData(this, "radstreifen_koordinaten.geojson");
		List<Marker> radstreifenMarkers = MapUtils.createSimpleMarkers(radstreifen);
		smallMap.addMarkers(radstreifenMarkers);

		for (Marker marker : radstreifenMarkers) {
			marker.setColor(radstreifenColor);
			marker.setStrokeWeight(1);
			marker.setStrokeColor(color(0, 0, 0, 0));
		}
		// ==== MISC ====
		// add a bar scale to your map
		barScale = new BarScaleUI(this, map, 700, 20);

		// optionally style your bar scale
		PFont myFont = createFont("Monospaced", 12);
		barScale.setStyle(color(60, 120), 6, -2, myFont);

		// ==== SCATTERPLOT =====
		textFont(createFont("Arial", 11), 11);

		// Both x and y data set here.
		scatterplot = new XYChart(this);

		// Load in data from a file
		table = loadTable("quartierdaten_formatiert.csv", "header");

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
		scatterplot.setXFormat("###,###");
		scatterplot.setXAxisLabel("Anzahl der Einwohner (Angabe in 1000)");
		scatterplot.setYAxisLabel("Länge der Radwege un Meter");

		// Symbol styles
		scatterplot.setPointColour(color(180, 50, 50, 200));
		scatterplot.setPointSize(7);

	}

	// Draws the scatterplot.
	public void draw() {
		background(255);

		map.draw();
		barScale.draw();

		smallMap.draw();

		fill(255);
		text(quartierLabel, mouseX, mouseY);

		scatterplot.draw(800, 0, 600, 400);

		// hover marker
		fill(0);
		ellipse(hoverX, hoverY, 10, 10);
		text(hoverLabel, hoverLabelX, hoverLabelY);

	}

	public void mousePressed() {

		selectedDistrictMarker = map.getFirstHitMarker(mouseX, mouseY);
		if (selectedDistrictMarker != null) {
			smallMap.getDefaultMarkerManager().clearMarkers();
			// selectedDistrictMarker.setColor(color(255, 10));
			smallMap.addMarker(selectedDistrictMarker);
			smallMap.zoomAndPanToFit(selectedDistrictMarker);
			String name = selectedDistrictMarker.getStringProperty("Quartiername");
			text(name, 600, 500);
		}

	}

	public void mouseMoved() {
		// damit nicht deselected wird wenn im Scatter gehovert wird
		if (mouseX <= 800 && mouseY <= 600) {
			for (Marker marker : map.getMarkers()) {
				marker.setSelected(false);
			}
			Marker marker = map.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				marker.setSelected(true);
				quartierLabel = (String) marker.getProperty("Quartiername");
			} else {
				quartierLabel = "";
			}
		}

		selectScatterPoint();
		findSelectedQuartier();
	}

	// markiert einen punkt im Scatterplot wenn darauf geklickt wurde und zeigt
	// auch den Quartiernamen an
	void selectScatterPoint() {
		PVector mousePoint = new PVector(mouseX, mouseY);
		for (int i = 0; i < einwohner.length; i++) {
			PVector koordinate = new PVector(einwohner[i], radwegeLaenge[i]);
			PVector koordinateOnScreen = scatterplot.getDataToScreen(koordinate);
			if (dist(mouseX, mouseY, koordinateOnScreen.x, koordinateOnScreen.y) <= 10) {
				hoverX = koordinateOnScreen.x;
				hoverY = koordinateOnScreen.y;
				hoverLabelX = koordinateOnScreen.x;
				hoverLabelY = koordinateOnScreen.y;
				hoverLabel = quartiernamen[i];
			}
		}
	}

	// sucht und markiert quartier in der karte, wellches in Scatterplot
	// angeklickt wurde
	void findSelectedQuartier() {
		String quartiernameMap;
		String quartiernameScatter = hoverLabel;
		Marker selectedMarker;
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
			quartiernameMap = (String) marker.getProperty("Quartiername");
			if (quartiernameMap.equals(quartiernameScatter)) {
				marker.setSelected(true);
			}
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { ScatterAndMap.class.getName() });
	}

}
