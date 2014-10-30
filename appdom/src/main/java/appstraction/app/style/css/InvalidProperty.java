package appstraction.app.style.css;

/**
* Created by Michael.Hancock on 7/30/2014.
*/
public class InvalidProperty extends BaseProperty {

    public Exception getException() {
        return exception;
    }

    private final Exception exception;


    public InvalidProperty(String name, Exception e, Object value) {
        super(name);
        this.exception = e;
        setValue(value);
    }


}
