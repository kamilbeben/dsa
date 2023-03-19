package pl.beben.datastructure;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Directed graph structure
 */
@Getter
public class Digraph<VERTEX> {

  final Set<VERTEX> vertices = new HashSet<>();
  final Map<VERTEX, Set<Edge<VERTEX>>> vertexToEdges = new HashMap<>();

  public VERTEX createVertex(VERTEX vertex) {
    final var vertexIsUnique = vertices.add(vertex);

    if (vertexIsUnique)
      return vertex;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Edge createEdge(VERTEX vertex, VERTEX adjacentVertice) {
    return createEdge(vertex, adjacentVertice, null);
  }

  public Edge createEdge(VERTEX vertex, VERTEX adjacentVertice, Integer weight) {
    final var edge = new Edge(vertex, adjacentVertice, weight);
    final var edges = vertexToEdges.computeIfAbsent(vertex, key -> new HashSet<>());
    final var edgeIsUnique = edges.add(edge);

    if (edgeIsUnique)
      return edge;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public Set<Edge<VERTEX>> getEdges(VERTEX vertex) {
    return vertexToEdges.getOrDefault(vertex, Collections.emptySet());
  }

  public record Edge<VERTEX>(
    VERTEX vertex,
    VERTEX adjacentVertex,
    Integer weight
  ) {

    @Override
    public String toString() {
      return vertex + " -> " + adjacentVertex + (
        weight != null
          ? " (" + weight + ")"
          : ""
      );
    }
  }

}
