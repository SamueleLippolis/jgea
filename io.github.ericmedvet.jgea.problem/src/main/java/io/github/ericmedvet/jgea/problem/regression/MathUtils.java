/*
 * Copyright 2020 Eric Medvet <eric.medvet@gmail.com> (as eric)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ericmedvet.jgea.problem.regression;

import io.github.ericmedvet.jgea.core.util.Sized;
import io.github.ericmedvet.jgea.problem.regression.univariate.UnivariateRegressionFitness;
import io.github.ericmedvet.jgea.problem.regression.univariate.synthetic.SyntheticUnivariateRegressionFitness;
import io.github.ericmedvet.jsdynsym.core.composed.AbstractComposed;
import io.github.ericmedvet.jsdynsym.core.numerical.UnivariateRealFunction;
import org.apache.commons.math3.stat.StatUtils;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * @author eric
 */
public class MathUtils {

  private static class ScaledUnivariateRealFunction extends AbstractComposed<UnivariateRealFunction> implements UnivariateRealFunction {
    private final double a;
    private final double b;

    public ScaledUnivariateRealFunction(
        UnivariateRealFunction inner,
        UnivariateRegressionFitness univariateRegressionFitness
    ) {
      super(inner);
      double[] targetYs = univariateRegressionFitness.getDataset().examples()
          .stream()
          .mapToDouble(e -> e.ys()[0])
          .toArray();
      double targetMean = StatUtils.mean(targetYs);
      double[] ys = univariateRegressionFitness.getDataset().examples().stream()
          .mapToDouble(e -> inner().applyAsDouble(e.xs()))
          .toArray();
      double mean = StatUtils.mean(ys);
      double nCovariance = 0d;
      double nVariance = 0d;
      for (int i = 0; i < targetYs.length; i++) {
        nCovariance = nCovariance + (targetYs[i] - targetMean) * (ys[i] - mean);
        nVariance = nVariance + (ys[i] - mean) * (ys[i] - mean);
      }
      b = (nVariance != 0d) ? nCovariance / nVariance : 0d;
      a = targetMean - mean * b;
    }

    @Override
    public double applyAsDouble(double[] value) {
      return a + b * inner().applyAsDouble(value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(a, b);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      ScaledUnivariateRealFunction that = (ScaledUnivariateRealFunction) o;
      return Double.compare(that.a, a) == 0 && Double.compare(that.b, b) == 0;
    }

    @Override
    public String toString() {
      return String.format("%.3f + %.3f * [%s]", a, b, inner());
    }

    @Override
    public int nOfInputs() {
      return inner().nOfInputs();
    }
  }

  private static class SizedUnivariateScaledRealFunction extends ScaledUnivariateRealFunction implements Sized {
    private final int size;

    public SizedUnivariateScaledRealFunction(
        UnivariateRealFunction innerF,
        SyntheticUnivariateRegressionFitness syntheticSymbolicRegressionFitness
    ) {
      super(innerF, syntheticSymbolicRegressionFitness);
      if (innerF instanceof Sized) {
        size = ((Sized) innerF).size();
      } else {
        size = 0;
      }
    }

    @Override
    public int size() {
      return size;
    }
  }

  public static List<double[]> cartesian(double[]... xs) {
    int l = Arrays.stream(xs).mapToInt(x -> x.length).reduce(1, (v1, v2) -> v1 * v2);
    List<double[]> list = new ArrayList<>(l);
    for (int i = 0; i < l; i++) {
      double[] x = new double[xs.length];
      int c = i;
      for (int j = 0; j < x.length; j++) {
        x[j] = xs[j][c % xs[j].length];
        c = Math.floorDiv(c, xs[j].length);
      }
      list.add(x);
    }
    return list;
  }

  public static double[] equispacedValues(double min, double max, double step) {
    List<Double> values = new ArrayList<>();
    double value = min;
    while (value <= max) {
      values.add(value);
      value = value + step;
    }
    return values.stream().mapToDouble(Double::doubleValue).toArray();
  }

  public static UnaryOperator<UnivariateRealFunction> linearScaler(SyntheticUnivariateRegressionFitness syntheticSymbolicRegressionFitness) {
    return f -> (f instanceof Sized) ? new SizedUnivariateScaledRealFunction(
        f,
        syntheticSymbolicRegressionFitness
    ) : new ScaledUnivariateRealFunction(f, syntheticSymbolicRegressionFitness);
  }

  public static List<double[]> pairwise(double[]... xs) {
    int l = xs[0].length;
    for (int i = 1; i < xs.length; i++) {
      if (xs[i].length != l) {
        throw new IllegalArgumentException(String.format(
            "Invalid input arrays: %d-th length (%d) is different than " + "1st length (%d)",
            i + 1,
            xs[i].length,
            l
        ));
      }
    }
    List<double[]> list = new ArrayList<>(l);
    for (int i = 0; i < l; i++) {
      double[] x = new double[xs.length];
      for (int j = 0; j < x.length; j++) {
        x[j] = xs[j][i];
      }
      list.add(x);
    }
    return list;
  }

  public static double[] uniformSample(double min, double max, int count, Random random) {
    double[] values = new double[count];
    for (int i = 0; i < count; i++) {
      values[i] = random.nextDouble() * (max - min) + min;
    }
    return values;
  }

}
