package pl.beben.algorithm.pathfinding;

import pl.beben.datastructure.Digraph;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GridBasedPathfindingAlgorithmTestUtils {

  record Problem(String name, String text, Digraph<GridVertex> digraph, GridVertex beginning, GridVertex destination) {}

  record Solution(Problem problem, List<Digraph.Edge<GridVertex>> path, Set<GridVertex> exploredVertices) {
    @Override
    public String toString() {
      return print(problem, path, exploredVertices);
    }

  }

  interface Algorithm {
    List<Digraph.Edge<GridVertex>> findPath(Set<GridVertex> exploredVertices, Digraph<GridVertex> digraph, GridVertex beginning, GridVertex destination);
  }

  /**
   * Example: <pre>{@code
   * GridBasedPathfindingAlgorithmTestUtils.sanityCheck(
   *   "problemName",
   *   """
   *   ···················
   *   ···B·····+·········
   *   ·········+·········
   *   ················D··
   *   ···················
   *   """,
   *   DijkstraAlgorithm::findPath
   * );
   * }</pre>
   * Legend:
   * <ul>
   *   <li>'B' - beginning</li>
   *   <li>'D' - destination</li>
   *   <li>'·' - vertex</li>
   *   <li>'+' - obstacle</li>
   * </ul>
   */
  static Solution computeSolution(String problemName, String problemText, Algorithm algorithm, boolean diagonalMovementIsEnabled) {

    final var problem = parseProblem(problemName, problemText, diagonalMovementIsEnabled);

    final var exploredVertices = new HashSet<GridVertex>();
    final var path = algorithm.findPath(exploredVertices, problem.digraph(), problem.beginning(), problem.destination());

    return new Solution(problem, path, exploredVertices);
  }

  // Problem text parsing

  private static Problem parseProblem(String name, String text, boolean diagonalMovementIsEnabled) {

    final var rows = problemTextTo2dCharArray(text);

    GridVertex beginning = null;
    GridVertex destination = null;
    final var digraph = new Digraph<GridVertex>();

    for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {

      final var cells = rows[rowIndex];

      for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        final var cellText = cells[cellIndex];
        if (!isVertex(cellText))
          continue;

        final var vertex = new GridVertex(cellIndex, rowIndex);
        final var edges = computeEdges(rows, vertex, diagonalMovementIsEnabled);

        digraph.getVertices().add(vertex);
        digraph.getVertexToEdges().put(vertex, edges);

        if (isBeginningVertex(cellText))
          beginning = vertex;

        if (isDestinationVertex(cellText))
          destination = vertex;
      }
    }

    assert beginning != null;
    assert destination != null;

    return new Problem(name, text, digraph, beginning, destination);
  }

  private static Set<Digraph.Edge<GridVertex>> computeEdges(char[][] rows, GridVertex vertex, boolean diagonalMovementIsEnabled) {
    final var rowCount = rows.length;
    final var cellCount = rows[0].length;

    final var edges = new HashSet<Digraph.Edge<GridVertex>>();

    // top
    if (vertex.yCoordinate() > 0)
      computeEdgeIfPossible(rows, vertex.yCoordinate() - 1, vertex.xCoordinate(), vertex, false)
        .ifPresent(edges::add);

    // top-right
    if (diagonalMovementIsEnabled && vertex.yCoordinate() > 0 && vertex.xCoordinate() < cellCount - 1)
      computeEdgeIfPossible(rows, vertex.yCoordinate() - 1, vertex.xCoordinate() + 1, vertex, true)
        .ifPresent(edges::add);

    // right
    if (vertex.xCoordinate() < cellCount - 1)
      computeEdgeIfPossible(rows, vertex.yCoordinate(), vertex.xCoordinate() + 1, vertex, false)
        .ifPresent(edges::add);

    // bottom-right
    if (diagonalMovementIsEnabled && vertex.yCoordinate() < rowCount - 1 && vertex.xCoordinate() < cellCount - 1)
      computeEdgeIfPossible(rows, vertex.yCoordinate() + 1, vertex.xCoordinate() + 1, vertex, true)
        .ifPresent(edges::add);

    // bottom
    if (vertex.yCoordinate() < rowCount - 1)
      computeEdgeIfPossible(rows, vertex.yCoordinate() + 1, vertex.xCoordinate(), vertex, false)
        .ifPresent(edges::add);

    // bottom-left
    if (diagonalMovementIsEnabled && vertex.yCoordinate() < rowCount - 1 && vertex.xCoordinate() > 0)
      computeEdgeIfPossible(rows, vertex.yCoordinate() + 1, vertex.xCoordinate() - 1, vertex, true)
        .ifPresent(edges::add);

    // left
    if (vertex.xCoordinate() > 0)
      computeEdgeIfPossible(rows, vertex.yCoordinate(), vertex.xCoordinate() - 1, vertex, false)
        .ifPresent(edges::add);

    // top-left
    if (diagonalMovementIsEnabled && vertex.yCoordinate() > 0 && vertex.xCoordinate() > 0)
      computeEdgeIfPossible(rows, vertex.yCoordinate() - 1, vertex.xCoordinate() - 1, vertex, true)
        .ifPresent(edges::add);

    return edges;
  }

  private static Optional<Digraph.Edge<GridVertex>> computeEdgeIfPossible(char[][] rows, int rowIndex, int cellIndex, GridVertex vertex, boolean isDiagonal) {
    final var cellText = rows[rowIndex][cellIndex];
    final var weight = isDiagonal
      ? GridVertex.DIAGONAL_EDGE_WEIGHT
      : GridVertex.EDGE_WEIGHT;

    return isVertex(cellText)
      ? Optional.of(new Digraph.Edge<>(vertex, new GridVertex(cellIndex, rowIndex), weight))
      : Optional.empty();
  }

  // Problem and solution printing
  private static final String ANSI_BOLD_RED_FONT = "\033[1;31m";
  private static final String ANSI_BRIGHT_BLACK_BG = "\033[0;100m";
  private static final String ANSI_RESET = "\033[0m";
  private static final String PRINT_BEGINNING = ANSI_BOLD_RED_FONT + "B" + ANSI_RESET;
  private static final String PRINT_DESTINATION = ANSI_BOLD_RED_FONT + "D" + ANSI_RESET;
  private static final String PRINT_PATH = ANSI_BOLD_RED_FONT + "*" + ANSI_RESET;
  private static final String PRINT_EXPLORED_VERTEX = "·";
  private static final String PRINT_UNEXPLORED_VERTEX = " ";
  private static final String PRINT_OBSTACLE = ANSI_BRIGHT_BLACK_BG + "■" + ANSI_RESET;

  private static String print(Problem problem, List<Digraph.Edge<GridVertex>> path, Set<GridVertex> exploredVertices) {

    final var pathVertices = path.stream()
      .map(Digraph.Edge::vertex)
      .collect(Collectors.toSet());

    final var output = new StringWriter();
    output.append(
      "\nProblem '" + problem.name() + "'\n" +
        "Explored vertices count: " + exploredVertices.size() + "\n" +
        "Path length: " + (pathVertices.size() + 1) // + destination
    );

    final var rows = problemTextTo2dCharArray(problem.text());
    for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
      output.append("\n|");

      for (int cellIndex = 0; cellIndex < rows[rowIndex].length; cellIndex++) {
        printCell(output, rows, rowIndex, cellIndex, pathVertices, exploredVertices);
      }
      output.append("|");

    }

    output.append(
      "\nLegend:\n" +
        "'" + PRINT_BEGINNING + "' - beginning\n" +
        "'" + PRINT_DESTINATION + "' - destination\n" +
        "'" + PRINT_PATH + "' - path\n" +
        "'" + PRINT_EXPLORED_VERTEX + "' - explored vertex\n" +
        "'" + PRINT_UNEXPLORED_VERTEX + "' - unexplored vertex\n" +
        "'" + PRINT_OBSTACLE + "' - obstacle\n"
    );

    return output.toString();
  }

  private static void printCell(StringWriter output, char[][] rows, int rowIndex, int cellIndex, Set<GridVertex> pathVertices, Set<GridVertex> exploredVertices) {
    final var cellText = rows[rowIndex][cellIndex];

    final var vertex = new GridVertex(cellIndex, rowIndex);
    final var isBeginning = isBeginningVertex(cellText);
    final var isPath = pathVertices.contains(vertex);
    final var isExplored = exploredVertices.contains(vertex);

    if (isBeginningVertex(cellText))
      output.append(PRINT_BEGINNING);

    else if (isDestinationVertex(cellText))
      output.append(PRINT_DESTINATION);

    else if (isPath && !isBeginning)
      output.append(PRINT_PATH);

    else if (isExplored && !isBeginning)
      output.append(PRINT_EXPLORED_VERTEX);

    else if (isVertex(cellText))
      output.append(PRINT_UNEXPLORED_VERTEX);

    else
      output.append(PRINT_OBSTACLE);
  }

  // Common

  private static char[][] problemTextTo2dCharArray(String text) {
    final var lines = text.split("\n");
    final var rows = new char[lines.length][];

    for (int i = 0; i < lines.length; i++) {
      rows[i] = lines[i].toCharArray();
    }

    return rows;
  }

  private static boolean isVertex(char cellText) {
    return
      cellText == '·' ||
        isBeginningVertex(cellText) ||
        isDestinationVertex(cellText);
  }

  private static boolean isBeginningVertex(char cellText) {
    return cellText == 'B';
  }

  private static boolean isDestinationVertex(char cellText) {
    return cellText == 'D';
  }

}
