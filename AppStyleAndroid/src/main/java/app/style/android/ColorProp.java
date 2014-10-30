package app.style.android;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
* Created by Michael.Hancock on 7/30/2014.
*/
public class ColorProp extends BaseColorProperty {

    public ColorProp() {
        super("color");
    }



    @Override
    protected void apply(View view) {
        if(view instanceof TextView){
            ((TextView) view).setTextColor( (Integer)getValue());
        }
    }
}
