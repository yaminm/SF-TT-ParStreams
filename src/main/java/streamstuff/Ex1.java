package streamstuff;

import java.util.stream.IntStream;

public class Ex1 {
  public static void main(String[] args) {
    long[] count2 = {0};
    long count = IntStream.range(0, 10)
        .peek(x -> count2[0]++)
        .map(x -> {
          count2[0]++;
          return 2 * x;
        })
        .filter(x -> true)
        .count();
    System.out.println("sum is " + count);
    System.out.println("sum is " + count2[0]);

    long [] count3 = {0L};
    IntStream.range(0, 1_000_000_000)
        .parallel()
        .peek(x -> count3[0]++)
        .filter(x -> true)
        .sum();
    System.out.println("count3[0] is " + count3[0]);

    System.out.println("---------------");
    int res = IntStream.rangeClosed(1, 10)
        .reduce(0, (a, b) -> a + b);
    System.out.println("sum is " + res);
  }
}
