package pl.beben.datastructure;

import lombok.Getter;
import java.util.HashSet;
import java.util.Set;

/**
 * @param <DATA> must implement a valid {@link Object#hashCode()} method
 */
@Getter
public class Graph<DATA> {

  final Set<Vertice<DATA>> vertices = new HashSet<>();
  final Set<Edge<DATA>> edges = new HashSet<>();

  public Vertice<DATA> createVertice(DATA data) {
    final var vertice = new Vertice<>(data);
    final var verticeIsUnique = vertices.add(vertice);

    if (verticeIsUnique)
      return vertice;
    else
      throw new IllegalArgumentException("Vertice is not unique");
  }

  public Edge<DATA> createEdge(Vertice<DATA> firstVertice, Vertice<DATA> secondVertice, int weight, Edge.Type type) {
    final var edge = new Edge<>(firstVertice, secondVertice, weight, type);
    final var edgeIsUnique = edges.add(edge);

    if (edgeIsUnique)
      return edge;
    else
      throw new IllegalArgumentException("Edge is not unique");
  }

  public record Vertice<VERTICE_DATA>(
    VERTICE_DATA data
  ) {
    @Override
    public String toString() {
      return data.toString();
    }
  }

  /**
   * If the Edge's {@link Edge#type} is {@link Edge.Type#UNIDIRECTIONAL}<br/>
   * then - {@link Edge#firstVertice} is a beginning and {@link Edge#secondVertice} is an end<br/>
   * else - their order doesn't matter
   */
  public record Edge<EDGE_DATA>(
    Vertice<EDGE_DATA> firstVertice,
    Vertice<EDGE_DATA> secondVertice,
    int cost,
    Graph.Edge.Type type
  ) {

    @Override
    public String toString() {
      final var link = type == Type.BIDIRECTIONAL
        ? " <-> "
        : " --> ";

      return firstVertice + link + secondVertice + " (" + cost + ")";
    }

  public enum Type {
      BIDIRECTIONAL, // eg 'two-way street'
      UNIDIRECTIONAL // eg 'one-way street'
    }
  }

}
