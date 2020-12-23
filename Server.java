import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.io.*;

public class Server {
	// initialize socket and mutex
	public static Socket socket = null;
	public static ServerSocket se = null;
	private static Semaphore mutex = new Semaphore(1);
	static int i = 0;

	// Array to keep track of all clients
	private static ArrayList<ClientHandler> chArr = new ArrayList<ClientHandler>();

	public class ClientHandler extends Thread {
		Socket s;
		final DataInputStream read;
		final DataOutputStream write;
		String name;

		public ClientHandler(Socket s, String name, DataInputStream din, DataOutputStream out) {
			this.s = s;
			this.read = din;
			this.name = name;
			this.write = out;
		}

		public void run() {
			// reads message from client until "Over" is sent
			String line = "";

			boolean flag = true;
			while (!line.equals("End Chat") && flag) {
				try {
					line = read.readUTF();
					System.out.println(line);

					// Loop through all other clients and send message to them
					for (int i = 0; i < chArr.size(); i++) {
						if (!(chArr.get(i).name == name)) {
							chArr.get(i).write.writeUTF(line);
						}
					}

				} catch (IOException i) {
					System.out.println(i);
					flag = false;
				}
			}

			System.out.println("Closing connection");

			// close connection
			try {
				socket.close();
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		se = new ServerSocket(5000);
		// starts server and waits for a connection
		while (true) {
			try {
				mutex.acquire();
				System.out.println("Waiting for a client ...");
				socket = se.accept();
				System.out.println("Client accepted");

				// takes input from the client socket
				DataInputStream read = new DataInputStream(socket.getInputStream());
				DataOutputStream write = new DataOutputStream(socket.getOutputStream());
				Server y = new Server();
				ClientHandler x = y.new ClientHandler(socket, "client" + i, read, write);
				Thread t = new Thread(x);
				t.start();
				i++;

				// Add client handler to array
				chArr.add(x);
			} catch (IOException i) {
				System.out.println(i);
			} finally {
				mutex.release();
			}

		}
	}
}
