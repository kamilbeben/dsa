package pl.beben.utils;

import org.junit.Assert;
import org.junit.Test;
import static pl.beben.utils.ComparableUtils.isEqualTo;
import static pl.beben.utils.ComparableUtils.isGreaterThan;
import static pl.beben.utils.ComparableUtils.isGreaterThanOrEqualTo;
import static pl.beben.utils.ComparableUtils.isLesserThan;
import static pl.beben.utils.ComparableUtils.isLesserThanOrEqualTo;

public class ComparableUtilsTest {

  @Test
  public void testIsGreaterThan() {
    Assert.assertTrue(isGreaterThan(10, 1));
  }

  @Test
  public void testIsGreaterThanOrEqualTo() {
    Assert.assertTrue(isGreaterThanOrEqualTo(10, 1));
    Assert.assertTrue(isGreaterThanOrEqualTo(10, 10));
    Assert.assertFalse(isGreaterThanOrEqualTo(1, 10));
  }

  @Test
  public void testIsLesserThan() {
    Assert.assertTrue(isLesserThan(1, 10));
    Assert.assertFalse(isLesserThan(10, 1));
  }

  @Test
  public void testIsLesserThanOrEqualTo() {
    Assert.assertTrue(isLesserThanOrEqualTo(1, 10));
    Assert.assertTrue(isLesserThanOrEqualTo(10, 10));
    Assert.assertFalse(isLesserThanOrEqualTo(10, 1));
  }

  @Test
  public void testIsEqualTo() {
    Assert.assertTrue(isEqualTo(1, 1));
    Assert.assertFalse(isEqualTo(2, 1));
  }

}