

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.jsonpath.JsonPath;

public class JsonEl{
	public final JsonNode node;
	
	public JsonEl(JsonNode n){
		node=n;
	}
	public JsonNode childNode(String... path){
		try{
			if(node != null){
			JsonNode curr = node;
			for(int i=0; i < path.length; i++)
				curr = curr.get(path[i]);
			return curr;
			}
		}catch(Exception e){
			warn("json prop (%s) access error (%s)",StringUtils.join((Object[])path) ,e.getMessage());
		}
		return null;
	}
	public String prop( String name ){
		try{
			if(node != null)
				return node.get(name).asText();
		}catch(Exception e){
			warn("json prop (%s) access error (%s)",name,e.getMessage());
		}
		return "";
	}
	public JsonEl child(String... path) {
		return new JsonEl(childNode(path));
	}
	public int size() {
		if(node!=null)
			return node.size();
		return 0;
	}
	
	public JsonEl get(int i) {
		try {
			if(node != null)
				return new JsonEl(node.get(i));
		} catch (Exception e) {
			warn( "json index (%s) access error (%s) ",i,e.getMessage());
		}
		return new JsonEl(null);
		
	}
	private void warn(String msg, Object... args) {
		System.err.printf(msg, args);
	}
	
	public JsonEl query(String jsonPath) {
		try{
			if(node != null)
				return new JsonEl(JsonPath.read(node, jsonPath));
		}catch(Exception e){
			warn( "query threw exception: query: %s doc:%s ex: %s",jsonPath,e.getMessage());
		}
		return new JsonEl(null);
	}	
	
	public String text( String path ){
		return query( path ).text();
	}
	
	public String text(){
		return node == null ? "" : node.asText();
	}
	
	public boolean has(String prop){
		return node == null ? false : node.has(prop);
	}
	
}