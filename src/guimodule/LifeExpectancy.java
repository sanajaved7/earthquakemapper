package guimodule;

import java.util.HashMap;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet {
	UnfoldingMap map;
	HashMap<String, Float> lifeExpMap;
	
	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//loads life expectancy data
		lifeExpMap = (HashMap<String, Float>) loadLifeExpectancyFromCSV("../../data/LifeExpectancyWorldBank.csv");
		println("Loaded " + lifeExpMap.size() + " data entries");
	}
	
	public void draw() {
		//draws map of countries
		map.draw();
	}
	
	//helper method to load life expectancy data from CSV
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) { 
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		
		String[] rows = loadStrings(fileName);
		
		for (String row : rows){
			String [] columns = row.split(",");
			if (columns[5].length() > 5) {
				float value = Float.parseFloat(columns[5]);
				lifeExpMap.put(columns[4], value);
			}
		}
		
		return lifeExpMap;
		
	}
	
}
