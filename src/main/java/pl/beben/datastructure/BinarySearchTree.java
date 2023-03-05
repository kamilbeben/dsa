package pl.beben.datastructure;

import lombok.NonNull;
import pl.beben.utils.ComparableUtils;
import java.util.function.Consumer;

/**
 * Basic (not self-balancing) binary search tree implementation
 * @implNote equality is checked using {@link Comparable#compareTo(Object)} method
 */
public class BinarySearchTree<VALUE extends Comparable> {

  Node<VALUE> rootNode;

  public void add(@NonNull VALUE value) {
    if (rootNode == null)
      rootNode = new Node<>(value);
    else
      rootNode.add(value);
  }

  public boolean contains(VALUE value) {
    return
      value != null &&
      rootNode != null &&
      rootNode.contains(value);
  }

  public boolean remove(VALUE value) {
    return
      value != null &&
      rootNode != null &&
      rootNode.remove(
        replacement -> rootNode = replacement,
        value
      );
  }

  public void forEach(Consumer<VALUE> consumer) {
    if (rootNode != null)
      rootNode.forEach(consumer);
  }

  static class Node<NODE_VALUE extends Comparable> {
    Node<NODE_VALUE> left;
    Node<NODE_VALUE> right;
    NODE_VALUE value;

    Node(NODE_VALUE value) {
      this.value = value;
    }

    void add(NODE_VALUE value) {
      if (isGreaterThan(value)) {
        if (left == null)
          left = new Node(value);
        else
          left.add(value);
      } else {
        if (right == null)
          right = new Node(value);
        else
          right.add(value);
      }
    }

    boolean contains(NODE_VALUE value) {
      if (isEqualTo(value))
        return true;
      else if (isGreaterThan(value))
        return left != null && left.contains(value);
      else
        return right != null && right.contains(value);
    }

    boolean remove(Consumer<Node<NODE_VALUE>> replaceSelf, NODE_VALUE value) {
      if (isEqualTo(value)) {
        removeSelf(replaceSelf);
        return true;
      } else if (isGreaterThan(value)) {
        return left != null && left.remove(
          replacement -> left = replacement,
          value
        );
      } else {
        return right != null && right.remove(
          replacement -> right = replacement,
          value
        );
      }
    }

    private void removeSelf(Consumer<Node<NODE_VALUE>> replaceSelf) {
      if (left == null && right == null) {
        replaceSelf.accept(null);
      } else if (left == null) {
        replaceSelf.accept(right);
      } else if (right == null) {
        replaceSelf.accept(left);
      } else {
        var parentOfMinNodeGreaterThanThis = this;
        var minNodeGreaterThanThis = right;

        while (minNodeGreaterThanThis.left != null) {
          parentOfMinNodeGreaterThanThis = minNodeGreaterThanThis;
          minNodeGreaterThanThis = minNodeGreaterThanThis.left;
        }

        this.value = minNodeGreaterThanThis.value;
        parentOfMinNodeGreaterThanThis.left = null;
      }
    }

    void forEach(Consumer<NODE_VALUE> consumer) {
      if (left != null)
        left.forEach(consumer);

      consumer.accept(this.value);

      if (right != null)
        right.forEach(consumer);
    }

    private boolean isGreaterThan(NODE_VALUE value) {
      return ComparableUtils.isGreaterThan(this.value, value);
    }

    private boolean isEqualTo(NODE_VALUE value) {
      return ComparableUtils.isEqualTo(this.value, value);
    }

  }
}
