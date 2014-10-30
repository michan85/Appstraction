package appstraction.tools.dev_server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import appstraction.app.Callback;




@SuppressWarnings("rawtypes")
public class RequestContext implements Callback<Object>{
	private String path = "";
	private String resolvedPath = "";
	private String relativePath = "";
	private Map<String, Object> args = new HashMap<String, Object>();
	
	private Callback cb;
	private Transform resultTransform;
	public RequestContext(String path, Callback cb){
		this.path = path;
		relativePath=path;
		resolvedPath=path;
		this.cb=  cb;
	}
	public String getRequestPath() {
		return path;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getResolvedPath() {
		return resolvedPath;
	}
	public void setResolvedPath(String resolvedPath) {
		this.resolvedPath = resolvedPath;
	}
	public void onResult(Object result) {
		
		try {
			if(resultTransform != null){
				
				cb.onResult(resultTransform.transform(result));
			}
			else
				cb.onResult(result);	
		} catch (Exception e) {
			onError(e);
		}
		
	}
	public void onError(Throwable error) {
		cb.onError(error);
	}
	public <T> T getArg(String name) {
		return (T)args.get(name);
	}
	
	
	public static interface Transform{
		Object transform(Object o);
	}
	
	
	public void setResultTransform(Transform t) {
		this.resultTransform = t;
	}
	public Transform getResultTransform(){
		return resultTransform;
	}
	
	//TODO: move to own package
	static Transform jsonTransform;
	public static Transform JsonTransform(){
		if(jsonTransform == null){
			jsonTransform = new Transform(){

				public Object transform(Object o) {
					
					try {
						return new ObjectMapper().writeValueAsString(o);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
		}
		return jsonTransform;
	}
	public void setArg(String key, Object val) {
		args.put(key, val);
	}
}