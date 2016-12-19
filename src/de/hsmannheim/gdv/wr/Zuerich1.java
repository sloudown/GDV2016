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
	int radwegColor = color(255, 0, 0);
	int radstreifenColor = color(0, 0, 153 );
	String quartierLabel = "";

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
		
		List<Feature> countries = GeoJSONReader.loadData(this, "data/statistischequartiere.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		
		  // Radwege einzeichnen
		  List<Feature> radwege = GeoJSONReader.loadData(this, "radwege_koordinaten.geojson");
		  List<Marker> radwegMarkers = MapUtils.createSimpleMarkers(radwege);
		  map.addMarkers(radwegMarkers); 
		  
		  
		  for (Marker marker : radwegMarkers) {
			  	marker.setColor(radwegColor);
			    marker.setStrokeWeight(1);
			    marker.setStrokeColor(color(0, 0, 0, 0));
		  }
		  
		  // Radstreifen einzeichnen
		  List<Feature> radstreifen = GeoJSONReader.loadData(this, "radstreifen_koordinaten.geojson");
		  List<Marker> radstreifenMarkers = MapUtils.createSimpleMarkers(radstreifen);
		  map.addMarkers(radstreifenMarkers); 
		  
		  
		  for (Marker marker : radstreifenMarkers) {
			  	marker.setColor(radstreifenColor);
			    marker.setStrokeWeight(1);
			    marker.setStrokeColor(color(0, 0, 0, 0));
		  }
		  
		
	}

	public void draw() {
		background(160);
		map.draw();
		barScale.draw();
		
		fill(255);
		text(quartierLabel, mouseX, mouseY);
	}

	public void keyPressed() {
		if (key == '+')
			map.zoomIn();
		if (key == '-')
			map.zoomOut();
	}
	
	public void mouseMoved() {
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

	public static void main(String args[]) {
		PApplet.main(new String[] { Zuerich1.class.getName() });
	}
}
