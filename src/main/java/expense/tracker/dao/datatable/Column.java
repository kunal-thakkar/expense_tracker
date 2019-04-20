package expense.tracker.dao.datatable;

public class Column {
	
	private String data;
	private String name;
	private Search search;
	private boolean searchable;
	private boolean orderable;
	public Column(String data, String name, Search search, boolean searchable, boolean orderable) {
		super();
		this.data = data;
		this.name = name;
		this.search = search;
		this.searchable = searchable;
		this.orderable = orderable;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	public boolean isSearchable() {
		return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	public boolean isOrderable() {
		return orderable;
	}
	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}
	
}
