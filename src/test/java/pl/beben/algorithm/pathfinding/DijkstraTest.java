package pl.beben.algorithm.pathfinding;

import org.junit.Assert;
import org.junit.Test;
import pl.beben.datastructure.Graph;
import pl.beben.datastructure.PathSegment;
import java.util.Arrays;
import java.util.Collections;
import static pl.beben.datastructure.Graph.Edge.Type.BIDIRECTIONAL;
import static pl.beben.datastructure.Graph.Edge.Type.UNIDIRECTIONAL;

public class DijkstraTest {

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
    // (0) - edge's cost
    //
    // Dataset borrowed from "Spanning Tree"'s video called "How Dijkstra's Algorithm Works" (https://www.youtube.com/watch?v=EFg3u_E6eHU)

    final var graph = new Graph<String>();

    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");
    final var townC = graph.createVertice("Town C");
    final var townD = graph.createVertice("Town D");
    final var townE = graph.createVertice("Town E");
    final var townF = graph.createVertice("Town F");
    final var townG = graph.createVertice("Town G");
    final var townLonely = graph.createVertice("Lonely town that's not connected to anything");

    final var acEdge = graph.createEdge(townA, townC, 3, BIDIRECTIONAL);
    final var afEdge = graph.createEdge(townA, townF, 2, BIDIRECTIONAL);
    final var cdEdge = graph.createEdge(townC, townD, 4, BIDIRECTIONAL);
    final var ceEdge = graph.createEdge(townC, townE, 1, BIDIRECTIONAL);
    final var cfEdge = graph.createEdge(townC, townF, 2, BIDIRECTIONAL);
    final var feEdge = graph.createEdge(townF, townE, 3, BIDIRECTIONAL);
    final var fbEdge = graph.createEdge(townF, townB, 6, BIDIRECTIONAL);
    final var fgEdge = graph.createEdge(townF, townG, 5, BIDIRECTIONAL);
    final var dbEdge = graph.createEdge(townD, townB, 1, BIDIRECTIONAL);
    final var ebEdge = graph.createEdge(townE, townB, 2, BIDIRECTIONAL);
    final var gbEdge = graph.createEdge(townG, townB, 2, BIDIRECTIONAL);

    var startingPoint = townA;
    var destination = townB;
    var expectedPath = Arrays.asList(new PathSegment<>(townA, acEdge), new PathSegment<>(townC, ceEdge), new PathSegment<>(townE, ebEdge));

    // then
    Assert.assertEquals(expectedPath, Dijkstra.findPath(graph, startingPoint, destination));

    // given
    startingPoint = townA;
    destination = townA;
    expectedPath = Collections.emptyList();

    // then
    Assert.assertEquals(expectedPath, Dijkstra.findPath(graph, startingPoint, destination));

    // given
    startingPoint = townA;
    destination = townLonely;
    expectedPath = null;

    // then
    Assert.assertEquals(expectedPath, Dijkstra.findPath(graph, startingPoint, destination));
  }

  @Test
  public void canHandleLoops() {
    // given
    final var graph = new Graph<String>();
    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");

    final var aaEdge = graph.createEdge(townA, townA, 1, BIDIRECTIONAL);
    final var abEdge = graph.createEdge(townA, townB, 3, BIDIRECTIONAL);

    var startingPoint = townA;
    var destination = townB;
    var expectedPath = Arrays.asList(new PathSegment<>(townA, abEdge));

    // then
    Assert.assertEquals(expectedPath, Dijkstra.findPath(graph, startingPoint, destination));
  }

  @Test
  public void canHandleUnidirectionalEdges() {
    // given
    final var graph = new Graph<String>();
    final var townA = graph.createVertice("Town A");
    final var townB = graph.createVertice("Town B");
    final var townC = graph.createVertice("Town C");
    final var townD = graph.createVertice("Town D");

    final var abEdge = graph.createEdge(townA, townB, 3, UNIDIRECTIONAL);
    final var adEdge = graph.createEdge(townA, townD, 8, UNIDIRECTIONAL);
    final var bcEdge = graph.createEdge(townB, townC, 1, UNIDIRECTIONAL);
    final var dcEdge = graph.createEdge(townD, townC, 1, UNIDIRECTIONAL);

    var startingPoint = townA;
    var destination = townC;
    var expectedPath = Arrays.asList(new PathSegment<>(townA, abEdge), new PathSegment<>(townB, bcEdge));

    // then
    Assert.assertEquals(expectedPath, Dijkstra.findPath(graph, startingPoint, destination));
  }

}