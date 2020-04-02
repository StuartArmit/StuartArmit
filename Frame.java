import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame implements ActionListener{
	private static int rows = 8;
	private static int columns = 8;
	private static Color col1 = Color.DARK_GRAY;
	private static Color col2 = Color.WHITE;
	private JButton[][] button = new JButton[rows][columns];
	private static String[][] data = new String[rows][columns];
	private char symbol1;
	private char symbol2;

	// General properties for game board
	// 2d array creates buttons in alternating colours, sets font for pieces
	public Frame() {

		setTitle("Draughts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setResizable(false);

		Container mainContainer = this.getContentPane();
		JPanel gridPanel = new JPanel(new GridLayout(rows, columns));
		mainContainer.add(gridPanel);

		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				int n = i * button[i].length + j + 1;
				button[i][j] = new JButton();// (String.valueOf(n));
				button[i][j].setActionCommand("Moved");
				button[i][j].addActionListener(this);
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

	// population script for each player that toStrings chars to represent game
	// pieces at startup
	public void pieceSetupP1(char p1) {
		symbol1 = p1;

		for (int j = 0; j < (8); j += 2) {
			button[5][j].setText(String.valueOf(symbol1));
			button[7][j].setText(String.valueOf(symbol1));
		}
		for (int j = 1; j < (8); j += 2)
			button[6][j].setText(String.valueOf(symbol1));
	}

	public void pieceSetupP2(char p2) {
		symbol2 = p2;
		for (int j = 1; j < (8); j += 2) {
			button[0][j].setText(String.valueOf(symbol2));
			button[2][j].setText(String.valueOf(symbol2));
		}
		for (int j = 0; j < (8); j += 2)
			button[1][j].setText(String.valueOf(symbol2));
	}
	//Disables buttons for when its not your turn
	public void blockBoard() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
					button[i][j].setEnabled(false);
				}
			}
		}	
	//Re-enables buttons for when it is your turn
	public void freeBoard() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
					button[i][j].setEnabled(true);
				}
			}
		}	
	
	

	public void actionPerformed(ActionEvent e) {
		int tempi = 0;
		int tempj = 0;
		String savedText;
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				if (e.getSource() == button[i][j]) {
					i = tempi;
					j = tempj;
				//	if(symbol1 == button[i][j].getText(String.valueOf(text)){
					
					
				}
			}
	}
	}
	
}
