package appstraction.app;

public interface Callback<TResult> {
	public void onResult(TResult result);
	public void onError(Throwable error);
}
