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
import org.gicentre.utils.colour.*;    // For colour tables.

public class Choropleth2 extends PApplet {
	
	
	 

	 
	// ------------------ Object variables --------------------
	 
	ColourTable cTable1, cTable2, cTable3;      // Colour tables to use.
	 
	// ------------------- Initialisation ---------------------

	public void settings(){
		  size(500,250);
	}
	
	public void setup()
	{
	 
	  // Create a continuous Brewer colour table (YlOrBr6).
	  cTable1 = new ColourTable();
	  cTable1.addContinuousColourRule((float) (0.5/6), 255,255,212);
	  cTable1.addContinuousColourRule((float) (1.5/6), 254,227,145);
	  cTable1.addContinuousColourRule((float) (2.5/6), 254,196, 79);
	  cTable1.addContinuousColourRule((float) (3.5/6), 254,153, 41);
	  cTable1.addContinuousColourRule((float) (4.5/6), 217, 95, 14);
	  cTable1.addContinuousColourRule((float) (5.5/6), 153, 52,  4);
	   
	  // Create a preset colour table and save it as a file
	  cTable1 = ColourTable.getPresetColourTable(ColourTable.YL_OR_BR,0,1);
	  //ColourTable.writeFile(cTable2,createOutput("imhofLand3.ctb"));
	   
 
	}
	 
	 
	// ------------------ Processing draw --------------------
	 
	/** Draws the colour tables as horizontal colour bars.
	 */
	public void draw()
	{
	  background(255);
	   
	  // Draw the continuous Brewer colour table.
	  float inc;
	 
	  // Draw the discrete version of the Brewer colour table.
	  //stroke(0,150);
	  inc = (float) (1/6.0);
	  for (float i=0; i<1; i+=inc)
	  {
	    fill(cTable1.findColour((float) (i + 0.5*inc)));
	    rect(width*i,70,width*inc,50,2);
	  }
	   
	
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { Choropleth2.class.getName() });
	}

}
