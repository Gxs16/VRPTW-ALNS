package main.algrithm;

import main.alns.Config;
import main.domain.Instance;
import main.domain.Solution;


public class Solver {

    public Solution getInitialSolution(Instance instance) {
    	GreedyVRP greedyVRP = new GreedyVRP(instance);
    	return greedyVRP.getInitialSolution();
    }

    public Solution improveSolution(Solution s, Config ac, Instance is) throws Exception {
        ALNSProcess ALNS = new ALNSProcess(s, is, ac);
    	return ALNS.improveSolution();
    }
}
