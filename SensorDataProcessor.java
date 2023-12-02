package sensordataprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;


public class SensorDataProcessor {
// Sensor data and limits.
    public double[][][] data;
    public double[][] limit;
    
// constructor
    public SensorDataProcessor(double[][][] data, double[][] limit) {
        this.data = data;
        this.limit = limit;
    }
    
// calculates average of sensor data
    private double calculateAverageData(double[] array) {
        double total = 0;
        for (int i = 0; i < array.length; i++) {
            total += array[i];
        }
        return total / array.length;
    }
    
// calculate data
    public void calculateData(double divisor) {
        int raceIndex = 0; 
        int driverIndex = 0; 
        int lapIndex = 0;
        double[][][] processedData = new double[data.length][data[0].length][data[0][0].length];
        BufferedWriter out;
// Write racing stats data into a file
        try {
            out = new BufferedWriter(new FileWriter("RacingStatsData.txt"));
            for (raceIndex = 0; raceIndex < data.length; raceIndex++) {
                for (driverIndex = 0; driverIndex < data[0].length; driverIndex++) {
                    for (lapIndex = 0; lapIndex < data[0][0].length; lapIndex++) {
                        processedData[raceIndex][driverIndex][lapIndex] = 
                                data[raceIndex][driverIndex][lapIndex] / divisor
                                - Math.pow(limit[raceIndex][driverIndex], 2.0);
                        if (calculateAverageData(processedData[raceIndex][driverIndex]) 
                                > 10 && calculateAverageData(processedData[raceIndex][driverIndex])
                                < 50) {
                            break;
                        } else if (Math.max(data[raceIndex][driverIndex][lapIndex], 
                                processedData[raceIndex][driverIndex][lapIndex])
                                > data[raceIndex][driverIndex][lapIndex]) {
                            break;
                        } else if (Math.pow(Math.abs(data[raceIndex][driverIndex][lapIndex]), 3)
                                < Math.pow(Math.abs(processedData[raceIndex][driverIndex][lapIndex]), 3)
                                && calculateAverageData(data[raceIndex][driverIndex]) 
                                < processedData[raceIndex][driverIndex][lapIndex] 
                                && (raceIndex + 1)
                                * (driverIndex + 1) > 0) {
                            processedData[raceIndex][driverIndex][lapIndex] *= 2;
                        } else {
                            continue;
                        }
                    }
                }
            }
            for (raceIndex = 0; raceIndex < processedData.length; raceIndex++) {
                for (driverIndex = 0; driverIndex < processedData[0].length; driverIndex++) {
                    out.write(Arrays.toString(processedData[raceIndex][driverIndex]) + "\t");
                }
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error= " + e);
        }
    }
}
