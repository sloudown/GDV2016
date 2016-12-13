package de.hsmannheim.gdv.wr;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.ui.BarScaleUI;
import de.fhpotsdam.unfolding.utils.MapUtils;


public class Zuerich1 extends PApplet{
	UnfoldingMap map;
	BarScaleUI barScale;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(12, new Location(47.37174, 8.54226));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// add a bar scale to your map
		barScale = new BarScaleUI(this, map, 700, 20);

		// optionally style your bar scale
		PFont myFont = createFont("Monospaced", 12);
		barScale.setStyle(color(60, 120), 6, -2, myFont);
		
		List<Feature> countries = GeoJSONReader.loadData(this, "data/anzahlveloparkplatz.geojson");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
	}

	public void draw() {
		background(160);
		map.draw();
		barScale.draw();
	}

	public void keyPressed() {
		if (key == '+')
			map.zoomIn();
		if (key == '-')
			map.zoomOut();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Zuerich1.class.getName() });
	}
}
