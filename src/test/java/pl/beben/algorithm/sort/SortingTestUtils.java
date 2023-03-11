package pl.beben.algorithm.sort;

import org.junit.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SortingTestUtils {
  static void testInPlaceSortingAlgorithm(Consumer<List<? extends Comparable>> sortingFunction, int size) {
    // given
    final var expectedOutput = createSortedList(size);

    final var input = new ArrayList<>(expectedOutput);
    Collections.shuffle(input);

    // when
    sortingFunction.accept(input);

    // then
    Assert.assertEquals(expectedOutput, input);
  }

  private static List<? extends Comparable> createSortedList(int size) {
    return IntStream.range(0, size).mapToObj(Integer::new).collect(Collectors.toList());
  }

}
