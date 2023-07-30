package test;

import main.algrithm.SolutionChecker;
import main.algrithm.Solver;
import main.alns.Config;
import main.domain.Instance;
import main.domain.Solution;

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

    public static void main(String[] args) throws Exception {

        String[] instances = { "c1_4_2" };
        String[][] result = new String[instances.length][];

        for (int j = 0; j < instances.length; j = j + 1) {
            result[j] = solve(
                    instances[j],                    //需要测试的算例
                    "Homberger",                      // 算例类型,输入Homberger或Solomon，注意大写
                    400,                        //客户点数量，Solomon可选择 25,50,100，Homberger可选择200，400
                    Config.DEFAULT);
        }
    }

    //  solve函数，输出解 输入变量：算例名，客户数，
    private static String[] solve(String name, String instanceType, int size, Config c) throws Exception {

        // 输入Solomon算例
        Instance instance = new Instance(size, name, instanceType);
        // 检查结果
        SolutionChecker solutionChecker = new SolutionChecker(instance);
        // 解决策略
        Solver solver = new Solver();
        // 初始解
        Solution is = solver.getInitialSolution(instance);
        // 满意解
        Solution ims = solver.improveSolution(is, c, instance);
        logger.info(ims.toString());
        logger.info(solutionChecker.check(ims));

        return new String[]{String.valueOf(ims.getTotalCost()), String.valueOf(ims.testTime)};
    }

}
