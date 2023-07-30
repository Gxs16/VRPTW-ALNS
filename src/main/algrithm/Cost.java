package main.algrithm;

public class Cost {
    public double total;
    public double cost;
    public double time;
    public double load;

    public double loadViolation;
    public double timeViolation;

    public Cost() {
        total = 0;
        cost = 0;
        load = 0;
        time = 0;

        loadViolation = 0;
        timeViolation = 0;
    }

    public Cost(Cost cost) {
        this.total = cost.total;
        this.cost = cost.cost;
        this.load = cost.load;
        this.time = cost.time;

        this.loadViolation = cost.loadViolation;
        this.timeViolation = cost.timeViolation;
    }


    @Override
    public String toString() {
        String result = "[ total =" + total +
                ", cost =" + cost +
                ", load =" + load +
                ", time =" + time +
                ", time windows violation =" + timeViolation +
                ", load violation =" + loadViolation;

        return result + " ]";
    }


    /**
     * Set the total cost based on alpha, beta
     */
    public void calculateTotalCost(double alpha, double beta) {
        total = cost + alpha * loadViolation + beta * timeViolation;
    }

    public void calculateTotalCost() {
        total = cost + loadViolation + timeViolation;
    }

    public double getLoadViolation() {
        return loadViolation;
    }

    public double getTimeViolation() {
        return timeViolation;
    }

}
