import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Kingdom {
	
	public HashMap<Integer, List<Integer>> directedList = new HashMap<>();
	public HashMap<Integer, List<Integer>> undirectedList = new HashMap<>();


    public void initializeKingdom(String filename) {
    	
    	// Read the txt file and fill your instance variables
    	try {
        	FileReader fr = new FileReader(filename);
        	BufferedReader br = new BufferedReader(fr);
        	
        	int city = 0;
        	String line;
        	
        	while ((line = br.readLine()) != null) {
        		
        		String[] temp = line.split(" ");
        		List<Integer> neighbours = new ArrayList<>();
        		
        		for (int i=0; i < temp.length; i++) {
					if (temp[i].equals("1"))
						neighbours.add(i);
				}
        		this.directedList.put(city, neighbours);
        		city++;
        	}
        	br.close();
    	
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	copy();
    }

    public List<Colony> getColonies() {
    	// Identify the colonies using the given input file.
        List<Colony> colonies = new ArrayList<>();
        
        boolean[] visited = new boolean[this.undirectedList.size()];
        
        for (int i=0; i<visited.length; i++) {
        	if(visited[i] == false) {
        		Colony colony = new Colony();
        		DFS(i, visited, colony);
        		Collections.sort(colony.cities);
        		colonies.add(colony);
        	}
        }
        return colonies;
    }

    private void DFS(int city, boolean[] visited, Colony colony) {
		colony.cities.add(city);
		colony.roadNetwork.put(city, this.directedList.get(city));
		visited[city] = true;
		
		for (int i : undirectedList.get(city)) {
            if (visited[i] == false)
                DFS(i, visited, colony);
        }
	}

	public void printColonies(List<Colony> discoveredColonies) {
        // Print the given list of discovered colonies conforming to the given output format.
        System.out.println("Discovered colonies are:");
        for (int i=0; i<discoveredColonies.size(); i++) {
        	List<Integer> newList = newList(discoveredColonies.get(i).cities);
        	System.out.println("Colony " + (i+1) + ": " + newList);
        }
    }
	
	
	public static List<Integer> newList(List<Integer> list) {
		List<Integer> temp = new ArrayList<>();
		
		for(int i = 0; i < list.size(); i++)
			temp.add(list.get(i)+1);
		return temp;
    }
    
	
    private void copy(){
    	
    	for (int i=0; i<this.directedList.size(); i++) {
    		this.undirectedList.put(i, new ArrayList<Integer>(directedList.get(i)));
    	}
    	
    	for (int i=0; i<this.undirectedList.size(); i++) {
    		for (int neighbour : this.undirectedList.get(i)) {
    			if(!this.undirectedList.get(neighbour).contains(i)) 
    				this.undirectedList.get(neighbour).add(i);
			}
		}
    }
}
