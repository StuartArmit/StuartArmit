import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket ss;
	MatrixButton matrixButton;
	private int numPlayers;
	private ServerConnection player1;
	private ServerConnection player2;
	private int[] checkerType;
	private static int rows = 8;
	private static int columns = 8;
	private static int[][] serverData = new int[rows][columns];
	private int p1rowButtonPress;
	private int p1colButtonPress;
	private int p2rowButtonPress;
	private int p2colButtonPress;

	
	public Server() {
		numPlayers = 0;
		checkerType = new int[4];
		checkerType[0] = 1; // whiteChecker
		checkerType[1] = 2; // blackChecker
		checkerType[2] = 3; // whiteKing
		checkerType[3] = 4; // blackKing

		try {
			ss = new ServerSocket(4999);
		} catch (IOException ex) {
			System.out.println("GameServer Failed");
		}
	}

	// each accepted connections is counted and player id, token and thread assigned
	public void acceptConnections() {
		try {
			while (numPlayers < 2) {
				Socket s = ss.accept();
				numPlayers++;
				System.out.println("Connected");
				System.out.println("Player " + numPlayers + " Connected.");
				ServerConnection sc = new ServerConnection(s, numPlayers);
				if (numPlayers == 1) {
					player1 = (sc);
				} else {
					player2 = (sc);
				}
				Thread t = new Thread(sc);
				t.start();
			}
			System.out.println("2 Players game ready");

		} catch (IOException ex) {
			System.out.println("Accept Connection Failed");

		}
	}

	


	// Sever connection sends and receives ints and chars
	private class ServerConnection implements Runnable {
		private Socket socket;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		private int playerID;

		public ServerConnection(Socket s, int id) {
			socket = s;
			playerID = id;
			try {
				dataIn = new DataInputStream(socket.getInputStream());
				dataOut = new DataOutputStream(socket.getOutputStream());
			} catch (IOException ex) {
				System.out.println("IOException from run()");
			}
			dataSetup();
		}
		
		//Run gives player ID and waits for connections back
		public void run() {
			try {
				
				dataOut.writeInt(playerID);
				dataOut.flush();

				while (true) {
					if (playerID == 1) {
						p1rowButtonPress = dataIn.readInt();
						p1colButtonPress = dataIn.readInt();
						System.out.println("Player 1 clicked  " + p1rowButtonPress +" "+ p1colButtonPress);
						returnButtonPos(p1rowButtonPress, p1colButtonPress);
				//		dataUpdate(playerID, p1rowButtonPress, p1colButtonPress);
					}else if (playerID == 2) {
						p2rowButtonPress = dataIn.readInt();
						p2colButtonPress = dataIn.readInt();
						System.out.println("Player 2 clicked  " + p2rowButtonPress +" "+ p2colButtonPress);
						returnButtonPos(p2rowButtonPress, p2colButtonPress);
					//	dataUpdate(playerID, p2rowButtonPress, p2colButtonPress);
					}
				}

			} catch (IOException ex) {
				System.out.println("game crash server not receiving data or player quit");
			}
		}

		// Returns data of button position for player
		public void returnButtonPos(int r, int c) {
			try {
				dataOut.writeInt(r);
				dataOut.writeInt(c);
				dataOut.flush();
			} catch (IOException ex) {
				System.out.println("IOException from sendButtonNum() cc");
			}
		}

		
		// Population script but for Server side game data
		public void dataSetup() {
			for (int j = 0; j < (8); j += 2) {
				serverData[5][j] = checkerType[0];
				serverData[7][j] = checkerType[0];
			}
			for (int j = 1; j < (8); j += 2) {
				serverData[6][j] = checkerType[0];
			}
			for (int j = 1; j < (8); j += 2) {
				serverData[0][j] = checkerType[1];
				serverData[2][j] = checkerType[1];
			}
			for (int j = 0; j < (8); j += 2) {
				serverData[1][j] = checkerType[1];
			}
		}

		// Client data is then stored and returned
		public void dataUpdate(int id, int r, int c) {
			if (id ==1) { 
				serverData[r][c] = checkerType[0];
				returnButtonPos(r, c);
		}else { 
				serverData[r][c] = checkerType[1];
				returnButtonPos(r, c);
			}
		}
	}

	

	public static void main(String[] args) throws IOException {

		Server s = new Server();
		s.acceptConnections();
	}
}