package appstraction.app.lifecycle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UiLifecycleUtils {

	
	
    public static void addListener(Object context, UiLifecycle lifecycleListener) {
        if(context instanceof UiLifecycleController)
            ((UiLifecycleController)context).addLifecycleListener( lifecycleListener );
        else{
        	log.error("activity listener not added - context does not implement ActivityLifecycleContainer {} this could be a potential memory leak!!!", context.getClass().getName());
        }
    }

}
