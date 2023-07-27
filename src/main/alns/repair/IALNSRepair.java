package main.alns.repair;

import main.algrithm.MyALNSSolution;
import main.alns.operation.IALNSOperation;

public interface IALNSRepair extends IALNSOperation {

    MyALNSSolution repair(MyALNSSolution from);
}
