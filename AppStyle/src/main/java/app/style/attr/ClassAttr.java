package app.style.attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.StringUtils;
import app.core.Log;
import app.style.dom.Node;

/**
 * Created by Michael.Hancock on 7/29/2014.
 */
public class ClassAttr extends SimpleAttr<String> {

    public ClassAttr() {

    }

    public ClassAttr(String v) {
        super(v);
        if(v!=null)
            classes.addAll(Arrays.asList(v.split(" ")));
    }

    Set<String> classes = new HashSet<String>();

    @Override
    public boolean set(Node owner, String v) {
        if(super.set(owner,v)) {
            System.out.println("class attr set: "+v);
            classes.clear();
            classes.addAll(Arrays.asList(v.split(" ")));
            owner.getDocument().getStyleEngine().nodeChanged( owner );
            return true;
        }
        return false;
    }

    public void addClass(Node owner, String clazz ){
        classes.add(clazz);
        Log.debug(this,"addClass "+clazz);
        super.set(owner, StringUtils.join(" ",classes));
        owner.getDocument().getStyleEngine().nodeChanged( owner );
    }

    public void removeClass(Node owner, String clazz ){
        classes.remove(clazz);
        Log.debug(this,"removeClass "+clazz);
        super.set(owner, StringUtils.join(" ",classes));
        owner.getDocument().getStyleEngine().nodeChanged( owner );
    }
}

