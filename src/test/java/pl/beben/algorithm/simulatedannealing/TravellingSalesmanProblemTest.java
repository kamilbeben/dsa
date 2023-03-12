package pl.beben.algorithm.simulatedannealing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class TravellingSalesmanProblemTest {

  static final String KUTNO = "Kutno";
  static final String WARSZAWA = "Warszawa";
  static final String POZNAN = "Poznan";
  static final String KRAKOW = "Krakow";

  @Test(timeout = 10_000)
  public void solve() {

    // given the following adjacency matrix of cities
    //            Kutno   Warszawa  Poznan  Krakow
    //  Kutno     0       10        90      10
    //  Warszawa  10      0         10      90
    //  Poznan    90      10        0       10
    //  Krakow    10      90        10      0
    //
    // I've made up the distances between these cities to make this case more obvious
    //
    // originCity: Kutno
    // Acceptable solution cost: 40
    // Acceptable solutions:
    //  - Kutno -> Warszawa -> Poznan -> Krakow   -> Kutno
    //  - Kutno -> Krakow   -> Poznan -> Warszawa -> Kutno
    // Any other combination would result in having to pick a connection with a cost of 90

    final var kutno = new TravellingSalesmanProblem.City(
      KUTNO,
      ImmutableMap.of(
        KUTNO, 0,
        WARSZAWA, 10,
        POZNAN, 90,
        KRAKOW, 10
      ));

    final var warszawa = new TravellingSalesmanProblem.City(
      WARSZAWA,
      ImmutableMap.of(
        KUTNO, 10,
        WARSZAWA, 0,
        POZNAN, 10,
        KRAKOW, 90
      ));

    final var poznan = new TravellingSalesmanProblem.City(
      POZNAN,
      ImmutableMap.of(
        KUTNO, 90,
        WARSZAWA, 10,
        POZNAN, 0,
        KRAKOW, 10
      ));

    final var krakow = new TravellingSalesmanProblem.City(
      KRAKOW,
      ImmutableMap.of(
        KUTNO, 10,
        WARSZAWA, 90,
        POZNAN, 10,
        KRAKOW, 0
      ));

    final var expectedSolutionCost = 40;

    final var acceptableSolutions = ImmutableSet.of(
      ImmutableList.of(kutno, warszawa, poznan, krakow, kutno),
      ImmutableList.of(kutno, krakow, poznan, warszawa, kutno)
    );

    final var cities = ImmutableSet.of(kutno, warszawa, poznan, krakow);

    // when
    final var solutionAndCost = TravellingSalesmanProblem.solve(cities, kutno);

    // then
    Assert.assertNotNull(solutionAndCost);
    Assert.assertEquals(expectedSolutionCost, (int) solutionAndCost.cost());
    Assert.assertTrue(acceptableSolutions.contains(solutionAndCost.solution()));
  }

  @Test(timeout = 10_000)
  public void solveReturningWithoutUsingSimulatedAnnealingAlgorithm() {

    // given any two cities
    final var kutno = new TravellingSalesmanProblem.City(
      KUTNO,
      ImmutableMap.of(
        KUTNO, 0,
        WARSZAWA, 10
      ));

    final var warszawa = new TravellingSalesmanProblem.City(
      WARSZAWA,
      ImmutableMap.of(
        KUTNO, 10,
        WARSZAWA, 0
      ));

    final var expectedSolution = ImmutableList.of(kutno, warszawa, kutno);
    final var expectedSolutionCost = 20;
    final var cities = ImmutableSet.of(kutno, warszawa);
    final var originCity = kutno;

    final var simulatedAnnealingAlgorithmHasFoundAtLeastOneSolution = new AtomicBoolean();

    // when
    final var solutionAndCost = TravellingSalesmanProblem.solve(cities, originCity);

    // then
    Assert.assertNotNull(solutionAndCost);
    Assert.assertEquals(expectedSolutionCost, (int) solutionAndCost.cost());
    Assert.assertEquals(expectedSolution, solutionAndCost.solution());
    Assert.assertFalse(simulatedAnnealingAlgorithmHasFoundAtLeastOneSolution.get());
  }

}