package de.hsmannheim.gdv.wr;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class RadwegeUndQuartiere extends PApplet {

	
	
	UnfoldingMap map;  
	Location zuerichLocation = new Location(47.372784, 8.540490);
	int radwegColor = color(255, 0, 0); //rot
	int radstreifenColor = color(0, 0, 153 );
	int quartierColor = color(2, 121, 0, 50);
	
	public void settings() {
		size(800, 600, P2D);
	}
	
	
	public void setup() {
		  size(800, 600);
		  map = new UnfoldingMap(this);
		  map.zoomAndPanTo(12, new Location(zuerichLocation));
		  MapUtils.createDefaultEventDispatcher(this, map);   
		  
		  
			List<Feature> quartiere = GeoJSONReader.loadData(this, "data/statistischequartiere.json");
			List<Marker> quartierMarkers = MapUtils.createSimpleMarkers(quartiere);
			for (Marker m : quartierMarkers) {
				
				m.setColor(color(120, 150));
			}
			map.addMarkers(quartierMarkers);
		  
		  
		  
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
		  map.draw();
		}

		public static void main(String[] args) {
			PApplet.main(new String[] { RadwegeUndQuartiere.class.getName() });
		}
	
}
