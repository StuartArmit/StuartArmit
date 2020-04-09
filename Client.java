import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements ActionListener {
	Frame frame;
	MatrixButton matrixButton;
	private static int rows = 8;
	private static int columns = 8;
	private static int[][] clientData = new int[rows][columns];
	private int playerID;

	private int turnsMade = 0;
	private ClientConnection cc;

	// Starting population script for Client side draughts data,
	// duplication of server method as data is not sent across until a move is made.
	public void clientDataSetup() {
		for (int j = 0; j < (8); j += 2) {
			clientData[5][j] = 1;
			clientData[7][j] = 1;
		}
		for (int j = 1; j < (8); j += 2) {
			clientData[6][j] = 1;
		}
		for (int j = 1; j < (8); j += 2) {
			clientData[0][j] = 2;
			clientData[2][j] = 2;
		}
		for (int j = 0; j < (8); j += 2) {
			clientData[1][j] = 2;
		}
	}

	// Creates game board and populates
	public void setUp() {
		frame = new Frame();
		frame.setVisible(true);
		frame.setLocation(100, 100);
		clientDataSetup();
		dataUpdate();

		if (playerID == 1) {
			System.out.println("you are player1, you go first");
			frame.playBoard();
		} else {
			System.out.println("you are player2, please wait");
			// frame.blockBoard();
			Thread t = new Thread(new Runnable() {
				public void run() {
					releaseTurn();

				}
			});
			t.start();
		}
	}
	
	// Click determines row and col data to send
	public void actionPerformed(ActionEvent ae) {
		frame.playBoard();
		int r = ((MatrixButton) ae.getSource()).getRow();
		int c = ((MatrixButton) ae.getSource()).getCol();
		System.out.println(r + "  " + c);
		frame.move(playerID, r, c);
		frame.removePiece(playerID, r, c);

		storeDeletedPieces(r, c);
		cc.sendButtonPos(r, c);
		turnsMade++;
		System.out.println("turns: " + turnsMade);

		Thread t = new Thread(new Runnable() {
			public void run() {
				releaseTurn();
				frame.playBoard();
			}
		});
		t.start();
	}

	// waits for other player to play before releasing
	public void releaseTurn() {

		cc.receiveDataPos();
		frame.freeBoard();
	}

	public void storeDeletedPieces(int r, int c) {
		clientData[r][c] = 0;
	}

	// checks for updates and writes to gui
	public void dataUpdate() {
		for (int i = 0; i < clientData.length; i++) {
			for (int j = 0; j < clientData.length; j++) {
				if (clientData[i][j] == 1) {
					frame.placePiece(1, i, j);
				} else if (clientData[i][j] == 2) {
					frame.placePiece(2, i, j);
				}
			}
		}
	}

	public void connectToServer() {
		cc = new ClientConnection();
	}

	// Client connection for send/receive int data
	private class ClientConnection {
		private Socket s;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		
		//Socket connection creates port to connect and data ins and outs
		public ClientConnection() {
			try {
				s = new Socket("localhost", 4999);
				dataIn = new DataInputStream(s.getInputStream());
				dataOut = new DataOutputStream(s.getOutputStream());
				playerID = dataIn.readInt();
				System.out.println("Connected to server as player " + playerID);

			} catch (IOException ex) {
				System.out.println("Client Socket Failure");
			}
		}
		
		//sends row and col data to server
		public void sendButtonPos(int r, int c) {
			try {
				dataOut.writeInt(r);
				dataOut.writeInt(c);
				// System.out.println("run SBP " + r + " " + c);
				dataOut.flush();
			} catch (IOException ex) {
				System.out.println("IOException from sendButtonNum() cc");
			}
		}

		public void receiveDataPos() {

			int r = 0;
			int c = 0;
			try {
				r = dataIn.readInt();
				c = dataIn.readInt();
				// System.out.println("run RDP " + r + " " + c);
				if (playerID == 1) {
					clientData[r][c] = 1;
					frame.placePiece(1, r, c);
					dataUpdate();
				} else if (playerID == 2) {
					clientData[r][c] = 2;
					frame.placePiece(2, r, c);
					dataUpdate();
					// System.out.println("receivedatapos test " + r + " " + c);
				}

			} catch (IOException ex) {
				System.out.println("IOException from receiveButtonNum() CC");
			}
		}

	}

	// each time a client runs new Client obj connects to server + runs setup method
	public static void main(String[] args) {
		Client c = new Client();
		c.connectToServer();
		c.setUp();
	}
}
