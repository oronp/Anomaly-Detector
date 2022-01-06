package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import test.Commands.DefaultIO;

public class ScreenIO implements DefaultIO {

    Scanner in;
    PrintWriter out;
    public ScreenIO() {
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);
    }

    @Override
    public String readText() {
        return in.nextLine();
    }

    @Override
    public void write(String text) {
        out.print(text);
    }

    @Override
    public float readVal() {
        return in.nextFloat();
    }

    @Override
    public void write(float val) {
        out.print(val);
    }

    public void close() {
        in.close();
        out.close();
    }
}
