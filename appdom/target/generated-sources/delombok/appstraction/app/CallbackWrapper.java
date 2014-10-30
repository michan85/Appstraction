package appstraction.app;



@SuppressWarnings("rawtypes")
public abstract class CallbackWrapper<T> implements Callback<T>{

	
	private Callback cb;

	
	public CallbackWrapper(Callback cb){
		this.cb=cb;
	}
	

	public void onError(Throwable error) {
		if(cb!=null)
			cb.onError(error);
	}
	
	
	
}