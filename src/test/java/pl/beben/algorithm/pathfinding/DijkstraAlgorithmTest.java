package pl.beben.algorithm.pathfinding;

import org.junit.Assert;
import org.junit.Test;
import pl.beben.datastructure.Digraph;
import java.util.Arrays;
import java.util.Collections;

public class DijkstraAlgorithmTest {

  @Test
  public void findPathBetweenTowns() {

    // given weighted digraph of the following structure
    //
    //  +-----→[C]----------(4)----------→[D]
    //  |       | \____                    |
    //  |       |      |                   |
    //  |       |      |                  (1)
    // (3)      |     (1)                  |
    //  |       |      ↓                   |
    // [A]     (2)    [E]---(2)---→[B]←----+
    //  |       |      ↑            ↑      |
    // (2)      |      |            |      |
    //  |       |     (3)          (6)    (2)
    //  |       |  ____|            |      |
    //  |       | /                 |      |
    //  +-----→[F]------------------^      |
    //          |                          |
    //          +--------(5)-------------→[G]
    //
    // [X] - vertex
    // (0) - edge's weight
    //
    // Dataset borrowed from "Spanning Tree"'s video called "How Dijkstra's Algorithm Works" (https://www.youtube.com/watch?v=EFg3u_E6eHU)

    final var digraph = new Digraph();

    final var a = digraph.createVertice("a");
    final var b = digraph.createVertice("b");
    final var c = digraph.createVertice("c");
    final var d = digraph.createVertice("d");
    final var e = digraph.createVertice("e");
    final var f = digraph.createVertice("f");
    final var g = digraph.createVertice("g");
    final var lonelyVertice = digraph.createVertice("Lonely  that's not connected to anything");

    // every second edge is a reversed version of it's previous
    final var ac = digraph.createEdge(a, c, 3);
    final var ca = digraph.createEdge(c, a, 3);
    final var af = digraph.createEdge(a, f, 2);
    final var fa = digraph.createEdge(f, a, 2);
    final var cd = digraph.createEdge(c, d, 4);
    final var dc = digraph.createEdge(d, c, 4);
    final var ce = digraph.createEdge(c, e, 1);
    final var ec = digraph.createEdge(e, c, 1);
    final var cf = digraph.createEdge(c, f, 2);
    final var fc = digraph.createEdge(f, c, 2);
    final var fe = digraph.createEdge(f, e, 3);
    final var ef = digraph.createEdge(e, f, 3);
    final var fb = digraph.createEdge(f, b, 6);
    final var bf = digraph.createEdge(b, f, 6);
    final var fg = digraph.createEdge(f, g, 5);
    final var gf = digraph.createEdge(g, f, 5);
    final var db = digraph.createEdge(d, b, 1);
    final var bd = digraph.createEdge(b, d, 1);
    final var eb = digraph.createEdge(e, b, 2);
    final var be = digraph.createEdge(b, e, 2);
    final var gb = digraph.createEdge(g, b, 2);
    final var bg = digraph.createEdge(b, g, 2);

    var beginning = a;
    var destination = b;
    var expectedPath = Arrays.asList(ac, ce, eb);

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(digraph, beginning, destination));

    // given
    beginning = a;
    destination = a;
    expectedPath = Collections.emptyList();

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(digraph, beginning, destination));

    // given
    beginning = a;
    destination = lonelyVertice;
    expectedPath = null;

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(digraph, beginning, destination));
  }

  @Test
  public void canHandleLoops() {
    // given
    final var digraph = new Digraph();
    final var a = digraph.createVertice("a");
    final var b = digraph.createVertice("b");

    final var aa = digraph.createEdge(a, a, 1);
    final var ab = digraph.createEdge(a, b, 3);

    var beginning = a;
    var destination = b;
    var expectedPath = Arrays.asList(ab);

    // then
    Assert.assertEquals(expectedPath, DijkstraAlgorithm.findPath(digraph, beginning, destination));
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsExceptionWhenEncountersNegative() {
    // given
    final var digraph = new Digraph();
    final var a = digraph.createVertice("a");
    final var b = digraph.createVertice("b");

    final var aa = digraph.createEdge(a, a, 1);
    final var ab = digraph.createEdge(a, b, -3);

    var beginning = a;
    var destination = b;

    // when
    DijkstraAlgorithm.findPath(digraph, beginning, destination);
  }

}