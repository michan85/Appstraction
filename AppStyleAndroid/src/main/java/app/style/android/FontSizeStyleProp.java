package app.style.android;

import android.view.View;
import android.widget.TextView;

import app.core.Log;


/**
* Created by Michael.Hancock on 7/30/2014.
*/
public class FontSizeStyleProp extends AndroidProperty {

    public FontSizeStyleProp() {
        super("font-size");
    }

    @Override
    public void setValue(Object value) {
        super.setValue(Integer.parseInt(value + ""));
    }

    @Override
    protected void apply(View view) {
        if(view instanceof TextView){
            ((TextView) view).setTextSize(  (Integer)getValue()  );
        }else{
            Log.warn(this, "failed to apply view %s wasnt a textview", view);
        }
    }
}
