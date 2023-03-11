package pl.beben.algorithm.pathfinding;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.beben.datastructure.Digraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static lombok.AccessLevel.PRIVATE;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
class DigraphPathRetracingAlgorithm {

  /**
   * @return list of edges needed to be followed in order to get to the destination, {@literal null} if such path does not exist
   */
  static List<Digraph.Edge> retracePath(Map<String, Digraph.Edge> verticeToEdgeLeadingToIt,
                                        String destination,
                                        Digraph.Edge nullEdgePlaceholder) {

    // If the destination was reached, the default `nullEdgePlaceholder` would have been overridden
    if (verticeToEdgeLeadingToIt.get(destination) == nullEdgePlaceholder) {
      log.debug("destination \"{}\" has not been reached - returning null", destination);
      return null;
    }

    final var path = new ArrayList<Digraph.Edge>();

    // Starting at the `destination`
    var edge = verticeToEdgeLeadingToIt.get(destination);

    do {
      // reconstruct the path by retracing algorithm's steps and going backwards to the `beginning`
      path.add(edge);
      edge = verticeToEdgeLeadingToIt.get(edge.vertice());
      // edge being null means that we've reached the `beginning`
    } while (edge != nullEdgePlaceholder);

    // Path was reconstructed by retracing algorithm's steps, it needs to be reversed
    Collections.reverse(path);
    log.debug("Path has been reconstructed - returning {}", path);
    return path;
  }

}
