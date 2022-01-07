package test;

import java.util.ArrayList;
import java.util.Scanner;
import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	SimpleAnomalyDetector anomalyDetector = new SimpleAnomalyDetector();
	
	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();

		commands.add(c.new PrintMenuCommand());
		commands.add(c.new UploadTime());
		commands.add(c.new AlgoSettings());
		commands.add(c.new DetectAnomalies());
		commands.add(c.new DisplayResults());
		commands.add(c.new UploadAndAnalize());
		commands.add(c.new Exit());
	}
	
	public void start() {
		commands.get(0).execute();
		String action = dio.readText();
		//action = "1";
		while(!action.equals("6")) {
			switch (action) {
				case "1":
					commands.get(1).execute();
					commands.get(0).execute();
					break;
				case "2":
					commands.get(2).execute();
					commands.get(0).execute();
					break;
				case "3":
					commands.get(3).execute();
					commands.get(0).execute();
					break;
				case "4":
					commands.get(4).execute();
					commands.get(0).execute();
					break;
				case "5":
					commands.get(5).execute();
					commands.get(0).execute();
					break;
				case "6":
					commands.get(6).execute();
					break;
				default:
			}
			action = dio.readText();
		}
	}
}
