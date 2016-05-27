/**
 * Copyright (c) 2016, Ecole des Mines de Nantes
 * All rights reserved.
 */
package org.chocosolver.samples;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.automata.FA.FiniteAutomaton;
import org.chocosolver.solver.constraints.nary.automata.FA.IAutomaton;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.util.tools.ArrayUtils;

/**
 * Nonogram
 * <p>
 *
 * @author Charles Prud'homme
 * @since 27/05/2016.
 */
public class Nonogram {

    private void modelAndSolve() {
        // number of columns
        int N = 15;
        // number of rows
        int M = 15;
        // sequence of shaded blocks
        int[][][] BLOCKS =
                new int[][][]{{
                        {2},
                        {4, 2},
                        {1, 1, 4},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 2, 2, 1},
                        {1, 3, 1},
                        {2, 1},
                        {1, 1, 1, 2},
                        {2, 1, 1, 1},
                        {1, 2},
                        {1, 2, 1},
                }, {
                        {3},
                        {3},
                        {10},
                        {2},
                        {2},
                        {8, 2},
                        {2},
                        {1, 2, 1},
                        {2, 1},
                        {7},
                        {2},
                        {2},
                        {10},
                        {3},
                        {2}}};

        Model model = new Model("Nonogram");
        // Variables declaration
        BoolVar[][] cells = model.boolVarMatrix("c", N, M);
        // Constraint declaration
        // one regular per row
        for (int i = 0; i < M; i++) {
            dfa(cells[i], BLOCKS[0][i], model);
        }
        for (int j = 0; j < N; j++) {
            dfa(ArrayUtils.getColumn(cells, j), BLOCKS[1][j], model);
        }
        if(model.getSolver().solve()){
            for (int i = 0; i < cells.length; i++) {
                System.out.printf("\t");
                for (int j = 0; j < cells[i].length; j++) {
                    System.out.printf(cells[i][j].getValue() == 1 ? "#" : " ");
                }
                System.out.printf("\n");
            }
        }

    }

    private void dfa(BoolVar[] cells, int[] seq, Model model) {
        StringBuilder regexp = new StringBuilder("0*");
        int m = seq.length;
        for (int i = 0; i < m; i++) {
            regexp.append('1').append('{').append(seq[i]).append('}');
            regexp.append('0');
            regexp.append(i == m - 1 ? '*' : '+');
        }
        IAutomaton auto = new FiniteAutomaton(regexp.toString());
        model.regular(cells, auto).post();
    }

    public static void main(String[] args) {
        new Nonogram().modelAndSolve();

    }
}
