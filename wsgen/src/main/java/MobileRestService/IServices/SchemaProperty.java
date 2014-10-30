
package MobileRestService.IServices;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaProperty {

    @JsonProperty("name")
    private String _name;
    @JsonProperty("type")
    private String _type;
    @JsonProperty("required")
    private Boolean _required;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public Boolean getRequired() {
        return _required;
    }

    public void setRequired(Boolean required) {
        this._required = required;
    }

}
