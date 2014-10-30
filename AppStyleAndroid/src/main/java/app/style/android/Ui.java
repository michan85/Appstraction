package app.style.android;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import app.core.Log;


public class Ui {

	private static Handler _uiThreadHandler;

	public static void hideKeyboard(Activity a){
		((InputMethodManager)a.getSystemService(Context.INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(a.getWindow().getDecorView().getWindowToken(), 0);
	}
	
//	public static void hideKeyboard(){
//	try{
//	if(currentActivity.get() != null)
//		currentActivity.get().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//	}catch(Exception e){
//		error(e);
//	}
//}
	
	public static void toast(final String message, final int length) {
		runOnUiThread(new Runnable(){
			@Override public void run() {
				Toast.makeText(getAppContext(),message,length).show();
			}
		});
	}

	public static void toast(final String message, final Object... args) {
		runOnUiThread(new Runnable(){
			@Override public void run() {
				Toast.makeText( getAppContext()  ,String.format(message,args),Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/*
	 * Toast.makeText(instance(),message,Toast.LENGTH_SHORT).show();
	 */
	public static void toast(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getAppContext(),message,Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public static void runOnUiThread(final Runnable runnable) {
	
		if(Looper.myLooper() == Looper.getMainLooper()){
			try {
				runnable.run();
			} catch (Exception e) {
				Log.warn(Ui.class, "runOnUiThread Error: %s", e.getMessage());
				Log.error(Ui.class, e);
			}
			return;
		}
		
		
		getUiThreadHandler().post(new Runnable() {
			@Override public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					Log.warn(Ui.class, "runOnUiThread Error: %s", e.getMessage());
					Log.error(Ui.class, e);
				}
			}
		});
	}
	
	public static Handler getUiThreadHandler(){
		if(_uiThreadHandler == null)
			 _uiThreadHandler = new Handler(Looper.getMainLooper());
		return _uiThreadHandler;
	}
	
	public static void runLater(final Runnable r, int delay){
		getUiThreadHandler().postDelayed(new Runnable() {
			@Override public void run() {
				try {
					r.run();
				} catch (Exception e) {
					Log.warn(Ui.class, "runLater Error: %s", e.getMessage());
					Log.error(Ui.class, e);
				}
			}
		}, delay);
	}

	
	public static abstract class Callback<TResult> implements app.Callback<TResult>{

		private WeakReference<Context> ref;

		public Callback(Context ctx){
			this.ref = new WeakReference<Context>(ctx);
		}
		@Override
		public final void onResult(final TResult result) {
			if(ref.get() == null) return;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					success(result);
				}
			});
			
		}

		@Override
		public final void onError(final Throwable error) {
			if(ref.get() == null) return;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					failure(error);
				}
			});
			
		}
		
		public abstract void success(TResult result);
		public abstract void failure(Throwable error);
		
	}
	
	
	public static void show( final View v ){
		runLater(new Runnable() {
			
			@Override
			public void run() {
				v.setVisibility(View.VISIBLE);
			}
		}, 0);
	}
	public static void hide( final View v ){
		runLater(new Runnable() {
			
			@Override
			public void run() {
				v.setVisibility(View.GONE);
			}
		}, 0);
	}
	
	
	
	
	public static abstract class ConfirmListener implements DialogInterface.OnClickListener{
		public DialogInterface	dialog;

		public abstract void onOk();
		
		public void onCancel(){}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			this.dialog = dialog;
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					onOk();
					dialog.dismiss();
					break;
				default:
					onCancel();
					break;
			}
		}
	}
	
	public static void confirm( Activity activity, String title, String msg, String okText, String cancelText, ConfirmListener listener ){
		new AlertDialog.Builder( activity)
			.setCancelable(true)
			.setMessage(msg)
			.setPositiveButton(okText, listener)
			.setNegativeButton(cancelText, listener)
			.create().show();
			
	}

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        Ui.appContext = appContext;
    }

    static Context appContext;

}
