package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import pl.beben.datastructure.Digraph;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static lombok.AccessLevel.PRIVATE;
import static pl.beben.algorithm.pathfinding.GridVertex.DIAGONAL_EDGE_WEIGHT;
import static pl.beben.algorithm.pathfinding.GridVertex.EDGE_WEIGHT;

@NoArgsConstructor(access = PRIVATE)
public class GridBasedAStarAlgorithm {

  public static List<Digraph.Edge<GridVertex>> findPath(Digraph<GridVertex> digraph, GridVertex beginning, GridVertex destination) {
    return findPath(new HashSet<>(), digraph, beginning, destination);
  }

  public static List<Digraph.Edge<GridVertex>> findPath(Set<GridVertex> exploredVertices,
                                                        Digraph<GridVertex> digraph,
                                                        GridVertex beginning,
                                                        GridVertex destination) {

    return AStarAlgorithm.findPath(
      exploredVertices,
      digraph,
      beginning,
      destination,
      vertex -> estimateTravelCost(vertex, destination)
    );
  }

  private static int estimateTravelCost(GridVertex from, GridVertex destination) {
    final var deltaX = abs(from.xCoordinate() - destination.xCoordinate());
    final var deltaY = abs(from.yCoordinate() - destination.yCoordinate());

    return EDGE_WEIGHT * (deltaX + deltaY) + (DIAGONAL_EDGE_WEIGHT - 2 * EDGE_WEIGHT) * min(deltaX, deltaY);
  }

}
