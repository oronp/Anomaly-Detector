package test;

import java.util.ArrayList;
import java.util.Scanner;
import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;

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
		while(!action.equals("6")) {
			commands.get(Integer.parseInt(action)).execute();
			commands.get(0).execute();
			action = dio.readText();
		}
		commands.get(6).execute();
	}
}
