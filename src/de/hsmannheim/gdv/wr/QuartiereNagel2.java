package de.hsmannheim.gdv.wr;

import java.util.List;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import java.util.*;

public class QuartiereNagel2 extends PApplet {

	UnfoldingMap map;
	HashMap<String, DataEntry> dataEntriesMap;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		size(800, 600);
		map = new UnfoldingMap(this, new OpenStreetMap.OpenStreetMapProvider());
		map.zoomAndPanTo(12, new Location(47.37, 8.54));
		MapUtils.createDefaultEventDispatcher(this, map);
		List<Feature> supermarkets = GeoJSONReader.loadData(this, "radwege_laenge_area.geojson");
		List<Marker> markers = MapUtils.createSimpleMarkers(supermarkets);
		map.addMarkers(markers);

		for (Marker marker : markers) {
			double radwegLaenge = (Double) marker.getProperty("radwegeLaengeLENGTH");
			double radstreifenLaenge = (Double) marker.getProperty("radstreifenLaengeLENGTH");
			float totalCyclePathsLength = (float) (radwegLaenge + radstreifenLaenge);
			float value = map(totalCyclePathsLength, 0, 24000, 0, 255);
			marker.setColor(color(value, 0, 0));
		}

		for (Marker marker : markers) {
			String quartiernamen = (String) marker.getProperty("Quartiername");
			double radwegLaenge = (Double) marker.getProperty("LENGTH");
			double radstreifenLaenge = (Double) marker.getProperty("radstreifen_LENGTH");
			double flaeche = (Double) marker.getProperty("area_km2");
			float totalCyclePathsLength = (float) (radwegLaenge + radstreifenLaenge);
			float wegeProKM2 = (float) (totalCyclePathsLength / flaeche);
			println(wegeProKM2 + "   " + quartiernamen + "   " + totalCyclePathsLength);

			float value = map(wegeProKM2, 0, 7900, 0, 255);
			marker.setColor(color(value, 0, 0));
		}

		dataEntriesMap = loadSalaryFromCSV("data/medeinkommenquartier.csv");
		for (Marker marker : markers) {
			int quartierNr = (int) marker.getProperty("Quartiernummer");
			DataEntry dataEntry = dataEntriesMap.get(quartierNr);
			println(dataEntry);

			float alpha = map(dataEntry.value, 0, 700, 10, 255);

		}

	}

	private HashMap<String, DataEntry> loadSalaryFromCSV(String string) {
		loadTable(string);
		return null;
	}

	public void draw() {
		map.draw();
	}

	

	class DataEntry {
		String countryName;
		String id;
		Integer year;
		Float value;
	}
	public static void main(String[] args) {
		PApplet.main(new String[] { QuartiereNagel2.class.getName() });
	}
}
