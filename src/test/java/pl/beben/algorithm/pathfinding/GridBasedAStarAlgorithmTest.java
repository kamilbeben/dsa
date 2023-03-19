package pl.beben.algorithm.pathfinding;

import org.junit.Assert;
import org.junit.Test;
import static pl.beben.algorithm.pathfinding.GridBasedPathfindingAlgorithmTestUtils.computeSolution;

public class GridBasedAStarAlgorithmTest {

  @Test
  public void testFoundOptimalSolutionNoObstacles() {
    // given
    final var problem =
      """
      ···················
      ···B···············
      ···················
      ···················
      ···················
      ················D··
      ···················
      """;

    // when
    final var solution = computeSolution(null, problem, GridBasedAStarAlgorithm::findPath, true);

    // then
    Assert.assertEquals(13, solution.path().size());
    Assert.assertTrue(solution.exploredVertices().size() < 26);
  }

  @Test
  public void testFoundOptimalSolutionWithObstacles() {
    // given
    final var problem =
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
      """;

    // when
    final var solution = computeSolution(null, problem, GridBasedAStarAlgorithm::findPath, true);

    // then
    Assert.assertEquals(19, solution.path().size());
    // as this problem is much more complicated the algorithm will and should explore much more than in simpler problem
  }

  @Test
  public void testNotFoundAnySolution() {
    // given
    final var problem =
      """
      ······
      ··■■■·
      ·B■D■·
      ··■■■·
      ······
      """;

    // when
    final var solution = computeSolution(null, problem, GridBasedAStarAlgorithm::findPath, true);

    // then
    Assert.assertNull(solution.path());
    Assert.assertEquals(21, solution.exploredVertices().size()); // every vertice that is outside of the obstacle
  }

}
