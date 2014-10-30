package appstraction.tools.dev_server;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import lombok.extern.slf4j.Slf4j;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

import appstraction.tools.dev_server.ServerUi.ServerListener;
import appstraction.tools.restool.ResToolHandler;
import appstraction.tools.wsgen.httpapi.WsGenHttpApi;


@Slf4j
public class App {
	private static ServerUi serverUi;
	File root = new File("~/.appserver");
	Preferences prefs = Preferences.userRoot();

	private String wwwRoot;
	private Undertow server;

	String host = "";
	String port = "";

	public void run() throws Throwable {

		wwwRoot = prefs.get("wwwRoot", "www");

		host = prefs.get("host", "localhost");
		port = prefs.get("port", "8080");
		mkdir(wwwRoot);

		createServer();
		showUI();

	}

	private void mkdir(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	private void createServer() throws Throwable {

		
		
		ResourceHandler wwwRootRes = resource(new FileResourceManager(new File(wwwRoot), 100))
				.setDirectoryListingEnabled(true).addWelcomeFiles("index.html");
		    
		server = Undertow
				.builder()
				
				.addHttpListener(Integer.parseInt(port), host)
				.setHandler(
					path(/*resource(new FileResourceManager(new File(wwwRoot), 100))*/wwwRootRes)
						.addPrefixPath("/",wwwRootRes)
						.addPrefixPath("/url", new UrlLogger())
						.addPrefixPath("/route", Handlers.pathTemplate().add("/{name}", new UrlLogger()))
						.addPrefixPath("/ws", websocket(new WebSocketApiHandler()))
						.addPrefixPath("/restool", new ResToolHandler())
						.addPrefixPath("/wsgen", new WsGenHandler())
						.addPrefixPath(appPath(WsGenHttpApi.class), restApp( WsGenHttpApi.class ) )
				)
				.build();

	}
	
	
	
	
	final ServletContainer container = ServletContainer.Factory.newInstance();
	final UndertowJaxrsServer restEasyDeployer = new UndertowJaxrsServer();
	
	public String appPath(Class<? extends Application> application){
		ApplicationPath appPath = (ApplicationPath) application.getAnnotation(ApplicationPath.class);
		String path = application.getName();
	    if (appPath != null) 
	    	path = appPath.value();
	    log.info("app path: {}",path);
	    return path;
	    
	}
	
	public HttpHandler restApp(Class<? extends Application> application) throws ServletException{
		DeploymentInfo deployment = restEasyDeployer.undertowDeployment(application, "/");
		log.info("ClassLoader: {}",application.getClassLoader());
		//deployment.setClassLoader(application.getClassLoader());
		deployment.setContextPath(appPath(application));
		
		deployment.setDeploymentName(application.getName()+" ("+appPath(application)+")");
	      
		DeploymentManager manager = container.addDeployment(deployment);
	    manager.deploy();
	    return manager.start();
	}

	public void showUI() {
		serverUi = new ServerUi(new ServerListener() {

			private boolean started;

			public void onStop() {
				if (started) {
					log("Server Stopping");

					try {
						server.stop();
						log("Server Stopped");
						started = false;
					} catch (Exception e) {
						log("failed " + e.getMessage());
						throw new RuntimeException(e);
					}
				}
			}

			public void onStart() {
				if (!started) {
					log("Starting Server");
					try {
						server.start();
						log("Server Started listening on " + getUrl());
						started = true;
					} catch (Exception e) {
						log("failed " + e.getMessage());
						throw new RuntimeException(e);
					}
				}
			}

			public String getUrl() {
				return "http://" + host + ":" + port;
			}

			public String getDir() {
				return wwwRoot;
			}
		});
	}

	public void log(String msg) {
		serverUi.log(msg);
		log.debug(msg);
	}

	public static void main(String[] args) throws Throwable {
		new App().run();

	}
}
