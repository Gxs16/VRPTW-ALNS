package main.algrithm;

import main.alns.config.IALNSConfig;
import main.domain.Instance;


public class Solver {

    public Solver() {
    }

    public Solution getInitialSolution(Instance instance) {
    	GreedyVRP greedyVRP = new GreedyVRP(instance);
    	return greedyVRP.getInitialSolution();
    }

    public Solution improveSolution(Solution s, IALNSConfig ac, Instance is) throws Exception {
        MyALNSProcess ALNS = new MyALNSProcess(s, is, ac);
    	return ALNS.improveSolution();
    }
}
