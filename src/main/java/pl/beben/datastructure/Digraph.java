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

  public String createVertice(String vertice) {
    final var verticeIsUnique = vertices.add(vertice);

    if (verticeIsUnique)
      return vertice;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Edge createEdge(String vertice, String adjacentVertice) {
    return createEdge(vertice, adjacentVertice, null);
  }

  public Edge createEdge(String vertice, String adjacentVertice, Integer weight) {
    final var edge = new Edge(vertice, adjacentVertice, weight);
    final var edgeIsUnique = edges.add(edge);

    if (edgeIsUnique)
      return edge;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Set<Edge> computeEdges(String vertice) {
    return getEdges().stream()
      .filter(edge -> edge.vertice().equals(vertice))
      .collect(Collectors.toSet());
  }

  public record Edge(
    String vertice,
    String adjacentVertice,
    Integer weight
  ) {

    @Override
    public String toString() {
      return vertice + " -> " + adjacentVertice + (
        weight != null
          ? " (" + weight + ")"
          : ""
      );
    }
  }

}
