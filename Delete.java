import java.io.Serializable;

	public class Delete implements Serializable {

		private int row;
		private int col;
		
		public Delete(int row, int col){
			
		this.row = row;
		this.col = col;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}
	}
