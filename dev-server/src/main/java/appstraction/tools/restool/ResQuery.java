package appstraction.tools.restool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResQuery{
	private List<String> nameFilters = new ArrayList<String>();
	Set<ResType> includedTypes = new HashSet<ResType>();
	Set<ResType> excludedTypes = new HashSet<ResType>();
	
	
	
	public ResQuery addFilter(String... filters){
		for(String s: filters)
			nameFilters.add(s);
		return this;
	}

	public ResQuery include( ResType... type ){
		for(ResType t: type)
			includedTypes.add(t);
		return this;
	}
	public ResQuery exclude( ResType... type ){
		for(ResType t: type)
			excludedTypes.add(t);
		return this;
	}
	
	
	public boolean isMatch( ResEntry  e){
		
		if(excludedTypes.size() > 0 && excludedTypes.contains(e.getType())){
			return false;
		}
		
		if(includedTypes.size() > 0 && !includedTypes.contains(e.getType()))
			return false;
		
		for(String search : nameFilters)
			if(e.getName().matches(search))
				return true;
		return false;
	}
	
	public List<ResEntry> search(Map<String, ResEntry> index  ){
		List<ResEntry> result = new ArrayList<ResEntry>();
		for(ResEntry e: index.values()){
			if(isMatch(e)){
				result.add(e);
			}
		}
		return result;
	}
	
	public ResQuery process(ResIndex index, ResProcessor processor  ){
		
		for(ResEntry e: index.values()){
			if(isMatch(e)){
				processor.process(e);
			}
		}
		return this;
	}
}