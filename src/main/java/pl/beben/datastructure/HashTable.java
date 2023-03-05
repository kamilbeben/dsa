package pl.beben.datastructure;

import java.util.Objects;
import java.util.function.Consumer;

public class HashTable<KEY, VALUE> {

  private static final int DEFAULT_INITIAL_CAPACITY = 8;

  Node<KEY, VALUE>[] buckets;

  public HashTable() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  public HashTable(int initialCapacity) {
    buckets = new Node[initialCapacity];
  }

  public void add(KEY key, VALUE value) {
    rehashIfNecessary();

    final var bucketIndex = calculateBucketIndex(key);
    final var presentBucket = buckets[bucketIndex];

    if (presentBucket == null)
      buckets[bucketIndex] = new Node<>(key, value);
    else
      presentBucket.add(key, value);
  }

  /**
   * @return whether the element has been successfully removed
   */
  public boolean remove(KEY key) {
    final var bucketIndex = calculateBucketIndex(key);
    final var bucket = buckets[bucketIndex];

    return bucket != null && bucket.remove(
      replacement -> buckets[bucketIndex] = replacement,
      key
    );
  }

  public boolean containsKey(KEY key) {
    final var bucketIndex = calculateBucketIndex(key);
    final var bucket = buckets[bucketIndex];

    return bucket != null && bucket.contains(key);
  }

  public int size() {
    var size = 0;
    for (var i = 0; i < buckets.length; i++) {
      if (buckets[i] != null) {
        size += buckets[i].size();
      }
    }
    return size;
  }

  int calculateBucketIndex(KEY key) {
    return calculateBucketIndex(key, buckets.length);
  }

  int calculateBucketIndex(KEY key, int bucketsLength) {
    return key.hashCode() % bucketsLength;
  }

  private void rehashIfNecessary() {
    if (size() <= buckets.length - 1)
      return;

    final var rehashedBuckets = new Node[buckets.length * 2];
    for (Node<KEY, VALUE> bucket : buckets) {
      final var rehashedIndex = calculateBucketIndex(bucket.key, rehashedBuckets.length);
      rehashedBuckets[rehashedIndex] = bucket;
    }

    buckets = rehashedBuckets;
  }

  static class Node<NODE_KEY, NODE_VALUE> {
    NODE_KEY key;
    NODE_VALUE value;
    Node<NODE_KEY, NODE_VALUE> next;

    public Node(NODE_KEY key, NODE_VALUE value) {
      this.key = key;
      this.value = value;
    }

    public void add(NODE_KEY key, NODE_VALUE value) {
      if (next == null)
        next = new Node<>(key, value);
      else
        next.add(key, value);
    }

    public boolean remove(Consumer<Node<NODE_KEY, NODE_VALUE>> replaceSelf, NODE_KEY key) {

      if (Objects.equals(this.key, key)) {
        replaceSelf.accept(next);
        return true;
      }

      return next != null && next.remove(
        replacement -> next = replacement,
        key
      );
    }

    public boolean contains(NODE_KEY key) {
      return
        Objects.equals(this.key, key) ||
        (
          next != null &&
          next.contains(key)
        );
    }

    public int size() {
      var size = 0;
      var node = this;
      while (node != null) {
        node = node.next;
        size++;
      }
      return size;
    }
  }
}
