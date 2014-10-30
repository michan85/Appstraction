package appstraction.app.lifecycle;



public interface UiLifecycle {
	void onCreate(UiLifecycleController container);
	void onStart(UiLifecycleController container);
	void onResume(UiLifecycleController container);
	void onPause(UiLifecycleController container);
	void onStop(UiLifecycleController container);
	void onSaveState(UiLifecycleController container);
	void onDestroy(UiLifecycleController container);
	void onVisibilityChanged(UiLifecycleController container,boolean visible);
}
