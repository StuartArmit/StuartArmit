import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;

public class Client {
	Frame frame;
	private int playerID;
	private int otherPlayer;
	private char token1;
	private char token2;


	private ClientConnection cc;

	// Creates game board and populates
	public void setUp() {
		frame = new Frame();
		frame.setVisible(true);
		frame.setLocation(0, 0);
		frame.pieceSetupP1(token1);
		frame.pieceSetupP2(token2);
		//frame.blockBoard();
	}

	public void connectToServer() {
		cc = new ClientConnection();
	}

	public void setUpButtons() {

	}

	// Client connection for send/receive ints and chars
	private class ClientConnection {
		private Socket s;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;

		public ClientConnection() {
			try {
				s = new Socket("localhost", 4999);
				dataIn = new DataInputStream(s.getInputStream());
				dataOut = new DataOutputStream(s.getOutputStream());
				playerID = dataIn.readInt();
				System.out.println("Connected to server as player " + playerID);
				token1 = dataIn.readChar();
				token2 = dataIn.readChar();


			} catch (IOException ex) {
				System.out.println("Client Socket Failure");
			}
		}


	}
	// each time a client is run a new Client obj connects to server and runs setup
	// method
	public static void main(String[] args) {
		Client c = new Client();
		c.connectToServer();
		c.setUp();
		

	}
}
