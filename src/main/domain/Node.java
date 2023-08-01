package main.domain;

/**
 * @author zll_hust
 *
 * Every instance of this class represents a Node (customer) of the VRP problem.
 */
public class Node {
    private final double readyTime;
    private final double dueTime;
	private final double serviceTime;

    /**
     * The X-axis coordinate in a theoretical 2-D space for the specific customer.
     */
    private final double x;

    /**
     * The Y-axis coordinate in a theoretical 2-D space for the specific customer.
     */
    private final double y;

    /**
     * A unique identifier for the customer
     */
    private final int id;

    /**
     * The current customer's demand.
     */
    private final double demand;
    public Node(int id, double x, double y, double demand, double readyTime, double dueTime, double serviceTime){
        this.id = id;
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.serviceTime = serviceTime;
    }
    
	public double getServiceTime() {
        return this.serviceTime;
    }

    public double getReadyTime() {
        return this.readyTime;
    }

    public double getDueTime() {
        return this.dueTime;
    }

	public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public double getDemand() {
        return demand;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", demand=" + demand +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;

    }
}
