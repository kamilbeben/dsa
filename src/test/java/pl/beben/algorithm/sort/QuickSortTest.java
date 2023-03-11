package pl.beben.algorithm.sort;

import org.junit.Test;

public class QuickSortTest {

  @Test
  public void test() {
    SortingTestUtils.testInPlaceSortingAlgorithm(QuickSort::sort, 10);
    SortingTestUtils.testInPlaceSortingAlgorithm(QuickSort::sort, 100);
    SortingTestUtils.testInPlaceSortingAlgorithm(QuickSort::sort, 1_000);
  }

}