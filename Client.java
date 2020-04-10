import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		frame = new Frame(this);
		frame.setVisible(true);
		frame.setLocation(100, 100);
		clientDataSetup();
		dataUpdate();

		if (playerID == 1) {
			System.out.println("you are player1, you go first");
			

		} else {
			System.out.println("you are player2, please wait");
			//frame.blockBoard();
		}
	}
		
	
	
	// Click determines row and col data to send
	public void actionPerformed(ActionEvent ae) {
		if(turnsMade%2 == 0) {
		int delr = ((MatrixButton) ae.getSource()).getRow();
		int delc = ((MatrixButton) ae.getSource()).getCol();
		System.out.println(playerID + "  "+ delr + "  " + delc);
		frame.playBoard(playerID);
		frame.removePiece(delr, delc);
		storeDeletedPieces(delr, delc);
		dataUpdate();
		frame.move(playerID, delr, delc);
		turnsMade++;
		System.out.println("turns: " + turnsMade);
		}
		else if (turnsMade%2 != 0) {
		int r = ((MatrixButton) ae.getSource()).getRow();
		int c = ((MatrixButton) ae.getSource()).getCol();
		System.out.println(playerID + "  "+ r + "  " + c);
		cc.sendButtonPos(playerID, r, c);
		turnsMade++;
		System.out.println("turns: " + turnsMade);

		Thread t = new Thread(new Runnable() {
			public void run() {
				releaseTurn();
			}
		});
		t.start();
	}
	}

	// waits for other player to play before releasing
	public void releaseTurn() {
		
		cc.receiveDataPos();
		dataUpdate();
		frame.playBoard(playerID);
		
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
				} if (clientData[i][j] == 0) {
				frame.removePiece(i, j);
				
				} else if (clientData[i][j] == 2) {
					frame.placePiece(2, i, j);
				} if (clientData[i][j] == 0) {
				frame.removePiece(i, j);
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
		private ObjectInputStream dataIn;
		private ObjectOutputStream dataOut;
		private DataInputStream intIn;
		private DataOutputStream intOut;
		
		//Socket connection creates port to connect and data ins and outs
		public ClientConnection() {
			try {
				s = new Socket("localhost", 4999);
				dataIn = new ObjectInputStream(s.getInputStream());
				dataOut = new ObjectOutputStream(s.getOutputStream());
				
				playerID = intIn.readInt();
				System.out.println("Connected to server as player " + playerID);

			} catch (IOException ex) {
				System.out.println("Client Socket Failure");
			}
		}
		
		//sends row and col data to server
		public void sendButtonPos(int id, int r, int c) {
			try {
				Player send = new Player(id,r,c);
				dataOut.writeObject(send);
			
				 System.out.println("TEST SBP " + id + " " + r + " " + c);
				dataOut.flush();
			} catch (IOException ex) {
				System.out.println("IOException from sendButtonPos cc");
			}
		}


		public void receiveDataPos() {
			try {
				
				Player returner = (Player)dataIn.readObject();
				int gPID = returner.getId();
				int r = returner.getRow();
				int c = returner.getCol();
			//	int pID = dataIn.readObject();
			//	int otherr = dataIn.readObject();
			//	int otherc = dataIn.readObject();

		//		 System.out.println("TEST RDP " + playerID + " " + gPID + " " + r + " " + c+ " returned move: " + pID + " " + otherr + " " + otherc);
				if (playerID == 1) {
					clientData[r][c] = 1;
					frame.placePiece(1, r, c);
					dataUpdate();
//				}
	//				if(pID == 2 && gPID == 1) {
	//				clientData[otherr][otherc] = 2;
	//				frame.placePiece(2, otherr, otherc);
	//				dataUpdate();
					
				} else if (playerID == 2) {
					clientData[r][c] = 2;	
					frame.placePiece(2, r, c);
					dataUpdate();
				}
	//				if(pID == 1 && gPID == 2) {
	//				clientData[otherr][otherc] = 1;
	//				frame.placePiece(1, otherr, otherc);
	//				dataUpdate();
	//			}

			} catch (IOException | ClassNotFoundException ex) {
				System.out.println("IOException from receiveButtonPos CC");
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
