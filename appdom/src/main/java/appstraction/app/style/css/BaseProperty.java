package appstraction.app.style.css;


import appstraction.app.style.dom.Element;

/**
* Created by Michael.Hancock on 7/30/2014.
*/
public class BaseProperty implements StyleProperty {

    public BaseProperty(String name){
        this.name = name;
    }
    String name;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean apply(Element n) {
        return false;
    }


    public String getName() {
        return name;
    }

    Object value;


}
