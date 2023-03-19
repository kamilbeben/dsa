package pl.beben.algorithm.pathfinding;

public record GridVertex (int xCoordinate, int yCoordinate) {
  public static final int EDGE_WEIGHT = 100;

  // exact value is closer to 141.42 which means that both Dijkstra and A-Star algorithms will probably prefer diagonal movement
  public static final int DIAGONAL_EDGE_WEIGHT = 141;
}
