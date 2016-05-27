/**
 * Copyright (c) 2016, Ecole des Mines de Nantes
 * All rights reserved.
 */
package org.chocosolver.samples;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 * Verbal arithmetic
 * <p>
 * @author Charles Prud'homme
 * @since 27/05/2016.
 */
public class SendMoreMoney {

    public void modelAndSolve(){
        Model model = new Model("SEND+MORE=MONEY");
        IntVar S = model.intVar("S", 1, 9, false);
        IntVar E = model.intVar("E", 0, 9, false);
        IntVar N = model.intVar("N", 0, 9, false);
        IntVar D = model.intVar("D", 0, 9, false);
        IntVar M = model.intVar("M", 1, 9, false);
        IntVar O = model.intVar("0", 0, 9, false);
        IntVar R = model.intVar("R", 0, 9, false);
        IntVar Y = model.intVar("Y", 0, 9, false);

        model.allDifferent(new IntVar[]{S, E, N, D, M, O, R, Y}).post();

        IntVar[] ALL = new IntVar[]{
                S, E, N, D,
                M, O, R, E,
                M, O, N, E, Y};
        int[] COEFFS = new int[]{
                1000, 100, 10, 1,
                1000, 100, 10, 1,
                -10000, -1000, -100, -10, -1};
        model.scalar(ALL, COEFFS, "=", 0).post();

        Solver solver = model.getSolver();
        solver.showStatistics();
        solver.showSolutions();
        solver.findSolution();
    }

    public static void main(String[] args) {
        new SendMoreMoney().modelAndSolve();
    }
}
