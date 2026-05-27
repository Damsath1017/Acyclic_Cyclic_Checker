//Name:W.W.Damsath Nadun Perera
//UOW:w2152929
//IIT:20242091

package src;

import java.util.*;

public class CycleDetector {
    
    private Graph graph;
    private Set<Integer> visited;
    private Set<Integer> recStack;
    private Map<Integer, Integer> parent;
    
    public CycleDetector(Graph graph) {
        this.graph = graph;
    }
    
    // Find one cycle in the graph using Depth First Search
    public List<Integer> findCycle() {
        visited = new HashSet<>();
        recStack = new HashSet<>();
        parent = new HashMap<>();
        
        for (int v : graph.getVertices()) {
            if (!visited.contains(v)) {
                List<Integer> cycle = dfs(v);
                if (cycle != null) {
                    return cycle;
                }
            }
        }
        return null; // No cycle found
    }
    
    private List<Integer> dfs(int u) {
        visited.add(u);
        recStack.add(u);
        
        for (int v : graph.getAdjNodes(u)) {
            if (!visited.contains(v)) {
                parent.put(v, u);
                List<Integer> cycle = dfs(v);
                if (cycle != null) {
                    return cycle;
                }
            } else if (recStack.contains(v)) {
                // We reached a node that is currently in the recursion stack, forming a back-edge. 
                // A cycle is detected!
                return constructCycle(u, v);
            }
        }
        
        recStack.remove(u); // Remove from recursion stack once fully explored
        return null;
    }
    
    // Construct the cycle based on parent pointers
    private List<Integer> constructCycle(int u, int v) {
        List<Integer> cycle = new ArrayList<>();
        // The cycle goes v -> ... -> u -> v
        // We will trace backwards from u to v using the parent map
        int curr = u;
        while (curr != v) {
            cycle.add(curr);
            curr = parent.get(curr);
        }
        cycle.add(v); // add the start node v
        
        // Reverse because we traced it backwards
        Collections.reverse(cycle);
        
        // Add v at the end to complete the loop representation (v -> ... -> u -> v)
        cycle.add(v);
        return cycle;
    }
}
