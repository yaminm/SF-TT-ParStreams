package reducetoaverage;

import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

class Average {
  private double sum;
  private long count;

  public Average(double sum, long count) {
    this.sum = sum;
    this.count = count;
  }

  public Average merge(Average this, Average other) {
    return new Average(this.sum + other.sum, this.count + other.count);
  }

  public OptionalDouble get() {
    if (count > 0) {
      return OptionalDouble.of(this.sum / this.count);
    } else {
      return OptionalDouble.empty();
    }
  }
}

// -XX:+PrintCompilation

public class Averager {
  public static void main(String[] args) {
    long start = System.nanoTime();
    DoubleStream.generate(
        () -> ThreadLocalRandom.current().nextDouble(-1, +1))
        .limit(3_000_000_000L)
        .parallel()
        .mapToObj(d -> new Average(d, 1))
        .reduce(new Average(0, 0), (a, a2) -> a.merge(a2))
        .get()
        .ifPresentOrElse(m -> System.out.println("Mean is " + m),
            () -> System.out.println("no values in stream"));
    long time = System.nanoTime() - start;
    System.out.printf("Time elapsed %7.3f\n", time / 1_000_000_000.0);
  }
}
