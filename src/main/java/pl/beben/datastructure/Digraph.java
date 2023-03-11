package pl.beben.datastructure;

import lombok.Getter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Directed graph structure
 */
@Getter
public class Digraph {

  final Set<String> vertices = new HashSet<>();
  final Set<Edge> edges = new HashSet<>();

  public String createVertice(String vertex) {
    final var vertexIsUnique = vertices.add(vertex);

    if (vertexIsUnique)
      return vertex;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Edge createEdge(String vertex, String adjacentVertice) {
    return createEdge(vertex, adjacentVertice, null);
  }

  public Edge createEdge(String vertex, String adjacentVertice, Integer weight) {
    final var edge = new Edge(vertex, adjacentVertice, weight);
    final var edgeIsUnique = edges.add(edge);

    if (edgeIsUnique)
      return edge;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Set<Edge> computeEdges(String vertex) {
    return getEdges().stream()
      .filter(edge -> edge.vertex().equals(vertex))
      .collect(Collectors.toSet());
  }

  public record Edge(
    String vertex,
    String adjacentVertice,
    Integer weight
  ) {

    @Override
    public String toString() {
      return vertex + " -> " + adjacentVertice + (
        weight != null
          ? " (" + weight + ")"
          : ""
      );
    }
  }

}
