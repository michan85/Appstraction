package appstraction.app.style.css;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.slf4j.Slf4j;
import appstraction.app.style.dom.Document;
import appstraction.app.style.dom.Element;
import appstraction.app.style.dom.Node;
import appstraction.app.style.dom.NodeSelector;

@Slf4j
public class StyleEngine {

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    Document document;

    public List<CssRule> getRules() {
        return rules;
    }

    List<CssRule> rules = new ArrayList<CssRule>();

    protected AtomicBoolean invalidating = new AtomicBoolean(false);
    public void invalidate(){

        if(invalidating.get()){
            System.out.println("invalidate skipped");
            return;
        }
        doInvalidate();




    }

    static Executor exe = Executors.newFixedThreadPool(3);

    protected void doInvalidate() {


        final ArrayList<CssRule> rules = new ArrayList<CssRule>( this.rules );

        exe.execute(
                new Runnable() {
            public void run() {

                Collections.sort(rules);
                NodeSelector nodeSelector = new NodeSelector(getDocument());

                final long istart = System.currentTimeMillis();
                System.out.println("invalidate start: rules:"+rules.size());
                final Map<Element, Map<String,StyleProperty>> elementStyleIndex = new HashMap<Element, Map<String, StyleProperty>>();
                final Map<StyleProperty, CssRule> styleRuleIndex = new HashMap<StyleProperty, CssRule>();
                for(CssRule rule : rules){
                    long start = System.currentTimeMillis();
                    List<Node> nodes = nodeSelector.select( rule.getSelector() );
                    time(start,"rule match","["+rule.getSelector() +"]  nodes: "+nodes.size()+"  styles:"+rule.getStyles().size());

                    for(StyleProperty p : rule.getStyles()){
                        styleRuleIndex.put(p,rule);
                    }

                    long astart = System.currentTimeMillis();
                    for(Node n : nodes) {

                        if(n instanceof Element) {

                            Element el = (Element) n;
                            Map<String, StyleProperty> styles = elementStyleIndex.get(el);
                            if (styles == null) {
                                styles = new HashMap<String, StyleProperty>();
                                elementStyleIndex.put(el, styles);
                            }
                            for (StyleProperty a : rule.getStyles()) {
                                styles.put(a.getName(), a);
                            }
                        }
                    }
                    time(astart,"computeStyles","["+rule.getSelector() +"]");
                }

                time(istart,"select complete","");


                applyStyles(new Runnable() {

                    public void run() {
                        long apstart = System.currentTimeMillis();
                        for(Map.Entry<Element,Map<String,StyleProperty>> e : elementStyleIndex.entrySet()){
                            for(StyleProperty a : e.getValue().values()){
                                log.debug( "apply style %s to %s  with value: %s from %s", a.getName(), e.getKey(), a.getValue(),styleRuleIndex.get(a));
                                a.apply(e.getKey());
                            }
                        }
                        time(apstart,"apply styles complete","");
                        time(istart,"complete","");
                        invalidating.set(false);
                    }
                });

//                new Walker(){
//                    @Override
//                    public int process(Node v) {
//                        String s = "";
//                        for(int i=0; i < level; i++){
//                            s+="-";
//                        }
//                        System.out.println(s+ v);
//                        return CONTINUE;
//                    }
//                }.walk(getDocument());
            }


        });

    }

    protected void applyStyles(Runnable styleRunner) {
        styleRunner.run();
    }

    protected void time(long start, String tag,String msg){
        long millis = System.currentTimeMillis() - start;
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        System.out.println("invalidate "+tag+"["+String.format("%02d:%02d:%02d:%d", hour, minute, second, millis % 1000)+"]: "+msg);
    }


    public void nodeChanged(Node owner) {
        System.out.print("nodeChanged");
        invalidate();
    }
}
