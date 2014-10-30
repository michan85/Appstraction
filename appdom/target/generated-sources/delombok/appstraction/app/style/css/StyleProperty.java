package appstraction.app.style.css;

import appstraction.app.style.dom.Element;

/**
 * Created by Michael.Hancock on 7/29/2014.
 */
public interface StyleProperty {
    String getName();
    Object getValue();
    void setValue( Object value );
    boolean apply(Element n);
}
