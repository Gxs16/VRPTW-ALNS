package main.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
* @author zll_hust  
*/
public class Instance {
    private final Random r;

    public Random getRandom() {
		return r;
	}

    /**
     * This list will keep all the nodes of the problem.
     * NOTE: position 0 of the list contains the depot.
     */
    private final List<Node> customers = new ArrayList<>();

    /**
     * The available vehicles numbers.
     */
    private int vehicleNr;
    
    /**
     * The capacity of vehicles.
     */
    private int vehicleCapacity;

    /**
     * A 2-D matrix that will keep the distances of every node to each other.
     */
    public double[][] distanceMatrix;


    public List<Node> getCustomers() {
        return this.customers;
    }

    public double[][] getDistanceMatrix() {
        return this.distanceMatrix;
    }
    
    public int getVehicleNr() {
    	return this.vehicleNr;
    }
    
    public int getVehicleCapacity() {
    	return this.vehicleCapacity;
    }
    
    public int getCustomerNr() {
    	return this.customers.size();
    }

    public void setVehicleNr(int vehicleNr) {
        this.vehicleNr = vehicleNr;
    }

    public void setVehicleCapacity(int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }
    
    public Instance() {
        this.r = new Random();
        this.r.setSeed(-1);
    }

}
