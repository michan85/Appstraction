package appstraction.app.data;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;




public interface DataSource<T> extends List<T>
{
	List<T> data();
    void fireChanged(ChangeEvent change);
    void addOnChangedListener( OnChanged<DataSource<T>> listener );
    void removeOnChangedListener( OnChanged<DataSource<T>> listener);
    void update(List<T> data);
    void update(T[] data);
    void onDestroy();
	Object[] toArray();
	List<T> subList(int start, int end);
	int size();
	T set(int location, T object);
	boolean retainAll(Collection<?> arg0);
	boolean removeAll(Collection<?> arg0);
	boolean remove(Object item);
	T remove(int location);
	ListIterator<T> listIterator(int location);
	ListIterator<T> listIterator();
	int lastIndexOf(Object object);
	Iterator<T> iterator();
	boolean isEmpty();
	int indexOf(Object object);
	<TItem> TItem[] toArray(TItem[] array);
	boolean containsAll(Collection<?> arg0);
	boolean contains(Object object);
	boolean addAll(int position, Collection<? extends T> items);
	boolean addAll(Collection<? extends T> arg0);
	boolean add(T item);
	void add(int location, T object);
}
