package pl.beben.algorithm.pathfinding;

import org.junit.Assert;
import org.junit.Test;
import pl.beben.datastructure.Digraph;
import java.util.Arrays;
import java.util.Collections;

public class DijkstraAlgorithmTest {

  @Test
  public void findPathBetweenTowns() {

    // given graph of the following structure
    //
    //         [C]----------(4)-----------[D]
    //    ____/ | \____                    |
    //   /      |      |                   |
    //  |       |      |                  (1)
    // (3)      |     (1)                  |
    //  |       |      |                   |
    // [A]     (2)    [E]---(2)----[B]-----+
    //  |       |      |            |      |
    // (2)      |     (3)           |      |
    //  |       |      |           (6)    (2)
    //   \      |  ____+            |      |
    //    \     | /                 |      |
    //     ^---[F]------------------^      |
    //          |________(5)______________[G]
    //
    //
    // [X] - vertice
    // (0) - edge's weight
    //
    // Dataset borrowed from "Spanning Tree"'s video called "How Dijkstra's Algorithm Works" (https://www.youtube.com/watch?v=EFg3u_E6eHU)

    final var graph = new Digraph();

    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");
    final var townC = graph.createVertice("Town C");
    final var townD = graph.createVertice("Town D");
    final var townE = graph.createVertice("Town E");
    final var townF = graph.createVertice("Town F");
    final var townG = graph.createVertice("Town G");
    final var townLonely = graph.createVertice("Lonely town that's not connected to anything");

    // every second edge is a reversed version of it's previous
    final var acEdge = graph.createEdge(townA, townC, 3);
    final var caEdge = graph.createEdge(townC, townA, 3);
    final var afEdge = graph.createEdge(townA, townF, 2);
    final var faEdge = graph.createEdge(townF, townA, 2);
    final var cdEdge = graph.createEdge(townC, townD, 4);
    final var dcEdge = graph.createEdge(townD, townC, 4);
    final var ceEdge = graph.createEdge(townC, townE, 1);
    final var ecEdge = graph.createEdge(townE, townC, 1);
    final var cfEdge = graph.createEdge(townC, townF, 2);
    final var fcEdge = graph.createEdge(townF, townC, 2);
    final var feEdge = graph.createEdge(townF, townE, 3);
    final var efEdge = graph.createEdge(townE, townF, 3);
    final var fbEdge = graph.createEdge(townF, townB, 6);
    final var bfEdge = graph.createEdge(townB, townF, 6);
    final var fgEdge = graph.createEdge(townF, townG, 5);
    final var gfEdge = graph.createEdge(townG, townF, 5);
    final var dbEdge = graph.createEdge(townD, townB, 1);
    final var bdEdge = graph.createEdge(townB, townD, 1);
    final var ebEdge = graph.createEdge(townE, townB, 2);
    final var beEdge = graph.createEdge(townB, townE, 2);
    final var gbEdge = graph.createEdge(townG, townB, 2);
    final var bgEdge = graph.createEdge(townB, townG, 2);

    var startingPoint = townA;
    var destination = townB;
    var expectedPath = Arrays.asList(acEdge, ceEdge, ebEdge);

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(graph, startingPoint, destination));

    // given
    startingPoint = townA;
    destination = townA;
    expectedPath = Collections.emptyList();

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(graph, startingPoint, destination));

    // given
    startingPoint = townA;
    destination = townLonely;
    expectedPath = null;

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(graph, startingPoint, destination));
  }

  @Test
  public void canHandleLoops() {
    // given
    final var graph = new Digraph();
    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");

    final var aaEdge = graph.createEdge(townA, townA, 1);
    final var abEdge = graph.createEdge(townA, townB, 3);

    var startingPoint = townA;
    var destination = townB;
    var expectedPath = Arrays.asList(abEdge);

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(graph, startingPoint, destination));
  }

  @Test
  public void canHandleUnidirectionalEdges() {
    // given
    final var graph = new Digraph();
    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");
    final var townC = graph.createVertice("Town C");
    final var townD = graph.createVertice("Town D");

    final var abEdge = graph.createEdge(townA, townB, 3);
    final var adEdge = graph.createEdge(townA, townD, 8);
    final var bcEdge = graph.createEdge(townB, townC, 1);
    final var dcEdge = graph.createEdge(townD, townC, 1);

    var startingPoint = townA;
    var destination = townC;
    var expectedPath = Arrays.asList(abEdge, bcEdge);

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(graph, startingPoint, destination));
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsExceptionWhenEncountersNegativeEdge() {
    // given
    final var graph = new Digraph();
    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");

    final var aaEdge = graph.createEdge(townA, townA, 1);
    final var abEdge = graph.createEdge(townA, townB, -3);

    var startingPoint = townA;
    var destination = townB;

    // when
    DijkstraAlgorithm.findPath(graph, startingPoint, destination);
  }

}