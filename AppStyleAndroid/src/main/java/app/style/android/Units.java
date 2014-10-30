package app.style.android;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import app.core.Log;


public class Units {

//	public static final int formSectionLinePadding = dp(16);
//	public static int labelWidth = dp(250);
//	public static int fieldSpacing = dp(8);
//	public static int fieldLabelSpacing = dp(16);
//	public static int iconWidth = dp(40);
//	public static int iconHeight = dp(40);

	public static int dp(int dp) {
		try {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					(float) dp, Ui.getAppContext().getResources()
							.getDisplayMetrics());
		} catch (Throwable t) {
			return dp;
		}
	}
	
	public static int pxToDp(int px) {
		try {
		    DisplayMetrics metrics = Ui.getAppContext().getResources().getDisplayMetrics();
		    float dp = px / (metrics.densityDpi / 160f);
		    return (int)dp;
		} catch (Throwable t) {
			return px;
		}
	}
	

	public static int sp(int sp) {
		try {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    (float) sp, Ui.getAppContext().getResources().getDisplayMetrics());
		} catch (Throwable t) {
            Log.warn(Units.class, "failed to convert value to sp: %s %s",sp,t.getMessage());
			return sp;
		}
	}

	public static int toE6(double deg) {
		return (int) (deg * 1e6);
	}

	public static int dimen(int resId) {
		return Ui.getAppContext().getResources().getDimensionPixelSize(resId);
	}

	public static int displayWidth() {
		return Ui.getAppContext().getResources().getDisplayMetrics().widthPixels;
	}
}
