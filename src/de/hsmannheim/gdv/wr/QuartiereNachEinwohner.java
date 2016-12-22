package de.hsmannheim.gdv.wr;

import java.util.List;

import org.gicentre.utils.colour.ColourTable;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.ui.BarScaleUI;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PFont;
import processing.data.Table;
import processing.data.TableRow;

public class QuartiereNachEinwohner extends PApplet {

	ColourTable myCTable;
	UnfoldingMap map;
	BarScaleUI barScale;
	Table table;
	int radwegColor = color(255, 0, 0);
	int radstreifenColor = color(0, 0, 153);

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		myCTable = ColourTable.getPresetColourTable(ColourTable.REDS, 0, 34);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(12, new Location(47.37174, 8.54226));
		MapUtils.createDefaultEventDispatcher(this, map);

		// add a bar scale to your map
		barScale = new BarScaleUI(this, map, 700, 20);

		// optionally style your bar scale
		PFont myFont = createFont("Monospaced", 12);
		barScale.setStyle(color(60, 120), 6, -2, myFont);

		List<Feature> quartiere = GeoJSONReader.loadData(this, "data/statistischequartiere.json");
		List<Marker> quartierMarkers = MapUtils.createSimpleMarkers(quartiere);
		map.addMarkers(quartierMarkers);

		table = loadTable("quartierdaten_formatiert.csv", "header");

		for (Marker marker : quartierMarkers) {
			String quartiername = (String) marker.getProperty("Quartiername");
			TableRow reihe = table.findRow(quartiername, "Quartiername");
			float einwohnerProKM2 = reihe.getFloat("einwohner_pro_km2");
			float value = map(einwohnerProKM2, 1300, 13200, 0, 255);
			marker.setColor(color(value, 0, 0));
			
		}

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

	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null)
			marker.setSelected(true);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { QuartiereNachEinwohner.class.getName() });
	}

}
