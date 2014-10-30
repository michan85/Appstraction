
package MobileRestService.IServices;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaValue {
	
    @JsonProperty("Name")
    private String _Name;
    @JsonProperty("Value")
    private String _value;
    
    public String getName() {
        return _Name;
    }

    public void setName(String name) {
        this._Name = name;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        this._value = value;
    }


}
