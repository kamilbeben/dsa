package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.beben.datastructure.Digraph;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import static java.lang.Integer.MAX_VALUE;
import static lombok.AccessLevel.PRIVATE;
import static pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm.retracePath;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
public class DijkstraAlgorithm {

  /**
   * See {@link pl.beben.algorithm.pathfinding.AStarAlgorithm} for more performant version of this algorithm
   *
   * @param digraph a directed, weighted graph
   * @return see {@link pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm#retracePath(java.util.Map, Object)}
   * @throws java.lang.IllegalArgumentException if it finds an {@link Digraph.Edge} with negative {@link Digraph.Edge#weight()}
   */
  public static <VERTEX> List<Digraph.Edge<VERTEX>> findPath(Digraph<VERTEX> digraph, VERTEX beginning, VERTEX destination) {
    return findPath(new HashSet<>(), digraph, beginning, destination);
  }

  public static <VERTEX> List<Digraph.Edge<VERTEX>> findPath(Set<VERTEX> exploredVertices, Digraph<VERTEX> digraph, VERTEX beginning, VERTEX destination) {
    log.debug("Beginning = {}, destination = {}", beginning, destination);

    if (beginning.equals(destination)) {
      log.debug("Beginning is equal to the destination - returning []");
      return Collections.emptyList();
    }

    final var vertexToBestEdge = new HashMap<VERTEX, Digraph.Edge<VERTEX>>();
    final var vertexToScore = new HashMap<VERTEX, Integer>();
    vertexToScore.put(beginning, 0);

    // lowest score first
    final var vertexQueue = new PriorityQueue<VERTEX>(Comparator.comparing(vertexToScore::get));
    vertexQueue.add(beginning);

    // The goal is to iterate over each vertex in the `vertexQueue` ([1]), each time picking the one having the lowest score ([2]).
    // At first, the queue contains only the beginning vertex - this changes later.

    // Each time we're exploring a next `vertex` it is guaranteed that we've found the shortest path to it -
    // - hence if the `vertex` is equal to the `destination` then we've found the shorted path to it ([3]).

    // [1]
    while (!vertexQueue.isEmpty()) {
      // [2]
      final var vertex = vertexQueue.poll();
      final var vertexScore = vertexToScore.getOrDefault(vertex, MAX_VALUE);
      exploredVertices.add(vertex);

      log.debug("Exploring vertex {}", vertex);

      // [3]
      if (vertex.equals(destination)) {
        log.debug("Found the shortest path to the destination - break");
        break;
      }

      // Now, going through all available edges ([4]), reach the `adjacentVertex` ([5]) to check if this is the shortest path to it ([6]).
      // Also, given that the `adjacentVertex` was not explored yet ([7]) - add it to the `vertexQueue`
      // [4]
      for (final var edge : digraph.getEdges(vertex)) {
        log.debug("Trying edge {}", edge);
        assertThatEdgeIsValid(edge);
        // [5]
        final var adjacentVertex = edge.adjacentVertex();
        final var adjacentVertexScore = vertexScore + edge.weight();
        // [6]
        if (vertexToScore.containsKey(adjacentVertex) && vertexToScore.get(adjacentVertex) <= adjacentVertexScore)
          continue;

        log.debug("Edge {} is the shortest path - overriding vertexToScore and vertexToBestEdge");
        vertexToBestEdge.put(adjacentVertex, edge);
        vertexToScore.put(adjacentVertex, adjacentVertexScore);
        // [7]
        // Do not requeue already explored vertices
        if (exploredVertices.contains(adjacentVertex))
          continue;

        // If the element is already present at the `vertexQueue`, just adding it again
        // (or doing nothing at all) would not update its priority - hence this "requeue" (remove & add)
        vertexQueue.remove(adjacentVertex);
        vertexQueue.add(adjacentVertex);
      }
    }

    // All that's left to do is to map that to the output
    return retracePath(vertexToBestEdge, destination);
  }

  private static <VERTEX> void assertThatEdgeIsValid(Digraph.Edge<VERTEX> edge) {
    if (edge.weight() < 0) {
      throw new IllegalArgumentException("Edge " + edge + " is not valid. Reason: Weight must not be negative");
    }
  }

}
