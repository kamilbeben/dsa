package pl.beben.utils;

import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ComparableUtils {

  public static boolean isGreaterThan(Comparable a, Comparable b) {
    return a.compareTo(b) > 0;
  }

  public static boolean isGreaterThanOrEqualTo(Comparable a, Comparable b) {
    return a.compareTo(b) >= 0;
  }

  public static boolean isLesserThan(Comparable a, Comparable b) {
    return a.compareTo(b) < 0;
  }

  public static boolean isLesserThanOrEqualTo(Comparable a, Comparable b) {
    return a.compareTo(b) <= 0;
  }

  public static boolean isEqualTo(Comparable a, Comparable b) {
    return a.compareTo(b) == 0;
  }

}
