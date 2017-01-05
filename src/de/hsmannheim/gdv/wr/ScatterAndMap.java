package de.hsmannheim.gdv.wr;

import java.util.List;

import org.gicentre.utils.colour.ColourTable;
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
import org.gicentre.utils.colour.*;    // For colour tables.


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
	String[] infos;

	// koordinaten fuer hoverpoint
	float hoverX;
	float hoverY;

	// koordinaten fuer hover label
	float hoverLabelX = 0;
	float hoverLabelY = 0;
	String hoverLabel = "";
	
	
	// koordinaten fuer clicked hover label
	float clickedHoverLabelX = 0;
	float clickedHoverLabelY = 0;
	String clickedHoverLabel = "";
	
	// ColorTable
	ColourTable cTable1;

	public void settings() {
		size(1600, 600, P2D);
	}

	// Loads data into the chart and customizes its appearance.
	public void setup() {
		
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
		
		int shortest = (int) getShortestBikelane()-1;
		int longest = (int) getLongestBikelane()+1;
		cTable1 = ColourTable.getPresetColourTable(ColourTable.YL_OR_RD,shortest,longest);

		// ==== MAP ====
		map = new UnfoldingMap(this, "map1", 0, 0, 800, 600);
		map.zoomAndPanTo(12, new Location(47.37174, 8.54226));
		MapUtils.createDefaultEventDispatcher(this, map);
		List<Feature> quartiere = GeoJSONReader.loadData(this, "data/statistischequartiere.json");
		List<Marker> quartierMarkers = MapUtils.createSimpleMarkers(quartiere);
		for (Marker m : quartierMarkers) {
			int indexOfQuartier = findeIndexOfQuartierByName((String)m.getProperty("Quartiername"));
			int radwegLaenge = (int) radwegeLaenge[indexOfQuartier];
			m.setColor(color(cTable1.findColour(radwegLaenge), 150));
		}
		map.addMarkers(quartierMarkers);

		// ==== SMALL MAP ====
		smallMap = new UnfoldingMap(this, "smallMap", 820, 420, 200, 200);
		smallMap.zoomAndPanTo(12, new Location(47.37174, 8.54226));		
		drawBikeLanes();

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
		text(quartierLabel, mouseX, mouseY-5);

		scatterplot.draw(800, 0, 600, 400);

		// hover marker scatter
		if(!hoverLabel.equals("")) {
		fill(0);
		ellipse(hoverX, hoverY, 10, 10);
		text(hoverLabel, hoverLabelX, hoverLabelY);
		}
		
		if(!clickedHoverLabel.equals("")) {
			fill(0);
			ellipse(clickedHoverLabelX, clickedHoverLabelY, 10, 10);
			text(clickedHoverLabel, clickedHoverLabelX+8, clickedHoverLabelY);
			}
		drawDetails();

	}

	public void mousePressed() {
		
		//if (mouseX <= 800 && mouseY <= 600) {
			for (Marker marker : map.getMarkers()) {
				marker.setSelected(false);
			}
			Marker marker = map.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				marker.setSelected(true);
				quartierLabel = (String) marker.getProperty("Quartiername");
				selectScatterPointByName((String) marker.getProperty("Quartiername"));
			} else {
				quartierLabel = "";
			}
			
		//}
		
		
		 marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			selectScatterPointByName((String) marker.getProperty("Quartiername"));
		} else {
			selectScatterPointByName("");
		}
		

		selectDistrictMarker();
	}

	public void mouseMoved() {
		// damit nicht deselected wird wenn im Scatter gehovert wird
		if (mouseX <= 800 && mouseY <= 600) {
			for (Marker marker : map.getMarkers()) {
				//marker.setSelected(false);
				marker.setStrokeWeight(1);
			}
			Marker marker = map.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				//marker.setSelected(true);
				marker.setStrokeWeight(3);
				quartierLabel = (String) marker.getProperty("Quartiername");
			} else {
				quartierLabel = "";
			}
		}
		
		deselectScatterPoint();
		selectScatterPoint();
		findSelectedQuartierFromScatter();
		findSelectedQuartierFromMap();

	}

	void drawBikeLanes() {
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
	}

	void selectDistrictMarker(){
		Marker selectedDistrictMarkerOriginal = map.getFirstHitMarker(mouseX, mouseY);
		 selectedDistrictMarker = selectedDistrictMarkerOriginal;
		if (selectedDistrictMarker != null) {
			smallMap.getDefaultMarkerManager().clearMarkers(); // cleared aber
																// auch die
																// radwege weg
			// selectedDistrictMarker.setColor(color(255, 10));
			smallMap.addMarkers(selectedDistrictMarker);
			smallMap.zoomAndPanToFit(selectedDistrictMarker);
			drawBikeLanes();
			String name = selectedDistrictMarker.getStringProperty("Quartiername");
			//muss in draw verschoben werden sonst blinkt es nur kurz auf- wird nur einmal gezeichnet
			text(name, 600, 500);
		}
	}
	
	// markiert einen punkt im Scatterplot, wenn darauf geklickt wurde und zeigt
	// auch den Quartiernamen an
	void selectScatterPoint() {
		PVector mousePoint = new PVector(mouseX, mouseY);
		PVector temp = null;
		int tempI = -1;
		for (int i = 0; i < einwohner.length; i++) {
			PVector koordinate = new PVector(einwohner[i], radwegeLaenge[i]);
			PVector koordinateOnScreen = scatterplot.getDataToScreen(koordinate);

			if (dist(mouseX, mouseY, koordinateOnScreen.x, koordinateOnScreen.y) <= 10) {
				if(temp == null) {
					temp = new PVector(koordinateOnScreen.x, koordinateOnScreen.y);
					tempI = i;
				} else if(dist(mouseX, mouseY, koordinateOnScreen.x, koordinateOnScreen.y) < dist(mouseX, mouseY, temp.x, temp.y)) {
					temp = new PVector(koordinateOnScreen.x, koordinateOnScreen.y);
					tempI = i;
					
				}
				hoverX = temp.x;
				hoverY = temp.y;
				hoverLabelX = temp.x+8;
				hoverLabelY = temp.y;
				hoverLabel = quartiernamen[tempI];
			} 
		}

	}
	
	void selectScatterPointByName(String name) {
		
		int indexOfQuartier = findeIndexOfQuartierByName(name);
		PVector koordinateOnScreen = null;
		
		if(indexOfQuartier > -1) {
			PVector koordinate = new PVector(einwohner[indexOfQuartier], radwegeLaenge[indexOfQuartier]);
			 koordinateOnScreen = scatterplot.getDataToScreen(koordinate);
			 
			
			 clickedHoverLabelX = koordinateOnScreen.x;
			 clickedHoverLabelY = koordinateOnScreen.y;
			 clickedHoverLabel = quartiernamen[indexOfQuartier];

		} else {
			clickedHoverLabel = "";
		}
				
				
			
		
	}
	
	void deselectScatterPoint() {
		hoverX = 0;
		hoverY = 0;
		hoverLabelX = 0;
		hoverLabelY = 0;
		hoverLabel = "";
	}

	// sucht und markiert quartier in der karte, welches in Scatterplot
	// angeklickt wurde
	void findSelectedQuartierFromScatter() {
		if(mouseX >= 800 && mouseY <= 400){
		String quartiernameMap;
		String quartiernameScatter = hoverLabel;
		Marker selectedMarker;
		for (Marker marker : map.getMarkers()) {
			//marker.setSelected(false);
			marker.setStrokeWeight(1);
			quartiernameMap = (String) marker.getProperty("Quartiername");
			if (quartiernameMap.equals(quartiernameScatter)) {
				//marker.setSelected(true);
				marker.setStrokeWeight(3);

			}
		}
		}
	}

	int findeIndexOfSelectedQuartier() {
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		int indexOfSelectedQuartier = -1;
		if (marker != null) {
			String quartiername = (String) marker.getProperty("Quartiername");
			
			for (int i = 0; i< quartiernamen.length; i++) {
				if (quartiername.equals(quartiernamen[i])) {
					indexOfSelectedQuartier = i;
				}
			}
		}
		return indexOfSelectedQuartier;
	}
	
	int findeIndexOfQuartierByName(String name) {
		
		int indexOfQuartier = -1;
		
			
			for (int i = 0; i< quartiernamen.length; i++) {
				if (name.equals(quartiernamen[i])) {
					indexOfQuartier = i;
				}
			}
		
		return indexOfQuartier;
	}
	
	
	//beim hover ueber die quartieren wird quartier im Scatter auch markiert
	void findSelectedQuartierFromMap() {

			int indexOfSelectedQuartier = findeIndexOfSelectedQuartier();
	
			if(indexOfSelectedQuartier > -1) {
				
				PVector koordinate = new PVector(einwohner[indexOfSelectedQuartier], radwegeLaenge[indexOfSelectedQuartier]);
				PVector koordinateOnScreen = scatterplot.getDataToScreen(koordinate);
			
					hoverX = koordinateOnScreen.x;
					hoverY = koordinateOnScreen.y;
					hoverLabelX = koordinateOnScreen.x+8;
					hoverLabelY = koordinateOnScreen.y;
					hoverLabel = quartiernamen[indexOfSelectedQuartier];
			}

		 

	}
	
	void drawDetails() {
			
			float radwege = 0;
			float einwohner = 0;
			int indexOfSelectedQuartier = findeIndexOfSelectedQuartier();
			
			if(indexOfSelectedQuartier > -1) {
				radwege = radwegeLaenge[indexOfSelectedQuartier];
				einwohner = this.einwohner[indexOfSelectedQuartier];
				
				fill(123,123,123);
				text("Radwege",1050, 450 );
				rect(1150, 435,radwege/100, 20);
				
				text("Einwohner", 1050, 480);
				rect(1150, 465, einwohner/100, 20);
				
				float longestRadweg = getLongestBikelane();
				float highestEinwohnerzahl = getHighestPopulation();
				
				fill(color(60, 150));
				rect(1150, 435, longestRadweg/100, 20);
				text((int) longestRadweg+ " Meter", 1150+(longestRadweg/100) +20, 450);
				rect(1150, 465, highestEinwohnerzahl/100, 20);
				text((int) highestEinwohnerzahl,1150+(highestEinwohnerzahl/100)+20, 480);
				
				
				
				String quartiername = quartiernamen[indexOfSelectedQuartier];
				String zusatzinfo ="";
				
				switch (quartiername) {
				case "Escher Wyss": 
					zusatzinfo = "Einwohner sind hier vermutlich eher gering weil es sehr viele öffentliche Gebäude\n wie Hotels, Museen und Theater gibt";
					break;
				case "Enge" : 
					zusatzinfo = "keine Ahnung";
					break;

				default:
					zusatzinfo = "";
				}
				
				text("Info:\n" + zusatzinfo, 1050, 510);
			}

		
	}
	
	float getLongestBikelane() {
		float laengste = 0;
		for(float radweg : radwegeLaenge) {
			if(laengste < radweg) {
				laengste = radweg;
			}
		}
		return laengste;
	}
	
	float getShortestBikelane() {
		float kuerzeste = 99999999;
		for(float radweg : radwegeLaenge) {
			if(kuerzeste > radweg) {
				kuerzeste = radweg;
			}
		}
		return kuerzeste;
	}
	
	float getHighestPopulation() {
		float highest = 0;
		for(float popultation : einwohner) {
			if(highest < popultation) {
				highest = popultation;
			}
		}
		return highest;
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { ScatterAndMap.class.getName() });
	}

}
