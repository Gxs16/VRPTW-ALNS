package main.alns.destroy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import main.algrithm.MyALNSSolution;
import main.alns.operation.ALNSAbstractOperation;
import main.domain.Node;
import main.domain.Route;
import main.domain.Instance;

/**
 * <p>Title: WorstCostDestroy</p>
 * <p>Description: </p>
 * @author zll_hust
 */
public class WorstCostDestroy extends ALNSAbstractOperation implements IALNSDestroy {
    private static final Logger logger = Logger.getLogger(WorstCostDestroy.class.getSimpleName());

    @Override
    public MyALNSSolution destroy(MyALNSSolution s, int removeNr) {

        if(!s.removalCustomers.isEmpty()) {
            logger.severe("removalCustomers is not empty.");
            return s;
        }

        // 计算fitness值，对客户进行评估。
        ArrayList<Fitness> customerFitness = new  ArrayList<Fitness>();
        for(Route route : s.routes) {
            for (Node customer : route.getRoute()) {
                double fitness = Fitness.calculateFitness(s.instance, customer, route);
                customerFitness.add(new Fitness(customer.getId(), fitness));
            }
        }
        Collections.sort(customerFitness);

        ArrayList<Integer> removal = new ArrayList<Integer>();
        for(int i = 0; i < removeNr; ++i) removal.add(customerFitness.get(i).customerNo);

        for(int j = 0; j < s.routes.size(); j++) {
            for (int i = 0; i < s.routes.get(j).getRoute().size();++i) {
                Node customer = s.routes.get(j).getRoute().get(i);
                if(removal.contains(customer.getId())) {
                    s.removeCustomer(j, i);
                }
            }
        }

        return s;
    }
}

class Fitness implements Comparable<Fitness>{
    public int customerNo;
    public double fitness;

    public Fitness(int cNo, double f) {
        customerNo = cNo;
        fitness = f;
    }

    public static double calculateFitness(Instance instance, Node customer, Route route) {
        double[][] distance = instance.getDistanceMatrix();

        double fitness =
                (route.getCost().getTimeViolation() + route.getCost().getLoadViolation() + customer.getDemand()) *
                        ( distance[customer.getId()][route.getRoute().get(0).getId()] +
                                distance[route.getRoute().get(0).getId()][customer.getId()] );

        return fitness;
    }

    @Override
    public int compareTo(Fitness o) {
        return Double.compare(o.fitness, this.fitness);
    }

}
