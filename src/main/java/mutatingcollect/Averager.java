package mutatingcollect;

import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

class Average {
  private double sum;
  private long count;

  public Average(double sum, long count) {
    this.sum = sum;
    this.count = count;
  }

  public Average() {
    sum = 0;
    count = 0;
  }

  public void merge(Average other) {
    System.out.println("doing a merge");
    this.sum += other.sum;
    this.count += other.count;
  }

  public void include(double d) {
    this.sum += d;
    this.count++;
  }

  public OptionalDouble get() {
    if (count == 0) {
      return OptionalDouble.empty();
    } else {
      return OptionalDouble.of(sum / count);
    }
  }
}

public class Averager {
  public static void main(String[] args) {
    long start = System.nanoTime();
    ThreadLocalRandom.current().doubles(3_000_000_000L, -1, +1)
//    DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(-1, +1))

//    DoubleStream.iterate(0.0,
//            x -> ThreadLocalRandom.current().nextDouble(-1, +1))

//        .limit(9_000_000_000L)
        .parallel()
        .collect(() -> new Average(),
            (a, d) -> a.include(d),
            (aFinal, aNother) -> aFinal.merge(aNother))
        .get()
        .ifPresentOrElse(m -> System.out.println("mean is " + m),
            () -> System.out.println("no elements in stream"));
    long time = System.nanoTime() - start;
    System.out.printf("Time taken %7.3f\n", time / 1_000_000_000.0);
  }
}
