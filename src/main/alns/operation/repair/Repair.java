package main.alns.operation.repair;

import main.algrithm.MyALNSSolution;
import main.alns.operation.Operation;

public interface Repair extends Operation {

    MyALNSSolution repair(MyALNSSolution from);
}
