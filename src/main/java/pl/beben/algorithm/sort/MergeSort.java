package pl.beben.algorithm.sort;

import java.util.ArrayList;
import java.util.List;
import static pl.beben.utils.ComparableUtils.isLesserThanOrEqualTo;

public class MergeSort {

  public static void sort(List<? extends Comparable> list) {
    sort(list, 0, list.size() - 1);
  }


  private static void sort(List<? extends Comparable> list,
                           int minIndex,
                           int maxIndex) {

    // General idea is to recursively ([1]) divide the list into subLists until all that's remaining
    // are one-element subLists which are by definition considered sorted - that's our base case
    final var subListSize = maxIndex + 1 - minIndex;
    if (subListSize > 1) {
      // The subLists are supposed to be half the size each iteration
      final var middleIndex = minIndex + subListSize / 2;
      // [1]
      sort(list, minIndex, middleIndex - 1);
      sort(list, middleIndex, maxIndex);
      // Both subLists are sorted on their own, below method call will sort them and merge back to the `list`
      sortAndMergeSubLists(list, minIndex, middleIndex, maxIndex);
    }
  }

  /**
   * Both subLists are sorted and merged back into the list
   * @param list that was passed to the root {@link #sort(java.util.List)} function
   * @param minIndex left subList's starting point
   * @param middleIndex right subList's starting point (which means that it's also [left subList's ending point - 1])
   * @param maxIndex right subList's ending point
   */
  private static <ANY extends Comparable> void sortAndMergeSubLists(List<ANY> list,
                                                                    int minIndex,
                                                                    int middleIndex,
                                                                    int maxIndex) {

    final var merged = new ArrayList<ANY>(maxIndex - minIndex);
    var leftIndex = minIndex;
    var rightIndex = middleIndex;

    while (leftIndex < middleIndex && rightIndex <= maxIndex) {
      if (isLesserThanOrEqualTo(list.get(leftIndex), list.get(rightIndex)))
        merged.add(list.get(leftIndex++));
      else
        merged.add(list.get(rightIndex++));
    }

    while (leftIndex < middleIndex)
      merged.add(list.get(leftIndex++));

    while (rightIndex <= maxIndex)
      merged.add(list.get(rightIndex++));

    for (var i = 0; i < merged.size(); i++)
      list.set(minIndex + i, merged.get(i));
  }
}
