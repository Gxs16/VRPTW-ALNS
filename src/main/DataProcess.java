package main;

import main.domain.Instance;
import main.domain.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataProcess {
    public static String getFilePath(String type, int size, String name) {
        if (type.equals("Solomon"))
            return "./instances" + "/solomon" + "/solomon_" + size + "/" + name + ".txt";
        else if (type.equals("Homberger"))
            return "./instances" + "/homberger" + "/homberger_" + size + "/" + name + ".txt";
        else {
            return "";
        }
    }

    public static void readLocalFile(Instance instance, String inputPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputPath));

        String line = "";

        for (int i = 0; i < 5; i++)
            line = br.readLine();

        String[] tokens = line.split("\\s+");
        instance.setVehicleNr(Integer.parseInt(tokens[1]));
        instance.setVehicleCapacity(Integer.parseInt(tokens[2]));

        for (int i = 0; i < 4; i++)
            br.readLine();

        line = br.readLine();
        while (line != null) {
            tokens = line.split("\\s+");
            if (tokens.length == 0) break;
            Node customer = new Node(Integer.parseInt(tokens[1]),
                    Double.parseDouble(tokens[2]),
                    Double.parseDouble(tokens[3]),
                    Double.parseDouble(tokens[4]),
                    Double.parseDouble(tokens[5]),
                    Double.parseDouble(tokens[6]),
                    Double.parseDouble(tokens[7]));
            instance.getCustomers().add(customer);
            line = br.readLine();
        }
        br.close();

    }

    public static void calDistanceMatrix(Instance instance) {
        instance.distanceMatrix = new double[instance.getCustomerNr()][instance.getCustomerNr()];
        for (int i = 0; i < instance.getCustomerNr(); i++) {
            Node n1 = instance.getCustomers().get(i);

            for (int j = 0; j < instance.getCustomerNr(); j++) {
                Node n2 = instance.getCustomers().get(j);

                instance.distanceMatrix[i][j] = Math.round ( Math.sqrt(Math.pow(n1.getX() - n2.getX(), 2) +
                        Math.pow(n1.getY() - n2.getY(), 2)) * 100 ) / 100.0;
            }
        }
    }

    public static Instance initInstance(String type, int size, String name) throws IOException {
        Instance instance = new Instance();
        String inputFilePath = getFilePath(type, size, name);
        readLocalFile(instance, inputFilePath);
        calDistanceMatrix(instance);
        return instance;
    }
}
