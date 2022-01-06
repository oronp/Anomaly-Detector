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
		// example: commands.add(c.new ExampleCommand());
		// implement
		commands.add(c.new PrintMenuCommand());
	}
	
	public void start() {
		commands.get(0).execute();
	}
}
