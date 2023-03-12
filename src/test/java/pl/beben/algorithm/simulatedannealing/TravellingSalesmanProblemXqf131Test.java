package pl.beben.algorithm.simulatedannealing;

import com.google.common.collect.ImmutableSet;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TravellingSalesmanProblemXqf131Test {

  @Test(timeout = 10_000l)
  public void solve() {

    final var cities = loadCities();
    final var originCity = cities.get(0);
    final var solutionAndCost = TravellingSalesmanProblem.solve(ImmutableSet.copyOf(cities), originCity);

    // then
    Assert.assertNotNull(solutionAndCost);

    // According to the https://www.math.uwaterloo.ca/tsp/vlsi/xqf131.log.html -
    // - the best solution that has been found so far has the cost of 564.
    // By playing around a little I was able to find a solution with the cost of 630 -
    // - by setting the INITIAL_TEMPERATURE to 150 and COOLING_RATE to 0.999999 which took about 3 seconds (with the logging off of course)
  }

  private List<TravellingSalesmanProblem.City> loadCities() {

    final var citiesWithCoordinates = loadCitiesWithCoordinates();

    return citiesWithCoordinates.stream()
      .map(city -> new TravellingSalesmanProblem.City(
        city.name(),
        citiesWithCoordinates.stream()
          .collect(
            Collectors.toMap(
              CityWithCoordinates::name,
              adjacentCity -> calculateEuclideanDistance(city, adjacentCity)
            )
          )
      ))
      .collect(Collectors.toList());
  }

  // https://www.math.uwaterloo.ca/tsp/vlsi/index.html
  // In these examples, the cost of travel between cities is specified by
  // the Euclidean distance rounded to the nearest whole number (the TSPLIB EUC_2D-norm)
  private int calculateEuclideanDistance(CityWithCoordinates a, CityWithCoordinates b) {
    final var distance = Math.round(
      Math.hypot(
        Math.abs(b.y - a.y),
        Math.abs(b.x - a.x)
      )
    );

    if (distance > Integer.MAX_VALUE)
      throw new UnsupportedOperationException("Distance is higher than integer's max value, the cast in the following line would cause overflow");

    return (int) distance;
  }

  @SneakyThrows
  private List<CityWithCoordinates> loadCitiesWithCoordinates() {
    final var resource = TravellingSalesmanProblemXqf131Test.class.getResource("/dataset/travelling_salesman_xqf131.tsp");
    final var asString = IOUtils.toString(resource, "UTF-8");
    final var lines = asString.split("\n");

    final var cityLinePattern = Pattern.compile("^(?<name>[0-9]+) (?<xCoordinate>[0-9]+) (?<yCoordinate>[0-9]+)");

    return Arrays.stream(lines)
      .map(line -> {
        final var cityLineMatcher = cityLinePattern.matcher(line);
        if (!cityLineMatcher.matches())
          return null;

        final var name = cityLineMatcher.group("name");
        final var xCoordinate = Integer.parseInt(cityLineMatcher.group("xCoordinate"));
        final var yCoordinate = Integer.parseInt(cityLineMatcher.group("yCoordinate"));

        return new CityWithCoordinates(name, xCoordinate, yCoordinate);
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  record CityWithCoordinates(String name, int x, int y) {}

}
