package main.alns;


public enum Config {

    DEFAULT(100000, 500, 0.1, 20, 5, 1, 0.99937, 0.05, 0.5);

    /**
     * 迭代次数
     */
    private final int omega;
    /**
     * 更新算子选择概率的间隔迭代次数
     */
    private final int tau;
    /**
     * 计算概率
     */
    private final double r_p;
    /**
     * 发现全局最优，add
     */
    private final int sigma_1;
    /**
     * 发现局部最优，add
     */
    private final int sigma_2;
    /**
     * 发现较差，add
     */
    private final int sigma_3;
    private final double c;
    private final double delta;
    private final double big_omega;

    Config(int omega, int tau, double r_p, int sigma_1, int sigma_2, int sigma_3, double c, double delta, double big_omega) {
        this.omega = omega;
        this.tau = tau;
        this.r_p = r_p;
        this.sigma_1 = sigma_1;
        this.sigma_2 = sigma_2;
        this.sigma_3 = sigma_3;
        this.c = c;
        this.delta = delta;
        this.big_omega = big_omega;
    }

    public int getOmega() {
        return omega;
    }

    public int getTau() {
        return tau;
    }

    public double getR_p() {
        return r_p;
    }

    public int getSigma_1() {
        return sigma_1;
    }

    public int getSigma_2() {
        return sigma_2;
    }

    public int getSigma_3() {
        return sigma_3;
    }

    public double getC() {
        return c;
    }

    public double getDelta() {
        return delta;
    }

    public double getBig_omega() {
        return big_omega;
    }
}
