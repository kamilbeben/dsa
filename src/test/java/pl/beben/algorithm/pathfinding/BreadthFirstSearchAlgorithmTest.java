package pl.beben.algorithm.pathfinding;

import org.junit.Assert;
import org.junit.Test;
import pl.beben.datastructure.Digraph;
import java.util.Arrays;
import java.util.Collections;

public class BreadthFirstSearchAlgorithmTest {

  @Test
  public void findPathBetweenPoints() {

    // given digraph of the following structure
    //
    //       +----------------------→ I
    //       |    +----+   +---+      ↑
    //       |    ↓    |   ↓   |      |
    //  A -→ B -→ C -→ D → J --+      |
    //            |        |          |
    //            ↓        ↓          |
    //            E -----→ F -→ G     |
    //                     |          |
    //                     +--→ H ----+
    //
    final var digraph = new Digraph<String>();

    final var a = digraph.createVertex("a");
    final var b = digraph.createVertex("b");
    final var c = digraph.createVertex("c");
    final var d = digraph.createVertex("d");
    final var e = digraph.createVertex("e");
    final var f = digraph.createVertex("f");
    final var g = digraph.createVertex("g");
    final var h = digraph.createVertex("h");
    final var i = digraph.createVertex("i");
    final var j = digraph.createVertex("j");

    final var ab = digraph.createEdge(a, b);
    final var bc = digraph.createEdge(b, c);
    final var bi = digraph.createEdge(b, i);
    final var cd = digraph.createEdge(c, d);
    final var dj = digraph.createEdge(d, j);
    final var dc = digraph.createEdge(d, c); // loop
    final var jf = digraph.createEdge(j, f);
    final var jj = digraph.createEdge(j, j); // self-loop
    final var ce = digraph.createEdge(c, e);
    final var ef = digraph.createEdge(e, f);
    final var fg = digraph.createEdge(f, g);
    final var fh = digraph.createEdge(f, h);
    final var hi = digraph.createEdge(h, i);

    var beginning = a;
    var destination = f;
    var expectedPath = Arrays.asList(ab, bc, ce, ef); // it's shorter than a -> b -> c -> d -> j -> f

    // then
    Assert.assertEquals(expectedPath, BreadthFirstSearchAlgorithm.findPath(digraph, beginning, destination));

    // given
    beginning = a;
    destination = i;
    expectedPath = Arrays.asList(ab, bi);

    // then
    Assert.assertEquals(expectedPath, BreadthFirstSearchAlgorithm.findPath(digraph, beginning, destination));

    // given
    beginning = a;
    destination = a;
    expectedPath = Collections.emptyList(); // beginning is equal to the destination

    // then
    Assert.assertEquals(expectedPath, BreadthFirstSearchAlgorithm.findPath(digraph, beginning, destination));

    // given
    beginning = b;
    destination = a;
    expectedPath = null; // path from b to a does not exist

    // then
    Assert.assertEquals(expectedPath, BreadthFirstSearchAlgorithm.findPath(digraph, beginning, destination));
  }

}
