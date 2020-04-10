import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket ss;
	private int numPlayers;
	private ServerConnection player1;
	private ServerConnection player2;
	private ServerConnection[] clientArray;
	private int[] checkerType;
	private static int rows = 8;
	private static int columns = 8;
	private static int[][] serverData = new int[rows][columns];
	private int otherPlayer;


	
	public Server() {
		numPlayers = 0;
		clientArray = new ServerConnection [2];
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
					clientArray[0] = sc;
					otherPlayer = 2;
				} else {
					player2 = (sc);
					clientArray[1] = sc;
					otherPlayer = 1;
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
		private ObjectOutputStream dataOut;
		private ObjectInputStream dataIn;
		private int playerID;

		public ServerConnection(Socket s, int id) {
			socket = s;
			playerID = id;
			try {
				dataOut = new ObjectOutputStream(socket.getOutputStream());
				dataIn = new ObjectInputStream(socket.getInputStream());
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
					Object server = dataIn.readObject();
					if (playerID == 1) {
		
						if(server instanceof Player) {
							Player collector = (Player)server;
							returnButtonPos(collector);						
							
						System.out.println("Player 1 clicked " + collector.getRow() +" "+ collector.getCol());
						dataUpdate(playerID, collector.getRow(), collector.getCol());
						
						outputToOpponent(collector);
						}
						
						if(server instanceof Delete) {
							Delete collectDel = (Delete)server;
						//	returnDelPos(collectDel);	
							delToOpponent(collectDel);
						}
					

					}else if (playerID == 2) {
						if(server instanceof Player) {
							Player collector = (Player)server;
							returnButtonPos(collector);						
							
						System.out.println("Player 2 clicked " + collector.getRow() +" "+ collector.getCol());
						dataUpdate(playerID, collector.getRow(), collector.getCol());
						outputToOpponent(collector);
						}
						
						if(server instanceof Delete) {
							Delete collectDel = (Delete)server;
					//		returnDelPos(collectDel);	
							delToOpponent(collectDel);
						}
					}
				}

			} catch (IOException | ClassNotFoundException ex) {
				System.out.println("game crash server not receiving data or player quit");
			}
		}

		// Returns data of button position for player
		public void returnButtonPos(Player collector) {
			int id = collector.getId();
			int r = collector.getRow();
			int c = collector.getCol();
			dataUpdate(id, r, c);
			
			System.out.println("RBP" + id + r + c);
			try {
				dataOut.writeObject(collector);			
			} catch (IOException ex) {
				System.out.println("IOException from sendButtonNum() cc");
			}
		}
		
		// Returns data of button position for player
		public void returnDelPos(Delete collectDel) {
			int r = collectDel.getRow();
			int c = collectDel.getCol();
			storeDeletedPieces(r, c);
			System.out.println("RDP" + r + c);
			try {
				dataOut.writeObject(collectDel);			
			} catch (IOException ex) {
				System.out.println("IOException from sendButtonNum() cc");
			}
		}

	public void outputToOpponent(Player collector) {
		int id = collector.getId();
		int r = collector.getRow();
		int c = collector.getCol();
		if(playerID ==1){
			clientArray[1].returnButtonPos(collector);
		}else {
		clientArray[0].returnButtonPos(collector);
		}
		}
	public void delToOpponent(Delete collectDel) {
		int r = collectDel.getRow();
		int c = collectDel.getCol();
		if (playerID == 1) {
		clientArray[1].returnDelPos(collectDel);
		}
		if (playerID == 2) {
		clientArray[0].returnDelPos(collectDel);
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
		
		public void storeDeletedPieces(int r, int c) {
			serverData[r][c] = 0;
		}

		// Client data is then stored and returned
		public void dataUpdate(int id, int r, int c) {
			if (id ==1) { 
				serverData[r][c] = checkerType[0];
		}else { 
				serverData[r][c] = checkerType[1];
			}
		}
	}

	

	public static void main(String[] args) throws IOException {

		Server s = new Server();
		s.acceptConnections();
	}
}