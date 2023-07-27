package main.algrithm;


import main.domain.Instance;
import main.domain.Route;

/**
 * <p>Title: CheckSolution</p>
 * <p>Description: </p>
 * @author zll_hust
 */
public class CheckSolution {

    private final double[][] distance;

    public CheckSolution(Instance instance) {
        this.distance = instance.getDistanceMatrix();
    }

    public String Check(Solution solution) {
        StringBuilder result = new StringBuilder();
        double totalCost = 0;

        int id = 0;

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            Route vehicle = solution.getRoutes().get(i);
            if (vehicle.getRoute().size() >= 3) {
                id++;

                double costInVehicle = 0;
                double loadInVehicle = 0;
                double time = 0;

                boolean checkCost = true;
                boolean checkLoad = true;
                boolean checkTime = true;
                boolean checkTimeWindows = true;

                for (int j = 1; j < vehicle.getRoute().size(); j++) {
                    time += distance[vehicle.getRoute().get(j - 1).getId()][vehicle.getRoute().get(j).getId()];
                    costInVehicle += distance[vehicle.getRoute().get(j - 1).getId()][vehicle.getRoute().get(j).getId()];
                    loadInVehicle += vehicle.getRoute().get(j).getDemand();
                    if (time < vehicle.getRoute().get(j).getTimeWindow()[0])
                        time = vehicle.getRoute().get(j).getTimeWindow()[0];
                    else if (time > vehicle.getRoute().get(j).getTimeWindow()[1])
                        checkTimeWindows = false;

                    time += vehicle.getRoute().get(j).getServiceTime();
                }

                totalCost += costInVehicle;

                if (Math.abs(vehicle.getCost().cost - costInVehicle) > 0.001) checkCost = false;
                if (Math.abs(vehicle.getCost().load - loadInVehicle) > 0.001) checkLoad = false;
                if (Math.abs(vehicle.getCost().time - time) > 0.001) checkTime = false;


                result.append("\n check route ").append(id).append(": ").append("\n check cost = ").append(costInVehicle).append("  ").append(checkCost).append("\n check demand = ").append(loadInVehicle).append("  ").append(checkLoad).append("\n check time = ").append(time).append("  ").append(checkTime).append("\n check time windows = ").append(checkTimeWindows).append("\n");

            }
        }

        boolean checkTotalCost = !(Math.abs(totalCost - solution.getTotalCost()) > 0.001);

        result.append("\ncheck total cost = ").append(Math.round(totalCost * 100) / 100.0).append("  ").append(checkTotalCost);

        return result.toString();
    }

}