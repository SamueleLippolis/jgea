/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.units.malelab.jgea.core.operator;

import it.units.malelab.jgea.core.listener.Listener;
import it.units.malelab.jgea.core.function.FunctionException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author eric
 */
public abstract class AbstractMutation<G> implements GeneticOperator<G> {

  @Override
  public int arity() {
    return 1;
  }

  @Override
  public List<G> apply(List<G> gs, Random random, Listener listener) throws FunctionException {
    return Collections.singletonList(mutate(gs.get(0), random, listener));
  }
  
  public abstract G mutate(G g, Random random, Listener listener);
  
}
