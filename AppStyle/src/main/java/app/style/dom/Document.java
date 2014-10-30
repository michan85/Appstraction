package app.style.dom;

import java.util.HashMap;
import java.util.Map;

import app.style.css.StyleEngine;
import app.style.css.StyleProperty;

/**
 * Created by Michael.Hancock on 7/30/2014.
 */
public abstract class Document<T> extends Element<T> {

    public Document(){
        super(null);
        setDocument(this);
        setContainer(true);
        setNodeName("Document");
    }

    public StyleEngine getStyleEngine() {
        return styleEngine;
    }

    public void setStyleEngine(StyleEngine styleEngine) {
        this.styleEngine = styleEngine;
        styleEngine.setDocument(this);
    }

    StyleEngine styleEngine;

    abstract public Element createElement(String tag);

    public abstract Element el(T obj);

//    Map<String, Class> attrFactory = new HashMap<String, Class>();
//    public StyleProperty createAttr(String attr, String value) {
//        try {
//            if(attrFactory.containsKey(attr)) {
//                StyleProperty prop = (StyleProperty) attrFactory.get(attr).newInstance();
//                App.log().debug(this, "style attr %s: %s; ", attr,value);
//                prop.setValue(value);
//                return prop;
//            }else{
//                App.log().debug(this, "style attr not implemented "+attr);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new InvalidProperty(attr,e, value);
//        }
//        return new NoImpProperty(attr);
//    }
}
