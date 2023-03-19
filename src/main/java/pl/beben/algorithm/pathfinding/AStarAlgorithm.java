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
import java.util.function.Function;
import static java.lang.Integer.MAX_VALUE;
import static lombok.AccessLevel.PRIVATE;
import static pl.beben.algorithm.pathfinding.DigraphPathRetracingAlgorithm.retracePath;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
public class AStarAlgorithm {

  /**
   * See {@link pl.beben.algorithm.pathfinding.DijkstraAlgorithm} if you're not familiar with it, because A-Star is an extension to it
   *
   * @param heuristicFunction function returning score (positive integer value) - an estimated cost of getting from given vertex to the destination
   */
  public static <VERTEX> List<Digraph.Edge<VERTEX>> findPath(Digraph<VERTEX> digraph,
                                                             VERTEX beginning,
                                                             VERTEX destination,
                                                             Function<VERTEX, Integer> heuristicFunction) {
    return findPath(new HashSet<>(), digraph, beginning, destination, heuristicFunction);
  }

  public static <VERTEX> List<Digraph.Edge<VERTEX>> findPath(Set<VERTEX> exploredVertices,
                                                             Digraph<VERTEX> digraph,
                                                             VERTEX beginning,
                                                             VERTEX destination,
                                                             Function<VERTEX, Integer> heuristicFunction) {

    // See Dijkstra's algorithm first.
    // I'm going to comment on the differences between these two algorithms, because A-Star is
    // an extension to Dijkstra's algorithm - so commenting on general logic seems redundant and
    // would obscure what's actually important.

    log.debug("Beginning = {}, destination = {}", beginning, destination);

    if (beginning.equals(destination)) {
      log.debug("Beginning is equal to the destination - returning []");
      return Collections.emptyList();
    }

    final var vertexToBestEdge = new HashMap<VERTEX, Digraph.Edge<VERTEX>>();

    // This is what was simply `vertexToScore` in Dijkstra's algorithm -
    // - the score that we've learned - explored
    final var vertexToGScore = new HashMap<VERTEX, Integer>();
    vertexToGScore.put(beginning, 0);

    // GScore + estimated score to the destination, or `FScore(v) = GScore(v) + HeuristicScore(v)`
    final var vertexToFScore = new HashMap<VERTEX, Integer>();
    vertexToFScore.put(beginning, heuristicFunction.apply(beginning));

    // Dijkstra's queue was simply `lowest score first` - this one is making use of both GScore and FScore
    final var vertexQueue = new PriorityQueue<>(createVertexQueueComparator(vertexToGScore, vertexToFScore));
    vertexQueue.add(beginning);

    while (!vertexQueue.isEmpty()) {
      final var vertex = vertexQueue.poll();
      final var vertexGScore = vertexToGScore.getOrDefault(vertex, MAX_VALUE);
      exploredVertices.add(vertex);
      log.debug("Exploring vertex {}", vertex);

      if (vertex.equals(destination)) {
        log.debug("Found the shortest path to the destination - break");
        break;
      }

      for (final var edge : digraph.getEdges(vertex)) {
        log.debug("Trying edge {}", edge);
        assertThatEdgeIsValid(edge);

        final var adjacentVertex = edge.adjacentVertex();
        final var adjacentVertexGScore = vertexGScore + edge.weight();

        if (vertexToGScore.containsKey(adjacentVertex) && vertexToGScore.get(adjacentVertex) <= adjacentVertexGScore)
          continue;

        log.debug("Edge {} is the shortest path - overriding vertexToGScore and vertexToBestEdge");
        vertexToBestEdge.put(adjacentVertex, edge);
        vertexToGScore.put(adjacentVertex, adjacentVertexGScore);
        vertexToFScore.put(adjacentVertex, adjacentVertexGScore + heuristicFunction.apply(adjacentVertex));

        if (exploredVertices.contains(adjacentVertex))
          continue;

        vertexQueue.remove(adjacentVertex);
        vertexQueue.add(adjacentVertex);
      }
    }

    return retracePath(vertexToBestEdge, destination);
  }

  private static <VERTEX> void assertThatEdgeIsValid(Digraph.Edge<VERTEX> edge) {
    if (edge.weight() < 0) {
      throw new IllegalArgumentException("Edge " + edge + " is not valid. Reason: Weight must not be negative");
    }
  }

  /*
   * Lowest F score first,
   * in case of a tie - highest G score first
   *
   * Reasoning.:
   * This is what a solution looks like if there is no mechanism of braking ties whatsoever -
   * notice how much vertices are explored just because their (F)inal score is equal to what's currently going on in the path, which really is unnecessary.
   *
   *  Problem 'A-Star (diagonal movement enabled) - sanityCheckWithNoObstacles'
   *  Explored vertices count: 358
   *  Path length: 72
   *  |                                                                             |
   *  |   B*···                                                                     |
   *  |    ·*······················                                                 |
   *  |     ·***********·············                                               |
   *  |      ···········*******·················                                    |
   *  |       ·················***********************···                           |
   *  |        ·······································***·                          |
   *  |         ········································ *****··················    |
   *  |          ·········· ·······························   *··················   |
   *  |           ··································· ······   ******************D  |
   *  |                                                                             |
   *  Legend:
   *  'B' - beginning
   *  'D' - destination
   *  '*' - path
   *  '·' - explored vertex
   *  ' ' - unexplored vertex
   *
   * Meanwhile, after introducing this tie-breaking mechanism, solution to the same problem now looks like this:
   *
   *  Problem 'A-Star (diagonal movement enabled) - sanityCheckWithNoObstacles'
   *  Explored vertices count: 72
   *  Path length: 72
   *  |                                                                             |
   *  |   B                                                                         |
   *  |    *                                                                        |
   *  |     *                                                                       |
   *  |      *                                                                      |
   *  |       *                                                                     |
   *  |        *                                                                    |
   *  |         *                                                                   |
   *  |          *                                                                  |
   *  |           ***************************************************************D  |
   *  |                                                                             |
   *
   * As you can see, the path length stays the same while 3 times less vertices are being explored.
   */
  private static <VERTEX> Comparator<VERTEX> createVertexQueueComparator(HashMap<VERTEX, Integer> vertexToGScore, HashMap<VERTEX, Integer> vertexToFScore) {
    return (Comparator<VERTEX>) Comparator
      .comparing(vertexToFScore::get)
      .thenComparing(
        Comparator
          .comparing(vertexToGScore::get)
          .reversed()
      );
  }

}
