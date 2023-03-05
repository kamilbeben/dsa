package pl.beben.datastructure;

import org.junit.Assert;
import org.junit.Test;

public class HashTableTest {

  private static final String DUMMY_VALUE = "a";

  @Test
  public void emptyThenRemoveHasFailed() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    // then
    Assert.assertFalse(hashTable.remove(1));
  }

  @Test
  public void emptyThenNotContainsKey() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    // then
    Assert.assertFalse(hashTable.containsKey(1));
  }

  @Test
  public void addThenRemove() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    // when
    hashTable.add(1, DUMMY_VALUE);
    // then
    Assert.assertTrue(hashTable.remove(1));
    Assert.assertEquals(0, hashTable.size());
  }

  @Test
  public void addThenContainsKey() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    // when
    hashTable.add(1, DUMMY_VALUE);
    // then
    Assert.assertTrue(hashTable.containsKey(1));
  }

  @Test
  public void addThenCheckSize() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    // then
    Assert.assertEquals(0, hashTable.size());
    // when
    hashTable.add(1, DUMMY_VALUE);
    hashTable.add(2, DUMMY_VALUE);
    hashTable.add(3, DUMMY_VALUE);
    // then
    Assert.assertEquals(3, hashTable.size());
  }

  @Test
  public void addAndRemoveThenCheckSize() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    hashTable.add(1, "a'");
    hashTable.remove(1);
    // then
    Assert.assertEquals(0, hashTable.size());
  }

  @Test
  public void addAndRemoveThenNotContainsKey() {
    // given
    final var hashTable = new HashTable<Integer, String>();
    hashTable.add(1, "a'");
    hashTable.remove(1);
    // then
    Assert.assertFalse(hashTable.containsKey(1));
  }

  @Test
  public void overflowCapacityThenContainsKey() {
    // given
    final var hashTable = new HashTable<Integer, String>(2);
    hashTable.add(1, DUMMY_VALUE);
    hashTable.add(2, DUMMY_VALUE);
    // when
    hashTable.add(3, DUMMY_VALUE);
    // then
    Assert.assertEquals(3, hashTable.size());
    Assert.assertTrue(hashTable.containsKey(1));
    Assert.assertTrue(hashTable.containsKey(2));
    Assert.assertTrue(hashTable.containsKey(3));
  }

  @Test
  public void overflowCapacityThenTableSizeHasBeenDoubled() {
    // given
    final var hashTable = new HashTable<Integer, String>(2);
    // then
    Assert.assertEquals(2, hashTable.buckets.length);
    // when
    hashTable.add(1, DUMMY_VALUE);
    hashTable.add(2, DUMMY_VALUE);
    hashTable.add(3, DUMMY_VALUE);
    // then
    Assert.assertEquals(4, hashTable.buckets.length);
  }

  @Test
  public void addMultipleIntoSingleBucketThenContainsKey() {
    // given
    final var hashTable = new HashTable<Integer, String>(10);
    final var firstInt = 1;
    final var secondInt = 11;
    final var thirdInt = 21;
    final var bucketIndex = hashTable.calculateBucketIndex(firstInt);
    // then
    Assert.assertEquals(
      "Broken test case, chosen integers must get in the same bucket",
      bucketIndex, hashTable.calculateBucketIndex(secondInt)
    );
    Assert.assertEquals(
      "Broken test case, chosen integers must get in the same bucket",
      bucketIndex, hashTable.calculateBucketIndex(thirdInt)
    );
    // given
    hashTable.add(firstInt, DUMMY_VALUE);
    hashTable.add(secondInt, DUMMY_VALUE);
    hashTable.add(thirdInt, DUMMY_VALUE);
    // then
    Assert.assertTrue(hashTable.containsKey(firstInt));
    Assert.assertTrue(hashTable.containsKey(secondInt));
    Assert.assertEquals(3, hashTable.buckets[bucketIndex].size());
  }

  @Test
  public void addMultipleIntoSingleBucketThenRemoveLastNode() {
    // given
    final var hashTable = new HashTable<Integer, String>(10);
    final var firstInt = 1;
    final var secondInt = 11;
    final var bucketIndex = hashTable.calculateBucketIndex(firstInt);
    // then
    Assert.assertEquals(
      "Broken test case, chosen integers must get in the same bucket",
      bucketIndex, hashTable.calculateBucketIndex(secondInt)
    );
    // given
    hashTable.add(firstInt, DUMMY_VALUE);
    hashTable.add(secondInt, DUMMY_VALUE);
    // when
    hashTable.remove(secondInt);
    // then
    Assert.assertTrue(hashTable.containsKey(firstInt));
    Assert.assertFalse(hashTable.containsKey(secondInt));
    Assert.assertEquals(1, hashTable.buckets[bucketIndex].size());
  }

  @Test
  public void addMultipleIntoSingleBucketThenRemoveFirstNode() {
    // given
    final var hashTable = new HashTable<Integer, String>(10);
    final var firstInt = 1;
    final var secondInt = 11;
    final var bucketIndex = hashTable.calculateBucketIndex(firstInt);
    // then
    Assert.assertEquals(
      "Broken test case, chosen integers must get in the same bucket",
      bucketIndex, hashTable.calculateBucketIndex(secondInt)
    );
    // given
    hashTable.add(firstInt, DUMMY_VALUE);
    hashTable.add(secondInt, DUMMY_VALUE);
    // when
    hashTable.remove(firstInt);
    // then
    Assert.assertFalse(hashTable.containsKey(firstInt));
    Assert.assertTrue(hashTable.containsKey(secondInt));
    Assert.assertEquals(1, hashTable.buckets[bucketIndex].size());
  }

}