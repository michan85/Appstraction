package appstraction.app.data;

import java.util.ArrayList;



public abstract class DataSourceTransform<TFrom, TTo>  extends ListDataSource<TTo> implements OnChanged<DataSource<TFrom>> {

	private DataSource<TFrom> dataSource;

	public DataSourceTransform(DataSource<TFrom> dataSource) {
		super();
		this.setDataSource(dataSource);
	}
	
	public DataSourceTransform(ArrayList<TTo> data, DataSource<TFrom> dataSource) {
		super(data);
		this.setDataSource(dataSource);
	}

	public DataSource<TFrom> getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource<TFrom> dataSource) {
		if(this.dataSource != null)
			this.dataSource.removeOnChangedListener(this);
		this.dataSource = dataSource;
		//setTextConverter(dataSource);
		dataSource.addOnChangedListener(this);
		onChanged(dataSource, ChangeEvent.UPDATE);
	}

	public void onChanged(DataSource<TFrom> dataSource, ChangeEvent change) {

        //TODO: HANDLE CHANGE EVENT

		this.data().clear();
		for (TFrom i : dataSource.data()) {
			this.data().add(transform( i ));
		}
		this.fireChanged(ChangeEvent.UPDATE);
	}

	public abstract TTo transform(TFrom item);
	

//	@Override public void search(String searchText) {
//		if(dataSource instanceof Searchable)
//			((Searchable)dataSource).search(searchText);
//		else
//			super.search(searchText);
//	}
}
