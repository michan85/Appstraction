package app.style.attr;

import app.style.css.StyleEngine;
import app.style.dom.Node;

/**
 * Created by Michael.Hancock on 7/29/2014.
 */
public class IdAttr extends SimpleAttr {

        private static final String EVENT_ID_CHANGED = "attr.id.changed";

    public IdAttr() {
    }

    public IdAttr(Object v) {
        super(v);
    }



        @Override
        public boolean set(Node owner, Object v) {
            if(super.set(owner,v)) {
                System.out.println("id attr set: "+v);
                owner.getDocument().getStyleEngine().nodeChanged( owner );
                return true;
            }
            return false;
        }
    }

