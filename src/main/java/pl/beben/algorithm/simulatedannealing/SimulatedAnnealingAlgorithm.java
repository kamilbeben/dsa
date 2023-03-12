package pl.beben.algorithm.simulatedannealing;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import java.util.function.Function;
import static lombok.AccessLevel.PRIVATE;

@Log4j2
@NoArgsConstructor(access = PRIVATE)
public class SimulatedAnnealingAlgorithm {

  // There is no point to simulate anything below that point, as with low temperatures the optimization differences are almost nonexistent
  private static final double LOWER_TEMPERATURE_THRESHOLD = 0.01;

  /**
   * @param calculateNeighbouringSolution <a href="https://en.wikipedia.org/wiki/Simulated_annealing#Efficient_candidate_generation">Efficient candidate generation section</a>
   * @param calculateSolutionCost Function calculating cost of a given solution (eg, travelled distance) - the lower, the better
   * @param initialTemperature <a href="https://en.wikipedia.org/wiki/Simulated_annealing#Cooling_schedule">Cooling schedule section</a>
   * @param coolingRate <a href="https://en.wikipedia.org/wiki/Simulated_annealing#Cooling_schedule">Cooling schedule section</a>
   */

  public static <SOLUTION> SolutionAndCost<SOLUTION> solve(SOLUTION initialSolution,
                                                           Function<SOLUTION, SOLUTION> calculateNeighbouringSolution,
                                                           Function<SOLUTION, Integer> calculateSolutionCost,
                                                           double initialTemperature,
                                                           double coolingRate) {

    validateArguments(initialTemperature, coolingRate);

    log.debug("initialSolution = {}, initialTemperature = {}, coolingRate = {}", initialSolution, initialTemperature, coolingRate);

    // Description of the algorithm is more or less copied from this (wikipedia article)[https://en.wikipedia.org/wiki/Simulated_annealing]
    // It has great description and is worth checking out.

    // The notion of slow cooling implemented in the simulated annealing algorithm is interpreted as a slow decrease in
    // the probability of accepting worse solutions as the solution space is explored. (see the description of shouldAcceptNeighbouringSolution function)
    //
    // Accepting worse solutions allows for a more extensive search for the global optimal solution (avoiding the local optima).
    // In general, simulated annealing algorithms work as follows:

    // The algorithm keeps track of current solution that it's working on
    var currentSolutionAndCost = new SolutionAndCost<>(initialSolution, calculateSolutionCost.apply(initialSolution));
    // and also, the best solution that has been found so far
    var bestSolutionAndCost = new SolutionAndCost<>(initialSolution, currentSolutionAndCost.cost());

    // The temperature progressively decreases from an initial positive value to zero
    for (
      var temperature = initialTemperature;
      temperature > LOWER_TEMPERATURE_THRESHOLD;
      temperature *= coolingRate
    ) {
      // The algorithm randomly selects a solution close to the current one, measures its quality
      final var neighbouringSolution = calculateNeighbouringSolution.apply(currentSolutionAndCost.solution());
      final var neighbouringSolutionCost = calculateSolutionCost.apply(neighbouringSolution);

      // and moves to it according to the temperature-dependent probabilities of selecting better or worse solutions
      if (shouldAcceptNeighbouringSolution(temperature, currentSolutionAndCost.cost(), neighbouringSolutionCost)) {
        log.debug("Neighbouring solution {} is accepted as current solution", neighbouringSolution);
        currentSolutionAndCost = new SolutionAndCost<>(neighbouringSolution, neighbouringSolutionCost);
      }

      if (currentSolutionAndCost.cost() < bestSolutionAndCost.cost()) {
        log.debug("Neighbouring solution is also the best solution so far");
        bestSolutionAndCost = currentSolutionAndCost;
      }
    }

    log.debug("Returning {}", bestSolutionAndCost);
    return bestSolutionAndCost;
  }

  private static boolean shouldAcceptNeighbouringSolution(double temperature, int solutionCost, int neighbouringSolutionCost) {

    // always accept better solution
    if (neighbouringSolutionCost <= solutionCost)
      return true;

    // Chances of selecting a worse solution are highest when:
    // 1. the difference between solutionCost and neighbouringSolutionCost is low
    // 2. the temperature is high

    // Some examples just to visualise how likely it is to actually accept a worse solution given the temperature and difference.
    // Think of the last column as "how likely is this going to happen if 0.0 is 0% and 1.0 is 100%?"
    //  +-------------+---------------+---------------------------+-----------------------+
    //  | temperature | solutionCost  | neighbouringSolutionCost  | acceptanceProbability |
    //  +-------------+---------------+---------------------------+-----------------------+
    //  | 10          | 100           | 101                       | 0.9                   |
    //  | 10          | 100           | 110                       | 0.36                  |
    //  | 5           | 100           | 110                       | 0.13                  |
    //  | 5           | 100           | 102                       | 0.67                  |
    //  | 0.1         | 100           | 101                       | 0.00004               |
    //  | 0.1         | 100           | 102                       | 0.0000000002          |
    //  +-------------+---------------+---------------------------+-----------------------+

    final var acceptanceProbability = Math.exp((solutionCost - neighbouringSolutionCost) / temperature);
    return acceptanceProbability > Math.random();
  }

  private static void validateArguments(double initialTemperature, double coolingRate) {
    if (initialTemperature <= 0)
      throw new IllegalArgumentException("Starting temperature must be positive");

    if (coolingRate <= 0 || coolingRate >= 1)
      throw new IllegalArgumentException("Cooling rate must be between 0 and 1 exclusive");
  }

}
