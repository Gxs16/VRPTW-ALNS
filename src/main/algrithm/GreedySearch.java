package main.algrithm;


import main.domain.Instance;
import main.domain.Node;
import main.domain.Route;
import main.domain.Solution;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the necessary functionality in order to solve the VRP problem using a greedy approach.
 */
public class GreedySearch {

    /**
     * All the customers
     */
    private final List<Node> customers;

    /**
     * All the vehicles.
     */
    private final List<Route> vehicles;

    /**
     * The distance matrix for the customers
     */
    private final double[][] distanceMatrix;
    
    private final int vehicleCapacity;

    /**
     * Constructor
     */
    public GreedySearch(Instance instance) {
		this.customers = new ArrayList<>(instance.getCustomers());
        this.distanceMatrix = instance.getDistanceMatrix();
		this.vehicleCapacity = instance.getVehicleCapacity();
		
		int vehicleNr = instance.getVehicleNr();
		this.vehicles = new ArrayList<>();
		for(int i = 0;i < vehicleNr; ++i) {
			Route route = new Route(i);
			this.vehicles.add(route);
        }
    }

    /**
     * Finds and returns a solution to the VRP using greedy algorithm approach
     *
     * @return Solution
     */
    public Solution solve() {
        // The final Solution
        Solution solution = new Solution();

        // Fetch the depot node.
        Node depot = this.customers.remove(0);

        // Fetch the first available vehicle
        Route currentVehicle = this.vehicles.remove(0);

        // Add the depot to the vehicle.
        currentVehicle.addNodeToRoute(depot);

        // Repeat until all customers are routed or if we run out vehicles.
        while (true) {

            // If we served all customers, exit.
            if (this.customers.isEmpty())
                break;

            // Get the last node of the current route. We will try to find the closest node to it that also satisfies the capacity constraint.
            Node lastInTheCurrentRoute = currentVehicle.getLastNodeOfTheRoute();

            // The distance of the closest node, if any, to the last node in the route.
            double smallestDistance = Double.MAX_VALUE;

            // The closest node, if any, to the last node in the route that also satisfies the capacity constraint.
            Node closestNode = null;

            // Find the nearest neighbor based on distance
            for (Node n: this.customers) {
                double distance = this.distanceMatrix[lastInTheCurrentRoute.getId()][n.getId()];

                // If we found a customer with closer that the value of "smallestDistance" ,store him temporarily
                if ( distance < smallestDistance &&
                		(currentVehicle.getCost().load + n.getDemand()) <= vehicleCapacity &&
                		(currentVehicle.getCost().time + distanceMatrix[currentVehicle.getId()][n.getId()]) < n.getDueTime() &&
                		(currentVehicle.getCost().time + distanceMatrix[currentVehicle.getId()][n.getId()] + n.getServiceTime() + distanceMatrix[n.getId()][depot.getId()]) < depot.getDueTime())
                {
                    smallestDistance = distance;
                    closestNode = n;
                }
            }
            
            // A node that satisfies the capacity constraint found
            if (closestNode != null) {
                // Increase the cost of the current route by the distance of the previous final node to the new one
                currentVehicle.getCost().cost += smallestDistance;

                // Increase the time of the current route by the distance of the previous final node to the new one and serves time
                currentVehicle.getCost().time += smallestDistance;
                
                // waiting time windows open
                if (currentVehicle.getCost().time < closestNode.getReadyTime()) currentVehicle.getCost().time = closestNode.getReadyTime();
                
                currentVehicle.getCost().time += closestNode.getServiceTime();
                
                // Increase the load of the vehicle by the demand of the new node-customer
                currentVehicle.getCost().load += closestNode.getDemand();

                // Add the closest node to the route
                currentVehicle.addNodeToRoute(closestNode);
                
                // Remove customer from the non-served customers list.
                this.customers.remove(closestNode);

            // We didn't find any node that satisfies the condition.
            } else {
                // Increase cost by the distance to travel from the last node back to depot
                currentVehicle.getCost().cost += this.distanceMatrix[lastInTheCurrentRoute.getId()][depot.getId()];
                currentVehicle.getCost().time += this.distanceMatrix[lastInTheCurrentRoute.getId()][depot.getId()];

                // Terminate current route by adding the depot as a final destination
                currentVehicle.addNodeToRoute(depot);
                
                currentVehicle.getCost().calculateTotalCost();

                // Add the finalized route to the solution
                solution.addRoute(currentVehicle);

                // Increase the solution's total cost by the cost of the finalized route
                solution.setTotalCost(solution.getTotalCost() + currentVehicle.getCost().cost);
                
                // If we used all vehicles, exit.
                if (this.vehicles.isEmpty()) {
                	break;
                	
                // if we still have some vehicles, use.
                } else {
                	// Recruit a new vehicle.
                    currentVehicle = this.vehicles.remove(0);

                    // Add the depot as a starting point to the new route
                    currentVehicle.addNodeToRoute(depot);
                }
            }
        }

        // Now add the final route to the solution
        currentVehicle.getCost().cost += this.distanceMatrix[currentVehicle.getLastNodeOfTheRoute().getId()][depot.getId()];
        currentVehicle.getCost().time += this.distanceMatrix[currentVehicle.getLastNodeOfTheRoute().getId()][depot.getId()];
        currentVehicle.addNodeToRoute(depot);
        currentVehicle.getCost().calculateTotalCost();
        
        solution.addRoute(currentVehicle);
        solution.setTotalCost(solution.getTotalCost() + currentVehicle.getCost().cost);
        solution.setTotalCost(Math.round(solution.getTotalCost() * 1000) / 1000.0);

        return solution;
    }
}
