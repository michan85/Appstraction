package appstraction.app;





public class CallbackUtils {

	protected static <T> Callback<T> cast(Callback c){
		return (Callback<T>)c;
	}

}
