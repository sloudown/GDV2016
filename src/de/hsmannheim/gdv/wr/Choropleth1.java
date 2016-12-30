package de.hsmannheim.gdv.wr;

import java.util.List;

import org.gicentre.geomap.*;
import org.gicentre.utils.colour.*;

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

public class Choropleth1 extends PApplet {
	GeoMap geoMap;
	ColourTable myCTable;
	// UnfoldingMap map;
	BarScaleUI barScale;
	Table table;
	int radwegColor = color(255, 0, 0);
	int radstreifenColor = color(0, 0, 153);

	int minColour, maxColour;
	float dataMax;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		myCTable = ColourTable.getPresetColourTable(ColourTable.REDS, 0, 34);
		geoMap = new GeoMap(10, 10, width - 20, height - 40, this);
		geoMap.readFile("world");

		// map = new UnfoldingMap(this);
		// map.zoomAndPanTo(12, new Location(47.37174, 8.54226));
		// MapUtils.createDefaultEventDispatcher(this, map);

		// add a bar scale to your map
		// barScale = new BarScaleUI(this, map, 700, 20);

		// optionally style your bar scale
		PFont myFont = createFont("Monospaced", 12);
		barScale.setStyle(color(60, 120), 6, -2, myFont);

		// List<Feature> quartiere = GeoJSONReader.loadData(this,
		// "data/statistischequartiere.json");
		// List<Marker> quartierMarkers =
		// MapUtils.createSimpleMarkers(quartiere);
		// map.addMarkers(quartierMarkers);

		table = loadTable("quartierdaten_formatiert.csv", "header");

		dataMax = 0;
		for (TableRow row : table.rows()) {
			dataMax = max(dataMax, row.getFloat(2));
		}

		minColour = color(222, 235, 247); // Light blue
		maxColour = color(49, 130, 189); // Dark blue

	}

	public void draw() {
		background(255);
		// Draw countries
		for (int id : geoMap.getFeatures().keySet()) {
			String countryCode = geoMap.getAttributes().getString(id, 2);
			TableRow row = table.findRow(countryCode, 1);

			if (row != null) // Table row matches country code
			{
				float normBadTeeth = row.getFloat(2) / dataMax;
				fill(lerpColor(minColour, maxColour, normBadTeeth));
			} else // No data found in table.
			{
				fill(250);
			}

			geoMap.draw(id); // Draw country
		}

	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Choropleth1.class.getName() });
	}

}
