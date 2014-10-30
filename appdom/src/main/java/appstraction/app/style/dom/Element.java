package appstraction.app.style.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import appstraction.app.style.attr.Attr;
import appstraction.app.style.attr.ClassAttr;
import appstraction.app.style.attr.IdAttr;
import appstraction.app.style.attr.SimpleAttr;

/**
* Created by Michael.Hancock on 7/29/2014.
*/
public class Element<T> implements Node {

    public Element(Document d){setDocument(d);}

    private String nodeName;
    Map<String, Attr> attributes;
    private Node parent;
    boolean isContainer;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    T view;


    
    public boolean hasAttribute(String name) {
        if(attributes!=null)
            return attributes.containsKey(name);
        return false;
    }

    
    public Object getAttribute(String name) {
        if(attributes!=null){
            Attr a = attributes.get(name);
            if(a!=null) {
                System.out.println("getAttr: "+name+" val: "+a.get(this));
                return a.get(this);
            }else{
                System.out.println("getAttr: "+name+" failed attr doesnt exist");
            }
        }else{
            System.out.println("getAttr: "+name+" no attrs");
        }
        return null;
    }




    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


    public boolean setAttribute(String name, Object value) {

        if(attributes==null)
            attributes = new HashMap<String, Attr>();

        Attr a = attributes.get(name);
        if(a!=null)
            a.set(this,value);
        else
            attributes.put(name, createAttr(name, value));
        return true;
    }

    public Attr createAttr(String name, Object value) {
        if("class".equalsIgnoreCase(name)){
            return new ClassAttr((String) value);
        }else if("id".equalsIgnoreCase(name)){
            return new IdAttr(value);
        }
        return new SimpleAttr(value);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


    
    public Document getDocument() {
        return document;
    }

    Document document;

    void setDocument(Document d){
        document = d;
    }



    public <T extends  Attr> T getAttr( String name, boolean create ){
        if(attributes == null)
            attributes = new HashMap<String, Attr>();
        Attr attr = attributes.get(name);
        if(create && attr == null){
            attr = createAttr(name, null);
            attributes.put(name,attr);
        }
        return (T)attr;
    }

    public void setAttr(String name, Attr attr){
        if(attributes == null)
            attributes = new HashMap<String, Attr>();
        attributes.put(name,attr);
    }



    public static String join(Collection<?> col, String delim) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = col.iterator();
        if (iter.hasNext())
            sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(delim);
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }

    public void setId(String id) {
        System.out.println("setId: " + id);
        setAttribute("id", id);
    }

    public String getId(){
        Object id = getAttribute("id");
        return  id==null ? "" : id.toString();
    }

    List<Node> children;

    
    public int getChildNodesCount() {
        return children == null? 0 : children.size();
    }

    
    public Node getChild(int i) {
        if(!isContainer){
            System.out.print("getChild failed element is not a container");
            return null;
        }
        if(children!=null)
            return children.get(i);
        return null;
    }

    
    public void appendChild(Node child) {
        if(!isContainer){
            System.out.print("appendChild failed element is not a container");
            return;
        }
        if(children==null)
            children=new ArrayList<Node>();
        children.add(child);
        child.setParent(this);
    }

    
    public void appendChild(int index, Node child) {
        if(!isContainer){
            System.out.print("appendChild failed element is not a container");
            return;
        }
        if(children==null )
            children=new ArrayList<Node>();
        children.add(index,child);
        child.setParent(this);
    }


    public int indexOfChild(Node n) {
        if(children!=null)
            return children.indexOf(n);
        return -1;
    }

    public boolean isContainer() {
        return isContainer;
    }

    public void setContainer(boolean isContainer) {
        this.isContainer = isContainer;
    }

    @Override
    public String toString() {
        String s = getNodeName();
        String id = getId();
        if(id != null && id.length() > 0)
            s+= " #"+id;

        String clazz = (String)getAttribute("class");
        if(clazz != null && clazz.length() > 0)
            s+= " class=["+clazz+"]";

        return s;
    }


    public void removeClass(String className) {
//        String clazz = (String)getAttribute("class");
//        if(clazz!=null && clazz.length()>0){
//            List<String> classes = Arrays.asList(clazz.split(" "));
//            classes.remove(className);
//            setAttribute("class", join(classes, " "));
//        }

        ClassAttr clazz = getAttr("class",true);
        clazz.removeClass(this, className);
    }

    public void addClass(String className) {

        ClassAttr clazz = getAttr("class",true);
        clazz.addClass(this,className);

    }

}
