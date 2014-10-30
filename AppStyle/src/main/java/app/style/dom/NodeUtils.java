package app.style.dom;

public class NodeUtils {

	public static Node getNextSiblingElement(Node n) {
		if(n != null && n.getParent()!=null ){
			int inx = n.getParent().indexOfChild(n);
			return n.getChild(inx+1);
		}
		return null;
	}

}
