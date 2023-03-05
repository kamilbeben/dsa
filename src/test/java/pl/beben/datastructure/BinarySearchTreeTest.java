package pl.beben.datastructure;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class BinarySearchTreeTest {

  @Test
  public void addThenValidateStructure() {

    // given tree of the following structure
    //          5
    //      3       8
    //  1         7   9
    //    2             10
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5); // root
    tree.add(3); //      -> left
    tree.add(1); //               -> left
    tree.add(2); //                         -> right
    tree.add(8); //      -> right
    tree.add(7); //               -> left
    tree.add(9); //               -> right
    tree.add(10);//                         -> right

    // then
    Assert.assertEquals(5, (int) tree.rootNode.value);
    // 5 -> 3: node (two children)
    Assert.assertEquals(3, (int) tree.rootNode.left.value);
    // 5 -> 3 -> 1 (one child)
    Assert.assertEquals(1, (int) tree.rootNode.left.left.value);
    Assert.assertNull(tree.rootNode.left.left.left);
    // 5 -> 3 -> 1 -> 2 (leaf)
    Assert.assertEquals(2, (int) tree.rootNode.left.left.right.value);
    Assert.assertNull(tree.rootNode.left.left.right.left);
    Assert.assertNull(tree.rootNode.left.left.right.right);
    // 5 -> 8: node (two children)
    Assert.assertEquals(8, (int) tree.rootNode.right.value);
    // 5 -> 8 -> 7 (leaf node)
    Assert.assertEquals(7, (int) tree.rootNode.right.left.value);
    Assert.assertNull(tree.rootNode.right.left.left);
    Assert.assertNull(tree.rootNode.right.left.right);
    // 5 -> 8 -> 9 (one child)
    Assert.assertEquals(9, (int) tree.rootNode.right.right.value);
    Assert.assertNull(tree.rootNode.right.right.left);
    // 5 -> 8 -> 9 -> 10 (leaf node)
    Assert.assertEquals(10, (int) tree.rootNode.right.right.right.value);
    Assert.assertNull(tree.rootNode.right.right.right.left);
    Assert.assertNull(tree.rootNode.right.right.right.right);
  }


  @Test
  public void addInAscendingOrderThenContains() {

    // given tree of the following structure
    //  1
    //    2
    //      3
    //        4
    //          5
    final var tree = new BinarySearchTree<Integer>();
    tree.add(1);
    tree.add(2);
    tree.add(3);
    tree.add(4);
    tree.add(5);

    // then
    Assert.assertFalse(tree.contains(0));

    Assert.assertTrue(tree.contains(1));
    Assert.assertTrue(tree.contains(2));
    Assert.assertTrue(tree.contains(3));
    Assert.assertTrue(tree.contains(4));
    Assert.assertTrue(tree.contains(5));

    Assert.assertFalse(tree.contains(6));
    Assert.assertFalse(tree.contains(7));
  }

  @Test
  public void addInDescendingOrderThenContains() {

    // given tree of the following structure
    //          5
    //        4
    //      3
    //    2
    //  1
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(4);
    tree.add(3);
    tree.add(2);
    tree.add(1);

    // then
    Assert.assertFalse(tree.contains(0));

    Assert.assertTrue(tree.contains(1));
    Assert.assertTrue(tree.contains(2));
    Assert.assertTrue(tree.contains(3));
    Assert.assertTrue(tree.contains(4));
    Assert.assertTrue(tree.contains(5));

    Assert.assertFalse(tree.contains(6));
    Assert.assertFalse(tree.contains(7));
  }

  @Test
  public void addRandomlyThenContains() {

    // given tree of the following structure
    //          5
    //      3       8
    //  1         7   9
    //    2
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(3);
    tree.add(3);
    tree.add(1);
    tree.add(2);
    tree.add(8);
    tree.add(7);
    tree.add(9);

    // then
    Assert.assertFalse(tree.contains(0));

    Assert.assertTrue(tree.contains(1));
    Assert.assertTrue(tree.contains(2));
    Assert.assertTrue(tree.contains(3));
    Assert.assertTrue(tree.contains(5));
    Assert.assertTrue(tree.contains(7));
    Assert.assertTrue(tree.contains(8));
    Assert.assertTrue(tree.contains(9));

    Assert.assertFalse(tree.contains(10));
    Assert.assertFalse(tree.contains(11));
  }

  @Test
  public void addRandomlyThenForEachInOrder() {

    // given tree of the following structure
    //          5
    //      3       8
    //    1   2   7   9
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(3);
    tree.add(1);
    tree.add(2);
    tree.add(8);
    tree.add(7);
    tree.add(9);

    final var expectedIterationOrder = Arrays.asList(1, 2, 3, 5, 7, 8, 9);

    // when
    final var actualIterationOrder = new ArrayList<Integer>();
    tree.forEach(actualIterationOrder::add);

    // then
    Assert.assertEquals(expectedIterationOrder, actualIterationOrder);
  }

  @Test
  public void addThenRemoveNonexistentElement() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);

    // when
    final var elementHasBeenRemoved = tree.remove(8);

    // then
    Assert.assertFalse(elementHasBeenRemoved);
    Assert.assertTrue(tree.contains(5));
  }

  @Test
  public void addThenRemoveNonexistentElementWithoutRootNode() {

    // given
    final var tree = new BinarySearchTree<Integer>();

    // when
    final var elementHasBeenRemoved = tree.remove(5);

    // then
    Assert.assertFalse(elementHasBeenRemoved);
  }

  @Test
  public void addThenRemoveRootNode() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);

    // when
    final var elementHasBeenRemoved = tree.remove(5);

    // then
    Assert.assertNull(tree.rootNode);
    Assert.assertTrue(elementHasBeenRemoved);
    Assert.assertFalse(tree.contains(5));
  }

  @Test
  public void addThenRemoveReplacingLeftNodeWithItsLeaf() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(3);

    // when
    final var elementHasBeenRemoved = tree.remove(3);

    // then
    Assert.assertTrue(elementHasBeenRemoved);
    Assert.assertTrue(tree.contains(5));
    Assert.assertFalse(tree.contains(3));
    Assert.assertNull(tree.rootNode.left);
    Assert.assertNull(tree.rootNode.right);
  }

  @Test
  public void addThenRemoveReplacingRightNodeWithItsLeaf() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(7);

    // when
    final var elementHasBeenRemoved = tree.remove(7);

    // then
    Assert.assertTrue(elementHasBeenRemoved);
    Assert.assertTrue(tree.contains(5));
    Assert.assertFalse(tree.contains(7));
    Assert.assertNull(tree.rootNode.left);
    Assert.assertNull(tree.rootNode.right);
  }

  @Test
  public void addThenRemoveReplacingSelfWithRightLeaf() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(7);

    // when
    final var elementHasBeenRemoved = tree.remove(5);

    // then
    Assert.assertTrue(elementHasBeenRemoved);
    Assert.assertTrue(tree.contains(7));
    Assert.assertFalse(tree.contains(5));
    Assert.assertNull(tree.rootNode.left);
    Assert.assertNull(tree.rootNode.right);
  }

  @Test
  public void addThenRemoveReplacingSelfWithLeftLeaf() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(3);

    // when
    final var elementHasBeenRemoved = tree.remove(5);

    // then
    Assert.assertTrue(elementHasBeenRemoved);
    Assert.assertTrue(tree.contains(3));
    Assert.assertFalse(tree.contains(5));
    Assert.assertNull(tree.rootNode.left);
    Assert.assertNull(tree.rootNode.right);
  }

  @Test
  public void addThenRemoveReplacingSelfWithMinValueOfTheRightNode() {

    // given
    final var tree = new BinarySearchTree<Integer>();
    tree.add(5);
    tree.add(4);
    tree.add(7);
    tree.add(6);

    // when
    final var elementHasBeenRemoved = tree.remove(5);

    // then
    Assert.assertTrue(elementHasBeenRemoved);
    Assert.assertFalse(tree.contains(5));
    Assert.assertTrue(tree.contains(7));
    Assert.assertTrue(tree.contains(6));
    Assert.assertTrue(tree.contains(4));
    Assert.assertEquals(6, (int) tree.rootNode.value);
    Assert.assertEquals(4, (int) tree.rootNode.left.value);
    Assert.assertEquals(7, (int) tree.rootNode.right.value);
    Assert.assertNull(tree.rootNode.left.left);
    Assert.assertNull(tree.rootNode.left.right);
    Assert.assertNull(tree.rootNode.right.left);
    Assert.assertNull(tree.rootNode.right.right);
  }

}