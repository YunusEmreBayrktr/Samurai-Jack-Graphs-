import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();


    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
            
		try {
			File file = new File(filename);
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(file);
	        document.getDocumentElement().normalize(); 
	            
	        // Reading Locations
	        NodeList locationsList = document.getElementsByTagName("Locations");
	        Node locations = locationsList.item(0);
		    
	        NodeList locationList = locations.getChildNodes();
		    for(int i=0 ; i<locationList.getLength(); i++) {
		    	
		    	Node location = locationList.item(i);
		    	if (location.getNodeType() != Node.ELEMENT_NODE)
		    		continue;
		    	Element locationElement = (Element) location;
		    	
		    	String name = locationElement.getElementsByTagName("Name").item(0).getTextContent();
		    	int Id = Integer.parseInt(locationElement.getElementsByTagName("Id").item(0).getTextContent());
		    	Location loc = new Location(name, Id);
		    	this.locations.add(loc);
		    	this.locationMap.put(Id, loc);
		    }
		    
		    // Reading Trails
		    NodeList trailsList = document.getElementsByTagName("Trails");
		    Node trails = trailsList.item(0);
		    
		    NodeList trailList = trails.getChildNodes();
		    for (int i=0; i<trailList.getLength(); i++) {
		    	
		    	Node trail = trailList.item(i);
		    	if(trail.getNodeType() != Node.ELEMENT_NODE)
		    		continue;
		    	Element trailElement = (Element) trail;
		    	
		    	int source = Integer.parseInt(trailElement.getElementsByTagName("Source").item(0).getTextContent());
		    	int destination = Integer.parseInt(trailElement.getElementsByTagName("Destination").item(0).getTextContent());
		    	int danger = Integer.parseInt(trailElement.getElementsByTagName("Danger").item(0).getTextContent());
		    	this.trails.add(new Trail(locationMap.get(source), locationMap.get(destination), danger));
		    }
		    
		} catch (SAXException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}catch (ParserConfigurationException e) {
				e.printStackTrace();
		} 
    }

    public List<Trail> getSafestTrails() {
        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
    	// Prim's algorithm is used.
        List<Trail> safestTrails = new ArrayList<>();
    	int numOfLocations = this.locations.size();
    	boolean[] visited = new boolean[numOfLocations];
    	Collections.sort(this.trails);
    	
    	safestTrails.add(this.trails.get(0));
    	visited[safestTrails.get(0).source.id] = true;
    	visited[safestTrails.get(0).destination.id] = true;
    	
    	// Handle the case where there is only one trail
    	if (this.trails.size() == 1)
            return safestTrails;
    	
    	// Looping untill finding V-1 edges.
    	while (safestTrails.size() < numOfLocations-1) {
    		PriorityQueue<Trail> minHeap = new PriorityQueue<>();
    		
    		for (Trail trail : this.trails) {
    			if (visited[trail.source.id] != visited[trail.destination.id])
    				minHeap.add(trail);
    		}
    		
    		Trail safest = minHeap.peek();
    		visited[safest.destination.id] = true;
    		visited[safest.source.id] = true;
    		safestTrails.add(safest);
    	}
        return safestTrails;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
    	int totalDanger = 0;
    	System.out.println("Safest trails are:");
    	for (Trail trail : safestTrails) {
			System.out.println("The trail from " + trail.source.name + " to " + trail.destination.name + " with danger " + trail.danger);
			totalDanger += trail.danger;
		}
    	System.out.println("Total danger: " + totalDanger);
    	
    }
}