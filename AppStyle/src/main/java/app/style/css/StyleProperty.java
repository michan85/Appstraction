package app.style.css;

import app.style.dom.Element;
import app.style.dom.Node;

/**
 * Created by Michael.Hancock on 7/29/2014.
 */
public interface StyleProperty {
    String getName();
    Object getValue();
    void setValue( Object value );
    boolean apply(Element n);
}
