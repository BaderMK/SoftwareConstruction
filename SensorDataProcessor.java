package sensordataprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author bader
 */
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
        double[][][] processedData = new double[data.length][data[0].length][data[0][0].length];
        BufferedWriter out;
// Write racing stats data into a file
        try {
            out = new BufferedWriter(new FileWriter("RacingStatsData.txt"));
            for (int firstArrayIndex = 0; firstArrayIndex < data.length; firstArrayIndex++) {
                for (int secondArrayIndex = 0; secondArrayIndex < data[0].length; secondArrayIndex++) {
                    for (int thirdArrayIndex = 0; thirdArrayIndex < data[0][0].length; thirdArrayIndex++) {
                        processedData[firstArrayIndex][secondArrayIndex][thirdArrayIndex] = 
                                data[firstArrayIndex][secondArrayIndex][thirdArrayIndex] / divisor
                                - Math.pow(limit[firstArrayIndex][secondArrayIndex], 2.0);
                        if (calculateAverageData(processedData[firstArrayIndex][secondArrayIndex]) 
                                > 10 && calculateAverageData(processedData[firstArrayIndex][secondArrayIndex])
                                < 50) {
                            break;
                        } else if (Math.max(data[firstArrayIndex][secondArrayIndex][thirdArrayIndex], 
                                processedData[firstArrayIndex][secondArrayIndex][thirdArrayIndex])
                                > data[firstArrayIndex][secondArrayIndex][thirdArrayIndex]) {
                            break;
                        } else if (Math.pow(Math.abs(data[firstArrayIndex][secondArrayIndex][thirdArrayIndex]), 3)
                                < Math.pow(Math.abs(processedData[firstArrayIndex][secondArrayIndex][thirdArrayIndex]), 3)
                                && calculateAverageData(data[firstArrayIndex][secondArrayIndex]) 
                                < processedData[firstArrayIndex][secondArrayIndex][thirdArrayIndex] 
                                && (firstArrayIndex + 1)
                                * (secondArrayIndex + 1) > 0) {
                            processedData[firstArrayIndex][secondArrayIndex][thirdArrayIndex] *= 2;
                        } else {
                            continue;
                        }
                    }
                }
            }
            for (int i = 0; i < processedData.length; i++) {
                for (int j = 0; j < processedData[0].length; j++) {
                    out.write(processedData[i][j] + "\t");
                }
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error= " + e);
        }
    }
}
