import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class chatGUI implements ActionListener, KeyListener {
	private JFrame frame = new JFrame();

	// Create chat log text area
	protected JTextArea log;
	private JScrollPane scroll;
	protected JTextField message;
	protected JTextField username;
	private JTextField textField;

	public chatGUI() {
		// Instantiate text area
		log = new JTextArea();
		log.setLineWrap(true);
		log.setText("Welcome to your conversation\n");
		log.setEditable(false);

		// Add log to scroll to add scrolling functionality
		scroll = new JScrollPane(log);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// Create send chat button
		JButton sendBtn = new JButton(new AbstractAction("send") {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Print own message for user
				log.setText(log.getText() + username.getText() + ": " + message.getText() + "\n");

				// Import text from gui into client
				Client.line = username.getText() + ": " + message.getText();

				// Send message to client for server to distribute
				try {
					Client.write.writeUTF(Client.line);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				message.setText("");
			}
		});
		sendBtn.setText("Send Message");

		// Create end chat button
		JButton endChatBtn = new JButton(new AbstractAction("end") {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Send string to trigger closing sequence
				try {
					Client.write.writeUTF("End Chat");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// Close client output stream
				try {
					Client.write.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Close client socket
				try {
					Client.socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Notify user connection has been terminated
				log.setText(log.getText() + "\nEnd Chat");
			}
		});
		endChatBtn.setText("End Session");

		// Create text field for users message and label
		message = new JTextField();
		message.setBounds(14, 114, 400, 30);
		message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Print own message for user
					log.setText(log.getText() + username.getText() + ": " + message.getText() + "\n");

					// Import text from gui into client
					Client.line = username.getText() + ": " + message.getText();

					// Send message to client for server to distribute
					try {
						Client.write.writeUTF(Client.line);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					message.setText("");
				}
			}
		});
		message.setPreferredSize(new Dimension(400, 30));
		JLabel messageLabel = new JLabel("Enter Message: ");
		messageLabel.setBounds(160, 89, 103, 14);

		// Create main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		mainPanel.setLayout(new GridLayout(4, 0));

		// Create panel to hold buttons
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());

		// Add buttons to button panel
		btnPanel.add(endChatBtn);
		btnPanel.add(sendBtn);

		// Create panel for users message
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(null);
		messagePanel.add(messageLabel);
		messagePanel.add(message);

		// Add textArea and btnPanel to main panel
		mainPanel.add(scroll);
		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(messagePanel);
		mainPanel.add(btnPanel);

		// for username
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(174, 11, 78, 14);
		messagePanel.add(lblUsername);

		username = new JTextField();
		username.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String un = username.getText();
			}
		});
		username.setBounds(14, 34, 400, 30);
		messagePanel.add(username);
		username.setColumns(10);

		// Set up main frame
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chat Application");
		frame.setVisible(true);
		frame.setSize(500, 700);
	}

	public void newMessage(String message) {
		// Displays new messages in chat log
		log.setText(log.getText() + message + "\n");
	}

	public void actionPerformed(ActionEvent e) {
	}

	public static void main(String[] args) {
		new chatGUI();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}