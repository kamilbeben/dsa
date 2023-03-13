package pl.beben.datastructure;

import lombok.Getter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Directed graph structure
 */
@Getter
public class Digraph<VERTEX> {

  final Set<VERTEX> vertices = new HashSet<>();
  final Set<Edge<VERTEX>> edges = new HashSet<>();

  public VERTEX createVertice(VERTEX vertex) {
    final var vertexIsUnique = vertices.add(vertex);

    if (vertexIsUnique)
      return vertex;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Edge createEdge(VERTEX vertex, String adjacentVertice) {
    return createEdge(vertex, adjacentVertice, null);
  }

  public Edge createEdge(VERTEX vertex, String adjacentVertice, Integer weight) {
    final var edge = new Edge(vertex, adjacentVertice, weight);
    final var edgeIsUnique = edges.add(edge);

    if (edgeIsUnique)
      return edge;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Set<Edge<VERTEX>> computeEdges(VERTEX vertex) {
    return getEdges().stream()
      .filter(edge -> edge.vertex().equals(vertex))
      .collect(Collectors.toSet());
  }

  public record Edge<VERTEX>(
    VERTEX vertex,
    VERTEX adjacentVertice,
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
