package app.style.android;

import android.view.View;


import app.core.Log;
import app.style.css.BaseProperty;
import app.style.dom.Element;

/**
 * Created by Michael.Hancock on 7/30/2014.
 */
public abstract class AndroidProperty extends BaseProperty {

    public AndroidProperty(String name) {
        super(name);
    }

    @Override
    public boolean apply(Element e) {
        try {
            if(e.getView() != null && e.getView() instanceof View){
                View v = ((View) e.getView());
                apply(v);
                return true;
            }else{
                Log.warn(this, "failed to apply element view was null %s", e);
            }
        }catch (Throwable t){
            Log.warn(this,t);
        }
        return false;
    }

    protected abstract void apply(View view);
}
