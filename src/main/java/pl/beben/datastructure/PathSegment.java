package pl.beben.datastructure;

/**
 * @param beginning since an {@link #edge} can be {@link Graph.Edge.Type#BIDIRECTIONAL}, a segment must have a beginning
 * @param edge leading to another segment
 */
public record PathSegment<DATA>(
  Graph.Vertice<DATA> beginning,
  Graph.Edge<DATA> edge
) {

  @Override
  public String toString() {
    final var end = edge.firstVertice().equals(beginning)
      ? edge.secondVertice()
      : edge.firstVertice();

    return beginning + " --> " + end;
  }

}
