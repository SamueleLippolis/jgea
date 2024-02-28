/*-
 * ========================LICENSE_START=================================
 * jgea-problem
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
/*
 * Copyright 2024 eric
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

package io.github.ericmedvet.jgea.problem.control;

import io.github.ericmedvet.jgea.core.order.PartialComparator;
import io.github.ericmedvet.jgea.core.problem.ProblemWithExampleSolution;
import io.github.ericmedvet.jgea.core.problem.QualityBasedProblem;
import io.github.ericmedvet.jnb.datastructure.DoubleRange;
import io.github.ericmedvet.jsdynsym.control.Environment;
import io.github.ericmedvet.jsdynsym.control.SingleAgentTask;
import io.github.ericmedvet.jsdynsym.core.DynamicalSystem;

import java.util.SortedMap;
import java.util.function.Function;
import java.util.function.Supplier;

public interface SingleAgentControlProblem<C extends DynamicalSystem<O, A, ?>, O, A, S, Q>
    extends QualityBasedProblem<C, SingleAgentControlProblem.Outcome<O, A, S, Q>>, ProblemWithExampleSolution<C> {
  record Outcome<O, A, S, Q>(SortedMap<Double, SingleAgentTask.Step<O, A, S>> behavior, Q quality) {}

  Function<SortedMap<Double, SingleAgentTask.Step<O, A, S>>, Q> behaviorQualityFunction();

  SingleAgentTask<C, O, A, S> singleAgentTask();

  static <C extends DynamicalSystem<O, A, ?>, O, A, S, Q> SingleAgentControlProblem<C, O, A, S, Q> fromEnvironment(
      Supplier<Environment<O, A, S>> environment,
      C example,
      Function<SortedMap<Double, SingleAgentTask.Step<O, A, S>>, Q> behaviorQualityFunction,
      PartialComparator<Outcome<O, A, S, Q>> qualityComparator,
      DoubleRange tRange,
      double dT) {
    SingleAgentTask<C, O, A, S> singleAgentTask = SingleAgentTask.fromEnvironment(environment, tRange, dT);
    return new SingleAgentControlProblem<>() {
      @Override
      public Function<SortedMap<Double, SingleAgentTask.Step<O, A, S>>, Q> behaviorQualityFunction() {
        return behaviorQualityFunction;
      }

      @Override
      public SingleAgentTask<C, O, A, S> singleAgentTask() {
        return singleAgentTask;
      }

      @Override
      public C example() {
        return example;
      }

      @Override
      public PartialComparator<Outcome<O, A, S, Q>> qualityComparator() {
        return qualityComparator;
      }
    };
  }

  @Override
  default Function<C, SingleAgentControlProblem.Outcome<O, A, S, Q>> qualityFunction() {
    return c -> {
      SortedMap<Double, SingleAgentTask.Step<O, A, S>> behavior =
          singleAgentTask().simulate(c);
      return new Outcome<>(behavior, behaviorQualityFunction().apply(behavior));
    };
  }
}
