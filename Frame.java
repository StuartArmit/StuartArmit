import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
	MatrixButton matrixButton;
	private static int rows = 8;
	private static int columns = 8;
	private static Color col1 = Color.DARK_GRAY;
	private static Color col2 = Color.WHITE;
	private JButton[][] button = new JButton[rows][columns];
	private String symbol1 = "O";
	private String symbol2 = "X";

	// General properties for game board
	// 2d array creates buttons in alternating colours, sets font for pieces
	public Frame(Client client) {
		setTitle("Draughts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setResizable(false);

		Container mainContainer = this.getContentPane();
		JPanel gridPanel = new JPanel(new GridLayout(rows, columns));
		mainContainer.add(gridPanel);

		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				button[i][j] = new MatrixButton(i, j);

				button[i][j].addActionListener(client);
				gridPanel.add(button[i][j]);
				button[i][j].setForeground(Color.RED);
				Font myFont = new Font("Monospace", Font.BOLD, 40);
				button[i][j].setFont(myFont);
			}
		}
		Color temp = null;
		for (int i = 0; i < rows; i++) {

			if (i % 2 == 0) {
				temp = col1;
				button[i][0].setBackground(temp);
			} else {
				temp = col2;
				button[i][0].setBackground(temp);
			}
			for (int j = 0; j < columns; j++) {
				if (temp.equals(col2)) {
					temp = col1;
					button[i][j].setBackground(temp);
				} else {
					temp = col2;
					button[i][j].setBackground(temp);
				}
			}
		}
	}

	// Clears board
	public void whipeBoard() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				button[i][j].setText(" ");
			}
		}
	}
	// receive data from client and creates game pieces
	public void placePiece(int token, int i, int j) {
		if (token == 1 || token == 3) {
			button[i][j].setText(symbol1);
		}
		if (token == 2 || token == 4) {
			button[i][j].setText(symbol2);
		}
	}
	
	// receive data from client and removes game pieces
	public void removePiece(int i, int j) {
	//	if (token == 1 || token == 3) {
			button[i][j].setText(null);
		}
//		if (token == 2 || token == 4) {
//			button[i][j].setText(null);
//		}
//	}

	// Disables buttons for when its not your turn
	public void blockBoard() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button.length; j++) {
				button[i][j].setEnabled(false);
			}
		}
	}
	
	// Disables buttons for when its not your turn
	public void playBoard(int id) {
		blockBoard();	
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button.length; j++) {
				if(id ==1) {
				if(button[i][j].getText() == "O"){
				button[i][j].setEnabled(true);
				}
				}else if(button[i][j].getText() == "X"){
					button[i][j].setEnabled(true);
			}
		}
	}
}		
	// frees buttons that are legal moves dependent on player
	public void move(int token, int r, int c) {
	
		blockBoard();
		if (token == 2) {
			r = r+1;
			c = c+1;
			button[r][c].setEnabled(true);
		//	System.out.println(r + " " + c);
			c = c-2;
			button[r][c].setEnabled(true);
		//	System.out.println(r + " " + c);

		}else {
			r = r-1;
			c = c+1;
			button[r][c].setEnabled(true);
		//	System.out.println(r + " " + c);
			c = c-2;
			button[r][c].setEnabled(true);
		//	System.out.println(r + " " + c);
}	
	}	
	
	// Re-enables buttons for when it is your turn
	public void freeBoard() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button.length; j++) {
				button[i][j].setEnabled(true);

			}
		}
	}

}