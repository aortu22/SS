//package main;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class AutomatedTauTest {
//    public static void main(String[] args) {
//        String csvFilePath = "table.csv"; // Replace with the actual path to your CSV file
//
//        try (FileReader reader = new FileReader(csvFilePath);
//             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
//
//            for (CSVRecord csvRecord : csvParser) {
//                String P_id = csvRecord.get("P_id");
//                float Vd_max = Float.parseFloat(csvRecord.get("Vd_max [m/s]"));
//                float Da = Float.parseFloat(csvRecord.get("Da [m]"));
//                float Time_a = Float.parseFloat(csvRecord.get("Time a"));
//                float Time_b = Float.parseFloat(csvRecord.get("Time b"));
//                int Frame_a = Integer.parseInt(csvRecord.get("Frame a"));
//                int Frame_b = Integer.parseInt(csvRecord.get("Frame b"));
//                float Y_a = Float.parseFloat(csvRecord.get("Y_a"));
//                float X_a = Float.parseFloat(csvRecord.get("X_a"));
//                float Y_b = Float.parseFloat(csvRecord.get("Y_b"));
//                float X_b = Float.parseFloat(csvRecord.get("X_b"));
//                double dTEscritura = 4/30;
//                double dt=0.0666666;
//                double rMin=0.12;
//                double rMax=0.3;
//                double B=1;
//                for(float tau = 0.3F; tau<3; tau+=0.3){
//                    //run simulation with params
//
//                    //compare velocities at different times with real sim
//                    calculateError("","./output_with_velocity.txt","",P_id,tau,Frame_a,Frame_b);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void calculateError(String simResultsFile, String originalFile,String output, String P_id, float tau,int frameStart,int frameEnd) {
//
//        List<Double> velocityDifferences = new ArrayList<>();
//
//        try (BufferedReader simResultsReader = new BufferedReader(new FileReader(simResultsFile));
//             BufferedReader outputWithVelocityReader = new BufferedReader(new FileReader(originalFile));
//             BufferedWriter resultFileWriter = new BufferedWriter(new FileWriter(output, true))) { // Append mode
//                String simResultLine;
//            String outputWithVelocityLine = outputWithVelocityReader.readLine(); // Read the header line
//
//            while ((simResultLine = simResultsReader.readLine()) != null) {
//                String[] simResultParts = simResultLine.split("\t");
//                double simResultTime = Double.parseDouble(simResultParts[0]);
//                double simResultVelocity = Double.parseDouble(simResultParts[1]);
//                if(outputWithVelocityLine == null){
//                    break;
//                }
//                while (outputWithVelocityLine != null) {
//                    String[] outputWithVelocityParts = outputWithVelocityLine.split("\t");
//
//                    int frame = Integer.parseInt(outputWithVelocityParts[0]);
//                    String id = outputWithVelocityParts[3];
//                    double velocity = Double.parseDouble(outputWithVelocityParts[5]);
//
//                    if (!id.equals(P_id) || frame < frameStart ||
//                            frame > frameEnd) {
//                        outputWithVelocityLine = outputWithVelocityReader.readLine();
//                    } else {
//                        double velocityDifference = Math.abs(simResultVelocity - velocity);
//                        velocityDifferences.add(velocityDifference);
//                        break;
//                    }
//                }
//            }
//
//            // Calculate the median error
//            Collections.sort(velocityDifferences);
//            int middle = velocityDifferences.size() / 2;
//            double medianError;
//            if (velocityDifferences.size() % 2 == 0) {
//                medianError = (velocityDifferences.get(middle - 1) + velocityDifferences.get(middle)) / 2.0;
//            } else {
//                medianError = velocityDifferences.get(middle);
//            }
//            // Append the result to the output file
//            resultFileWriter.write("tau "+tau+"\n");
//            resultFileWriter.write(medianError + "\n");
//            System.out.println("Median Velocity Difference: " + medianError);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
