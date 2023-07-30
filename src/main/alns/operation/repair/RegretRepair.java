package main.alns.operation.repair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import main.algrithm.Cost;
import main.algrithm.ALNSSolution;
import main.alns.operation.AbstractOperation;
import main.domain.Node;


/**
 * <p>Title: RegretRepair</p>
 * <p>Description: </p>
 * @author zll_hust
 */
public class RegretRepair extends AbstractOperation implements Repair {
    private static final Logger logger = Logger.getLogger(RegretRepair.class.getSimpleName());
    @Override
    public ALNSSolution repair(ALNSSolution s) {
        // 如果没有移除的客户，上一步错误
        if(s.removalCustomers.isEmpty()) {
            logger.severe("removalCustomers is empty!");
            return s;
        }

        ArrayList<BestPos> bestPoses = new ArrayList<BestPos>();

        int removeNr = s.removalCustomers.size();

        for(int k = 0; k < removeNr; k++) {

            Node insertNode = s.removalCustomers.remove(0);

            double first,second;
            int bestCusP = -1;
            int bestRouteP = -1;
            first = second = Double.POSITIVE_INFINITY;

            for(int j = 0; j < s.routes.size(); j++) {

                if(s.routes.get(j).getRoute().isEmpty()) {
                    continue;
                }

                // 寻找最优插入位置
                for (int i = 1; i < s.routes.get(j).getRoute().size() - 1; ++i) {

                    // 评价插入情况
                    Cost newCost = new Cost(s.cost);
                    s.evaluateInsertCustomer(j, i, insertNode, newCost);

                    if(newCost.total > Double.MAX_VALUE) {
                        newCost.total = Double.MAX_VALUE;
                    }

                    // if a better insertion is found, set the position to insert in the move and update the minimum cost found
                    if (newCost.total < first) {
                        bestCusP = i;
                        bestRouteP = j;
                        second = first;
                        first = newCost.total;
                    }else if(newCost.total < second && newCost.total != first) {
                        second = newCost.total;
                    }
                }
            }
            bestPoses.add(new BestPos(insertNode, bestCusP, bestRouteP, second - first));
        }
        Collections.sort(bestPoses);

        for(BestPos bp : bestPoses) {
            s.insertCustomer(bp.bestCustomerPosition, bp.bestRroutePosition, bp.insertNode);
        }

        return s;
    }
}

class BestPos implements Comparable<BestPos>{

    public int bestRroutePosition;
    public int bestCustomerPosition;
    public Node insertNode;
    public double deltaCost;

    public BestPos(Node insertNode, int customer, int route, double f) {
        this.insertNode = insertNode;
        this.bestRroutePosition = customer;
        this.bestCustomerPosition = route;
        this.deltaCost = f;
    }

    @Override
    public int compareTo(BestPos o) {
        return Double.compare(o.deltaCost, this.deltaCost);
    }
}
