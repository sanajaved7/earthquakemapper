package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Sana Javed
 * Date: September 27, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();
	    

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int blue = color(12, 16, 245);
	    int red = color(245, 12, 55);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());

	    }
	    
	    //Iterates through list of earthquakes, 
	    //creates marker for each earthquake, 
	    //sets marker color based on magnitude 
	    //and appends the marker to the marker list.
	    for (int i = 0; i < earthquakes.size(); i++ ){
	    	PointFeature f = earthquakes.get(i);
	    	Location loc = f.getLocation();
	    	
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	SimplePointMarker m = new SimplePointMarker(loc);
	    	
	    	if (mag >= THRESHOLD_MODERATE){
	    		m.setColor(red);
	    	}
	    	else if (mag < THRESHOLD_MODERATE && mag >= THRESHOLD_LIGHT){
	    		m.setColor(blue);
	    	}
	    	else {
	    		m.setColor(yellow);
	    	}
	    	
	    	m.setRadius(10);
	    	markers.add(m);
	    }
	    //Passes markers list to map to display all markers for earthquakes
	    map.addMarkers(markers);
	    
	}
		
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}

	private void addKey() 
	{	
		int yellow = color(255, 255, 0);
	    int blue = color(12, 16, 245);
	    int red = color(245, 12, 55);
		//creates box for key
	    fill(255, 255, 225);
		rect(20, 50, 150, 300);
		String s = "Earthquake Key";
		fill(50);
		text(s, 35, 55, 120, 300);
		//creates red dot for high threshold earthquakes
		fill(red);
		ellipse(35, 110, 20, 20);
		String t = "5.0+ Magnitude";
		fill(50);
		text(t, 55, 105, 120, 300);
		//creates blue dot for middle threshold magnitude earthquakes
		fill(blue);
		ellipse(35, 175, 20, 20);
		String u = "4.0+ Magnitude";
		fill(50);
		text(u, 55, 170, 120, 300);
		//creates yellow dot for low threshold magnitude earthquakes
		fill(yellow);
		ellipse(35, 240, 20, 20);
		String v = "Below 4.0 Magnitude";
		fill(50);
		text(v, 55, 235, 120, 300);
		
	}
}
