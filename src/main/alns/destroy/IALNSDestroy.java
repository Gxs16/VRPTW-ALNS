package main.alns.destroy;

import main.algrithm.MyALNSSolution;
import main.alns.operation.IALNSOperation;

public interface IALNSDestroy extends IALNSOperation {

    MyALNSSolution destroy(MyALNSSolution s, int nodes) throws Exception;

}
