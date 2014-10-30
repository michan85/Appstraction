
package MobileRestService.IServices;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaArgument {

    @JsonProperty("name")
    private String _name;
    @JsonProperty("type")
    private String _type;
    @JsonProperty("isPathArg")    
    private Boolean _isPathArg;
    @JsonProperty("isQueryStringArg")
    private Boolean _isQueryStringArg;
    @JsonProperty("doc")
    private String _doc;

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

    public Boolean getIsPathArg() {
        return _isPathArg;
    }

    public void setIsPathArg(Boolean isPathArg) {
        this._isPathArg = isPathArg;
    }

	public boolean getIsQueryStringArg() {
		return _isQueryStringArg;
	}
	
	public void setIsQueryStringArg(Boolean isQueryStringArg){
		this._isQueryStringArg = isQueryStringArg;
	}
	
	public String getDoc() {
		return _doc;
	}
	
	public void setDoc(String doc){
		this._doc = doc;
	}

}
