package appstraction.tools.wsgen.httpapi;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import appstraction.tools.wsgen.WsGenerator;

@ApplicationPath("/wsgen")
public class WsGenHttpApi extends Application {

	//http://www.mastertheboss.com/resteasy/resteasy-tutorial/page-2
	//http://www.mastertheboss.com/resteasy/resteasy-tutorial-part-two-web-parameters
	
	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(WsGenApi.class);
		return classes;
	}
	@Path("api")
	public static class WsGenApi {
		@GET
		@Produces("text/plain")
		@Path("hello")
		public String get() {
			return "hello world";
		}
		
		@GET
		@Produces("application/json")
		@Path("echo")
		public Object echo(@QueryParam("msg") final String mmsg) {
			
			WsGenerator.Options options = new WsGenerator.Options();
			
			options.setNs("com.android24.services");
			options.setOut("\\dev\\temp\\");
			options.setUrl("http://betadev.24.com/wsmobile/Schema.svc/schema/Comments,Articles,Multimedia,RelatedArticle,Vote,Weather,Components");
			new WsGenerator(options).generate();
			
			return new Object(){
				public String msg = mmsg;
			};
		}
		
		@GET
		@Produces("application/json")
		@Consumes("application/json")
		@Path("generate")
		public Object generate(WsGenerator.Options options) {
			
			new WsGenerator(options).generate();
			
			return new Object(){
				public String msg = "test";
			};
		}
		
		
	}

}
