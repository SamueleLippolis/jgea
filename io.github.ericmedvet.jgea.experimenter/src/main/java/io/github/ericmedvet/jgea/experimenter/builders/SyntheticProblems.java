/*-
 * ========================LICENSE_START=================================
 * jgea-experimenter
 * %%
 * Copyright (C) 2018 - 2023 Eric Medvet
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

import io.github.ericmedvet.jgea.problem.grid.CharShapeApproximation;
import io.github.ericmedvet.jgea.problem.synthetic.*;
import io.github.ericmedvet.jnb.core.Discoverable;
import io.github.ericmedvet.jnb.core.Param;
import java.io.IOException;

@Discoverable(prefixTemplate = "ea.problem|p.synthetic|s")
public class SyntheticProblems {

  private SyntheticProblems() {}

  @SuppressWarnings("unused")
  public static Ackley ackley(@Param(value = "p", dI = 100) int p) {
    return new Ackley(p);
  }

  @SuppressWarnings("unused")
  public static CharShapeApproximation charShapeApproximation(
      @Param("target") String syntheticTargetName,
      @Param(value = "translation", dB = true) boolean translation,
      @Param(value = "smoothed", dB = true) boolean smoothed,
      @Param(value = "weighted", dB = true) boolean weighted) {
    try {
      return new CharShapeApproximation(syntheticTargetName, translation, smoothed, weighted);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unused")
  public static DoublesOneMax doublesOneMax(@Param(value = "p", dI = 100) int p) {
    return new DoublesOneMax(p);
  }

  @SuppressWarnings("unused")
  public static DoublesVariableTarget doublesVariableTarget(
      @Param(value = "p", dI = 100) int p, @Param(value = "target") double target) {
    return new DoublesVariableTarget(p, target);
  }

  @SuppressWarnings("unused")
  public static IntOneMax intOneMax(
      @Param(value = "p", dI = 100) int p, @Param(value = "upperBound", dI = 100) int upperBound) {
    return new IntOneMax(p, upperBound);
  }

  @SuppressWarnings("unused")
  public static LinearPoints linearPoints(@Param(value = "p", dI = 100) int p) {
    return new LinearPoints(p);
  }

  @SuppressWarnings("unused")
  public static OneMax oneMax(@Param(value = "p", dI = 100) int p) {
    return new OneMax(p);
  }

  @SuppressWarnings("unused")
  public static Rastrigin rastrigin(@Param(value = "p", dI = 100) int p) {
    return new Rastrigin(p);
  }

  @SuppressWarnings("unused")
  public static Sphere sphere(@Param(value = "p", dI = 100) int p) {
    return new Sphere(p);
  }
}
