package app.style.attr;

import app.style.dom.Node;

/**
* Created by Michael.Hancock on 7/29/2014.
*/
public class SimpleAttr<T> implements Attr<T>{

    public SimpleAttr(){}
    public SimpleAttr(T v){this.v=v;}
    T v;
    @Override
    public T get(Node owner) {
        return v;
    }

    @Override
    public boolean set(Node owner, T v) {
        this.v=v;
        return true;
    }

}
