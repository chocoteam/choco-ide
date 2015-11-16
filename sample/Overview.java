/**
 * Copyright (c) 2015, Ecole des Mines de Nantes
 * This class is released under a BSD 4-clause License.
 * Visit http://choco-solver.org/ for more details.
 */
package org.chocosolver.examples;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

/**
 * Basic steps to be done when modelling a problem with Choco3.
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 16/11/2015
 */
public class Overview {

    public static void main(String[] args) {
        // 1. Create a Solver
        Solver solver = new Solver("my first problem");
        // 2. Create variables through the variable factory
        IntVar x = VariableFactory.bounded("X", 0, 5, solver);
        IntVar y = VariableFactory.bounded("Y", 0, 5, solver);
        // 3. Create and post constraints by using constraint factories
        solver.post(IntConstraintFactory.arithm(x, "+", y, "<", 5));
        // 4. Define the search strategy
        solver.set(IntStrategyFactory.lexico_LB(x, y));
        // 5. Indicates that all solutions should be print to the console
        Chatterbox.showSolutions(solver);
        // 6. Launch the resolution process
        solver.findSolution();
        // 7. Finally, outputs the resolution statistics
        Chatterbox.printStatistics(solver);
    }
}
