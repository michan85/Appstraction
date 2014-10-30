package appstraction.app.style.attr;

import appstraction.app.style.dom.Node;

/**
* Created by Michael.Hancock on 7/29/2014.
*/
public class SimpleAttr<T> implements Attr<T>{

    public SimpleAttr(){}
    public SimpleAttr(T v){this.v=v;}
    T v;
    public T get(Node owner) {
        return v;
    }

    public boolean set(Node owner, T v) {
        this.v=v;
        return true;
    }

}
