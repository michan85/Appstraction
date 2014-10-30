package appstraction.tools.wsgen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.http.POST;

import MobileRestService.IServices.Schema;
import MobileRestService.IServices.SchemaArgument;
import MobileRestService.IServices.SchemaEndpoint;
import MobileRestService.IServices.SchemaOperation;
import MobileRestService.IServices.SchemaProperty;
import MobileRestService.IServices.SchemaType;
import MobileRestService.IServices.SchemaValue;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JPrimitiveType;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JType;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;


/**
 * 
 * A Webservice Generator Tool
 * 
 * Example Usage:
 * 
 * wsgen 	-ns com.android24.services  // the output namespace
 * 		 	-extensionDir ..\wsgentest\src\extensions // the dir where to load type extensions from
 * 			-out ..\..\..\Android24.UI\trunk\src  // the ouput directory
 * 			-url http://betadev.24.com/wsmobile/Schema.svc/schema/Comments,Articles,Multimedia,RelatedArticle,Vote,Weather,Components
 * 			-reuseService http://localhost:65084/Schema.svc/schema/Comments,Articles,Multimedia,RelatedArticle@..\..\..\Android24.UI\trunk\bin\android24.ui.jar@com.android24.services"
 * 
 * @author Michael.Hancock
 *
 */
public class WsGenerator {

	public static void main(String[] args) {
		Options cli = new Options();
		new JCommander(cli, args);
		new WsGenerator(cli).generate();
	}

	public static class Options {
		@Parameter(names = "-ns")
		private String ns;
		
		@Parameter(names = "-excludeNs",description="exclude a list of types by NameSpace eg Common.Entities;Common.Entities.Sport")
		private String excludeNs = "";
		
		@Parameter(names = "-excludeIds",description="exclude a list of types by type ids eg Common.Entities.Article;Common.Entities.EntityListOfArticle")
		private String excludeIds = "";
		
		@Parameter(names = "-excludeNames",description="exclude a list of types by Class Names eg Article;EntityListOfArticle")		
		private String excludeNames = "";
		
		@Parameter(names = "-reuseJars",description="reuse types from jars - format: path@ns1,ns2;path2@nsX \n\teg: ../path/to/lib.jar@namespace,namespace;../path/to/another.jar@com.android24.services")
		private String reuseJars = "";
		
		@Parameter(names = "-reuseService",description="reuse types from another service(s) - format: url@pathToJar@serviceNamesapce ")
		private String reuseService = "";
		
		@Parameter(names = "-out", description="the output directory")
		private String out;

		@Parameter(names = "-url")
		private String url;
		
		@Parameter(names = "-extensionDir")
		private String extensionDir;
		
		public String getNs() {
			return ns;
		}

		public void setNs(String ns) {
			this.ns = ns;
		}

		public String getExcludeNs() {
			return excludeNs;
		}

		public void setExcludeNs(String excludeNs) {
			this.excludeNs = excludeNs;
		}

		public String getExcludeIds() {
			return excludeIds;
		}

		public void setExcludeIds(String excludeIds) {
			this.excludeIds = excludeIds;
		}

		public String getExcludeNames() {
			return excludeNames;
		}

		public void setExcludeNames(String excludeNames) {
			this.excludeNames = excludeNames;
		}

		public String getReuseJars() {
			return reuseJars;
		}

		public void setReuseJars(String reuseJars) {
			this.reuseJars = reuseJars;
		}

		public String getReuseService() {
			return reuseService;
		}

		public void setReuseService(String reuseService) {
			this.reuseService = reuseService;
		}

		public String getOut() {
			return out;
		}

		public void setOut(String out) {
			this.out = out;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getExtensionDir() {
			return extensionDir;
		}

		public void setExtensionDir(String extensionDir) {
			this.extensionDir = extensionDir;
		}

	}

	public WsGenerator() {

	}
	
	static String argDelim = ";";

	public WsGenerator(Options options) {
		this.options = options;

		System.out.println("Options: ");
		System.out.println("Namespace: " + this.options.ns);
		System.out.println("url: " + this.options.url);
		System.out.println("out: " + this.options.out);
		System.out.println("reuseJars: " + this.options.reuseJars);
		System.out.println("reuseService: " + this.options.reuseService);
		
		System.out.println("excludeNs: " + this.options.excludeNs);
		System.out.println("excludeNames: " + this.options.excludeNames);
		System.out.println("excludeIds: " + this.options.excludeIds);
		System.out.println("extensionDir: " + this.options.extensionDir);
	}

	HashMap<String, BaseGenType> baseTypes = new HashMap<String,BaseGenType>();

	public static class BaseGenType extends TypeEntry {
		public BaseGenType(JClass jclass, BaseType e) {
			super((SchemaType)null);
			this.jclazz = jclass;
			this.baseType = e;
		}

		public BaseType baseType;
	}

//	public static class GenType {
//		public GenType(JDefinedClass jclass, SchemaType e) {
//			this.clazz = jclass;
//			this.type = e;
//		}
//
//		public JClass clazz;
//		public SchemaType type;
//	}

	HashMap<String, JClass> genericTypes = new HashMap<String, JClass>();
	
	HashMap<String, TypeEntry> reusedTypes = new HashMap<String, TypeEntry>();

	HashMap<String, TypeEntry> generatedTypes = new HashMap<String, TypeEntry>();

	HashMap<String, TypeEntry> typeIndex = new HashMap<String, TypeEntry>();

	Map<String, ArrayList<String>> inheritance = new HashMap<String, ArrayList<String>>();

	Map<String, TypeExtension> extensions = new HashMap<String, TypeExtension>();

	static class TypeEntry {
		SchemaType type;
		JType jclazz;
		Class<?> clazz;
		boolean generated = false;
		JTypeVar	genericArg;

		public TypeEntry(SchemaType t) {
			this.type = t;
		}
		
		public TypeEntry(JTypeVar  t) {
			this.genericArg = t;
		}
		
		public TypeEntry(SchemaType t, JClass clazz) {
			this.type = t;
			this.jclazz = clazz;
		}
	}

	private Options options;

	private JCodeModel codeModel;

	//JClass GeneratedEnumType;

	private JPrimitiveType	IntType;
	public void generate() {
		try {
			this.codeModel = new JCodeModel();

			File outputDir = new File(options.out);
			outputDir.mkdirs();

			for (BaseType baseType : BaseType.class.getEnumConstants()) {

				baseTypes.put(baseType.name(),new BaseGenType(baseType.getJType(codeModel),baseType));
			}

			// "http://localhost:65084/Schema.svc/schema/"
			Schema schema = loadSchema(options.url);
			ArrayList<SchemaType> types = schema.getTypes();
			ArrayList<SchemaEndpoint> endpoints = schema.getEndpoints();

			loadExtensions();
			
			processExcludes(schema);
			processReuseJars();
			processReuseServices(schema);
			
			// index the types
			for (SchemaType type : types) {
				typeIndex.put(type.getId(), new TypeEntry(type));
				if (StringUtils.isNotEmpty(type.getExtends())) {
					ArrayList<String> entry = inheritance
							.get(type.getExtends());
					if (entry == null) {
						entry = new ArrayList<String>();
						inheritance.put(type.getExtends(), entry);
					}
					entry.add(type.getId());
				}
			}
			IntType = JPrimitiveType.parse(codeModel, "int");
			
			//GeneratedEnumType = createGeneratedEnumType();

			for (SchemaType type : types) {
				generateType(type);
			}

			processInheritance(types);

			generateEndpoints(endpoints);

			System.out.println("writing to filesystem");
			codeModel.build(outputDir);
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Schema loadSchema(String url) throws FileNotFoundException, MalformedURLException, IOException, JsonParseException, JsonMappingException {
		String json;
		if (url.startsWith("file://"))
			json = stream2String(new FileInputStream(new File(
					url.replace("file://", ""))));
		else
			json = fetchUrl(url);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		Schema schema = mapper.readValue(json, Schema.class);
		return schema;
	}

	private void processReuseJars() {
		if(StringUtils.isNotEmpty( options.reuseJars)){
			try {
				
			
			String entries[] = options.reuseJars.split(argDelim);
			for (String entry : entries) {
				String[]parts = entry.split("@");
				String path = parts[0];
				String[] ns = parts[1].split(",");
				JarFile jarFile = new JarFile(path);
				Enumeration e = jarFile.entries();
				URL[] urls = { new URL("jar:file:" + path+"!/") };
	            URLClassLoader cl = URLClassLoader.newInstance(urls);
	            
	            while (e.hasMoreElements()) {
	                JarEntry je = (JarEntry) e.nextElement();
	                if(je.isDirectory() || !je.getName().endsWith(".class")){
	                    continue;
	                }
	                // -6 because of .class
	                String className = je.getName().substring(0,je.getName().length()-6);
	                className = className.replace('/', '.');
	                for (String n : ns) {
						if(className.startsWith(n)){
							System.out.println("reusing type : " + className);
							Class c = cl.loadClass(className);
							codeModel._ref(c);
						}
					}
	            }
			}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
            

            
		}
	}
	
	private void processReuseServices(Schema schema){
		
		if(StringUtils.isNotEmpty( options.reuseService)){
			try {
				
				String entries[] = options.reuseService.split(argDelim);
				for (String entry : entries) {
					String[]parts = entry.split("@");
					String url = parts[0];
					String pathToJar = parts[1];
					String ns = parts[2];
					
					
					Schema libSchema = loadSchema(url);
					Map<String, TypeEntry > typeIndex = new HashMap<String, TypeEntry>();
					for(SchemaType type: libSchema.getTypes()){
						typeIndex.put(ns+"."+type.getName(), new TypeEntry(type));
					}
					
					
					JarFile jarFile = new JarFile(pathToJar);
					Enumeration e = jarFile.entries();
					URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
		            URLClassLoader cl = URLClassLoader.newInstance(urls);
		            
		            while (e.hasMoreElements()) {
		                JarEntry je = (JarEntry) e.nextElement();
		                if(je.isDirectory() || !je.getName().endsWith(".class")){
		                    continue;
		                }
		                // -6 because of .class
		                String className = je.getName().substring(0,je.getName().length()-6);
		                className = className.replace('/', '.');
		                TypeEntry typeEntry = typeIndex.get(className);
		                if(typeEntry != null){
							System.out.println("reusing schema type : " + className);
							Class c = cl.loadClass(className);
							typeEntry.clazz = c;
							typeEntry.jclazz = codeModel._ref(c);
							this.reusedTypes.put(typeEntry.type.getId(), typeEntry);
							this.typeIndex.put(typeEntry.type.getId(), typeEntry);
							SchemaType typeToRemove = null;
							for(SchemaType existingType : schema.getTypes()){
								if(existingType.getId().equals(typeEntry.type.getId())){
									typeToRemove = existingType;
									break;
								}
							}
							if(typeToRemove != null)
								schema.getTypes().remove(typeToRemove);
						}
		            }
				}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
            

            
		}
		
	}
	

	protected void loadExtensions() throws Exception {
		// load extensions
		if (StringUtils.isNotEmpty(options.extensionDir)) {
			File extDir = new File(options.extensionDir);
			if (extDir.exists()) {
				String[] files = extDir.list();
				for (int i = 0; i < files.length; i++) {
					System.out.println("Processing extension: " + files[i]);
					File file = new File(extDir.getAbsolutePath() + "/"
							+ files[i]);
					TypeExtension ext = readExtension(file);
					if (extensions.containsKey(ext.typeId))
						throw new RuntimeException(
								"duplicate extensions for type: "
										+ ext.typeId
										+ "\n first declated in:"
										+ ext.file + "  duplicate: "
										+ files[i]);

					extensions.put(ext.typeId, ext);
				}
			}
		}
	}

	protected Map<String, SchemaType> processExcludes(Schema schema) {
		Map<String,SchemaType> exclude = new HashMap<String,SchemaType>();
		
		if(StringUtils.isNotEmpty(options.excludeNs)){
			
			String ns[] = options.excludeNs.split(argDelim);
			for (String n : ns) {
				for (SchemaType type : schema.getTypes()) {
					if(type.getNs().equalsIgnoreCase(n)){
						System.out.println("excluding type (ns match: "+n+"): "+ type.getId());
						exclude.put(type.getId(),type);
					}
				}					
			}
			
		}
		
		if(StringUtils.isNotEmpty(options.excludeIds)){
			
			String ids[] = options.excludeIds.split(argDelim);
			for (String id : ids) {
				for (SchemaType type : schema.getTypes()) {
					if(type.getId().equalsIgnoreCase(id)){
						System.out.println("excluding type (id match: "+id+"): "+ type.getId());
						exclude.put(type.getId(),type);
					}
				}					
			}
			
		}
		
		if(StringUtils.isNotEmpty(options.excludeNames)){
			
			String names[] = options.excludeNames.split(argDelim);
			for (String n : names) {
				for (SchemaType type : schema.getTypes()) {
					if(type.getName().equalsIgnoreCase(n)){
						System.out.println("excluding type (name match: "+n+"): "+ type.getId());
						exclude.put(type.getId(),type);
					}
				}					
			}
			
		}
		schema.getTypes().removeAll(exclude.values());
		for(SchemaType type : exclude.values()){
			typeIndex.put(type.getId(), new TypeEntry(type,codeModel.directClass(getNs(type.getNs()) + "." + type.getName())) );
			
		
		}
		return exclude;
	}

	private JClass createGeneratedEnumType() {
		JDefinedClass enumClass = null;
		try {
			enumClass = codeModel._package(options.ns)._interface(JMod.PUBLIC,"GeneratedEnum");
			enumClass.method(JMod.NONE, IntType, "value");
		} catch (JClassAlreadyExistsException e) {
			e.printStackTrace();
		}
		return enumClass;
	}

	static class TypeExtension {
		public String file = "";
		public String typeId = "";
		public ArrayList<String> imports = new ArrayList<String>();
		public String body = "";
	}

	enum ExtesionSection {
		Import, Body
	}

	private TypeExtension readExtension(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			if (!line.startsWith("//<TypeId>")) {
				throw new RuntimeException(
						"Error parsing extension file: "
								+ file.getAbsolutePath()
								+ "\n\t //<TypeId>TYPENAME</TypeId> must be the first line in the file");
			} else if (!line.contains("</TypeId>")) {
				throw new RuntimeException("Error parsing extension file: "
						+ file.getAbsolutePath()
						+ "\n\t typeid tag not closed </TypeId> missing");
			}
			TypeExtension ext = new TypeExtension();
			ext.file = file.getAbsolutePath();
			ext.typeId = line.replace("//<TypeId>", "")
					.replace("</TypeId>", "").trim();

			ExtesionSection currSection = null;

			String exptectedEnd = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					if (currSection == null) {

						for (ExtesionSection sec : ExtesionSection.values()) {
							if (line.startsWith("//<" + sec.name() + ">")) {
								exptectedEnd = "//</" + sec.name() + ">";
								currSection = sec;
								sb = new StringBuilder();
								sb.append("//<" + sec.name() + ">");
								sb.append("\n");
								break;
							}
						}
					} else {
						if (line.startsWith(exptectedEnd)) {

							sb.append("\n");
							sb.append(exptectedEnd);
							sb.append("\n");
							switch (currSection) {
							case Body:
								ext.body = sb.toString();
								break;
							case Import:
								String[] imports = sb.toString().split("\n");
								for (String imp : imports) {
									if (StringUtils.isNotEmpty(imp.trim()))
										ext.imports.add(imp.trim());
								}

								break;
							}
							currSection = null;

						} else {
							sb.append(line);
							sb.append('\n');
						}
					}
				}

			}
			return ext;
		} finally {
			br.close();
		}
	}

	private void processInheritance(ArrayList<SchemaType> types) {
		// TODO: impmenenet with custom deserializers..
		// http://programmerbruce.blogspot.com/2011/05/deserialize-json-with-jackson-into.html

		// for(SchemaType schemaType: types)if
		// (BaseType.Object.name().equalsIgnoreCase(schemaType.getType())){
		//
		// try{
		// JDefinedClass jclass =
		// (JDefinedClass)resolveType(schemaType.getId());
		// ArrayList<String> knownTypes = inheritance.get(schemaType.getId());
		// if(knownTypes != null){
		// JFieldVar typeField = defineProperty(jclass, "__type__",
		// resolveType("String"));
		//
		// // @JsonTypeInfo(
		// // use = JsonTypeInfo.Id.NAME,
		// // include = JsonTypeInfo.As.PROPERTY,
		// // property = "type")
		// JAnnotationUse typeInfoAnno = jclass.annotate(JsonTypeInfo.class);
		// typeInfoAnno.param("use", staticRef(JsonTypeInfo.Id.class,"NAME"));
		// typeInfoAnno.param("include",
		// staticRef(JsonTypeInfo.As.class,"PROPERTY"));
		// typeInfoAnno.param("property", typeField.name());
		//
		// // @JsonSubTypes({
		// // @Type(value = BatchTransform.class, name = "transform"),
		// // @Type(value = UpdateToSnapshot.class, name = "snapshot") })
		//
		// JAnnotationUse subTypesAnno = jclass.annotate(JsonSubTypes.class);
		// JAnnotationArrayMember subTypes = subTypesAnno.paramArray("value");
		// for(String subClassId : knownTypes){
		// JClass subType = resolveType(subClassId);
		// subTypes.annotate(JsonSubTypes.Type.class)
		// .param("value",subType )
		// .param("name",subType.fullName());
		// }
		// }
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
	}

	private void generateEndpoints(ArrayList<SchemaEndpoint> endpoints)
			throws JClassAlreadyExistsException {
		for (SchemaEndpoint schemaEndpoint : endpoints) {

			JPackage pkg = codeModel._package(getNs(schemaEndpoint.getNs()));
			JDefinedClass endpoint = pkg._interface(JMod.PUBLIC,
					schemaEndpoint.getName() + "Service");
			System.out.println("generating Service: "
					+ schemaEndpoint.getName() + "Service");
			for (SchemaOperation operation : schemaEndpoint.getOperations()) {
				TypeEntry typeEntry = resolveType(operation.getResult());

				JMethod sync = null;
				if(typeEntry.jclazz == null)
					sync = endpoint.method(JMod.PUBLIC, typeEntry.clazz,operation.getName());
				else
					sync = endpoint.method(JMod.PUBLIC, typeEntry.jclazz,operation.getName());
				
				JMethod async = endpoint.method(JMod.PUBLIC, Void.TYPE,
						operation.getName());

				Class<? extends Annotation> httpMethod = "GET"
						.equalsIgnoreCase(operation.getMethod()) ? retrofit.http.GET.class
						: retrofit.http.POST.class;

				String path = operation.getPath();
				if (!path.startsWith("/"))
					path = "/" + path;

				// strip any params in the path, they should be given as
				// arguments with type query
				String queryStringRegex = "(\\&?\\w+\\=\\{\\w+\\})";

				// why the .*? you ask? because string.matches sucks!!!!
				// http://stackoverflow.com/questions/10578254/java-string-matches-and-replaceall-differ-in-matching-parentheses
				if (path.matches(".*?" + queryStringRegex)) {
					String oldPath = path;
					path = path.replaceAll(queryStringRegex, "");
					if (path.endsWith("?")) // sometimes there is a trailing ?
						path = path.substring(0, path.length() - 1);
					System.out
							.println("WARN: path contains querystring params.. "
									+ schemaEndpoint.getName()
									+ "."
									+ operation.getName()
									+ "  patch changed from "
									+ oldPath
									+ "  to " + path);
				}
				
				if(path.startsWith("//"))
					path.replaceFirst("//", "/");

				sync.annotate(httpMethod).param("value", path);
				async.annotate(httpMethod).param("value", path);

				for (SchemaArgument arg : operation.getArguments()) {
					TypeEntry atype = resolveType(arg.getType());
					JVar sParam = null;
					JVar asParam = null;
					if(atype.jclazz == null){
						asParam = async.param(atype.clazz, arg.getName());
						sParam = sync.param(atype.clazz, arg.getName());
					}else{
						asParam = async.param(atype.jclazz, arg.getName());
						sParam = sync.param(atype.jclazz, arg.getName());
					}

					if (arg.getIsQueryStringArg()) {
						sParam.annotate(retrofit.http.Query.class).param(
								"value", arg.getName());
						asParam.annotate(retrofit.http.Query.class).param(
								"value", arg.getName());
					} else if (arg.getIsPathArg()) {
						sParam.annotate(retrofit.http.Path.class).param(
								"value", arg.getName());
						asParam.annotate(retrofit.http.Path.class).param(
								"value", arg.getName());
					} else if (httpMethod == POST.class) {
						sParam.annotate(retrofit.http.Body.class);
						asParam.annotate(retrofit.http.Body.class);
					}
				}
				if(typeEntry.jclazz == null){
					async.param(codeModel.ref(Callback.class).narrow(typeEntry.clazz), "cb");
				}else{
					async.param(codeModel.ref(Callback.class).narrow(typeEntry.jclazz), "cb");	
				}
				
			}

		}
	}

	/*
	 * private void generateEndpoints_Retrofit(ArrayList<SchemaEndpoint>
	 * endpoints) throws JClassAlreadyExistsException { for (SchemaEndpoint
	 * schemaEndpoint : endpoints) { JPackage pkg = codeModel
	 * ._package(getNs(schemaEndpoint.getNs())); JDefinedClass endpoint =
	 * pkg._interface(JMod.PUBLIC, schemaEndpoint.getName());
	 * 
	 * for (SchemaOperation operation : schemaEndpoint.getOperations()) { JClass
	 * type= resolveType(operation.getResult());
	 * 
	 * JMethod sync = endpoint.method(JMod.PUBLIC,type,operation.getName());
	 * JMethod async =
	 * endpoint.method(JMod.PUBLIC,Void.TYPE,operation.getName());
	 * 
	 * Class<? extends Annotation> httpMethod =
	 * "GET".equalsIgnoreCase(operation.getMethod()) ? retrofit.http.GET.class :
	 * retrofit.http.POST.class;
	 * 
	 * String path = operation.getPath(); if(!path.startsWith("/")); path =
	 * "/"+path; sync.annotate(httpMethod).param("value", path);
	 * async.annotate(httpMethod).param("value", path);
	 * 
	 * 
	 * for (SchemaArgument arg : operation.getArguments()) { JClass atype=
	 * resolveType(arg.getType()); JVar sParam =
	 * sync.param(atype,arg.getName()); JVar asParam =
	 * async.param(atype,arg.getName());
	 * 
	 * if (arg.getIsPathArg()) {
	 * sParam.annotate(retrofit.http.Path.class).param("value", arg.getName());
	 * asParam.annotate(retrofit.http.Path.class).param("value", arg.getName());
	 * } else if (arg.getIsQueryStringArg()) {
	 * sParam.annotate(retrofit.http.Query.class).param("value", arg.getName());
	 * asParam.annotate(retrofit.http.Query.class).param("value",
	 * arg.getName()); } else if (httpMethod == POST.class) {
	 * sParam.annotate(retrofit.http.Body.class);
	 * asParam.annotate(retrofit.http.Body.class); } }
	 * async.param(codeModel.ref(Callback.class).narrow(type), "cb"); } } }
	 */
	private void generateType(SchemaType type) {
		if (BaseType.Object.name().equalsIgnoreCase(type.getType())) {
			generateSimpleType(type);
		} else if (BaseType.Enum.name().equalsIgnoreCase(type.getType())) {
			generateEnum(type);
		} else if (BaseType.List.name().equalsIgnoreCase(type.getType())
				|| BaseType.Map.name().equalsIgnoreCase(type.getType())) {
			generateCollectionType(type);
		}else if(BaseType.Synthetic.name().equalsIgnoreCase(type.getType())){
			generateSyntheticType(type);
		}
	}

//	private void generateEnum(SchemaType schemaType) {
//		TypeEntry typeEntry = typeIndex.get(schemaType.getId());
//		if (typeEntry.clazz != null)
//			return;
//
//		JDefinedClass jclass;
//		try {
//			jclass = codeModel._class(getNs(schemaType.getNs()) + "."+ schemaType.getName());
//		} catch (JClassAlreadyExistsException e) {
//			System.out.println("Duplicate Type: " + schemaType.getId());
//			e.printStackTrace();
//			return;
//		}
//		
//		if (schemaType.getExtends() != null
//				&& (!schemaType.getExtends().trim().equals(""))) {
//			jclass._extends(resolveType(schemaType.getExtends()));
//		}
//
//		typeEntry.clazz = jclass;
//		System.out.println("generating Type: " + jclass.fullName());
//		
//		
//		
//		
//		for (SchemaValue v : schemaType.getValues()) {
//			
//			JEnumConstant enumVal = jclass.enumConstant(v.getName());
//			enumVal.arg(JExpr.lit(Integer.parseInt(v.getValue())));
//		}
//		
//		
//		processTypeExtensions(schemaType, jclass);
//		
//
////		JFieldVar prop = jclass.field(JMod.FINAL | JMod.PUBLIC, Integer.class,
////				"value");
////
////		JMethod constr = jclass.constructor(JMod.NONE);
////		constr.param(Integer.class, "value");
////		constr.body().assign(JExpr._this().ref(prop), JExpr.ref("value"));
////
////		for (SchemaValue v : schemaType.getValues()) {
////			JEnumConstant enumVal = jclass.enumConstant(v.getName());
////			enumVal.arg(JExpr.lit(Integer.parseInt(v.getValue())));
////		}
////
////		typeEntry.clazz = jclass;
//	}
//	
//	public static final class TestEnum{
//		private TestEnum(String string, int i) {}
//
//		public static final TestEnum ONE = new TestEnum( "ONE", 45 );
//	}
	
	private void generateEnum(SchemaType schemaType) {
		TypeEntry typeEntry = typeIndex.get(schemaType.getId());
		if (typeEntry.jclazz != null)
			return;

		JDefinedClass jclass;
		try {
			jclass = codeModel._package(getNs(schemaType.getNs()))._enum(
					schemaType.getName());
		} catch (JClassAlreadyExistsException e) {
			System.out.println("Duplicate Type: " + schemaType.getId());
			e.printStackTrace();
			return;
		}

		
		//jclass._implements(GeneratedEnumType);

		JFieldVar _value = jclass.field(JMod.FINAL | JMod.PRIVATE, IntType,"_value");
		
		JMethod constr = jclass.constructor(JMod.NONE);
		constr.body().assign(JExpr._this().ref(_value), constr.param(Integer.class, "value"));
		
		JMethod value = jclass.method(JMod.PUBLIC, IntType, "value");
		value.body()._return(_value);
		
		JMethod intCreator = jclass.method(JMod.PUBLIC | JMod.STATIC, jclass, "fromInt");
		
		JVar intValParam = intCreator.param(IntType, "val");
		JForEach iforEach = intCreator.body().forEach(jclass, "entry", jclass.staticInvoke("values"));
		iforEach.body()._if(intValParam.eq(iforEach.var().invoke("value")))
			._then()._return(iforEach.var());
		intCreator.body()._throw( JExpr._new(codeModel.ref(IllegalArgumentException.class)) );
		
		
		JMethod stringCreator = jclass.method(JMod.PUBLIC | JMod.STATIC, jclass, "fromString");
		stringCreator.annotate(JsonCreator.class);
		JVar stringValParam = stringCreator.param(String.class, "val");
		JForEach sforEach = stringCreator.body().forEach(jclass, "entry", jclass.staticInvoke("values"));
		sforEach.body()._if(sforEach.var().invoke("name").invoke("equalsIgnoreCase").arg(stringValParam))
			._then()._return(sforEach.var());
		JTryBlock _try = stringCreator.body()._try();
		_try.body()._return( jclass.staticInvoke(intCreator).arg( codeModel.ref(Integer.class).staticInvoke("parseInt") .arg(stringValParam) ) );
		_try._catch( codeModel.ref(Exception.class) );
							
		stringCreator.body()._throw( JExpr._new(codeModel.ref(IllegalArgumentException.class)) );
		
		
		JMethod toString = jclass.method(JMod.PUBLIC, codeModel.ref(String.class), "toString");
		toString.body()._return(_value.plus(JExpr.lit("")));
		
		
				 		

		for (SchemaValue v : schemaType.getValues()) {
			JEnumConstant enumVal = jclass.enumConstant(v.getName());
			enumVal.arg(JExpr.lit(Integer.parseInt(v.getValue())));
		}

		typeEntry.jclazz = jclass;
		
	}

	
	
	private void generateCollectionType(SchemaType type) {
		TypeEntry typeEntry = typeIndex.get(type.getId());
		if (typeEntry.jclazz != null)
			return;

		ArrayList<JClass> typeArgs = new ArrayList<JClass>();
		ArrayList<Class<?>> classArgs = new ArrayList<Class<?>>();

		for (String t : type.getTypeArgs()) {
			TypeEntry genType = resolveType(t);
			if(genType.jclazz == null)
				throw new RuntimeException(genType.type + " was not defined");
			else
				typeArgs.add((JClass)genType.jclazz);
		}
		
		
		
		BaseType bt = baseTypes.get(type.getType()).baseType;
		if(bt.getType() == null){
			throw new RuntimeException(bt.name() + " cannot be used as a generic type");
		}
		String name = bt.getType().getCanonicalName();
		typeEntry.jclazz = codeModel.ref(name).narrow(typeArgs);
		

	}
	
	private void generateSyntheticType(SchemaType type) {
		TypeEntry typeEntry = typeIndex.get(type.getId());
		if (typeEntry.jclazz != null)
			return;

		ArrayList<JClass> typeArgs = new ArrayList<JClass>();
		ArrayList<Class<?>> classArgs = new ArrayList<Class<?>>();

		for (String t : type.getTypeArgs()) {
			TypeEntry genType = resolveType(t);
			if(genType.jclazz == null)
				throw new RuntimeException(genType.type + " was not defined");
			else
				typeArgs.add((JClass)genType.jclazz);
		}
		
		
		
		TypeEntry base = resolveType(type.getExtends());
		typeEntry.jclazz = codeModel.ref(base.jclazz.fullName()).narrow(typeArgs);
		

	}
	

	private void generateSimpleType(SchemaType schemaType) {
		TypeEntry typeEntry = typeIndex.get(schemaType.getId());
		if (typeEntry.jclazz != null)
			return;

		JDefinedClass jclass;
		try {
			jclass = codeModel._class(getNs(schemaType.getNs()) + "."
					+ schemaType.getName());
		} catch (JClassAlreadyExistsException e) {
			System.out.println("Duplicate Type: " + schemaType.getId());
			e.printStackTrace();
			return;
		}

		if (schemaType.getExtends() != null
				&& (!schemaType.getExtends().trim().equals(""))) {
			TypeEntry type = resolveType(schemaType.getExtends());
			if(type.jclazz != null)
				jclass._extends((JClass)type.jclazz);
			else 
				jclass._extends(type.clazz);
		}

		typeEntry.jclazz = jclass;
		System.out.println("generating Type: " + jclass.fullName());
		
		Map<String, JTypeVar> genericArgs = new HashMap<String, JTypeVar>();
		if(hasGenericPlaceholders(schemaType.getId())){
			for(String arg: schemaType.getTypeArgs()){
				System.out.println(String.format("Narrowing class: %s %s", jclass.name(),getGenericArgName(arg) ));
				genericArgs.put(arg, jclass.generify(getGenericArgName(arg)));
			}
		}
			

		for (SchemaProperty property : schemaType.getProperties()) {
			
				TypeEntry type = null;
				
				if(property.getRequired()){
					type = getPrimitiveType( property.getType() );
				}else if(hasGenericPlaceholders(property.getType())){
					if(genericArgs.containsKey(property.getType()))
						type = new TypeEntry(genericArgs.get(property.getType()));
					else{
						
						type = resolveType(property.getType());
						List<String> placeholders = getGenericPlaceHolders( property.getType() );
						JClass clazz =((JClass)type.jclazz); 
						for(String pl : placeholders)
							clazz=clazz.narrow( genericArgs.get(pl) );
						
						type = new TypeEntry(null,clazz);
					}
				}
				if(type == null)
					type = resolveType(property.getType());
				
				defineProperty(jclass, property.getName(), type);
			
		}

		processTypeExtensions(schemaType, jclass);

	}

	public String getGenericArgName(String arg) {
		return arg.replace("#","");
	}
	
	public List<String> getGenericPlaceHolders(String typeId) {
		ArrayList<String> placeholders = new ArrayList<String>();
		
		
		//typeId="List<#String,#Int>";
		Pattern pattern = Pattern.compile("(\\#\\w*)\\,*");
		Matcher matcher = pattern.matcher(typeId);

		while(matcher.find()) {
		   // System.out.println("found: " + matcher.group(1) +" in "+typeId);
		    placeholders.add(matcher.group(1));
		}
		
		return placeholders;
	}
	

	boolean hasGenericPlaceholders(String typeId){
		return typeId!=null&&typeId.contains("#");
	}
	protected void processTypeExtensions(SchemaType schemaType, JDefinedClass jclass) {
		TypeExtension xtension = extensions.get(schemaType.getId());
		if (xtension != null) {
			System.out.println("processing extension for "+schemaType.getId());
			for (@SuppressWarnings("unused") String imp : xtension.imports) {
				// codemodel doesnt seem to have the ability to add an import
				throw new RuntimeException(
						"Imports are not supported yet.. use fully qualified names in the mean time... :(");
			}
			jclass.direct(xtension.body);

		}
	}

	private TypeEntry getPrimitiveType(String type) {
		
		
		if (baseTypes.containsKey(type)){
			TypeEntry entry = new TypeEntry((SchemaType)null);
			switch(baseTypes.get(type).baseType){
				case Bool:
					entry.jclazz = JPrimitiveType.parse(codeModel, "boolean");
				case Int:
					entry.jclazz = JPrimitiveType.parse(codeModel, "int");
				default:
					break;
			}
		}
		return null;
		
	}

	@SuppressWarnings("unused")
	private JFieldRef staticRef(Class<?> clazz, String field) {
		JClass factory = codeModel.directClass(clazz.getName()
				.replace("$", "."));
		return factory.staticRef(field);
	}

	private JFieldVar defineProperty(JDefinedClass jclass, String name,
			TypeEntry type) {
		String cName = name.substring(0, 1).toUpperCase() + name.substring(1);
		
		
		
		JMethod setter = jclass.method(JMod.PUBLIC, Void.TYPE, "set" + cName);
		
		JFieldVar field = null;
		JMethod getter = null;
		if(type.jclazz != null){
			field = jclass.field(JMod.PRIVATE, type.jclazz, "_" + name);
			getter = jclass.method(JMod.PUBLIC, type.jclazz, "get" + cName);
			setter.param(type.jclazz, name);
		}else if(type.clazz!=null){
			field = jclass.field(JMod.PRIVATE, type.clazz, "_" + name);
			getter = jclass.method(JMod.PUBLIC, type.clazz, "get" + cName);
			setter.param(type.clazz, name);
		}
		else if(type.genericArg !=null){
			field = jclass.field(JMod.PRIVATE, type.genericArg, "_" + name);
			getter = jclass.method(JMod.PUBLIC, type.genericArg, "get" + cName);
			setter.param(type.genericArg, name);
		}
 
		
		JAnnotationUse jp = field.annotate(JsonProperty.class);
		jp.param("value", name);
		
		getter.body()._return(field);
		
		
		setter.body().assign(JExpr._this().ref(field), JExpr.ref(name));
		return field;
	}
	
	

	public String getNs(String ns) {
		return options.ns != null ? options.ns : ns;
	}

	public TypeEntry resolveType(String type) {
		return resolveType(type,true);
	}
	public TypeEntry resolveType(String type,boolean errIfNotFound) {

		if (baseTypes.containsKey(type))
			return baseTypes.get(type);

		if (!typeIndex.containsKey(type)){
			if(hasGenericPlaceholders(type)){
				String baseName = type.substring(0,type.indexOf('<'));
				if (baseTypes.containsKey(baseName))
					return baseTypes.get(baseName);
			}
			throw new RuntimeException("type not found " + type);
		}
		
		TypeEntry typeEntry = typeIndex.get(type);
		if (typeEntry.jclazz == null && typeEntry.clazz == null )
			generateType(typeEntry.type);
		
		if (typeEntry.jclazz == null && typeEntry.clazz == null){
			if(errIfNotFound)
				throw new RuntimeException("type not generated... " + type);
		}
		
		
			
		return typeEntry;

	}

	// static HashMap<String,GenType> generatedTypes = new HashMap<>();
	//
	// public static class GenType{
	// public GenType(JDefinedClass jclass, JsonNode e) {
	// this.clazz = jclass;
	// this.type = e;
	// }
	// public JDefinedClass clazz;
	// public JsonNode type;
	// }
	//
	// public static void main(String[] args) {
	//
	// try {
	// JCodeModel codeModel = new JCodeModel();
	// File outputDir = new File("..\\wsgentest\\src");
	// outputDir.mkdirs();
	// for( BaseType baseType : BaseType.class.getEnumConstants()){
	//
	// baseTypes.put(baseType.name(), new
	// BaseGenType(codeModel.ref(baseType.getType()), baseType));
	// }
	//
	// String json = fetchUrl("http://localhost:65084/Schema.svc/schema/");
	// ObjectMapper mapper = new ObjectMapper();
	// JsonNode schema = mapper.readTree(json);
	// JsonNode types = schema.get("Types");
	// JsonNode endpoints = schema.get("Endpoints");
	//
	// // index the types
	// for(JsonNode e : types){
	// if("Object".equals(e.get("type").asText())){
	// JDefinedClass jclass =
	// codeModel._class(e.get("ns").asText()+"."+e.get("name").asText());
	// generatedTypes.put(e.get("id").asText(), new GenType( jclass, e ));
	// }
	// }
	// for(JsonNode e : types){
	// if("List".equals(e.get("type").asText()) ||
	// "Map".equals(e.get("type").asText())){
	// ArrayList<JClass> typeArgs = new ArrayList<JClass>();
	//
	// for (JsonNode t: e.get("typeArgs")) {
	// typeArgs.add( resolveType(t.asText()) );
	// }
	// genericTypes.put( e.get("id").asText(), codeModel.ref(
	// baseTypes.get(e.get("type").asText()).type.type.getCanonicalName()
	// ).narrow(typeArgs));
	// }
	// }
	//
	// // process the types
	// System.out.println("generating types");
	// for(GenType gtype : generatedTypes.values()){
	// JDefinedClass jclass = gtype.clazz;
	// System.out.println("generating "+ jclass.fullName());
	// JsonNode e = gtype.type;
	// for(JsonNode property: e.get("properties")){
	// JType type = resolveType( property.get("type").asText() );
	//
	// String name = property.get("name").asText();
	// String cName = name.substring(0,1).toUpperCase() +name.substring(1);
	// JFieldVar field = jclass.field(JMod.PRIVATE,type
	// ,"_"+property.get("name").asText());
	//
	// JAnnotationUse jp = field.annotate( JsonProperty.class);
	// jp.param("value", name);
	//
	// JMethod getter = jclass.method(JMod.PUBLIC, type,"get"+cName);
	// getter.body()._return(field);
	//
	// JMethod setter = jclass.method(JMod.PUBLIC, Void.TYPE,"set"+cName);
	// JVar param = setter.param(type, name);
	// setter.body().assign(JExpr._this().ref(field), JExpr.ref(name));
	//
	// }
	//
	// }
	//
	//
	//
	// // for( JsonNode endpointNode : endpoints ){
	// // JPackage pkg = codeModel._package(endpointNode.get("ns").asText());
	// // JDefinedClass endpoint = pkg._interface( JMod.PUBLIC,
	// endpointNode.get("name").asText());
	// //
	// // for(JsonNode operation : endpointNode.get("operations")){
	// // JMethod op =
	// endpoint.method(JMod.PUBLIC,resolveType(operation.get("result").asText()),
	// operation.get("name").asText());
	// // Class<? extends Annotation> httpMethod =
	// "GET".equalsIgnoreCase(operation.get("method").asText()) ?
	// retrofit.http.GET.class : retrofit.http.POST.class;
	// // JAnnotationUse httpAnnotation = op.annotate(httpMethod);
	// // httpAnnotation.param("value", operation.get("path").asText());
	// //
	// //
	// // }
	// // }
	//
	// System.out.println("writing to filesystem");
	// codeModel.build(outputDir);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	static String fetchUrl(String url) throws MalformedURLException,
			IOException {
		return stream2String(new URL(url).openConnection().getInputStream());
	}

	static String stream2String(InputStream s) {

		try {
			StringBuilder response = new StringBuilder();
			String line = "";
			BufferedReader is = new BufferedReader(new InputStreamReader(s));

			while ((line = is.readLine()) != null)
				response.append(line);

			is.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
