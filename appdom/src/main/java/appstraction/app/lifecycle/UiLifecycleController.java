package appstraction.app.lifecycle;

public interface UiLifecycleController {
	void addLifecycleListener(UiLifecycle listener);
	boolean removeLifecycleListener(UiLifecycle listener);
}
