package pl.beben.algorithm.pathfinding;

import org.junit.Test;
import static pl.beben.algorithm.pathfinding.GridBasedPathfindingAlgorithmTestUtils.computeSolution;

public class PathfindingAlgorithmsSanityCheck {

  @Test
  public void sanityCheckWithNoObstacles() {
    sanityCheck(
      "sanityCheckWithNoObstacles",
      """
      ···················
      ···B···············
      ···················
      ···················
      ···················
      ················D··
      ···················
      """
    );
  }

  @Test
  public void sanityCheckWithLargeGridNoObstacles() {
    sanityCheck(
      "sanityCheckWithNoObstacles",
      """
      ············································
      ···B········································
      ············································
      ············································
      ············································
      ············································
      ············································
      ············································
      ············································
      ·········································D··
      ············································
      """
    );
  }

  @Test
  public void sanityCheckWithSmallObstacle() {
    sanityCheck(
      "sanityCheckWithSmallObstacle",
      """
      ···················
      ···B·····■·········
      ·········■·········
      ················D··
      ···················
      """
    );
  }

  @Test
  public void sanityCheckWithLargeObstacle() {
    sanityCheck(
      "sanityCheckWithLargeObstacle",
      """
      ·······························
      ·······························
      ······■■■■■■■■■■■■■■■■■■·······
      ·······················■·······
      ··B····················■···D···
      ·······················■·······
      ······■■■■■■■■■■■■■■■■■■·······
      ·······························
      ·······························
      """
    );
  }


  @Test
  public void sanityCheckWithLargeObstacle2() {
    sanityCheck(
      "sanityCheckWithLargeObstacle2",
      """
      ····················
      ····················
      ······■■■■■■■■■■····
      ······■·············
      ··B···■····D········
      ······■·············
      ······■■■■■■■■■■····
      ····················
      ····················
      """
    );
  }

  @Test
  public void sanityCheckWithMultipleObstacles() {
    sanityCheck(
      "sanityCheckWithMultipleObstacles",
      """
      ·······■····························
      ···B···■······■·····················
      ·······■······■·······■■■■■■■■■■■■··
      ■■■■■··■······■·····················
      ·············■■············■■■■·····
      ··········■■■■············■■··■■■···
      ········■■■···············■·····■■··
      ··························■■········
      ············■··············■■·······
      ············■···············■■···D··
      ·····························■······
      """
    );
  }

  private void sanityCheck(String problemName, String problemText) {
    System.out.print(
      computeSolution("BFS (diagonal movement disabled) - " + problemName,      problemText, BreadthFirstSearchAlgorithm::findPath, false).toString() +
      computeSolution("Dijkstra (diagonal movement enabled) - " + problemName,  problemText, DijkstraAlgorithm::findPath,           true) +
      computeSolution("A-Star (diagonal movement enabled) - " + problemName,    problemText, GridBasedAStarAlgorithm::findPath,     true)
    );
  }

}
