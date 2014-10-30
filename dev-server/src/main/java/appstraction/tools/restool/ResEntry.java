package appstraction.tools.restool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResEntry{
	public ResEntry(ResType type, String name){
		this.id=id(type, name);
		this.type = type;
		this.name = name;
	}
	final String id;
	final ResType type;
	final String name;
	private List<File> locations = new ArrayList<File>();
	
	
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public ResType getType(){
		return type;
	}

	
	
	public static String id( ResType type, String name ){
		return type.name()+'/'+name;
	}
	public List<File> getLocations() {
		return locations;
	}
	public void addLocation(File f) {
		this.locations.add(f);
	}
}