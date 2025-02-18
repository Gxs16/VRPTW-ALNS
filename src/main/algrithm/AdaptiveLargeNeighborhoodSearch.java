package main.algrithm;

import main.alns.Config;
import main.alns.operation.destroy.Destroy;
import main.alns.operation.destroy.RandomDestroy;
import main.alns.operation.destroy.ShawDestroy;
import main.alns.operation.destroy.WorstCostDestroy;
import main.alns.operation.repair.GreedyRepair;
import main.alns.operation.repair.RandomRepair;
import main.alns.operation.repair.RegretRepair;
import main.alns.operation.repair.Repair;
import main.domain.Instance;
import main.domain.Solution;

import java.util.Random;
import java.util.logging.Logger;

public class AdaptiveLargeNeighborhoodSearch {
    // 参数
    private final Config config;
    private final Destroy[] destroyOperations = new Destroy[]{
            new ShawDestroy(),
            new RandomDestroy(),
            new WorstCostDestroy()
    };
    private final Repair[] repairOperations = new Repair[]{
            new RegretRepair(),
            new GreedyRepair(),
            new RandomRepair()
    };

    // 全局满意解
    private ALNSSolution s_g;
    // 局部满意解
    private ALNSSolution localSatisfiedSolution;
    private int i = 0;
    // time
    private double T;
    private static final Logger logger = Logger.getLogger(AdaptiveLargeNeighborhoodSearch.class.getSimpleName());

    public AdaptiveLargeNeighborhoodSearch(Solution s_, Instance instance, Config c) {

        config = c;
        s_g = new ALNSSolution(s_, instance);
        localSatisfiedSolution = new ALNSSolution(s_g);
        
        initStrategies();
    }

    public Solution solve() throws Exception {

        T = -(config.getDelta() / Math.log(config.getBig_omega())) * localSatisfiedSolution.cost.total;
        // time end

        // 计时开始
        // time start
        long t_start = System.currentTimeMillis();

        while (true) {
        	
        	// sc局部最优解，从局部最优中生成新解
            ALNSSolution s_c_new = new ALNSSolution(localSatisfiedSolution);
            int q = getQ(s_c_new);

            // 轮盘赌找出最优destroy、repair方法
            Destroy destroyOperator = getALNSDestroyOperator();
            Repair repairOperator = getALNSRepairOperator();

            // destroy solution
            ALNSSolution s_destroy = destroyOperator.destroy(s_c_new, q);

            // repair solution，重组后新解st
            ALNSSolution s_t = repairOperator.repair(s_destroy);

            logger.info("迭代次数 ：" +  i + "当前解 ：" + Math.round(s_t.cost.total * 100) / 100.0);

            // 更新局部满意解
            if (s_t.cost.total < localSatisfiedSolution.cost.total) {
                localSatisfiedSolution = s_t;
                // 更新全局满意解，sg全局满意解
                if (s_t.cost.total < s_g.cost.total) {
                    handleNewGlobalMinimum(destroyOperator, repairOperator, s_t);
                } else {
                    // 更新局部满意解
                    handleNewLocalMinimum(destroyOperator, repairOperator);
                }
            } else {
                // 概率接受较差解
                handleWorseSolution(destroyOperator, repairOperator, s_t);
            }

            if (i % config.getTau() == 0 && i > 0) {
                segmentFinished();
            }
            
            T = config.getC() * T;
            i++;
            
            if (i > config.getOmega() && s_g.feasible()) break;
            if (i > config.getOmega() * 1.5 ) break;
        }
        
        Solution solution = s_g.toSolution();

        // 输出程序耗时s
        double s = Math.round((System.currentTimeMillis() - t_start) * 1000) / 1000000.;
        logger.info("AdaptiveLargeNeighborhoodSearch progress cost " + s + "s.");

        // 输出算子使用情况
        for (Destroy destroy : destroyOperations){
            logger.info(destroy.getClass().getName() + " 被使用 " + destroy.getDraws() + "次.");
        }
        
        for (Repair repair : repairOperations){
            logger.info(repair.getClass().getName() + " 被使用 " + repair.getDraws() + "次.");
        }
        solution.testTime = s;
        return solution;
    }

    private void handleWorseSolution(Destroy destroyOperator, Repair repairOperator, ALNSSolution s_t) {
        //概率接受较差解
    	double p_accept = calculateProbabilityToAcceptTempSolutionAsNewCurrent(s_t);
        if (Math.random() < p_accept) {
            localSatisfiedSolution = s_t;
        }
        destroyOperator.addToPi(config.getSigma_3());
        repairOperator.addToPi(config.getSigma_3());
    }

    private void handleNewLocalMinimum(Destroy destroyOperator, Repair repairOperator) {
        destroyOperator.addToPi(config.getSigma_2());
        repairOperator.addToPi(config.getSigma_2());
    }

    private void handleNewGlobalMinimum(Destroy destroyOperator, Repair repairOperator, ALNSSolution s_t) {

        //接受全局较优
        s_g = s_t;
        destroyOperator.addToPi(config.getSigma_1());
        repairOperator.addToPi(config.getSigma_1());
    }

    private double calculateProbabilityToAcceptTempSolutionAsNewCurrent(ALNSSolution s_t) {
        return Math.exp (-(s_t.cost.total - localSatisfiedSolution.cost.total) / T);
    }

    private int getQ(ALNSSolution s_c2) {
        int q_l = Math.min((int) Math.ceil(0.05 * s_c2.instance.getCustomerNr()), 10);
        int q_u = Math.min((int) Math.ceil(0.20 * s_c2.instance.getCustomerNr()), 30);

        Random r = new Random();
        return r.nextInt(q_u - q_l + 1) + q_l;
    }


    private void segmentFinished() {
        double w_sum = 0;
        // Update new weighting for Destroy Operator
        for (Destroy dstr : destroyOperations) {
            double w_old1 = dstr.getW() * (1 - config.getR_p());
            double recentFactor = dstr.getDraws() < 1 ? 0 : (double) dstr.getPi() / (double) dstr.getDraws();
            double w_old2 = config.getR_p() * recentFactor;
            double w_new = w_old1 + w_old2;
            w_sum += w_new;
            dstr.setW(w_new);
        }
        // Update new probability for Destroy Operator
        for (Destroy dstr : destroyOperations) {
            dstr.setP(dstr.getW() / w_sum);
        }
        w_sum = 0;
        // Update new weighting for Repair Operator
        for (Repair rpr : repairOperations) {
            double recentFactor = rpr.getDraws() < 1 ? 0 : (double) rpr.getPi() / (double) rpr.getDraws();
            double w_new = (rpr.getW() * (1 - config.getR_p())) + config.getR_p() * recentFactor;
            w_sum += w_new;
            rpr.setW(w_new);
        }
        // Update new probability for Repair Operator
        for (Repair rpr : repairOperations) {
            rpr.setP(rpr.getW() / w_sum);
        }
    }


    private Repair getALNSRepairOperator() {
        double random = Math.random();
        double threshold = 0.;
        for (Repair rpr : repairOperations) {
            threshold += rpr.getP();
            if (random <= threshold) {
                rpr.drawn();
                return rpr;
            }
        }
        repairOperations[repairOperations.length - 1].drawn();
        return repairOperations[repairOperations.length - 1];
    }


    private Destroy getALNSDestroyOperator() {
        double random = Math.random();
        double threshold = 0.;
        for (Destroy dstr : destroyOperations) {
            threshold += dstr.getP();
            if (random <= threshold) {
                dstr.drawn();
                return dstr;
            }
        }
        
        destroyOperations[destroyOperations.length - 1].drawn();
        return destroyOperations[destroyOperations.length - 1];
    }


    private void initStrategies() {
        for (Destroy destroy : destroyOperations) {
            destroy.setP(1 / (double) destroyOperations.length);
        }
        for (Repair repair : repairOperations) {
            repair.setP(1 / (double) repairOperations.length);
        }
    }

}
