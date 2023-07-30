package main.alns.operation.repair;

import main.algrithm.ALNSSolution;
import main.alns.operation.Operation;

public interface Repair extends Operation {

    ALNSSolution repair(ALNSSolution from);
}
