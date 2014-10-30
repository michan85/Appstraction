
package MobileRestService.IServices;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaEndpoint {

    @JsonProperty("name")
    private String _name;
    @JsonProperty("path")
    private String _path;
    @JsonProperty("operations")
    private ArrayList<SchemaOperation> _operations;
    @JsonProperty("ns")
    private String _ns;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String path) {
        this._path = path;
    }

    public ArrayList<SchemaOperation> getOperations() {
        return _operations;
    }

    public void setOperations(ArrayList<SchemaOperation> operations) {
        this._operations = operations;
    }

    public String getNs() {
        return _ns;
    }

    public void setNs(String ns) {
        this._ns = ns;
    }

}
