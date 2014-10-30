package appstraction.app.style.dom;



public interface Node {

//	enum NodeType{
//		ELEMENT
//		
//	}

//	NodeType getNodeType();

	String getNodeName();

	boolean hasAttribute(String name);

	Object getAttribute(String name);
	boolean setAttribute(String name, Object value);

	int getChildNodesCount();

	Node getChild(int i);
	
	void appendChild(Node child);
	void appendChild(int index, Node child);
	int indexOfChild(Node n);

//	Node getNextSiblingElement();
//	Node getPrevSiblingElement();
	Node getParent();
	void setParent(Node n);




    //void trigger(String event);

    Document getDocument();
}
