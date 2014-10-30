
package MobileRestService.IServices;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaType {

    @JsonProperty("id")
    private String _id;
    @JsonProperty("ns")
    private String _ns;
    @JsonProperty("name")
    private String _name;
    @JsonProperty("typeArgs")
    private ArrayList<String> _typeArgs;
    @JsonProperty("type")
    private String _type;
    @JsonProperty("properties")
    private ArrayList<SchemaProperty> _properties;
    @JsonProperty("values")
	private ArrayList<SchemaValue> _values;
    @JsonProperty("extends")
	private String _extends;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getNs() {
        return _ns;
    }

    public void setNs(String ns) {
        this._ns = ns;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public ArrayList<String> getTypeArgs() {
        return _typeArgs;
    }

    public void setTypeArgs(ArrayList<String> typeArgs) {
        this._typeArgs = typeArgs;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public ArrayList<SchemaProperty> getProperties() {
        return _properties;
    }

    public void setProperties(ArrayList<SchemaProperty> properties) {
        this._properties = properties;
    }

	public ArrayList<SchemaValue> getValues() {
		return this._values;
	}
	
	public void setValues(ArrayList<SchemaValue> values) {
        this._values = values;
    }
	
	public String getExtends(){
		return _extends;
	}
	public void setExtends(String ext){
		_extends = ext;
	}

}
