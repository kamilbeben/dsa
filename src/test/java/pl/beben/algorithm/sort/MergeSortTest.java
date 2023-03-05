package pl.beben.algorithm.sort;

import org.junit.Test;

public class MergeSortTest {

  @Test
  public void test() {
    SortingTestUtils.testInPlaceSortingAlgorithm(MergeSort::sort, 10);
    SortingTestUtils.testInPlaceSortingAlgorithm(MergeSort::sort, 100);
    SortingTestUtils.testInPlaceSortingAlgorithm(MergeSort::sort, 1_000);
    SortingTestUtils.testInPlaceSortingAlgorithm(MergeSort::sort, 10_000);
    SortingTestUtils.testInPlaceSortingAlgorithm(MergeSort::sort, 100_000);
    SortingTestUtils.testInPlaceSortingAlgorithm(MergeSort::sort, 1_000_000);
  }

}