package appstraction.app.style.attr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import appstraction.app.StringUtils;
import appstraction.app.style.dom.Node;

/**
 * Created by Michael.Hancock on 7/29/2014.
 */
@Slf4j
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
        log.debug("addClass "+clazz);
        super.set(owner, StringUtils.join(" ",classes));
        owner.getDocument().getStyleEngine().nodeChanged( owner );
    }

    public void removeClass(Node owner, String clazz ){
        classes.remove(clazz);
        
        log.debug("removeClass "+clazz);
        super.set(owner, StringUtils.join(" ",classes));
        owner.getDocument().getStyleEngine().nodeChanged( owner );
    }
}

