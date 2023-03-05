package pl.beben.algorithm.sort;

import lombok.NoArgsConstructor;
import java.util.List;
import static lombok.AccessLevel.PRIVATE;
import static pl.beben.utils.ComparableUtils.isGreaterThan;
import static pl.beben.utils.ComparableUtils.isLesserThan;

@NoArgsConstructor(access = PRIVATE)
public class QuickSort {

  public static void sort(List<? extends Comparable> list) {
    sort(list, 0, list.size() - 1);
  }

  private static void sort(List<? extends Comparable> list, int minIndex, int maxIndex) {

    // General idea is to recursively ([1]) divide the list into partitions ([2]) until all that's remaining
    // are one-element partitions which are by definition considered sorted - that's our base case
    final var partitionSize = maxIndex + 1 - minIndex;
    if (partitionSize > 1) {
      // [2]
      final var pivotIndex = partitionReturningPivotIndex(list, minIndex, maxIndex);
      // [1]
      if (minIndex < pivotIndex - 1)
        sort(list, minIndex, pivotIndex - 1);
      // [1]
      if (pivotIndex < maxIndex)
        sort(list, pivotIndex, maxIndex);
    }
  }

  /**
   * Partition's pivot will be chosen (middle element),<br/>
   * then all elements that are smaller than it will be moved to the left of it,<br/>
   * then all elements that are greater than it will be moved to the right of it.<br/>
   * <br/>
   * Example: <br/>
   * <pre>{@code
   * list:       [ 1, 8, 4, 3, 5, 7, 2, x, y... ] // what's after 6th index doesnt matter because that's the end of this partition
   * minIndex:     0
   * maxIndex:                       6
   * partition:  [ 1, 8, 4, 3, 5, 7, 2 ]
   * pivot:                 3
   * list after: [ 1, 2, 4, 3, 5, 7, 8, x, y... ]
   * pivotIndex:            3
   * }</pre>
   * List of course is still not sorted after single partitioning, but it will be after all of the iterations.
   * @param list that was passed to the root {@link #sort(java.util.List)} function
   * @param minIndex partition starting point
   * @param maxIndex partition ending point
   * @return partition's pivot index
   */
  private static int partitionReturningPivotIndex(List<? extends Comparable> list, int minIndex, int maxIndex) {
    // Picking first or last element is quite common in online examples.
    // This way it's less likely to encounter worst case scenario, which is
    // when only one element gets swapped every iteration - that would lead to O(n^2) time complexity.
    // Some implementations are going even further by picking the pivot element randomly.
    final var middleElementIndex = (maxIndex + minIndex) / 2;
    final var pivot = list.get(middleElementIndex);

    var ltrIndex = minIndex; // left to right
    var rtlIndex = maxIndex; // right to left

    //  - move all elements that are smaller than the pivot to the left of it
    //  - move all elements that are greater than the pivot to the right of it
    do {
      // Going from left to right - find first element that is greater than the pivot
      while (isLesserThan(list.get(ltrIndex), pivot))
        ltrIndex++;

      // Going from right to left - find first element that is lesser than the pivot
      while (isGreaterThan(list.get(rtlIndex), pivot))
        rtlIndex--;

      // And unless you've already checked all the elements, swap their places
      if (ltrIndex <= rtlIndex)
        swap(list, ltrIndex++, rtlIndex--);

      // Repeat until you've checked all the elements
    } while (ltrIndex <= rtlIndex);

    return ltrIndex;
  }

  private static <ANY extends Comparable> void swap(List<ANY> list, int leftIndex, int rightIndex) {
    final var leftValue = list.get(leftIndex);
    final var rightValue = list.get(rightIndex);

    list.set(leftIndex, rightValue);
    list.set(rightIndex, leftValue);
  }

}
