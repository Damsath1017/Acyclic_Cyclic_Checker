//Name:W.W.Damsath Nadun Perera
//UOW:w2152929
//IIT:20242091

package src;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("        Graph Acyclicity Checker         ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1) Enter the folder name");
            System.out.println("2) Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("2")) {
                System.out.println("Exiting checker. Goodbye!");
                break;
            } else if (choice.equals("1")) {
                // Ask for the folder
                System.out.print("Enter folder to go to (e.g., 'cyclic' or 'acyclic'): ");
                String folder = scanner.nextLine().trim();

                // Ask for the file name
                System.out.print("Enter file name (e.g., 'a_40_1.txt'/'c_40_1.txt'): ");
                String filename = scanner.nextLine().trim();

                // Automatically append .txt if the user forgot it
                if (!filename.endsWith(".txt")) {
                    filename += ".txt";
                }

                // Construct the path based on your benchmark folder structure
                String path = "benchmarks/" + folder + "/" + filename;
                File file = new File(path);

                // If it's not found in benchmarks/, just check the root or exact path as a fallback
                if (!file.exists()) {
                    File fallbackFile = new File(filename);
                    if (fallbackFile.exists()) {
                        file = fallbackFile;
                    }
                }

                if (!file.exists() || !file.isFile()) {
                    System.out.println("Error: Could not find file at '" + path + "'!");
                    continue; // Loop back to the menu
                }

                System.out.println("\nLoading graph from: " + file.getPath() + "\n");

                try {
                    // Initialize graph (parsing time is not counted in algorithm time usually)
                    Graph graph = new Graph(file);

                    // Start timing
                    long startTimeNano = System.nanoTime();

                    // Run the sink elimination algorithm (true prints the steps)
                    boolean isAcyclic = graph.isAcyclic(true);

                    // Stop timing immediately after the algorithm finishes
                    long endTimeNano = System.nanoTime();

                    // Calculate duration
                    long durationNano = endTimeNano - startTimeNano;
                    double durationMilli = durationNano / 1_000_000.0;

                    // Print final outcome
                    System.out.println("-----------------------------------------");
                    if (isAcyclic) {
                        System.out.println("Result: The graph is ACYCLIC.");
                    } else {
                        System.out.println("Result: The graph contains a CYCLE.");

                        // Fetch and print the cycle if it wasn't acyclic
                        List<Integer> cycle = graph.getCycle();
                        if (cycle != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < cycle.size(); i++) {
                                sb.append(cycle.get(i));
                                if (i < cycle.size() - 1) {
                                    sb.append(" -> ");
                                }
                            }
                            System.out.println("Detected cycle: " + sb.toString());
                        }
                    }

                    // Print the execution times
                    System.out.printf("Execution Time: %d ns (%.4f ms)\n", durationNano, durationMilli);
                    System.out.println("-----------------------------------------");

                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        scanner.close();
    }
}