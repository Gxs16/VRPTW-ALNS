package main.alns.operation.destroy;

import main.algrithm.MyALNSSolution;
import main.alns.operation.AbstractOperation;
import main.domain.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

/**  
* <p>Title: RandomDestroy</p>  
* <p>Description: </p>  
* @author zll_hust  
 */
public class RandomDestroy extends AbstractOperation implements Destroy {
	private static final Logger logger = Logger.getLogger(RandomDestroy.class.getSimpleName());


	@Override
	public MyALNSSolution destroy(MyALNSSolution s, int removeNr) {
		
		if(!s.removalCustomers.isEmpty()) {
			logger.severe("removalCustomers is not empty.");
			return s;
		}
		
		while(s.removalCustomers.size() < removeNr ) {

			// 获取随机数
			Random r = s.instance.getRandom();
			
    		ArrayList<Integer> routeList= new ArrayList<>();
            for(int j = 0; j < s.routes.size(); j++)
                routeList.add(j);  
            
            Collections.shuffle(routeList);

			// 选择被移除客户所在的路径
			int removenRoutePosition = routeList.remove(0);
			Route removenRoute = s.routes.get(removenRoutePosition);
			
			while(removenRoute.getRoute().size() <= 2) {
				removenRoutePosition = routeList.remove(0);
				removenRoute = s.routes.get(removenRoutePosition);
			}

			// 选择被移除的客户
			int removedCustomerPosition = r.nextInt(removenRoute.getRoute().size() - 2) + 1;
			
			s.removeCustomer(removenRoutePosition, removedCustomerPosition);
		}

		return s;
	}

}
