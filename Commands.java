package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
				"6. exit");}

		@Override
		public void execute() {
			dio.write(description);
		}
	}
	
	// implement here all other commands
	
}
