package expense.tracker.dao.datatable;

public class DataTable {

	private int draw;
	private Column[] columns;
	private Order[] order;
	private Long start;
	private Long length;
	private Search search;
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public Column[] getColumns() {
		return columns;
	}
	public void setColumns(Column[] columns) {
		this.columns = columns;
	}
	public Order[] getOrder() {
		return order;
	}
	public void setOrder(Order[] order) {
		this.order = order;
	}
	public Long getStart() {
		return start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	
}
