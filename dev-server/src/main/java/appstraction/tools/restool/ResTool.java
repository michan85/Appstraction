package appstraction.tools.restool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResTool {

	
	public static void main(String[] args){
		//TODO: add commandline api
	}
	
//	private void processRes(Map<String, ResEntry> resIndex, String search, ResProcessor resProcessor) {
//	System.out.println("processRes: "+search);
//	for(ResEntry e: resIndex.values()){
//		if(e.getName().matches(search)){
//			resProcessor.process(e);
//		}
//	}
//		
//}
	
	
	
	public ResTool() {

		// String srcPath = "C:\\dev\\svn\\Android\\old\\Android24\\trunk\\res";
		// String dstPath = "temp/test";
		

		

		// System.out.println("resources: "+resIndex.size());
		// for(ResEntry e: resIndex.values()){
		// System.out.println(e.getName() +
		// "   files:"+e.getLocations().size());
		// }
		//
		// String search = "weather.*";
		//
		// System.out.println("search: "+search);
		// for(ResEntry e: resIndex.values()){
		// if(e.getName().matches(search))
		// System.out.println("Match:   "+e.getName() +
		// "   files:"+e.getLocations().size());
		// }
		//
		
		
		//
		String srcPath = "C:\\dev\\svn\\Android\\Elections24\\trunk\\res";
		//String srcPath = "C:\\dev\\svn\\Android\\old\\Android24\\trunk\\res";
		String dstPath = "C:\\dev\\svn\\Androidv2\\modules\\android\\Android24.UI\\trunk\\Android24.UI\\src\\main\\res";
		File srcDir = new File(srcPath);
		File outDir = new File(dstPath);

		// srcDir=outDir;

		ResIndex index = new ResIndex(srcDir);
		ResProcessor action = null;
		ResAction resAction = ResAction.copy;
		String filter = ".*connect";
		ResType[] includes = new ResType[]{ ResType.drawable };

		switch (resAction) {
		case copy:
			action = COPY(outDir,null);
			break;
		case delete:
			action = DELETE();
			break;
		case list:
			action = LIST();
			break;
		case move:
			action = MOVE(outDir);
			break;
		case rename:
			String newName = "ic_action_social_share";
			action = RENAME( newName );
			break;
		default:
			System.out.println("invalid res action: " + resAction.name());
			break;
		}

		new ResQuery().include(includes).addFilter(filter).process(index, action);
		
	}

	public static ResProcessor COPY(final File outDir, final String newName) {
		return new ResProcessor() {
			public void process(ResEntry entry) {
				for (File f : entry.getLocations()) {
					copyFile(f, new File(new File(outDir, f.getParentFile().getName()), normalizeResName(newName == null ?  f.getName(): newName)));
				}
			}
		};
	};

	public static ResProcessor LIST() {
		return new ResProcessor() {
			public void process(ResEntry entry) {
				System.out.println(entry.getId());
			}
		};
	};

	public static ResProcessor MOVE(final File outDir) {
		return new ResProcessor() {
			public void process(ResEntry entry) {
				for (File f : entry.getLocations()) {
					moveFile(f, new File(new File(outDir, f.getParentFile().getName()), normalizeResName(f.getName())));
				}
			}
		};
	};

	public static ResProcessor RENAME(final String name) {
		return new ResProcessor() {
			public void process(ResEntry entry) {
				for (File f : entry.getLocations()) {
					log.info("renaming: {} {} {}", f.getName(),f.getName().split("\\."),f.getName().split("\\.").length);
					String[] exts = f.getName().split("\\.");
					File ren = new File(f.getParentFile(), normalizeResName(name)+"."+exts[exts.length-1]);
					log.info("RENAME:  {} to {}", f , ren);

					f.renameTo(ren);
				}
			}
		};
	};

	public static ResProcessor DELETE() {
		return new ResProcessor() {
			public void process(ResEntry entry) {
				for (File f : entry.getLocations()) {
					System.out.println("DELETE:   " + f);
					f.delete();
					// moveFile(f, new File(new File(outDir,
					// f.getParentFile().getName()), normalizeResName(
					// f.getName() )) );
				}
			}
		};
	};

	public static String normalizeResName(String name) {
		name = name.replaceAll("[^A-Za-z_.0-9]", "_");
		String[] parts = name.split("\\.");
		if (parts.length > 2) {
			for (int i = 0; i < parts.length - 1; i++) {
				name += parts[i];
			}
			name += "." + parts[parts.length - 1];
		}
		return name.toLowerCase();
	}

	public static boolean moveFile(File src, File dest) {
		System.out.println("MOVE:   " + src + " to :" + dest);
		return src.renameTo(dest);
	}

	public static boolean copyFileToDir(File src, File toDir) {
		if (!toDir.exists())
			if (!toDir.mkdirs())
				return false;
		return copyFile(src, new File(toDir, src.getName()));
	}

	public static boolean copyFile(File src, File dest) {
		if (!dest.getParentFile().exists())
			if (!dest.getParentFile().mkdirs())
				return false;
		System.out.println("Copy:   " + src + " to :" + dest);

		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			inStream = new FileInputStream(src);
			outStream = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
