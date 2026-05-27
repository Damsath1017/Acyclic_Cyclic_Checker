package src;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Benchmark {
    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("   Graph Benchmarks   ");
        System.out.println("==================================");
        
        String[] benchmarkFolders = {"benchmarks/acyclic", "benchmarks/cyclic"};
        
        for (String folder : benchmarkFolders) {
            System.out.println("\n>>> Reading from: " + folder);
            File dir = new File(folder);
            
            if (!dir.exists() || !dir.isDirectory()) {
                System.out.println("Directory " + folder + " not found. Skipping.");
                continue;
            }

            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files == null || files.length == 0) continue;
            
            // Sort files logically rather than alphabetically (e.g. 40 before 80)
            Arrays.sort(files, Comparator.comparing(f -> extractV(f.getName())));

            for (File file : files) {
                try {
                    // Timing parsing and initialization
                    Graph g = new Graph(file);
                    
                    int initialV = g.getVertices().size();
                    
                    // Timing algorithm
                    long start = System.nanoTime();
                    boolean isAcyclic = g.isAcyclic();
                    long timeMs = (System.nanoTime() - start) / 1000000;
                    
                    System.out.printf("[%-15s] V: %-6d | Sink Elim Time: %4d ms | Acyclic? %-5s", 
                            file.getName(), initialV, timeMs, isAcyclic);
                    
                    if (!isAcyclic) {
                        List<Integer> cycle = g.getCycle();
                        if (cycle != null) {
                            if (cycle.size() > 8) {
                                System.out.println(" | Cycle: " + cycle.subList(0, 8) + "... (len: " + cycle.size() + ")");
                            } else {
                                System.out.println(" | Cycle: " + cycle);
                            }
                        } else {
                            System.out.println(" | Cycle: Unable to resolve.");
                        }
                    } else {
                        System.out.println();
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error processing " + file.getName() + " - " + e.getMessage());
                }
            }
        }
    }
    
    // Simple helper to sort file names by the integer V value in them (e.g. a_40_0.txt -> 40)
    private static Integer extractV(String name) {
        try {
            String[] parts = name.split("_");
            if (parts.length > 1) {
                return Integer.parseInt(parts[1]);
            }
        } catch (Exception ignored) {}
        return 0;
    }
}
