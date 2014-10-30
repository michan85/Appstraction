package appstraction.tools.restool;

import java.io.File;
import java.util.TreeMap;

public class ResIndex extends TreeMap<String, ResEntry>{
	
	public ResIndex(File srcDir){
		
		
		for(String dirName : srcDir.list()){
			File dir = new File( srcDir,dirName);
			if(!dir.isDirectory() || dir.isHidden()){
				continue;
			}
			
			for(ResType type: ResType.values())
			{
				if(dirName.startsWith(type.name())){
					
					for( String resPath: dir.list() ){
						File resFile = new File(dir,resPath);
						if(resFile.isDirectory() || resFile.isHidden())
							continue;
						
						//http://stackoverflow.com/questions/924394/how-to-get-file-name-without-the-extension
						String name = resFile.getName().replaceFirst("[.][^.]+$", "");
						ResEntry entry = get( ResEntry.id(type, name) );
						if(entry == null){
							entry = new ResEntry(type, name);
							put(entry.getId(), entry);
						}
						entry.addLocation(resFile);
					}
					
					break;
				}
			}
		}
	}
}