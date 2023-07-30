package main.alns.operation;

import java.util.*;

public abstract class AbstractOperation implements Operation {
    private final Random r = new Random();
    private int pi = 0;
    private double p;
    private int draws = 0;
    private double w = 1.0;

    /**
     * 被使用的次数
     */
    @Override
    public void drawn() {
        draws++;
    }

    /**
     * 优化最优满意解，则增加pi值
     */
    @Override
    public void addToPi(int pi) {
        this.pi += pi;
    }

    public int getPi() {
        return this.pi;
    }

    public double getP() {
        return this.p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public int getDraws() {
        return this.draws;
    }

    public double getW() {
        return this.w;
    }

    public void setW(double w) {
        this.w = w;
    }
}
