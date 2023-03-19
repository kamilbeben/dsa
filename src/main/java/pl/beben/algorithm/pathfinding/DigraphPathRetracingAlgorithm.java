package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.beben.datastructure.Digraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static lombok.AccessLevel.PRIVATE;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
class DigraphPathRetracingAlgorithm {

  /**
   * @return list of edges needed to be followed in order to get to the destination, {@literal null} if such path does not exist
   */
  static <VERTEX> List<Digraph.Edge<VERTEX>> retracePath(Map<VERTEX, Digraph.Edge<VERTEX>> vertexToBestEdge, VERTEX destination) {

    if (!vertexToBestEdge.containsKey(destination)) {
      log.debug("destination \"{}\" has not been reached - returning null", destination);
      return null;
    }

    final var path = new ArrayList<Digraph.Edge<VERTEX>>();

    // Starting at the `destination`
    var edge = vertexToBestEdge.get(destination);

    do {
      // reconstruct the path by retracing algorithm's steps and going backwards to the `beginning`
      path.add(0, edge);
      edge = vertexToBestEdge.get(edge.vertex());
      // edge being null means that we've reached the `beginning`
    } while (edge != null);

    log.debug("Path has been reconstructed - returning {}", path);
    return path;
  }

}
