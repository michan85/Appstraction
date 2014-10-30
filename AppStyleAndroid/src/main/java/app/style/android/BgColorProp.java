package app.style.android;

import android.graphics.Color;
import android.view.View;

/**
* Created by Michael.Hancock on 7/30/2014.
*/
public class BgColorProp extends BaseColorProperty {

    public BgColorProp() {
        super("background-color");
    }


    @Override
    protected void apply(View view) {
        view.setBackgroundColor((Integer)getValue());
    }
}
