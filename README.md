
# Data Structures and Algorithms examples in Java
Examples that I coded while studying DSA.

I've tried to make everything verbose and comment all the why's leaving the obvious things uncommented -
I feel like these are easier to understand than most of the stuff that you can find on web or in the books, because they tend to be very cryptic and compact.

You can use it however you want, but it's probably a bad idea to use it in any other way but to learn.

## Table of contents
  - Data structures
    - Binary search tree ([implementation](src/main/java/pl/beben/datastructure/BinarySearchTree.java), [test](src/test/java/pl/beben/datastructure/BinarySearchTreeTest.java))
    - Hash table ([implementation](src/main/java/pl/beben/datastructure/HashTable.java), [test](src/test/java/pl/beben/datastructure/HashTableTest.java))
    - Trie ([implementation](src/main/java/pl/beben/datastructure/Trie.java), [test](src/test/java/pl/beben/datastructure/TrieTest.java))
    - Digraph ([implementation](src/main/java/pl/beben/datastructure/Digraph.java))
  - Algorithms
    - Sorting
      - Quick sort ([implementation](src/main/java/pl/beben/algorithm/sort/QuickSort.java), [test](src/test/java/pl/beben/algorithm/sort/QuickSortTest.java))
      - Merge sort ([implementation](src/main/java/pl/beben/algorithm/sort/MergeSort.java), [test](src/test/java/pl/beben/algorithm/sort/MergeSortTest.java))
    - Path finding
      - Breadth first search algorithm ([implementation](src/main/java/pl/beben/algorithm/pathfinding/BreadthFirstSearchAlgorithm.java), [test](src/test/java/pl/beben/algorithm/pathfinding/BreadthFirstSearchAlgorithmTest.java))
      - Dijkstra's algorithm ([implementation](src/main/java/pl/beben/algorithm/pathfinding/DijkstraAlgorithm.java), [test](src/test/java/pl/beben/algorithm/pathfinding/DijkstraAlgorithmTest.java))
      - A-Star algorithm ([implementation](src/main/java/pl/beben/algorithm/pathfinding/AStarAlgorithm.java))
        - GridBasedAStarAlgorithm ([implementation](src/main/java/pl/beben/algorithm/pathfinding/GridBasedAStarAlgorithm.java), [test](src/test/java/pl/beben/algorithm/pathfinding/GridBasedAStarAlgorithmTest.java))
      - Sanity check (BFS, Dijkstra, A-Star) ([test](src/test/java/pl/beben/algorithm/pathfinding/PathfindingAlgorithmsSanityCheck.java), [log](PathfindingAlgorithmsSanityCheck.log))
      - Simulated annealing algorithm ([implementation](src/main/java/pl/beben/algorithm/simulatedannealing/SimulatedAnnealingAlgorithm.java))
        - Travelling salesman problem ([implementation](src/main/java/pl/beben/algorithm/simulatedannealing/TravellingSalesmanProblem.java), [test on small dataset](src/test/java/pl/beben/algorithm/simulatedannealing/TravellingSalesmanProblemTest.java), [test on large dataset](src/test/java/pl/beben/algorithm/simulatedannealing/TravellingSalesmanProblemXqf131Test.java))
