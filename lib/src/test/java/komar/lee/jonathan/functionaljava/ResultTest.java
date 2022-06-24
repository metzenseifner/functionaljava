package komar.lee.jonathan.functionaljava;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;

public class ResultTest {

  /** Tests of internal state */
  public static int getDefault() {
    throw new IllegalStateException();
  }

  @Test
  public void testGetOrElseSuccess() {
    Result<Integer> result = Result.success(2);
    assertThat(Integer.valueOf(2), equalTo(result.getOrElse(ResultTest::getDefault)));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOrElseFailure() {
    Result<Integer> result = Result.failure("error");
    result.getOrElse(ResultTest::getDefault);
  }

  @Test
  public void testOrElseSuccess() {
    Result<Integer> result = Result.success(2);
    assertEquals("Success(4)", result.map(x -> x * 2).orElse(() -> {throw new RuntimeException();}).toString());
  }

  @Test(expected=RuntimeException.class)
  public void testOrElseFailure() {
    Result<Integer> result = Result.failure("error");
    result.map(x -> x * 2).orElse(() -> {throw new RuntimeException();});
  }

  @Test
  public void testMapSuccess() {
    Result<Integer> result = Result.success(2);
    assertEquals("Success(4)", result.map(x -> x * 2).toString());
  }

  @Test
  public void testMapFailure() {
    Result<Integer> result = Result.failure("error");
    assertEquals("Failure(error)", result.map(x -> x * 2).toString());
  }

  @Test
  public void testFlatMapSuccess() {
    Result<Integer> result = Result.success(2);
    assertEquals("Success(4)", result.flatMap(x -> Result.success(x * 2)).toString());
  }

  @Test
  public void testFlatMapFailure() {
    Result<Integer> result = Result.failure("error");
    assertEquals("Failure(error)", result.flatMap(x -> Result.success(x * 2)).toString());
  }



  /** Tests of filter */
  Result<Integer> empty = Result.empty();
  Result<Integer> failure = Result.failure("Failure message");
  Result<Integer> success = Result.success(4);
  Function<Integer, Boolean> even = x -> x % 2 == 0;
  Function<Integer, Boolean> odd = x -> !even.apply(x);

  @Test
  public void testFilterFunctionOfVBooleanEmpty() {
    assertEquals("Empty()", empty.filter(even).toString());
    assertEquals("Empty()", empty.filter(odd).toString());
  }

  @Test
  public void testFilterFunctionOfVBooleanStringEmpty() {
    assertEquals("Empty()", empty.filter(even, "Condition is not matched").toString());
    assertEquals("Empty()", empty.filter(odd, "Condition is not matched").toString());
  }

  @Test
  public void testFilterFunctionOfVBooleanFailure() {
    assertEquals("Failure(Failure message)", failure.filter(even).toString());
    assertEquals("Failure(Failure message)", failure.filter(odd).toString());
  }

  @Test
  public void testFilterFunctionOfVBooleanStringFailure() {
    assertEquals("Failure(Failure message)", failure.filter(even, "Condition is not matched").toString());
    assertEquals("Failure(Failure message)", failure.filter(odd, "Condition is not matched").toString());
  }

  @Test
  public void testFilterFunctionOfVBooleanSuccess() {
    assertEquals("Success(4)", success.filter(even).toString());
    assertEquals("Failure(Condition not matched)", success.filter(odd).toString());
  }

  @Test
  public void testFilterFunctionOfVBooleanStringSuccess() {
    assertEquals("Success(4)", success.filter(even, "The number is not even").toString());
    assertEquals("Failure(The number is not even)", success.filter(odd, "The number is not even").toString());
  }

  /** Tests of */

  @Test
  public void testMapFailureStringEmpty() {
    assertEquals("Empty()", empty.mapFailure("no data").toString());
  }

  @Test
  public void testMapFailureStringFailure() {
    assertEquals("Failure(no data)", failure.mapFailure("no data").toString());
  }

  @Test
  public void testMapFailureStringSuccess() {
    assertEquals("Success(4)", success.mapFailure("no data").toString());
  }

  /** Tests of */
  @Test
  public void testOfTValue() {
    assertEquals("Success(4)", Result.of(4).toString());
  }

  @Test
  public void testOfTNull() {
    assertEquals("Empty()", Result.of((Integer) null).toString());
  }

  @Test
  public void testOfTStringValue() {
    assertEquals("Success(4)", Result.of(4, "no value").toString());
  }

  @Test
  public void testOfTStringNull() {
    assertEquals("Failure(no value)", Result.of((Integer) null, "no value").toString());
  }

  @Test
  public void testOfFunctionOfTBooleanTValueTrue() {
    assertEquals("Success(4)", Result.of((Integer x) -> x % 2 == 0, 4).toString());
  }

  @Test
  public void testOfFunctionOfTBooleanTValueFalse() {
    assertEquals("Empty()", Result.of((Integer x) -> x % 2 == 0, 5).toString());
  }

  @Test
  public void testOfFunctionOfTBooleanTException() {
    assertEquals("Failure(Exception while evaluating predicate: 4)", Result.of((Integer x) -> {throw new RuntimeException("exception");}, 4).toString());
  }

  @Test
  public void testOfFunctionOfTBooleanTStringValueTrue() {
    assertEquals("Success(4)", Result.of((Integer x) -> x % 2 == 0, 4, "odd").toString());
  }

  @Test
  public void testOfFunctionOfTBooleanTStringValueFalse() {
    assertEquals("Failure(odd)", Result.of((Integer x) -> x % 2 == 0, 5, "odd").toString());
  }

  @Test
  public void testOfFunctionOfTBooleanTStringException() {
    assertEquals("Failure(Exception while evaluating predicate: odd)", Result.of((Integer x) -> {throw new RuntimeException("exception");}, 4, "odd").toString());
  }

  /** Tests of forEach */
  public static class TestResult {
    int value;
  }

  @Test
  public void testForEachEmpty() {
    TestResult tr = new TestResult();
    empty.forEach(x -> tr.value = x);
    assertEquals(0, tr.value);
  }

  @Test
  public void testForEachFailure() {
    TestResult tr = new TestResult();
    failure.forEach(x -> tr.value = x);
    assertEquals(0, tr.value);
  }

  @Test
  public void testForEachSuccess() {
    TestResult tr = new TestResult();
    success.forEach(x -> tr.value = x);
    assertEquals(4, tr.value);
  }

  @Test
  public void testForEachOrThrowEmpty() {
    TestResult tr = new TestResult();
    empty.forEachOrThrow(x -> tr.value = x);
    assertEquals(0, tr.value);
  }

  @Test(expected = IllegalStateException.class)
  public void testForEachOrThrowFailure() {
    TestResult tr = new TestResult();
    failure.forEachOrThrow(x -> tr.value = x);
  }

  @Test
  public void testForEachOrThrowSuccess() {
    TestResult tr = new TestResult();
    success.forEachOrThrow(x -> tr.value = x);
    assertEquals(4, tr.value);
  }

  @Test
  public void testForEachOrExceptionEmpty() {
    TestResult tr = new TestResult();
    empty.forEachOrException(x -> tr.value = x).forEach(e -> tr.value = 12);
    assertEquals(0, tr.value);
  }

  @Test
  public void testForEachOrExceptionFailure() {
    TestResult tr = new TestResult();
    failure.forEachOrException(x -> tr.value = x).forEach(e -> tr.value = 12);
    assertEquals(12, tr.value);
  }

  @Test
  public void testForEachOrExceptionSuccess() {
    TestResult tr = new TestResult();
    success.forEachOrException(x -> tr.value = x).forEach(e -> tr.value = 12);
    assertEquals(4, tr.value);
  }

  /** Test of lift */
  private Function<Result<String>, Result<Integer>> parseIntResult = Result.lift(Integer::parseInt);

  @Test
  public void testLift() {
    assertEquals(Result.success(345).toString(), parseIntResult.apply(Result.success("345")).toString());
  }

  @Test
  public void testLiftEmpty() {
    assertEquals(Result.empty(), parseIntResult.apply(Result.empty()));
  }

  private static Function<Integer, Function<String, Integer>> parseWithRadix = radix -> string -> Integer.parseInt(string, radix);

  private static Function<char[], Function<Integer, Function<Integer, String>>> valueOf = data -> offset -> count -> String.valueOf(data, offset, count);

  @Test
  public void testLift2() {
    int radix = 16;
    String string = "AEF15DB";
    assertEquals(Result.success(Integer.parseInt(string, radix)), Result.lift2(parseWithRadix).apply(Result.success(radix)).apply(Result.success(string)));
  }

  @Test
  public void testLift3() {
    Result<char[]> data = Result.of("Hello, World!".toCharArray());
    Result<Integer> offset = Result.of(7);
    Result<Integer> count = Result.of(5);
    assertEquals(Result.success("World"), Result.lift3(valueOf).apply(data).apply(offset).apply(count));
  }

  @Test
  public void testMap2SuccessSuccess() {
    int radix = 16;
    String string = "AEF15DB";
    assertEquals(Result.success(Integer.parseInt(string, radix)), Result.map2(Result.success(radix), Result.success(string), parseWithRadix));
  }

  @Test(expected = IllegalStateException.class)
  public void testMap2SuccessFailure() {
    int radix = 16;
    Result.map2(Result.success(radix), Result.failure("error"), parseWithRadix).forEachOrThrow(ResultTest::failTest);
  }

  @Test(expected = IllegalStateException.class)
  public void testMap2FailureSuccess() {
    String string = "AEF15DB";
    Result.map2(Result.failure("error"), Result.success(string), parseWithRadix).forEachOrThrow(ResultTest::failTest);
  }

  @Test
  public void testMap2EmptySuccess() {
    String string = "AEF15DB";
    Result.map2(Result.empty(), Result.success(string), parseWithRadix).forEachOrThrow(ResultTest::failTest);
  }

  @Test
  public void testMap2SuccessEmpty() {
    int radix = 16;
    Result.map2(Result.success(radix), Result.empty(), parseWithRadix).forEachOrThrow(ResultTest::failTest);
  }

  private static void failTest(Object o) {
    fail();
  }

  /** Tests of fold */
  @Test
  public void testfoldLeftSuccess() {
    Result<Character> result = Result.success('a');
    assertEquals("_a", result.foldLeft("_", x -> y -> x + y));
  }

  @Test
  public void testfoldRightSuccess() {
    Result<Character> result = Result.success('a');
    assertEquals("a_", result.foldRight("_", x -> y -> x + y));
  }

  @Test
  public void testFoldLeftFailure() {
    Result<Character> result = Result.failure("error");
    assertEquals("_", result.foldLeft("_", x -> y -> x + y));
  }

  @Test
  public void testFoldRightFailure() {
    Result<Character> result = Result.failure("error");
    assertEquals("_", result.foldRight("_", x -> y -> x + y));
  }

  @Test
  public void testGetOrElseViaFoldLeftSuccess() {
    Result<Integer> result = Result.success(1);
    assertEquals(Integer.valueOf(1), result.getOrElseViaFoldLeft(0));
  }

  @Test
  public void testGetOrElseViaFoldLeftEmpty() {
    Result<Integer> result = Result.empty();
    assertEquals(Integer.valueOf(0), result.getOrElseViaFoldLeft(0));
  }

  @Test
  public void testGetOrElseViaFoldRightSuccess() {
    Result<Integer> result = Result.success(1);
    assertEquals(Integer.valueOf(1), result.getOrElseViaFoldRight(0));
  }

  @Test
  public void testGetOrElseViaFoldRightEmpty() {
    Result<Integer> result = Result.empty();
    assertEquals(Integer.valueOf(0), result.getOrElseViaFoldRight(0));
  }


}
