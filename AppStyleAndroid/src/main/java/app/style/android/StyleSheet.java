package app.style.android;


import android.opengl.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import app.core.Log;
import app.style.css.CssRule;
import app.style.css.InvalidProperty;
import app.style.css.NoImpProperty;
import app.style.css.StyleProperty;

/**
* Created by Michael.Hancock on 7/30/2014.
*/
public class StyleSheet {



    public List<CssRule> getRules() {
        return rules;
    }

    List<CssRule> rules = new ArrayList<CssRule>();

    public StyleProperty createAttr(String attr, String value) {
        try {
            if(attrFactory.containsKey(attr)) {
                StyleProperty prop = (StyleProperty) attrFactory.get(attr).newInstance();
                Log.debug(this, "style attr %s: %s; ", attr, value);
                prop.setValue(value);
                return prop;
            }else{
                Log.debug(this, "style attr not implemented "+attr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new InvalidProperty(attr,e, value);
        }
        return new NoImpProperty(attr);
    }


    Map<String, Class> attrFactory = new HashMap<String, Class>();
    public StyleSheet(){
        attrFactory.put("font-size", FontSizeStyleProp.class );
        attrFactory.put("background-color",  BgColorProp.class);
        attrFactory.put("color",  ColorProp.class);
        attrFactory.put("max-lines",  MaxLines.class);
        attrFactory.put("max-width",  MaxWidth.class);
        attrFactory.put("max-height",  MaxHeight.class);
        attrFactory.put("width",  Width.class);
        attrFactory.put("height",  Height.class);
        attrFactory.put("visibility",  Visibility.class);
        attrFactory.put("margin-top",  MarginTop.class);
        attrFactory.put("margin-left",  MarginLeft.class);
        attrFactory.put("margin-right",  MarginRight.class);
        attrFactory.put("margin-bottom",  MarginBottom.class);
    }

    public static class MaxLines extends AndroidProperty {

        public MaxLines() {
            super("max-lines");
        }

        @Override
        public void setValue(Object value) {
            super.setValue(Integer.parseInt(value + ""));
        }

        @Override
        protected void apply(View view) {
            if(view instanceof TextView){
                ((TextView) view).setMaxLines((Integer) getValue());
            }else{
                Log.warn(this, "failed to apply view %s wasnt a textview", view);
            }
        }
    }

    public static class Visibility extends AndroidProperty {

        public static enum Vis{ visible, invisible, gone, hidden }
        public Visibility() {
            super("visibility");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Enum.valueOf(Vis.class, (value+"").toLowerCase())  );
        }

        @Override
        protected void apply(View view) {

            Vis v = (Vis)getValue();
            switch (v){
                case visible:
                    view.setVisibility(View.VISIBLE);
                    break;
                case hidden:
                case invisible:
                    view.setVisibility(View.INVISIBLE);
                    break;
                case gone:
                    view.setVisibility(View.GONE);
                    break;
            }


        }
    }


    public static class MaxWidth extends AndroidProperty {

        public MaxWidth() {
            super("max-width");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {

            if(view instanceof TextView){
                ((TextView) view).setMaxWidth((Integer) getValue());
            }else  if(view instanceof ImageView){
                ((ImageView) view).setMaxWidth((Integer) getValue());
            }else{
                Log.warn(this, "%s couldnt be applied to %s", getName(), view);
            }
        }
    }


    public static class MaxHeight extends AndroidProperty {

        public MaxHeight() {
            super("max-height");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {

            if(view instanceof TextView){
                ((TextView) view).setMaxHeight((Integer) getValue());
            }else  if(view instanceof ImageView){
                ((ImageView) view).setMaxHeight((Integer) getValue());
            }else{
                Log.warn(this, "%s couldnt be applied to %s", getName(), view);
            }
        }
    }

    public static class Width extends AndroidProperty {

        public Width() {
            super("width");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {
            view.getLayoutParams().width = (Integer)getValue();
            view.setLayoutParams(view.getLayoutParams());
        }
    }

    public static class Height extends AndroidProperty {

        public Height() {
            super("height");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {
            view.getLayoutParams().height = (Integer)getValue();
            view.setLayoutParams(view.getLayoutParams());
        }
    }


    public static class MarginTop extends AndroidProperty {

        public MarginTop() {
            super("margin-top");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {
            if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                lp.topMargin = (Integer)getValue();
                view.setLayoutParams(lp);
            }
        }
    }
    public static class MarginBottom extends AndroidProperty {

        public MarginBottom() {
            super("margin-bottom");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {
            if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                lp.bottomMargin = (Integer)getValue();
                view.setLayoutParams(lp);
            }
        }
    }
    public static class MarginLeft extends AndroidProperty {

        public MarginLeft() {
            super("margin-left");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {
            if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                lp.leftMargin = (Integer)getValue();
                view.setLayoutParams(lp);
            }
        }
    }
    public static class MarginRight extends AndroidProperty {

        public MarginRight() {
            super("margin-right");
        }

        @Override
        public void setValue(Object value) {
            super.setValue( Units.dp( Integer.parseInt(value + "")));
        }

        @Override
        protected void apply(View view) {
            if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                lp.rightMargin = (Integer)getValue();
                view.setLayoutParams(lp);
            }
        }
    }

}
