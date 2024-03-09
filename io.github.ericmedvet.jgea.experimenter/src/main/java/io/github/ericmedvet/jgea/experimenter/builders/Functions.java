/*-
 * ========================LICENSE_START=================================
 * jgea-experimenter
 * %%
 * Copyright (C) 2018 - 2024 Eric Medvet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package io.github.ericmedvet.jgea.experimenter.builders;

import io.github.ericmedvet.jgea.core.problem.MultiTargetProblem;
import io.github.ericmedvet.jgea.core.problem.Problem;
import io.github.ericmedvet.jgea.core.representation.sequence.bit.BitString;
import io.github.ericmedvet.jgea.core.representation.sequence.integer.IntString;
import io.github.ericmedvet.jgea.core.solver.Individual;
import io.github.ericmedvet.jgea.core.solver.POCPopulationState;
import io.github.ericmedvet.jgea.core.solver.State;
import io.github.ericmedvet.jgea.core.util.*;
import io.github.ericmedvet.jgea.core.util.Misc;
import io.github.ericmedvet.jgea.experimenter.Run;
import io.github.ericmedvet.jgea.experimenter.Utils;
import io.github.ericmedvet.jnb.core.Discoverable;
import io.github.ericmedvet.jnb.core.Param;
import io.github.ericmedvet.jnb.datastructure.FormattedNamedFunction;
import io.github.ericmedvet.jnb.datastructure.NamedFunction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Discoverable(prefixTemplate = "ea.function|f")
public class Functions {

  private static final String DEFAULT_FORMAT = "%.0s";

  private Functions() {
  }

  @SuppressWarnings("unused")
  public static <X, I extends Individual<G, S, Q>, G, S, Q> NamedFunction<X, Collection<I>> all(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<I, G, S, Q, ?>> beforeF
  ) {
    Function<POCPopulationState<I, G, S, Q, ?>, Collection<I>> f =
        state -> state.pocPopulation().all();
    return NamedFunction.from(f, "all").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, I extends Individual<G, S, Q>, G, S, Q> NamedFunction<X, I> best(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<I, G, S, Q, ?>> beforeF
  ) {
    Function<POCPopulationState<I, G, S, Q, ?>, I> f =
        state -> state.pocPopulation().firsts().iterator().next();
    return NamedFunction.from(f, "best").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, Double> elapsedSecs(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, State<?, ?>> beforeF,
      @Param(value = "format", dS = "%6.1f") String format
  ) {
    Function<State<?, ?>, Double> f = s -> s.elapsedMillis() / 1000d;
    return FormattedNamedFunction.from(f, format, "elapsed.secs").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, I extends Individual<G, S, Q>, G, S, Q> NamedFunction<X, Collection<I>> firsts(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<I, G, S, Q, ?>> beforeF
  ) {
    Function<POCPopulationState<I, G, S, Q, ?>, Collection<I>> f =
        state -> state.pocPopulation().firsts();
    return NamedFunction.from(f, "firsts").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, G> FormattedNamedFunction<X, G> genotype(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Individual<G, ?, ?>> beforeF,
      @Param(value = "format", dS = DEFAULT_FORMAT) String format
  ) {
    Function<Individual<G, ?, ?>, G> f = Individual::genotype;
    return FormattedNamedFunction.from(f, format, "genotype").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, TextPlotter.Miniplot> hist(
      @Param(value = "nOfBins", dI = 8) int nOfBins,
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Collection<Number>> beforeF
  ) {
    Function<Collection<Number>, TextPlotter.Miniplot> f =
        vs -> TextPlotter.histogram(vs.stream().toList(), nOfBins);
    return FormattedNamedFunction.from(f, "%" + nOfBins + "s", "hits").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, Double> hypervolume2D(
      @Param("minReference") List<Double> minReference,
      @Param("maxReference") List<Double> maxReference,
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Collection<List<Double>>> beforeF,
      @Param(value = "format", dS = "%.2f") String format
  ) {
    Function<Collection<List<Double>>, Double> f = ps -> Misc.hypervolume2D(ps, minReference, maxReference);
    return FormattedNamedFunction.from(f, format, "hv").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, I extends Individual<G, S, Q>, G, S, Q> NamedFunction<X, Collection<I>> lasts(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<I, G, S, Q, ?>> beforeF
  ) {
    Function<POCPopulationState<I, G, S, Q, ?>, Collection<I>> f =
        state -> state.pocPopulation().lasts();
    return NamedFunction.from(f, "lasts").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, I extends Individual<G, S, Q>, G, S, Q> NamedFunction<X, Collection<I>> mids(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<I, G, S, Q, ?>> beforeF
  ) {
    Function<POCPopulationState<I, G, S, Q, ?>, Collection<I>> f =
        state -> state.pocPopulation().mids();
    return NamedFunction.from(f, "mids").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, Long> nOfBirths(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<?, ?, ?, ?, ?>> beforeF,
      @Param(value = "format", dS = "%5d") String format
  ) {
    Function<POCPopulationState<?, ?, ?, ?, ?>, Long> f = POCPopulationState::nOfBirths;
    return FormattedNamedFunction.from(f, format, "n.births").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, Long> nOfEvals(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<?, ?, ?, ?, ?>> beforeF,
      @Param(value = "format", dS = "%5d") String format
  ) {
    Function<POCPopulationState<?, ?, ?, ?, ?>, Long> f = POCPopulationState::nOfQualityEvaluations;
    return FormattedNamedFunction.from(f, format, "n.evals").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, Long> nOfIterations(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, State<?, ?>> beforeF,
      @Param(value = "format", dS = "%4d") String format
  ) {
    Function<State<?, ?>, Long> f = State::nOfIterations;
    return FormattedNamedFunction.from(f, format, "n.iterations").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, P extends MultiTargetProblem<S>, S> FormattedNamedFunction<X, Double> overallTargetDistance(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, POCPopulationState<?, ?, S, ?, P>> beforeF,
      @Param(value = "format", dS = "%.2f") String format
  ) {
    Function<POCPopulationState<?, ?, S, ?, P>, Double> f = state -> state.problem().targets().stream()
        .mapToDouble(ts -> state.pocPopulation().all().stream()
            .mapToDouble(s -> state.problem().distance().apply(s.solution(), ts))
            .min()
            .orElseThrow())
        .average()
        .orElseThrow();
    return FormattedNamedFunction.from(f, format, "overall.target.distance").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, P extends Problem<S>, S> NamedFunction<X, P> problem(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, State<P, S>> beforeF
  ) {
    Function<State<P, S>, P> f = State::problem;
    return NamedFunction.from(f, "problem").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> NamedFunction<X, Progress> progress(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, State<?, ?>> beforeF
  ) {
    Function<State<?, ?>, Progress> f = State::progress;
    return NamedFunction.from(f, "progress").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, Q> FormattedNamedFunction<X, Q> quality(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Individual<?, ?, Q>> beforeF,
      @Param(value = "format", dS = DEFAULT_FORMAT) String format
  ) {
    Function<Individual<?, ?, Q>, Q> f = Individual::quality;
    return FormattedNamedFunction.from(f, format, "quality").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> NamedFunction<X, Double> rate(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Progress> beforeF
  ) {
    Function<Progress, Double> f = Progress::rate;
    return NamedFunction.from(f, "rate").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, String> runKey(
      @Param("runKey") Map.Entry<String, String> runKey,
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Run<?, ?, ?, ?>> beforeF,
      @Param(value = "format", dS = DEFAULT_FORMAT) String format
  ) {
    Function<Run<?, ?, ?, ?>, String> f = run -> Utils.interpolate(runKey.getValue(), run);
    return FormattedNamedFunction.from(f, format, runKey.getKey()).compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X> FormattedNamedFunction<X, Integer> size(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Object> beforeF,
      @Param(value = "format", dS = "%d") String format
  ) {
    Function<Object, Integer> f = o -> {
      if (o instanceof Sized s) {
        return s.size();
      }
      if (o instanceof Collection<?> c) {
        if (Misc.first(c) instanceof Sized s) {
          return c.stream().mapToInt(i -> s.size()).sum();
        }
        return c.size();
      }
      if (o instanceof String s) {
        return s.length();
      }
      throw new IllegalArgumentException(
          "Cannot compute size of %s".formatted(o.getClass().getSimpleName()));
    };
    return FormattedNamedFunction.from(f, format, "size").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, S> FormattedNamedFunction<X, S> solution(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Individual<?, S, ?>> beforeF,
      @Param(value = "format", dS = DEFAULT_FORMAT) String format
  ) {
    Function<Individual<?, S, ?>, S> f = Individual::solution;
    return FormattedNamedFunction.from(f, format, "solution").compose(beforeF);
  }

  @SuppressWarnings("unused")
  public static <X, Z> NamedFunction<X, List<Double>> toDoubleString(
      @Param(value = "beforeF", dNPM = "f.identity()") Function<X, Z> beforeF
  ) {
    Function<Z, List<Double>> f = z -> {
      if (z instanceof IntString is) {
        return is.asDoubleString();
      }
      if (z instanceof BitString bs) {
        return bs.asDoubleString();
      }
      if (z instanceof List<?> list) {
        return list.stream()
            .map(i -> {
              if (i instanceof Number n) {
                return n.doubleValue();
              }
              throw new IllegalArgumentException("Cannot convert %s to double"
                  .formatted(i.getClass().getSimpleName()));
            })
            .toList();
      }
      throw new IllegalArgumentException(
          "Cannot convert %s to double string".formatted(z.getClass().getSimpleName()));
    };
    return NamedFunction.from(f, "to.double.string").compose(beforeF);
  }
}
