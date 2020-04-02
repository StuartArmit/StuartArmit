import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket ss;

	private int numPlayers;
	private char pToken1;
	private char pToken2;
	private ServerConnection player1;
	private ServerConnection player2;
	private int p1ButtonPress;
	private int p2ButtonPress;

	public Server() {
		numPlayers = 0;
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
					pToken1 = 'O';
				} else {
					player2 = (sc);
					pToken2 = 'X';
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
		}
		


		public void run() {
			try {
				dataOut.writeInt(playerID);
				dataOut.writeChar(pToken1);
				dataOut.writeChar(pToken2);
			
				dataOut.flush();

				while (true) {

				}

			} catch (IOException ex) {
				System.out.println("Button aren't working correctly");
			}
		}

	}

	public static void main(String[] args) throws IOException {

		Server s = new Server();
		s.acceptConnections();

	}
}