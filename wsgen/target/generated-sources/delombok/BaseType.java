import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.mime.TypedByteArray;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPrimitiveType;
import com.sun.codemodel.JType;


public enum BaseType {
	String(String.class), Int(Integer.class), Decimal(Double.class), Bool(
			Boolean.class), Date(java.util.Date.class), List(
			ArrayList.class), Map(HashMap.class), Object(
			java.lang.Object.class), Enum(java.lang.Enum.class), UUID(
			java.util.UUID.class), Void(java.lang.Void.class), Stream("byte"), Synthetic("#SYNTHETIC");

	private Class<?> type;
	private java.lang.String	primitiveArrayType;

	private BaseType(Class<?> clazz) {
		this.type = clazz;
	}
	private BaseType(String primitiveArrayType) {
		this.primitiveArrayType = primitiveArrayType;
		
	}
	

	public Class<?> getType() {
		return type;
	}
	
	public JClass getJType(JCodeModel codeModel) {
		if(this == Synthetic)
			return null;
		if(type == null){
			return JPrimitiveType.parse(codeModel, primitiveArrayType).array();
		}else
			return codeModel.ref(type);
	}
	
	
}