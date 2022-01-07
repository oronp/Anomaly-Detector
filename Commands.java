package test;

import java.io.*;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Commands {

	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);
		default public boolean CreateFile(String FileName){
			try {
				File csvFile = new File(FileName);
				if (csvFile.createNewFile())
					return true;
				if(csvFile.exists())
					return true;
			} catch (IOException e) {
				System.out.println("Can't open file");
				e.printStackTrace();
			}
			return false;
		}
		default public int FillCSV(String fileName) {
			int totalLines = 0;
			FileIO csvWriter = new FileIO(fileName, fileName);
			String line = this.readText();
			while (!line.equals("done")) {
				csvWriter.write(line);
				csvWriter.write("\n");
				line = this.readText();
				totalLines++;
				if (totalLines % 100 == 0)
					csvWriter.out.flush();
			}
			csvWriter.close();
			return totalLines;
		}
		// you may add default methods here
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	SimpleAnomalyDetector anomalyDetector = new SimpleAnomalyDetector();

	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		// implement here whatever you need
		
	}
	
	private  SharedState sharedState=new SharedState();

	
	// Command abstract class
	public abstract class Command{
		protected String description;

		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}
	
	// Command class for example:
	public class ExampleCommand extends Command{

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}		
	}

	//Command for the openning and menu for the user.
	public class PrintMenuCommand extends Command{

		public PrintMenuCommand() {
			super("Welcome to the Anomaly Detection Server.\n" +
				"Please choose an option:\n" +
				"1. upload a time series csv file\n" +
				"2. algorithm settings\n" +
				"3. detect anomalies\n" +
				"4. display results\n" +
				"5. upload anomalies and analyze results\n" +
				"6. exit\n");}

		@Override
		public void execute() {
			dio.write(description);
		}
	}

	public class UploadTime extends Command{

		public UploadTime() {
			super("Uploading the test and train anomalies lists.\n");}

		@Override
		public void execute() {
			dio.write("Please upload your local train CSV file.\n");
			String trainFile = "anoamlyTrain.csv";
			String testFile = "anomalyTest.csv";
			if(dio.CreateFile(trainFile) && dio.CreateFile(testFile)) {
				dio.FillCSV(trainFile);
				dio.write("Upload complete.\nPlease upload your local test CSV file.\n");
				dio.FillCSV(testFile);
				dio.write("Upload complete.\n");
			}else{
				dio.write("hmmm we had a problem in you csvFile...");
			}
		}
	}

	public class AlgoSettings extends Command{

		public AlgoSettings() {
			super("algorithm settings");}

		@Override
		public void execute() {
			dio.write("The current correlation threshold is ");
			dio.write(anomalyDetector.ThresHold + "\n" + "Type a new threshold\n");
			float userChoice = dio.readVal();
			if(userChoice >= 0 && userChoice <= 1){
				anomalyDetector.ThresHold = userChoice;
			}	else	{
				dio.write("please choose a value between 0 and 1.");
			}
		}
	}

	public class DetectAnomalies extends Command{

		public DetectAnomalies() {
			super("detect anomalies");}

		@Override
		public void execute() {
			TimeSeries trainTable = new TimeSeries("anoamlyTrain.csv");
			TimeSeries testTable = new TimeSeries("anomalyTest.csv");
			anomalyDetector.learnNormal(trainTable);
			anomalyDetector.detect(testTable);
			dio.write("anomaly detection complete.\n");
		}
	}

	public class DisplayResults extends Command{

		public DisplayResults() {
			super("display results");}

		@Override
		public void execute() {
			for(int i = 0; i < anomalyDetector.anomalyReports.size(); i++){
				dio.write(anomalyDetector.anomalyReports.get(i).timeStep + "\t");
				dio.write(anomalyDetector.anomalyReports.get(i).description + "\n");
			}
			dio.write("Done\n");
		}
	}

	public class UploadAndAnalize extends Command{

		public UploadAndAnalize() {
			super("upload anomalies and analyze results");}

		@Override
		public void execute() {
			dio.write("Please upload your local anomalies file.\n");
			//float P = StraightReports();//P => number of anomaly reports.
			float N = anomalyDetector.totalTimeSteps - anomalyDetector.anomalyReports.size();//total time without reports
			ArrayList<Long> inputValue = inputToValue();
			float truePositive = TruePositive(inputValue);
			float falsePositive = (inputValue.size()/2) - truePositive;
			float P = inputValue.size()/2;
			dio.write("Upload complete.\n");
			DecimalFormat df = new DecimalFormat("#.###");
			//df.setRoundingMode(RoundingMode.CEILING);
			dio.write("True Positive Rate: " + df.format(truePositive/P) + "\n");
			dio.write("False Positive Rate: " + df.format(falsePositive/N) + "\n");
		}

		public ArrayList<Long> inputToValue(){
			String[] strings = dio.readText().split(",");
			ArrayList<Long> timeSteps = new ArrayList<>();
			while(!strings[0].equals("done")){
				timeSteps.add(Long.parseLong(strings[0]));
				timeSteps.add(Long.parseLong(strings[1]));
				strings = dio.readText().split(",");
			}
			return timeSteps;
		}//even place means start and odd place means ends.

		public int TruePositive(ArrayList<Long> input){
			int numOfFPs = 0;
			long current;
			for(int i = 0; i < input.size(); i+=2){
				for(int j = 0; j < anomalyDetector.anomalyReports.size(); j++){
					current = anomalyDetector.anomalyReports.get(j).timeStep;
					if(current > input.get(i) && current < input.get(i+1)) {
						numOfFPs++;
						break;
					}
				}
			}
			return numOfFPs;
		}

		public int StraightReports(){
			if(anomalyDetector.anomalyReports.size() == 0)
				return 0;
			int straightReports = 1;
			String current = anomalyDetector.anomalyReports.get(0).description;
			for(int i = 1; i < anomalyDetector.anomalyReports.size(); i++){
				if(!anomalyDetector.anomalyReports.get(i).description.equals(current)
				|| anomalyDetector.anomalyReports.get(i).timeStep != (anomalyDetector.anomalyReports.get(i-1).timeStep + 1)) {
					straightReports++;
					current = anomalyDetector.anomalyReports.get(i).description;
				}
			}
			return straightReports;
		}//checks the number of straight reports -> straight reports means reports that
		//has the same headline and keeps the time steps.
	}

	public class Exit extends Command{

		public Exit() {
			super("exit");}

		@Override
		public void execute() {

		}
	}
}
