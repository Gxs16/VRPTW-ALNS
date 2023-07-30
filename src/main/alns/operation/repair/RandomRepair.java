package main.alns.operation.repair;

import java.util.*;
import java.util.logging.Logger;

import main.algrithm.Cost;
import main.algrithm.MyALNSSolution;
import main.alns.operation.AbstractOperation;
import main.domain.Node;
import main.domain.Route;

/**
 * <p>Title: RandomRepair</p>
 * <p>Description: </p>
 * @author zll_hust
 */
public class RandomRepair extends AbstractOperation implements Repair {
    private static final Logger logger = Logger.getLogger(RandomRepair.class.getSimpleName());

    @Override
    public MyALNSSolution repair(MyALNSSolution s) {
        // 如果没有移除的客户，上一步错误
        if(s.removalCustomers.isEmpty()) {
            logger.severe("removalCustomers is empty!");
            return s;
        }

        // 获取随机数
        Random r = s.instance.getRandom();
        int insertCusNr = s.removalCustomers.size();

        for (int i = 0; i < insertCusNr; i++) {

            Node insertNode = s.removalCustomers.remove(0);

            // 随机决定查找多少条路径
            int randomRouteNr = r.nextInt(s.routes.size() - 1) + 1;

            // 最优插入方案
            int bestRoutePosition = -1;
            int bestCusomerPosition = -1;
            Cost bestCost = new Cost();
            bestCost.total = Double.MAX_VALUE;

            ArrayList<Integer> routeList= new ArrayList<Integer>();
            for(int j = 0; j < s.routes.size(); j++)
                routeList.add(j);

            Collections.shuffle(routeList);

            for (int j = 0; j < randomRouteNr; j++) {

                // 随机选择一条route
                int insertRoutePosition = routeList.remove(0);
                Route insertRoute = s.routes.get(insertRoutePosition);

                while(insertRoute.getRoute().isEmpty()) {
                    insertRoutePosition = routeList.remove(0);
                    insertRoute = s.routes.get(insertRoutePosition);
                }

                // 随机决定查找多少个位置
                int insertTimes = r.nextInt(insertRoute.getRoute().size() - 1) + 1;

                ArrayList<Integer> customerList= new ArrayList<Integer>();
                for(int k = 1; k < insertRoute.getRoute().size(); k++)
                    customerList.add(k);

                Collections.shuffle(customerList);

                // 随机选择一条位置
                for (int k = 0; k < insertTimes; k++) {

                    int insertCusPosition = customerList.remove(0);

                    // 评价插入情况
                    Cost newCost = new Cost(s.cost);
                    s.evaluateInsertCustomer(insertRoutePosition, insertCusPosition, insertNode, newCost);

                    // 更新最优插入位置
                    if (newCost.total < bestCost.total) {
                        bestRoutePosition = insertRoutePosition;
                        bestCusomerPosition = insertCusPosition;
                        bestCost = newCost;
                    }
                }
                // 执行插入操作
                s.insertCustomer(bestRoutePosition, bestCusomerPosition, insertNode);
            }
        }

        return s;
    }

}
