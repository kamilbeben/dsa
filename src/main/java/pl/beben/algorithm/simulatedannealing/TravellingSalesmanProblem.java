package pl.beben.algorithm.simulatedannealing;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import static lombok.AccessLevel.PRIVATE;

public class TravellingSalesmanProblem {
  private static final Random RANDOM = new Random();

  // These parameters definitely could be optimized further, I've chosen them by playing around with this problem for some time
  private static final int INITIAL_TEMPERATURE = 150;
  private static final double COOLING_RATE = 0.999995;

  /**
   * Quoted from <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">wikipedia article</a>
   * <blockquote>
   * The travelling salesman problem (also called the travelling salesperson problem or TSP) asks the following question:
   * "Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits
   * each city exactly once and returns to the origin city?"
   * </blockquote>
   */
  public static SolutionAndCost<List<City>> solve(Set<City> cities, City originCity) {

    validateInput(cities, originCity);

    final var initialSolution = calculateInitialSolution(cities, originCity);

    // First and last positions must always be equal to the originCity - which means that if the solution size is equal to 3 -
    // - there is no need to pursue any further because this is already the only possible solution to this problem.
    if (initialSolution.size() == 3)
      return new SolutionAndCost<>(initialSolution, calculateSolutionCost(initialSolution));

    return SimulatedAnnealingAlgorithm.solve(
      initialSolution,
      TravellingSalesmanProblem::calculateNeighbouringSolution,
      TravellingSalesmanProblem::calculateSolutionCost,
      INITIAL_TEMPERATURE,
      COOLING_RATE
    );
  }

  private static List<City> calculateInitialSolution(Set<City> cities, City originCity) {
    return ImmutableList.<City>builder()
      .add(originCity)
      .addAll(
        cities.stream()
          .filter(otherCity -> !otherCity.equals(originCity))
          .collect(Collectors.toList())
      )
      .add(originCity)
      .build();
  }

  private static List<City> calculateNeighbouringSolution(List<City> solution) {
    final var neighbouringSolution = new ArrayList<>(solution);

    final var leftIndex = generateRandomIndex(neighbouringSolution);
    final var rightIndex = generateRandomIndexDifferentThan(neighbouringSolution, leftIndex);

    Collections.swap(neighbouringSolution, leftIndex, rightIndex);

    return neighbouringSolution;
  }

  private static int generateRandomIndex(List<City> solution) {
    // 1 and -1: because in this problem we cannot touch the originCity positions, which are the first and the last in the list
    return RANDOM.nextInt(1, solution.size() - 1);
  }

  private static int generateRandomIndexDifferentThan(List<City> solution, int otherIndex) {
    final var index = generateRandomIndex(solution);
    return index != otherIndex
      ? index
      : generateRandomIndexDifferentThan(solution, otherIndex);
  }

  private static Integer calculateSolutionCost(List<City> solution) {
    var distance = 0;
    for (int i = 0; i < solution.size() - 1; i++) {
      final var city = solution.get(i);
      final var nextCity = solution.get(i + 1);
      distance += city.getDistance(nextCity);
    }
    return distance;
  }

  private static void validateInput(Set<City> cities, City originCity) {

    if (originCity == null)
      throw new IllegalArgumentException("Origin city must not be null");

    if (cities == null || cities.size() < 2)
      throw new IllegalArgumentException("Cities size must be grater than 1, but it is equal to " + cities.size());

    for (final var city : cities) {
      for (final var otherCity : cities) {

        if (city.equals(otherCity))
          continue;

        final var distance = city.getDistance(otherCity);

        if (distance == null)
          throw new IllegalArgumentException("All cities must have a direct connection to every other city (from = " + city + ", to = " + otherCity + ")");

        if (distance < 0)
          throw new IllegalArgumentException("Distance between two cities cannot be negative (from = " + city + ", to = " + otherCity + ")");
      }
    }
  }

  @RequiredArgsConstructor
  @EqualsAndHashCode(of = "name")
  @FieldDefaults(level = PRIVATE, makeFinal = true)
  public static class City {
    @Getter
    String name;

    Map<String, Integer> otherCityNameToDistance;

    public Integer getDistance(City otherCity) {
      return otherCityNameToDistance.get(otherCity.name);
    }

    @Override
    public String toString() {
      return name;
    }
  }

}
