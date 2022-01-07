package test;

import java.io.*;
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

		// you may add default methods here
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;

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
			super("Please upload your local train CSV file.\n");}

		@Override
		public void execute() {
			dio.write(description);
			String fileName = "csvFile.txt";
			if(CreateFile(fileName)) {
				File csvFile = new File(fileName);
				FillCSV(fileName);
				dio.write("Upload complete.\n");
			}else{
				dio.write("hmmm we had a problem in you csvFile...");
			}
		}

		public boolean CreateFile(String FileName){
			try {
				File csvFile = new File(FileName);
				if (csvFile.createNewFile())
					return true;
				if(csvFile.exists())
					return true;
			} catch (IOException e) {
				System.out.println("Can't open csvFile");
				e.printStackTrace();
			}
			return false;
		}

		public void FillCSV(String fileName){
			FileIO csvWriter = new FileIO(fileName,fileName);
			String line = dio.readText();
			while(!line.equals("done")){
				csvWriter.write(line);
				csvWriter.write("\n");
				line = dio.readText();
			}
		}
	}

	public class AlgoSettings extends Command{

		public AlgoSettings() {
			super("algorithm settings");}

		@Override
		public void execute() {
			//need to put here the command activity.
		}
	}

	public class DetectAnomalies extends Command{

		public DetectAnomalies() {
			super("detect anomalies");}

		@Override
		public void execute() {
			//need to put here the command activity.
		}
	}

	public class DisplayResults extends Command{

		public DisplayResults() {
			super("display results");}

		@Override
		public void execute() {
			//need to put here the command activity.
		}
	}

	public class UploadAndAnalize extends Command{

		public UploadAndAnalize() {
			super("upload anomalies and analyze results");}

		@Override
		public void execute() {
			//need to put here the command activity.
		}
	}

	public class Exit extends Command{

		public Exit() {
			super("exit");}

		@Override
		public void execute() {
			//need to put here the command activity.
		}
	}
	// implement here all other commands
	
}
