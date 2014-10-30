
package MobileRestService.IServices;

import retrofit.http.GET;

public interface ISchemaService {


    @GET("/schema/")
    public Schema GenerateSchema();

    @GET("/test/")
    public String test();

}
