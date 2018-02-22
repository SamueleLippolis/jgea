/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.units.malelab.jgea.core.listener.collector;

import it.units.malelab.jgea.core.Individual;
import it.units.malelab.jgea.core.listener.event.EvolutionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author eric
 */
public class Diversity implements DataCollector {

  @Override
  public Map<String, Object> collect(EvolutionEvent evolutionEvent) {
    List<Collection<Individual>> rankedPopulation = new ArrayList<>((List)evolutionEvent.getRankedPopulation());
    Set genotypes = new HashSet<>();
    Set solutions = new HashSet<>();
    Set fitnesses = new HashSet<>();
    double count = 0;
    for (Collection<Individual> rank : rankedPopulation) {
      for (Individual individual : rank) {
        genotypes.add(individual.getGenotype());
        solutions.add(individual.getSolution());
        fitnesses.add(individual.getFitness());
        count = count + 1;
      }
    }
    Map<String, Object> indexes = new LinkedHashMap<>();
    indexes.put("diversity.genotype", (double) genotypes.size() / count);
    indexes.put("diversity.solution", (double) solutions.size() / count);
    indexes.put("diversity.fitness", (double) fitnesses.size() / count);
    return indexes;
  }

  @Override
  public Map<String, String> getFormattedNames() {
    LinkedHashMap<String, String> formattedNames = new LinkedHashMap<>();
    formattedNames.put("diversity.genotype", "%4.2f");
    formattedNames.put("diversity.solution", "%4.2f");
    formattedNames.put("diversity.fitness", "%4.2f");
    return formattedNames;
  }

}
