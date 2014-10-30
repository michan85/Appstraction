package app.style.android;

import android.graphics.Color;

import app.StringUtils;
import app.core.Log;

/**
 * Created by Michael.Hancock on 8/4/2014.
 */
public abstract class BaseColorProperty extends AndroidProperty{
    private String rawValue;

    public BaseColorProperty(String name) {
        super(name);
    }

    @Override
    public void setValue(Object value) {
        String v = value+"";
        this.rawValue = v;
        int color = 0;
         if(v.startsWith("rgba")){
            int i = v.indexOf("(");
             String tmp = v.substring(i+1, v.indexOf(")"));
            String[] rgb = tmp.split(",");
             Log.debug(this, "rgba compoenents: [%s][%s] [%s] [%s]",value,tmp, rgb.length, StringUtils.join(rgb));
            color = Color.argb(
                    Integer.parseInt(rgb[3]),
                    Integer.parseInt(rgb[0]),
                    Integer.parseInt(rgb[1]),
                    Integer.parseInt(rgb[2])
            );
        }else if(v.startsWith("rgb")){
             int i = v.indexOf("(");
             String[] rgb = v.substring(i+1,  v.indexOf(")")).split(",");
             color = Color.rgb(
                     Integer.parseInt(rgb[0]),
                     Integer.parseInt(rgb[1]),
                     Integer.parseInt(rgb[2])
             );
         }else{
            color = Color.parseColor(v.toUpperCase());
        }
        Log.debug(this, "value changed: rawValue: %s computedValue:%s",rawValue,color);
        super.setValue(color);

    }
}
