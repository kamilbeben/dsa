package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import pl.beben.datastructure.Graph;
import pl.beben.datastructure.PathSegment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.lang.Integer.MAX_VALUE;
import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class Dijkstra {

  // Null values are not permitted by the collector, hence this dummy value
  private static final Graph.Edge NULL_EDGE = new Graph.Edge<>(null, null, 0, null);

  /**
   * @return list of path segments needed to be followed in order to get to the destination, {@literal null} if such path does not exist
   */
  public static <DATA> List<PathSegment<DATA>> findPath(Graph<DATA> graph, Graph.Vertice<DATA> startingPoint, Graph.Vertice<DATA> destination) {

    if (startingPoint.equals(destination))
      return Collections.emptyList();

    final var verticeToBestCost = graph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertice -> vertice.equals(startingPoint)
            ? 0 // There is no cost to get to where we are already
            : MAX_VALUE // This will be overridden when we reach given vertice
        ));

    // Best edge so far - is going to be overridden along with verticeToBestCost
    final var verticeToBestEdge = graph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertice -> (Graph.Edge<DATA>) NULL_EDGE // Made so to avoid collector's NullPointerException
        ));

    final var verticeToEdges = createVerticeToEdgesMap(graph);
    final var unexploredVertices = new HashSet<>(graph.getVertices());

    // The goal is to iterate over each vertice in a graph ([1]), each time picking the one having the lowest cost ([2]).

    // Each time we're exploring a next `vertice` it is guaranteed that we've found the shortest path to it -
    // - hence if the `vertice` is equal to the `destination` then we've found the shorted path to it ([3]).

    // It might seem like a job for a PriorityQueue at first glance, but PriorityQueue's order is decided upon element's insertion -
    // - and in this case it's not yet known at the insertion time.

    // [1]
    while (!unexploredVertices.isEmpty()) {
      // [2]
      final var vertice = pickVerticeHavingLowestCost(unexploredVertices, verticeToBestCost);
      final var verticeCost = verticeToBestCost.get(vertice);
      unexploredVertices.remove(vertice);

      // [3] - the shortest path to the destination has been found
      if (vertice.equals(destination))
        break;

      // Now, through all available edges ([4]), reach the `oppositeVertice` ([5]) to check if this is the quickest path to it ([6]).
      // [4]
      for (final var edge : verticeToEdges.get(vertice)) {
        assertThatEdgeIsValid(edge);

        final var edgeCost = verticeCost + edge.cost();
        // [5]
        final var oppositeVertice = getOppositeVertice(vertice, edge);
        // [6] - better path to the `oppositeVertice` has been found
        if (verticeToBestCost.get(oppositeVertice) > edgeCost) {
          verticeToBestCost.put(oppositeVertice, edgeCost);
          verticeToBestEdge.put(oppositeVertice, edge);
        }
      }
    }

    // `verticeToBestEdge` now stores the quickest way to all vertices that are reachable
    // from the `startingPoint` (that is, up until the `destination`, see ([3]))

    // All that's left to do is to map that to the output
    return createPathSegments(verticeToBestEdge, destination);
  }

  private static void assertThatEdgeIsValid(Graph.Edge edge) {
    if (edge.cost() < 0) {
      throw new IllegalArgumentException("Edge " + edge + " is not valid. Reason: Cost must not be negative");
    }
  }

  private static <DATA> Map<Graph.Vertice<DATA>, Set<Graph.Edge<DATA>>> createVerticeToEdgesMap(Graph<DATA> graph) {

    return graph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertice -> graph.getEdges().stream()
            .filter(edge ->
              // Seems weird when you look at it, but it's allowed for vertice to have edges pointing back at these vertices (it's called loop)
              !edge.firstVertice().equals(edge.secondVertice()) &&
              (
                edge.firstVertice().equals(vertice) ||
                (
                  edge.type() == Graph.Edge.Type.BIDIRECTIONAL &&
                  edge.secondVertice().equals(vertice)
                )
              )
            )
            .collect(Collectors.toSet())
        ));
  }

  private static <DATA> Graph.Vertice<DATA> pickVerticeHavingLowestCost(Set<Graph.Vertice<DATA>> vertices,
                                                                        Map<Graph.Vertice<DATA>, Integer> verticeToCost) {

    return vertices.stream()
      .sorted(Comparator.comparing(verticeToCost::get))
      .findFirst()
      .orElseThrow();
  }

  private static <DATA> List<PathSegment<DATA>> createPathSegments(Map<Graph.Vertice<DATA>, Graph.Edge<DATA>> verticeToBestEdge, Graph.Vertice<DATA> destination) {

    // If the destination was reached, the default NULL_EDGE would have been overridden
    // Returning null means that path leading from `startingPoint` to the `destination` was not found
    if (verticeToBestEdge.get(destination) == NULL_EDGE)
      return null;

    final var pathSegments = new ArrayList<PathSegment<DATA>>();

    // Starting at the `destination` ([1]), reconstruct the path ([2])
    // by retracing algorithm's steps and going backwards ([3]) to the `startingPoint` ([4]).

    // [1]
    var vertice = destination;
    var edge = verticeToBestEdge.get(destination);

    do {
      // [2]
      final var segmentBeginning = getOppositeVertice(vertice, edge);
      pathSegments.add(new PathSegment<>(segmentBeginning, edge));
      // [3]
      vertice = segmentBeginning;
      edge = verticeToBestEdge.get(vertice);
      // [4] - edge being null means that we've reached the `startingPoint`
    } while (edge != NULL_EDGE);

    // Path was reconstructed by retracing algorithm's steps from the end to the beginning - so at last, it needs to be reversed
    Collections.reverse(pathSegments);

    return pathSegments;
  }

  private static <DATA> Graph.Vertice<DATA> getOppositeVertice(Graph.Vertice<DATA> vertice, Graph.Edge<DATA> edge) {
    return edge.firstVertice().equals(vertice)
      ? edge.secondVertice()
      : edge.firstVertice();
  }

}
