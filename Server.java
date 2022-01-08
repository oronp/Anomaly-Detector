package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {

	//ClientHandler interface.
	public interface ClientHandler{
		void handleClient(InputStream inFromClient, OutputStream outToClient);
	}

	//variables
	private volatile boolean stop;
	private int numOfClients = 1;

	//Constructor and functions.
	public Server() {
		stop=false;
	}


	private void startServer(int port, ClientHandler ch){
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while (!stop) {
				try {
					Socket client = server.accept();
					try {
						ch.handleClient(client.getInputStream(), client.getOutputStream());
						client.getInputStream().close();
						client.getOutputStream().close();
						client.close();
					}catch (IOException e){/*somthing here??*/}
				} catch (SocketTimeoutException e) {/*somthing here??*/}
			}
			server.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}

	public void stop() {
		stop=true;
	}
}