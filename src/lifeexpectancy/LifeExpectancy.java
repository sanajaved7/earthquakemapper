package lifeexpectancy;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.providers.Google.*;

import java.util.List;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;

import java.util.HashMap;

public class LifeExpectancy extends PApplet {
	
	UnfoldingMap map;
	HashMap<String, Float> lifeExpMap;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//loads life expectancy data
		lifeExpMap = loadLifeExpectancyFromCSV("../../data/LifeExpectancyWorldBankModule3.csv");
		println("Loaded " + lifeExpMap.size() + " data entries");
		
		//creates 1 Feature and 1 Marker per country
		countries = GeoJSONReader.loadData(this, "../../data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
				
		//countries are shaded according to life expectancy
		shadeCountries();
	}
	
	public void draw() {
		//draws map of countries
		map.draw();
	}
	
	private void shadeCountries() {
		for (Marker marker: countryMarkers) {
			String countryId = marker.getId();
			
			if (lifeExpMap.containsKey(countryId)) {
				float lifeExp = lifeExpMap.get(countryId);
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else {
				marker.setColor(color(150, 150, 150));
			}
		}
	}
	
	//helper method to load life expectancy data from CSV
	private HashMap<String, Float> loadLifeExpectancyFromCSV(String fileName) { 
		HashMap<String, Float> lifeExpMap = new HashMap<String, Float>();
		
		String[] rows = loadStrings(fileName);
		
		for (String row : rows){
			String [] columns = row.split(",");
			if (columns.length == 6 && !columns[5].equals("..")) {
				lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
			}
		}
		return lifeExpMap;
	}
	
}
