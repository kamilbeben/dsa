package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.beben.datastructure.Digraph;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static lombok.AccessLevel.PRIVATE;
import static pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm.retracePath;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
public class BreadthFirstSearchAlgorithm {

  /**
   * Time complexity: <i>O(VerticeCount + EdgeCount)</i> <br/>
   * It's faster than Dijkstra, but it doesn't care about edge's weights - which is why it's application is completely different.
   *
   * @param digraph a directed, unweighted graph
   * @return see {@link pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm#retracePath(java.util.Map, Object)}
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

    final var exploredVerticeToEdge = new HashMap<VERTEX, Digraph.Edge<VERTEX>>();

    exploredVertices.add(beginning);

    final var vertexQueue = new LinkedList<VERTEX>();
    vertexQueue.addLast(beginning);

    queue: while (!vertexQueue.isEmpty()) {
      final var vertex = vertexQueue.pollFirst();
      log.debug("Polled vertex {}", vertex);

      for (final var edge : digraph.getEdges(vertex)) {
        final var adjacentVertice = edge.adjacentVertex();
        final var vertexIsExploredForTheFirstTime = exploredVertices.add(adjacentVertice);
        if (!vertexIsExploredForTheFirstTime) {
          log.debug("Tried to explore adjacentVertex {}, but it is already explored - continue", adjacentVertice);
          continue;
        }

        log.debug("Exploring adjacentVertex {}", adjacentVertice);
        vertexQueue.addLast(adjacentVertice);
        exploredVerticeToEdge.put(adjacentVertice, edge);

        if (adjacentVertice.equals(destination)) {
          log.debug("Destination has been found - breaking from the queue");
          break queue;
        }
      }
    }

    return retracePath(exploredVerticeToEdge, destination);
  }

}
