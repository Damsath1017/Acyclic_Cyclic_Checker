//Name:W.W.Damsath Nadun Perera
//UOW:w2152929
//IIT:20242091

package src;

import java.util.*;
import java.io.*;

public class Graph {
    // Adjacency list: node -> list of nodes it points to
    private Map<Integer, List<Integer>> adjList;
    
    // Reverse adjacency list: node -> list of nodes that point to it
    // Useful for efficiently removing incoming edges to a sink
    private Map<Integer, List<Integer>> revAdjList;
    
    // Track out-degrees for efficient sink detection
    private Map<Integer, Integer> outDegrees;
    
    public Graph() {
        adjList = new HashMap<>();
        revAdjList = new HashMap<>();
        outDegrees = new HashMap<>();
    }
    
    // Read from File constructor
    public Graph(File file) throws IOException {
        this();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Skip empty lines
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    try {
                        int u = Integer.parseInt(parts[0]);
                        int v = Integer.parseInt(parts[1]);
                        addEdge(u, v);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
    }
    
    // Add directed edge u -> v
    public void addEdge(int u, int v) {
        addVertex(u);
        addVertex(v);
        
        adjList.get(u).add(v);
        revAdjList.get(v).add(u);
        
        outDegrees.put(u, outDegrees.get(u) + 1);
    }
    
    // Ensure the vertex exists in the graph structure
    private void addVertex(int v) {
        if (!adjList.containsKey(v)) {
            adjList.put(v, new ArrayList<>());
            revAdjList.put(v, new ArrayList<>());
            outDegrees.put(v, 0);
        }
    }
    
    // Find a sink (a vertex with out-degree 0)
    public Integer findSink() {
        for (Map.Entry<Integer, Integer> entry : outDegrees.entrySet()) {
            if (entry.getValue() == 0) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    // Remove a vertex and all its incident (incoming/outgoing) edges
    public void removeVertex(int v) {
        if (!adjList.containsKey(v)) return;
        
        // Remove outgoing edges
        for (int target : adjList.get(v)) {
            revAdjList.get(target).remove(Integer.valueOf(v));
        }
        
        // Remove incoming edges
        for (int source : revAdjList.get(v)) {
            adjList.get(source).remove(Integer.valueOf(v));
            // Decrease out-degree of the source, since its outgoing edge was removed
            outDegrees.put(source, outDegrees.get(source) - 1);
        }
        
        // Remove the vertex itself from our mappings
        adjList.remove(v);
        revAdjList.remove(v);
        outDegrees.remove(v);
    }
    
    // Get all remaining vertices in the graph
    public Set<Integer> getVertices() {
        return adjList.keySet();
    }
    
    // Check if the graph is completely empty
    public boolean isEmpty() {
        return adjList.isEmpty();
    }
    
    // Get the nodes adjacent to v (v points to them)
    public List<Integer> getAdjNodes(int v) {
        return adjList.getOrDefault(v, new ArrayList<>());
    }
    
    // Runs the Sink Elimination Algorithm without printing
    public boolean isAcyclic() {
        return isAcyclic(false);
    }
    
    // Runs the Sink Elimination Algorithm, optionally printing the eliminated sinks
    public boolean isAcyclic(boolean printSteps) {
        while (!isEmpty()) {
            Integer sink = findSink();
            if (sink == null) {
                // Not empty, but no sink -> Contains a cycle
                if (printSteps) {
                    System.out.println("No further sinks found, but graph is not empty -> Graph contains a cycle.");
                }
                return false;
            }
            if (printSteps) {
                System.out.println("Found sink: " + sink + ". Eliminating it from the graph.");
            }
            removeVertex(sink);
        }
        if (printSteps) {
            System.out.println("All vertices eliminated successfully -> Graph is acyclic.");
        }
        return true;
    }
    
    // Detects and returns a cycle if the graph is not acyclic
    public List<Integer> getCycle() {
        CycleDetector detector = new CycleDetector(this);
        return detector.findCycle();
    }
}

