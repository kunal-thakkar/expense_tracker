package expense.tracker.dao.datatable;

public class Order {

	private int column;
	private String dir;
	public Order(int column, String dir) {
		this.column = column;
		this.dir = dir;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	
}
