package Client;

import static server.Server.PORT;
import static server.Server.receiveMsg;
import static server.Server.sendMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import contacts.Entity;
import contacts.WalkUtil;

public class Client {

	private static final int DELAY = 4000;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private volatile boolean terminate;
	private String id;

	void start() throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket("localhost", PORT);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream());
		String helo = receiveMsg(reader);
		String id = helo.substring(helo.indexOf(" ") + 1);

		Thread sendThread = new Thread(() -> {
			try {
				while (!terminate) {
					try {
						Thread.sleep(DELAY);

						// DO SOMETHING WITH ENTITITIES ?
						// SEND POSITION DATA???
						Entity me = Entity.of(Integer.parseInt(id), 100, 100);
						me = WalkUtil.walk(me);
						sendMsg(writer, me.getX() + " " + me.getY() + " " + id);
					} catch (InterruptedException e) {
					} catch (IOException e) {
					}
				}
				sendMsg(writer, "END " + id);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("SENDER TERMINATED");
		});
		sendThread.start();

		Thread rcvThread = new Thread(() -> {
			try {
				while (!terminate) {
					// GET CONTACTS OR SOMETHING ??
					// DONT KNOW ??
					// PANIC
					// PACEHOLDER HERE
					String recv = receiveMsg(reader);
					System.out.println(recv);
				}
			} catch (IOException e) {
			}

			System.out.println("RECEIVER TERMINATED");
		});
		rcvThread.start();

		// END
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		terminate = true;
		sc.close();
		rcvThread.join();
		sendThread.join();
		System.out.println("CLIENT TERMINATED");

	}

}
