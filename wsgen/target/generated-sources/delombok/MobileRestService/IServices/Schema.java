
package MobileRestService.IServices;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schema {

    @JsonProperty("Endpoints")
    private ArrayList<SchemaEndpoint> _Endpoints;
    @JsonProperty("Types")
    private ArrayList<SchemaType> _Types;

    public ArrayList<SchemaEndpoint> getEndpoints() {
        return _Endpoints;
    }

    public void setEndpoints(ArrayList<SchemaEndpoint> Endpoints) {
        this._Endpoints = Endpoints;
    }

    public ArrayList<SchemaType> getTypes() {
        return _Types;
    }

    public void setTypes(ArrayList<SchemaType> Types) {
        this._Types = Types;
    }

}
