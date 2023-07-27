package main.algrithm;

import main.alns.config.ControlParameter;
import main.alns.config.IALNSConfig;
import main.domain.Instance;


public class Solver {

    public Solver() {
    }

    public Solution getInitialSolution(Instance instance) {
    	GreedyVRP greedyVRP = new GreedyVRP(instance);
    	return greedyVRP.getInitialSolution();
    }

    public Solution improveSolution(Solution s, IALNSConfig ac, ControlParameter cp, Instance is) throws Exception {
        MyALNSProcess ALNS = new MyALNSProcess(s, is, ac, cp);
    	return ALNS.improveSolution();
    }
}
