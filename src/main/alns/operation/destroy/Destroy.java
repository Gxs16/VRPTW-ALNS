package main.alns.operation.destroy;

import main.algrithm.MyALNSSolution;
import main.alns.operation.Operation;

public interface Destroy extends Operation {

    MyALNSSolution destroy(MyALNSSolution s, int nodes) throws Exception;

}
