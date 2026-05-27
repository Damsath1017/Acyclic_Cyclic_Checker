# Acyclic and Cyclic Graph Checker

A Java application that reads graph structure from input text files and determines if the graph contains cycles. It implements the **Sink Elimination Algorithm** for efficient acyclicity detection, outputs detected cycles if any exist, and features a benchmarking utility.

## Author Details
* **Name**: W.W.Damsath Nadun Perera

## Key Components
* **Graph**: Custom directed graph representation with adjacency lists, reverse adjacency lists (for sink elimination efficiency), and sink node detection.
* **CycleDetector**: Identifies back-edges and prints exact cycle paths when the graph is determined to be cyclic.
* **Benchmark**: Runs checks on large datasets of cyclic and acyclic graphs to evaluate algorithm efficiency and execution time.
* **Main**: CLI interface for testing individual graph files and running benchmarks.

## How to Run
Ensure Java Development Kit (JDK) is installed, then compile and run the project:
```bash
javac src/*.java
java src.Main
```
