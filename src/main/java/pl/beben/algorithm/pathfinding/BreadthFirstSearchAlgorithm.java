package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.beben.datastructure.Digraph;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
   * @return see {@link pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm#retracePath(java.util.Map, String, pl.beben.datastructure.Digraph.Edge)}
   */
  public static List<Digraph.Edge> findPath(Digraph digraph, String beginning, String destination) {
    log.debug("Beginning = {}, destination = {}", beginning, destination);

    if (beginning.equals(destination)) {
      log.debug("Beginning is equal to the destination - returning []");
      return Collections.emptyList();
    }

    final var exploredVerticeToEdge = new HashMap<String, Digraph.Edge>();

    final var exploredVertices = new HashSet<String>();
    exploredVertices.add(beginning);

    // `addLast` and `pollFirst` makes this a FIFO queue
    // replacing `pollFirst` with `pollLast` would turn it into a LIFO, and hence - turn BreadthFirstSearch to DepthFirstSearch
    final var verticeQueue = new LinkedList<String>();
    verticeQueue.addLast(beginning);

    queue: while (!verticeQueue.isEmpty()) {
      final var vertice = verticeQueue.pollFirst();
      log.debug("Polled vertice {}", vertice);

      for (final var edge : digraph.computeEdges(vertice)) {
        final var adjacentVertice = edge.adjacentVertice();
        final var verticeIsExploredForTheFirstTime = exploredVertices.add(adjacentVertice);
        if (!verticeIsExploredForTheFirstTime) {
          log.debug("Tried to explore adjacentVertice {}, but it is already explored - continue", adjacentVertice);
          continue;
        }

        log.debug("Exploring adjacentVertice {}", adjacentVertice);
        verticeQueue.addLast(adjacentVertice);
        exploredVerticeToEdge.put(adjacentVertice, edge);

        if (adjacentVertice.equals(destination)) {
          log.debug("Destination has been found - breaking from the queue");
          break queue;
        }
      }
    }

    return retracePath(exploredVerticeToEdge, destination, null);
  }

}
