// Generated by delombok at Fri Aug 29 15:58:54 CAT 2014
package appstraction.app.lifecycle;

public class UiLifecycleUtils {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UiLifecycleUtils.class);
	
	
	public static void addListener(Object context, UiLifecycle lifecycleListener) {
		if (context instanceof UiLifecycleController) ((UiLifecycleController)context).addLifecycleListener(lifecycleListener); else {
			log.error("activity listener not added - context does not implement ActivityLifecycleContainer {} this could be a potential memory leak!!!", context.getClass().getName());
		}
	}
}