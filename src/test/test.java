package test;

import main.algrithm.CheckSolution;
import main.algrithm.Solution;
import main.algrithm.Solver;
import main.alns.config.ALNSConfiguration;
import main.alns.config.ControlParameter;
import main.alns.config.IALNSConfig;
import main.domain.Instance;

import java.util.logging.Logger;

public class test {
    private static final String[] SOLOMON_ALL = new String[]{
            "C101", "C102", "C103", "C104", "C105", "C106", "C107", "C108", "C109", "C201", "C202", "C203", "C204", "C205", "C206", "C207", "C208",
            "R101", "R102", "R103", "R104", "R105", "R106", "R107", "R108", "R109", "R110", "R111", "R112", "R201", "R202", "R203", "R204", "R205", "R206", "R207", "R208", "R209", "R210", "R211",
            "RC101", "RC102", "RC103", "RC104", "RC105", "RC106", "RC107", "RC108", "RC201", "RC202", "RC203", "RC204", "RC205", "RC206", "RC207", "RC208"
    };
    static String[] SOLOMON_CLUSTERED = new String[]{"C101", "C102", "C103", "C104", "C105", "C106", "C107", "C108", "C109", "C201", "C202", "C203", "C204", "C205", "C206", "C207", "C208"};
    static String[] SOLOMON_RANDOM = new String[]{"R101", "R102", "R103", "R104", "R105", "R106", "R107", "R108", "R109", "R110", "R111", "R112", "R201", "R202", "R203", "R204", "R205", "R206", "R207", "R208", "R209", "R210", "R211",};
    static String[] SOLOMON_CLUSTERRANDOM = new String[]{"RC101", "RC102", "RC103", "RC104", "RC105", "RC106", "RC107", "RC108", "RC201", "RC202", "RC203", "RC204", "RC205", "RC206", "RC207", "RC208"};
    static String[] VRPFD_INSTANCES = new String[]{"C108", "C206", "C203", "R202", "R207", "R104", "RC202", "RC205", "RC208"};
    static String[] Homberger_200 = new String[] {"C1_2_1", "C1_2_2", "C1_2_3", "C1_2_4"};
    private static final Logger logger = Logger.getLogger(test.class.getSimpleName());

    public static void main(String args[]) {

        String[] instances = { "C1_4_1" };
        String[][] result = new String[instances.length][];

        for (int j = 0; j < instances.length; j = j + 1) {
            try {
                result[j] = solve(
                        instances[j],                    //需要测试的算例
                        "Homberger",                      // 算例类型,输入Homberger或Solomon，注意大写
                        400,                        //客户点数量，Solomon可选择 25,50,100，Homberger可选择200，400
                        ALNSConfiguration.DEFAULT,    //ALNS相关参数
                        new ControlParameter(
                                false,                //历史满意解、当前满意解、新解的时序图，收敛效果展示
                                false,                //ALNS算子时序图
                                false                //生成解对应效果图（针对每次迭代的历史满意解）
                        ));
                //printToCSV("ALNS TEST", result, instances.length);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    //  solve函数，输出解 输入变量：算例名，客户数，
    private static String[] solve(String name, String instanceType, int size, IALNSConfig c, ControlParameter cp) throws Exception {

        // 输入Solomon算例
        Instance instance = new Instance(size, name, instanceType);
        // 检查结果
        CheckSolution checkSolution = new CheckSolution(instance);
        // 解决策略
        Solver solver = new Solver();
        // 初始解
        Solution is = solver.getInitialSolution(instance);
        // 满意解
        Solution ims = solver.improveSolution(is, c, cp, instance);
        logger.info(ims.toString());
        logger.info(checkSolution.Check(ims));

        return new String[]{String.valueOf(ims.getTotalCost()), String.valueOf(ims.testTime)};
    }

}
