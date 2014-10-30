package appstraction.app.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import appstraction.app.StringUtils;



//TODO: extract searchable to SearchableDataSource

public class ListDataSource<T> implements DataSource<T> {

	ArrayList<T> _results;
	ArrayList<T> _data;
	
	@SuppressWarnings("rawtypes")
	ArrayList _changeListeners;

//	private TextConverter textConverter;
	
	public ListDataSource( ){
		_data = new ArrayList<T>();
	}
//	public ListDataSource(TextConverter textConverter ){
//		this();
//		setTextConverter(textConverter);
//	}
	
	public ListDataSource( ArrayList<T> data ){
		_data = data;
	}
	
	
	public ListDataSource( T[] data ){
		_data = new ArrayList<T>(Arrays.asList(data));
	}
	
//	public ListDataSource( ArrayList<T> data, TextConverter textConverter ){
//		_data = data;
////		setTextConverter(textConverter);
//	}
	
	public List<T> data() {
		if(_results != null)
			return _results;
		return _data;
	}

	@SuppressWarnings("unchecked")
	public void fireChanged(ChangeEvent event) {
		if(_changeListeners != null){
//			Ui.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
					for (Object listener : _changeListeners) {
						((OnChanged<DataSource<T>>)listener).onChanged(ListDataSource.this,event);
					}
//				}
//			});
		}
	}
	

	public T get(int index) {
		return data().get(index);
	}

	public int size() {
		return data().size();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addOnChangedListener(OnChanged<DataSource<T>> listener) {
		if(_changeListeners == null)
			_changeListeners = new ArrayList();
		_changeListeners.add(listener);
	}
	public void removeOnChangedListener(OnChanged<DataSource<T>> listener) {
		if(_changeListeners!=null)
			_changeListeners.remove(listener);
	}
	
	
	public void clear() {
		if(_data.size() > 0 || _results != null){
			_data.clear();
			_results = null;
			fireChanged(ChangeEvent.CLEAR);
		}
	}
	public void update(List<T> data) {
		_data.clear();
		_data.addAll(data);
		_results = null;
		fireChanged(ChangeEvent.UPDATE);
	}
	
	public void update(T[] data) {
		_data.clear();
		for (int i = 0; i < data.length; i++) {
			T t = data[i];
			_data.add(t);
		}
		_results = null;
		fireChanged(ChangeEvent.UPDATE);
	}

    public void onDestroy() {
        if(_changeListeners!=null) {
            _changeListeners.clear();
            _changeListeners = null;
        }
        if(_data!=null)
            _data.clear();
        if(_results!=null)
            _results.clear();
    }


	public String toString(Object o) {
		return StringUtils.convert( o );
	}

//	public TextConverter getTextConverter() {
//		return textConverter;
//	}
//
//	public void setTextConverter(TextConverter textConverter) {
//		this.textConverter = textConverter;
//	}
//
//	@Override
//	public void search(String searchText) {
//		_results = performSearch(searchText);
//		fireChanged(ChangeEvent.UPDATE);
//	}
	
//	protected ArrayList<T> performSearch(String searchText) {
//		if(searchText == null)
//			return null;
//		 searchText=  StringUtils.toLowerCase(searchText);
//		ArrayList<T> results = new ArrayList<T>();
//		for (T item : _data) {
//			if(StringUtils.toLowerCase(toString(item)).startsWith( searchText))
//				results.add(item);
//		}
//		return results;
//	}
	
	public boolean add(T item) {
		boolean ret = _data.add(item);
		fireChanged(new ChangeEvent.Add(item));
		return ret;
	}
	
	public boolean remove(Object item) {
		boolean ret = _data.remove(item);
		fireChanged(new ChangeEvent.Remove(item));
		return ret;
	}
	
	
	
	
//
//	ArrayList<DataSourceLoadListener> loadListners = new ArrayList<DataSourceLoadListener>();
//	@Override public void addLoadListener(DataSourceLoadListener listener) {
//		loadListners.add(listener);
//		listener.loadingChanged(isLoading(), this);
//	}
//
//	public void removeLoadListener(DataSourceLoadListener listener) {
//		loadListners.remove(listener);
//	}
//
//	private boolean loading = false;
//	public void setLoading( boolean loading ){
//		this.loading = loading;
//		for (DataSourceLoadListener l : loadListners) {
//			l.loadingChanged(loading, this);
//		}
//	}
//
//	public boolean isLoading(){
//		return loading;
//	}

	
	
	public static <TItem> ListDataSource<TItem> of(TItem...items){
		return new ListDataSource<TItem>(items);
	}
	public void add(int location, T object) {
		_data.add(location,object);
		fireChanged(new ChangeEvent.Add(object, location));
	}
	public boolean addAll(Collection<? extends T> arg0) {
		boolean ret = _data.addAll(arg0);
		fireChanged(new ChangeEvent.Add(arg0,-1,true));
		return ret;
	}
	public boolean addAll(int position, Collection<? extends T> items) {
		boolean ret = _data.addAll(position,items);
		fireChanged(new ChangeEvent.Add(items,position ,true));
		return ret;
	}
	public boolean contains(Object object) {
		return _data.contains(object);
	}
	public boolean containsAll(Collection<?> arg0) {
		return _data.containsAll(arg0);
	}
	public int indexOf(Object object) {
		return _data.indexOf(object);
	}
	public boolean isEmpty() {
		return _data.isEmpty();
	}
	public Iterator<T> iterator() {
		return _data.iterator();
	}
	public int lastIndexOf(Object object) {
		return _data.lastIndexOf(object);
	}
	public ListIterator<T> listIterator() {
		return _data.listIterator();
	}
	public ListIterator<T> listIterator(int location) {
		return _data.listIterator(location);
	}
	public T remove(int location) {
		T t = _data.remove(location);
		fireChanged(new ChangeEvent.Remove(t,location  ));
		return t;
	}
	public boolean removeAll(Collection<?> arg0) {
		boolean ret = _data.removeAll(arg0);
		fireChanged(new ChangeEvent.Remove(arg0,-1,true));
		return ret;
	}
	public boolean retainAll(Collection<?> arg0) {
		boolean ret = _data.retainAll(arg0);
		fireChanged(ChangeEvent.UPDATE);
		return ret;
	}
	public T set(int location, T object) {
		T ret = _data.set(location, object);
		fireChanged(new ChangeEvent.ItemUpdate(object, location));
		return ret;
	}
	public List<T> subList(int start, int end) {
		return _data.subList(start, end);
	}
	public Object[] toArray() {
		return _data.toArray();
	}
	public <TItem> TItem[] toArray(TItem[] array) {
		return _data.toArray(array);
	}
	
	
}
