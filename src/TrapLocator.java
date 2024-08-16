import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {
        List<List<Integer>> traps = new ArrayList<>();
        
        for (Colony colony : colonies) {
            List<Integer> cycle = new ArrayList<>();
            List<Integer> visited = new ArrayList<>();
            
            for (int city : colony.cities) {
                if (!visited.contains(city)) {
                    if (DFS(city, colony, visited, cycle)) {
                        Collections.sort(cycle);
                        traps.add(cycle);
                        break;
                    }
                }
            }
            if (cycle.size() == 0) {
                traps.add(cycle);
            }
        }
        return traps;
    }


    private boolean DFS(int city, Colony colony, List<Integer> visited, List<Integer> cycle) {
        
    	visited.add(city);
        cycle.add(city);
        
        for (int neighbour : colony.roadNetwork.get(city)) {
            
        	if (!visited.contains(neighbour)) {
                if (DFS(neighbour, colony, visited, cycle)) 
                    return true;  
            } 
            else if (cycle.size() > 1 && neighbour == cycle.get(0)) {
                if (!cycle.contains(neighbour)) 
                    cycle.add(neighbour);
                return true;
            }
        }
        
        cycle.remove(cycle.size() - 1);
        return false;
    }



    public void printTraps(List<List<Integer>> traps) {
    	
    	System.out.println("Danger exploration conclusions:");
        int i = 1;
    	for (List<Integer> trap : traps) {
    		System.out.print("Colony " + i++ + ": ");
    		List<Integer> newList = Kingdom.newList(trap);
    		if (trap.size() == 0)
    			System.out.println("Safe");
    		else 
    			System.out.println("Dangerous. Cities on the dangerous path: " + newList);

		}
    }

}
