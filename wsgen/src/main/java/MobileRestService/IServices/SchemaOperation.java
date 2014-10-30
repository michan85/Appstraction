
package MobileRestService.IServices;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaOperation {

    @JsonProperty("name")
    private String _name;
    @JsonProperty("path")
    private String _path;
    @JsonProperty("arguments")
    private ArrayList<SchemaArgument> _arguments;
    @JsonProperty("method")
    private String _method;
    @JsonProperty("result")
    private String _result;

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

    public ArrayList<SchemaArgument> getArguments() {
        return _arguments;
    }

    public void setArguments(ArrayList<SchemaArgument> arguments) {
        this._arguments = arguments;
    }

    public String getMethod() {
        return _method;
    }

    public void setMethod(String method) {
        this._method = method;
    }

    public String getResult() {
        return _result;
    }

    public void setResult(String result) {
        this._result = result;
    }

}
