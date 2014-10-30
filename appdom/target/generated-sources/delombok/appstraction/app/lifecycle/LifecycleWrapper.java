package appstraction.app.lifecycle;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LifecycleWrapper implements UiLifecycle, UiLifecycleController{

	Logger log= LoggerFactory.getLogger(LifecycleWrapper.class);
	

	private ArrayList<UiLifecycle>	lifecycles;

	public LifecycleWrapper( UiLifecycle... lifecycles ){
		this.setLifecycles(new ArrayList<UiLifecycle>(Arrays.asList(lifecycles)));
	}
	public void onCreate(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onCreate(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	public void onStart(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onStart(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	public void onResume(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onResume(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	public void onPause(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onPause(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	public void onStop(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onStop(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	public void onSaveState(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onSaveState(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	public void onDestroy(UiLifecycleController container) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onDestroy(container);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		clearLifeCycles();
	}

	public void onVisibilityChanged(UiLifecycleController container,boolean visible) {
		for(UiLifecycle l : getLifecycles()){
			try {
				if(l!=null)
					l.onVisibilityChanged(container,visible);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}
	public ArrayList<UiLifecycle> getLifecycles() {
		return lifecycles;
	}
	public void setLifecycles(ArrayList<UiLifecycle> lifecycles) {
		this.lifecycles = lifecycles;
	}
	
	public void clearLifeCycles(){
		lifecycles.clear();
	}
	
	public LifecycleWrapper add(UiLifecycle l){
		
		return this;
	}
	
	public LifecycleWrapper remove(UiLifecycle l){
		lifecycles.remove(l);
		return this;
	}
	public void addLifecycleListener(UiLifecycle listener) {
		lifecycles.add(listener);
		
	}
	public boolean removeLifecycleListener(UiLifecycle listener) {
		return lifecycles.remove(listener);
	}

}
