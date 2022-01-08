package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;
import java.io.*;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
			SocketIO sio = new SocketIO(inFromClient,outToClient);
			CLI cli = new CLI(sio);
			cli.start();
			sio.close();
	}

	public class SocketIO implements DefaultIO{

		BufferedReader in;
		PrintWriter out;

		public SocketIO(InputStream inputStream, OutputStream outputStream) {
			in = new BufferedReader(new InputStreamReader(inputStream));
			out = new PrintWriter(new OutputStreamWriter(outputStream));
		}

		@Override
		public String readText() {
			try{
				return in.readLine();
			} catch (IOException e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void write(String text) {
			out.print(text);
			out.flush();
		}

		@Override
		public float readVal() {
			try {
				return Float.parseFloat(in.readLine());
			} catch(IOException e){
				e.printStackTrace();
			}
			return 1;
		}

		@Override
		public void write(float val) {
			out.print(val);
			out.flush();
		}

		@Override
		public boolean CreateFile(String FileName) {
			return DefaultIO.super.CreateFile(FileName);
		}

		@Override
		public int FillCSV(String fileName) {
			return DefaultIO.super.FillCSV(fileName);
		}

		public void close(){
			try{
				in.close();
			} catch(IOException e){
				e.printStackTrace();
			}
			out.close();
		}
	}
}