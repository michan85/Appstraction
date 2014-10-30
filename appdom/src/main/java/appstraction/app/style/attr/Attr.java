package appstraction.app.style.attr;

import appstraction.app.style.dom.Node;

/**
* Created by Michael.Hancock on 7/29/2014.
*/
public interface Attr<T> {
    public T get(Node owner);
    public boolean set(Node owner,T v);
}
