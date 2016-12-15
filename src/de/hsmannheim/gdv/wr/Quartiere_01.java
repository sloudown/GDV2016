package de.hsmannheim.gdv.wr;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;  
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import java.util.Map;

public class Quartiere_01 extends PApplet {

	UnfoldingMap map;
	Location zuerichLocation = new Location(47.372784, 8.540490);
	int quartierColor = color(255, 0, 0, 50);

	
	public void settings() {
		size(800, 600, P2D);
	}
	
	
	
	public void setup() {
		  size(800, 600);
		  // map = new UnfoldingMap(this, new Google.GoogleMapProvider()); ging iwann net mehr
		  map = new UnfoldingMap(this, new OpenStreetMap.OpenStreetMapProvider());
		  map.zoomAndPanTo(zuerichLocation, 12);
		  MapUtils.createDefaultEventDispatcher(this, map);

		  //  List<Feature> quartiere = GeoJSONReader.loadData(this, "https://data.stadt-zuerich.ch/dataset/statistisches_quartier/resource/c837926e-035d-48b9-8656-03f1b13c323b/download/statistischequartiere.json");
		  //  List<Marker> quartiereMarkers = MapUtils.createMarkers(quartiere);
		  //  map.addMarkers(quartiereMarkers);


		  List<Feature> quartiere = GeoJSONReader.loadData(this, "https://data.stadt-zuerich.ch/dataset/statistisches_quartier/resource/c837926e-035d-48b9-8656-03f1b13c323b/download/statistischequartiere.json");
		  List<Marker> quartierMarkers = new ArrayList<Marker>();
		  for (Feature feature : quartiere) {
		    ShapeFeature polygonFeature = (ShapeFeature) feature;
		    SimplePolygonMarker m = new SimplePolygonMarker(polygonFeature.getLocations());
		    m.setColor(quartierColor);
		    m.setStrokeWeight(1);
		    m.setStrokeColor(color(0, 0, 0, 0));
		    quartierMarkers.add(m);

		    List<Location> listLocation= polygonFeature.getLocations();

		    for (Location loc : listLocation) {
		      print("xkoori"+loc.x );
		    }

		    println(polygonFeature.getLocations());
		    println();
		    println();
		  }
		  map.addMarkers(quartierMarkers);


		  /*
		    List<Feature> radwege = GeoJSONReader.loadData(this, "countries.geo.json");
		   List<Marker> radwegMarkers = new ArrayList<Marker>();
		   for (Feature feature : radwege) {
		   ShapeFeature lineFeature = (ShapeFeature) feature;
		   SimpleLinesMarker m2 = new SimpleLinesMarker(lineFeature.getLocations());
		   m2.setColor(color(255, 0, 0, 250));
		   m2.setStrokeWeight(10);
		   //m2.setStrokeColor(color(112,222,111,0));
		   radwegMarkers.add(m2);
		   }
		   map.addMarkers(radwegMarkers);
		   */
		  //map.getMarkers() geht auch 
		  List<Marker> quartierMarkers2 = quartierMarkers;
		  int colorNr = 1;
		  //durchlaeuft quartiere und faerbt sie ein
		  for ( Marker marker : quartierMarkers2) {
		    // range 0-40
		    float alpha = map(colorNr, 0, 40, 10, 255);
		    marker.setColor(color(255, 0, 0, alpha));
		    colorNr++;
		    HashMap<String, Object> mapProp = marker.getProperties();


		    // soll propertie von einem quartier ausgeben
		    //    for (Map.Entry me : mapProp.entrySet()) {
		    // print(me.getKey() + " is ");
		    // println(me.getValue());
		    //}
		    // name des qurtier rausfinden gib taber immer einen nullpointer exception
		    //String quartierName = marker.getStringProperty("Quartiername");
		    //println(quartierName);
		  }
		}

		public void draw() {
		  map.draw();
		}


		public void mouseMoved() {
		  Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		  if (hitMarker != null) {
		    // Deselect all other markers
		    for (Marker marker : map.getMarkers ()) {
		      marker.setSelected(false);
		    }
		    // Select current marker 
		    hitMarker.setSelected(true);
		  } else {
		    // Deselect all other markers
		    for (Marker marker : map.getMarkers ()) {
		      marker.setSelected(false);
		    }
		  }
		}



		public void mousePressed() {
		  Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);

		  if (hitMarker != null) {
		    //ScreenPosition markerPos = hitMarker.getScreenPosition(map);
		    //map.zoomAndPanTo(zuerichLocation, 14);
		  }
		}







		

	
	
	
	public static void main(String[] args) {
		PApplet.main(new String[] { Quartiere_01.class.getName() });
	}
}







/**
 * Displays the polygon ID as label at the geometric center of its shape. For this, it uses the geo-spatial centroid of
 * the polygon, and converts it to object coordinates, to be consistent to other marker draw methods.
 */
/*public class CentroidLabelMarker extends SimplePolygonMarker {

  public CentroidLabelMarker(List<Location> locations) {
    super(locations);
  }

  // Overrides the method with map, as we need to convert the centroid location ourself.
  @Override
    protected void draw(PGraphics pg, List<MapPosition> mapPositions, HashMap<String, Object> properties, 
  UnfoldingMap map) {

    // Polygon shape is drawn by the SimplePolygonMarker
    super.draw(pg, mapPositions, properties, map);

    // Draws the country code at the centroid of the polygon
    if (getId() != null) {
      pg.pushStyle();

      // Gets geometric center as geo-location
      Location centerLocation = getCentroid();
      // Converts geo-location to position on the map (NB: Not the screen!)
      float[] xy = map.mapDisplay.getObjectFromLocation(centerLocation);
      int x = Math.round(xy[0] - pg.textWidth(getId()) / 2);
      int y = Math.round(xy[1] + 6);

      // Draws label
      pg.fill(255);
      pg.text(getId(), x, y);
      pg.popStyle();
    }
  }
}
*/