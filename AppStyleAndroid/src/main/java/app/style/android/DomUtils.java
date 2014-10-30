package app.style.android;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class DomUtils {

	public interface Walker {
		int process(View v);
	}

	public static final int CONTINUE = 0;
	public static final int EXIT = 1;
	public static final int SKIP_CHILDREN = 2;
	private static final int SKIP_PARENT = 3;

	public static int walkEl(ViewGroup view, Walker walker) {

		for (int i = 0; i < view.getChildCount(); i++) {
			View v = view.getChildAt(i);
			int result = walker.process(v);
			switch (result) {
			case CONTINUE:
				if (v instanceof ViewGroup) {
					result = walkEl((ViewGroup) v, walker);
					if (result == EXIT)
						return EXIT;
				}
				break;
			case EXIT:
				return EXIT;
			case SKIP_CHILDREN:
				break;
			case SKIP_PARENT:
				return CONTINUE;
			}
		}

		return CONTINUE;
	}

	public static View findByCoord(int x, int y, ViewGroup root) {
		Rect r = new Rect();
		for (int i =  root.getChildCount()-1; i >= 0; i--) {
			View v = root.getChildAt(i);
			if (v.getVisibility() != View.VISIBLE)
				continue;

			if (v instanceof ViewGroup) {
				View child = findByCoord(x, y, (ViewGroup) v);
				if (child != null)
					return child;
			}
			//v.getGlobalVisibleRect(r);
			v.getHitRect(r);
			if (r.contains(x, y))
				return v;
		}
		return null;

	}

}
