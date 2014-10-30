package appstraction.tools.dev_server;


public interface RequestHandler{
	public void handleRequest(RequestContext ctx) throws Exception;
}