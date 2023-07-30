package main.alns.operation.destroy;

import main.algrithm.ALNSSolution;
import main.alns.operation.Operation;

public interface Destroy extends Operation {

    ALNSSolution destroy(ALNSSolution s, int nodes) throws Exception;

}
