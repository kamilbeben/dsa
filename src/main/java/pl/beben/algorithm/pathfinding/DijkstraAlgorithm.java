package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import pl.beben.datastructure.Digraph;
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
public class DijkstraAlgorithm {

  // Null values are not permitted by the collector, hence this dummy value
  private static final Digraph.Edge NULL_EDGE = new Digraph.Edge(null, null, 0);

  /**
   * @return list of edges needed to be followed in order to get to the destination, {@literal null} if such path does not exist
   */
  public static List<Digraph.Edge> findPath(Digraph digraph, String beginning, String destination) {

    if (beginning.equals(destination))
      return Collections.emptyList();

    final var verticeToBestWeight = digraph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertice -> vertice.equals(beginning)
            ? 0 // There is no weight to get to where we are already
            : MAX_VALUE // This will be overridden when we reach given vertice
        ));

    // Best edge so far - is going to be overridden along with verticeToBestWeight
    final var verticeToBestEdge = digraph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertice -> NULL_EDGE // Made so to avoid collector's NullPointerException
        ));

    final var verticeToEdges = createVerticeToEdgesMap(digraph);
    final var unexploredVertices = new HashSet<>(digraph.getVertices());

    // The goal is to iterate over each vertice in a digraph ([1]), each time picking the one having the lowest weight ([2]).

    // Each time we're exploring a next `vertice` it is guaranteed that we've found the shortest path to it -
    // - hence if the `vertice` is equal to the `destination` then we've found the shorted path to it ([3]).

    // It might seem like a job for a PriorityQueue at first glance, but PriorityQueue's order is decided upon element's insertion -
    // - and in this case it's not yet known at the insertion time.

    // [1]
    while (!unexploredVertices.isEmpty()) {
      // [2]
      final var vertice = pickVerticeHavingLowestWeight(unexploredVertices, verticeToBestWeight);
      final var verticeWeight = verticeToBestWeight.get(vertice);
      unexploredVertices.remove(vertice);

      // [3] - the shortest path to the destination has been found
      if (vertice.equals(destination))
        break;

      // Now, through all available edges ([4]), reach the `adjacentVertice` ([5]) to check if this is the quickest path to it ([6]).
      // [4]
      for (final var edge : verticeToEdges.get(vertice)) {
        assertThatEdgeIsValid(edge);

        final var edgeWeight = verticeWeight + edge.weight();
        // [5]
        final var adjacentVertice = edge.adjacentVertice();
        // [6] - better path to the `adjacentVertice` has been found
        if (verticeToBestWeight.get(adjacentVertice) > edgeWeight) {
          verticeToBestWeight.put(adjacentVertice, edgeWeight);
          verticeToBestEdge.put(adjacentVertice, edge);
        }
      }
    }

    // `verticeToBestEdge` now stores the quickest way to all vertices that are reachable
    // from the `beginning` (that is, up until the `destination`, see ([3]))

    // All that's left to do is to map that to the output
    return createPath(verticeToBestEdge, destination);
  }

  private static void assertThatEdgeIsValid(Digraph.Edge edge) {
    if (edge.weight() < 0) {
      throw new IllegalArgumentException("Edge " + edge + " is not valid. Reason: Weight must not be negative");
    }
  }

  private static Map<String, Set<Digraph.Edge>> createVerticeToEdgesMap(Digraph graph) {

    return graph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertice -> graph.getEdges().stream()
            .filter(edge -> edge.vertice().equals(vertice))
            .collect(Collectors.toSet())
        ));
  }

  private static String pickVerticeHavingLowestWeight(Set<String> vertices, Map<String, Integer> verticeToWeight) {

    return vertices.stream()
      .sorted(Comparator.comparing(verticeToWeight::get))
      .findFirst()
      .orElseThrow();
  }

  private static List<Digraph.Edge> createPath(Map<String, Digraph.Edge> verticeToBestEdge, String destination) {

    // If the destination was reached, the default NULL_EDGE would have been overridden
    // Returning null means that path leading from `beginning` to the `destination` was not found
    if (verticeToBestEdge.get(destination) == NULL_EDGE)
      return null;

    final var path = new ArrayList<Digraph.Edge>();

    // Starting at the `destination`
    var edge = verticeToBestEdge.get(destination);

    do {
      // reconstruct the path by retracing algorithm's steps and going backwards to the `beginning`
      path.add(edge);
      edge = verticeToBestEdge.get(edge.vertice());
      // edge being null means that we've reached the `beginning`
    } while (edge != NULL_EDGE);

    // Path was reconstructed by retracing algorithm's steps, it needs to be reversed
    Collections.reverse(path);

    return path;
  }

}
