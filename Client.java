import java.net.*;
import java.io.*;

public class Client {
	// initialize socket and input output streams
	protected static Socket socket = null;
	protected static String line = "";
	protected static DataOutputStream write = null;
	protected static DataInputStream read = null;
	private static chatGUI gui;

	// constructor to put ip address and port
	public Client(String address, int port) throws IOException {
		// New Socket
		socket = new Socket(address, port);

		// Output to gui connect confirmation
		gui.log.setText(gui.log.getText() + "Connected\n\n");

		// sends output and receive input to/from the socket
		write = new DataOutputStream(socket.getOutputStream());
		read = new DataInputStream(socket.getInputStream());

		// Read in messages until an error occurs or connection is terminated
		boolean flag = true;
		while (flag) {
			try {
				gui.newMessage(read.readUTF());
			} catch (IOException i) {
				System.out.println(i);
				flag = false;
			}
		}

	}

	public static void main(String args[]) throws UnknownHostException, IOException {
		gui = new chatGUI();

		// Replace IP string with server IP address
		Client client = new Client("23.124.16.36", 5000);

	}

}