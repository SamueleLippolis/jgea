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

package it.units.malelab.jgea.problem.classification;

import it.units.malelab.jgea.core.ProblemWithValidation;
import it.units.malelab.jgea.core.order.ParetoDominance;
import it.units.malelab.jgea.core.order.PartialComparator;
import it.units.malelab.jgea.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eric
 */
public class ClassificationProblem<O, L> implements ProblemWithValidation<Classifier<O, L>, List<Double>> {

  // TODO fix this
  private final static PartialComparator<List<Double>> COMPARATOR = ParetoDominance.build(Double.class, 1);

  private final ClassificationFitness<O, L> fitnessFunction;
  private final ClassificationFitness<O, L> validationFunction;
  private final List<Pair<O, Label<L>>> learningData;
  private final List<Pair<O, Label<L>>> validationData;

  public ClassificationProblem(
      List<Pair<O, Label<L>>> data,
      int folds,
      int i,
      ClassificationFitness.Metric learningMetric,
      ClassificationFitness.Metric validationMetric
  ) {
    validationData = DataUtils.fold(data, i, folds);
    learningData = new ArrayList<>(data);
    learningData.removeAll(validationData);
    fitnessFunction = new ClassificationFitness<>(learningData, learningMetric);
    validationFunction = new ClassificationFitness<>(validationData, validationMetric);
  }

  public List<Pair<O, Label<L>>> getLearningData() {
    return learningData;
  }

  public List<Pair<O, Label<L>>> getValidationData() {
    return validationData;
  }

  @Override
  public PartialComparator<List<Double>> qualityComparator() {
    return COMPARATOR;
  }

  @Override
  public ClassificationFitness<O, L> qualityFunction() {
    return fitnessFunction;
  }

  @Override
  public ClassificationFitness<O, L> validationQualityFunction() {
    return validationFunction;
  }

}
