package socket;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
	Socket socket;
	BufferedReader in;
	PrintWriter out;

	public Client() {
		try {
			ExecutorService exec = Executors.newFixedThreadPool(5);
			System.out.println("Try to Connect to 10.28.1.7:8889");
			socket = new Socket("127.0.0.1", 8889);
			System.out.println("The Server Connected!");
			System.out.println("Please enter some Character:");
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			// mtthread

				exec.execute(new mothread(in));
				// out
				exec.execute(new mtthread(out));

		} catch (IOException e) {
			out.println("Wrong");
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}