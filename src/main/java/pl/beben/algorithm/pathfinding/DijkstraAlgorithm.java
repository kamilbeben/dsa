package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.beben.datastructure.Digraph;
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
import static pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm.retracePath;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
public class DijkstraAlgorithm {

  // Null values are not permitted by the collector, hence this dummy value
  private static final Digraph.Edge NULL_EDGE = new Digraph.Edge(null, null, 0);

  /**
   * Performance could be improved by introducing a heuristic function (look up A-star algorithm).
   *
   * @param digraph a directed, weighted graph
   * @return see {@link pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm#retracePath(java.util.Map, String, pl.beben.datastructure.Digraph.Edge)}
   * @throws java.lang.IllegalArgumentException if it finds an {@link Digraph.Edge} with negative {@link Digraph.Edge#weight()}
   */
  public static List<Digraph.Edge> findPath(Digraph digraph, String beginning, String destination) {
    log.debug("Beginning = {}, destination = {}", beginning, destination);

    if (beginning.equals(destination)) {
      log.debug("Beginning is equal to the destination - returning []");
      return Collections.emptyList();
    }

    final var vertexToBestWeight = digraph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertex -> vertex.equals(beginning)
            ? 0 // There is no weight to get to where we are already
            : MAX_VALUE // This will be overridden when we reach given vertex
        ));

    // Best edge so far - is going to be overridden along with vertexToBestWeight
    final var vertexToBestEdge = digraph.getVertices().stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          vertex -> NULL_EDGE // Made so to avoid collector's NullPointerException
        ));

    final var unexploredVertices = new HashSet<>(digraph.getVertices());

    // The goal is to iterate over each vertex in a digraph ([1]), each time picking the one having the lowest weight ([2]).

    // Each time we're exploring a next `vertex` it is guaranteed that we've found the shortest path to it -
    // - hence if the `vertex` is equal to the `destination` then we've found the shorted path to it ([3]).

    // [1]
    while (!unexploredVertices.isEmpty()) {
      // [2]
      final var vertex = pickVerticeHavingLowestWeight(unexploredVertices, vertexToBestWeight);
      final var vertexWeight = vertexToBestWeight.get(vertex);
      unexploredVertices.remove(vertex);
      log.debug("Exploring vertex {}", vertex);

      // [3]
      if (vertex.equals(destination)) {
        log.debug("Found the shortest path to the destination - break");
        break;
      }

      // Now, going through all available edges ([4]), reach the `adjacentVertice` ([5]) to check if this is the shortest path to it ([6]).
      // [4]
      for (final var edge : digraph.computeEdges(vertex)) {
        log.debug("Trying edge {}", edge);
        assertThatEdgeIsValid(edge);
        final var edgeWeight = vertexWeight + edge.weight();
        // [5]
        final var adjacentVertice = edge.adjacentVertice();
        // [6]
        if (vertexToBestWeight.get(adjacentVertice) > edgeWeight) {
          log.debug("Edge {} is the shortest path - overriding vertexToBestWeight and vertexToBestEdge");
          vertexToBestWeight.put(adjacentVertice, edgeWeight);
          vertexToBestEdge.put(adjacentVertice, edge);
        }
      }
    }

    // `vertexToBestEdge` now stores the quickest way to all* vertices that are reachable
    // from the `beginning` (*that is, up until the `destination`, see ([3]))

    // All that's left to do is to map that to the output
    return retracePath(vertexToBestEdge, destination, NULL_EDGE);
  }

  private static void assertThatEdgeIsValid(Digraph.Edge edge) {
    if (edge.weight() < 0) {
      throw new IllegalArgumentException("Edge " + edge + " is not valid. Reason: Weight must not be negative");
    }
  }

  private static String pickVerticeHavingLowestWeight(Set<String> vertices, Map<String, Integer> vertexToWeight) {
    return vertices.stream()
      .min(Comparator.comparing(vertexToWeight::get))
      .orElseThrow();
  }

}
