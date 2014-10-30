package appstraction.app.data;

public interface OnChanged<T> {
	public void onChanged( T sender, ChangeEvent change);
}
