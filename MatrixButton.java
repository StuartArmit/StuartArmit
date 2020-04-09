import javax.swing.JButton;

class MatrixButton extends JButton {
    private final int row;
    private final int col;

    public MatrixButton(int row, int col) {
    this.row = row;
    this.col = col;
    }

    public int getRow() {
    return row;
    }

    public int getCol() {
    return col;
    }
}