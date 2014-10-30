package app.style.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import java.util.ArrayList;
import java.util.List;

import app.core.Log;
import app.style.css.StyleEngine;
import app.style.dom.Document;
import app.style.dom.Element;


/**
 * Created by Michael.Hancock on 7/29/2014.
 */
public class AndroidDoc extends Document<View> implements  ViewGroup.OnHierarchyChangeListener {
    private ViewGroup view;

    public AndroidDoc() {
        super();

        setStyleEngine(new AndroidStyleEngine());

    }


    public static class AndroidStyleEngine extends  StyleEngine{

        @Override
        protected void doInvalidate() {
            Ui.runLater(new Runnable(){
                @Override
                public void run() {
                    superDoInvalidate();
                }
            },50);
        }
        private void  superDoInvalidate(){
            super.doInvalidate();
        }

        @Override
        protected void applyStyles(Runnable styleRunner) {
            Ui.runOnUiThread(styleRunner);
        }
    }



    @Override
    @SuppressWarnings( {"rawtypes", "unchecked"} )
    public void setView(View v){

        if(!(v instanceof ViewGroup))
            throw new RuntimeException("document view must be a view group");
        ViewGroup view = (ViewGroup)v;
        if(this.view!=null){
            DomUtils.walkEl(this.view,new DomUtils.Walker() {
                @Override
                public int process(View v) {
                    if(v instanceof  ViewGroup)
                        ((ViewGroup)v).setOnHierarchyChangeListener(null);
                    v.setTag(R.id.dom_node, null);

                    return DomUtils.CONTINUE;
                }
            });
        }

        this.view=view;
        //setNodeName(view.getClass().getName());
        view.setOnHierarchyChangeListener( HierarchyTreeChangeListener.wrap(this));
        view.setTag(R.id.dom_node,this);

        List<View> children = new ArrayList<View>();
        for(int i=0; i < view.getChildCount(); i++){
            children.add(view.getChildAt(i));
        }

        if(view instanceof AdapterView){
            ((AdapterView) view).setAdapter(((AdapterView) view).getAdapter());
        }else{
            view.removeAllViews();
            for (View aChildren : children) {
                view.addView(aChildren);
            }
        }
        super.setView(view);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        Log.debug(this, "onChildViewAdded %s %s %s %s",parent,parent.getId(), child,child.getId());
        el(parent).appendChild(el(child));
        getStyleEngine().invalidate();

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        Log.debug(this, "onChildViewRemoved %s %s %s %s", parent, parent.getId(), child, child.getId());
        //el(parent).removeChild(el(child));
        child.setTag(R.id.dom_node,null);
        getStyleEngine().invalidate();
    }

    public Element el(View v){
        Element node = (Element) v.getTag(R.id.dom_node);
        if(node==null){
            node = createElement(v);
        }
        return node;
    }

    public Element createElement(View v) {
        Element<View> e = new Element<View>(this);
        e.setNodeName(getTagForClass(v.getClass()));
        e.setContainer( ViewGroup.class.isAssignableFrom(v.getClass()) );
        if(v.getId() > 0) {
            String id = v.getContext().getResources().getResourceName(v.getId());
            e.setId(id.substring(id.indexOf("/")+1));
        }
        v.setTag(R.id.dom_node, e);
        e.setView(v);
        return e;
    }

    public String getTagForClass(Class<? extends View> aClass) {
        return aClass.getSimpleName();
    }


    public Element createElement(String tag) {
        Element e = new Element(this);
        e.setNodeName(tag);
        e.setContainer( ViewGroup.class.isAssignableFrom(getClassForTag(tag)) );

        return e;
    }

    public Class<?> getClassForTag(String tag) {
        try {
            return Class.forName(tag);
        } catch (Exception e) {
            throw new RuntimeException("getClassForTag could not map tag("+tag+") to class ",e );
        }

    }
}
