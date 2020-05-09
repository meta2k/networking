package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.regex.Pattern;

public class Server {

	public static final int PORT = 5678;
	public static int ID = 1;

	private ServerSocket server;
	private volatile int count;
	private volatile boolean terminate = false;

	public Server() {
		this.count = 0;
	}

	public void start() {
		System.out.println("Server started");
		try {
			server = new ServerSocket(PORT);
			server.setSoTimeout(2000);
			while (!terminate) {
				try {
					Socket clientSocket = server.accept();
					System.out.println("Client accepted.");
					new Thread(new ClientHandler(ID++, clientSocket)).start();
				} catch (SocketTimeoutException e) {
				}
			}
			System.out.println("Server terminated");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {

			}
		}
	}

	private class ClientHandler implements Runnable {
		private final int id;
		private final Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private volatile boolean terminateClient = false;

		ClientHandler(int id, Socket socket) {
			this.id = id;
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				//BIG TODO HERE
				//dont know what to send ???
				sendMsg(writer, "Hello " + id + " beep boop beep" );
				while(!terminateClient) {
					String msg = receiveMsg(reader);
					System.out.println("recieved: " + msg);				
				//TODO implement method to check if coordiante
					if(isCordinate(msg)) {
					//TODO do something with coodinate ??
					//check contact maybe ?
					//NO IDEA
					//MANY COMMENTS FOR VISIBILITY
					// LOL
				} else {
					sendMsg(writer, "BYE" + id);
					terminateClient = true;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	}

	// TODO: check
	// regex 100% false
	public static boolean isCordinate(String s) {
		return !Pattern.matches("[a-zA-Z]+", s);
	}

	public static void sendMsg(PrintWriter out, String m) throws IOException {
		out.print(m);
		out.print("\r\n");
		out.flush();
	}

	public static String receiveMsg(BufferedReader in) throws IOException {
		String m = in.readLine();
		return m;
	}

	public void terminate() {
		terminate = true;
	}

}
