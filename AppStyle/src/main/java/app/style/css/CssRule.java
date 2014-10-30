package app.style.css;

import java.util.ArrayList;
import java.util.List;

import app.style.css.selector.AttributeSelector;

/**
* Created by Michael.Hancock on 7/29/2014.
*/
public class CssRule implements Comparable<CssRule> {
    public CssRule(){

    }
    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;



        List<CssSelector> sel = new CSSelly(selector).parse();

        // calc specificity
        // http://www.w3.org/TR/css3-selectors/#specificity
        int ids=0,attrs=0,tags=0;
        for(CssSelector cssSel : sel){
            if(cssSel.getElement()!= null && !cssSel.getElement().equals("*")){
                tags++;
            }
            for(int i=0; i< cssSel.selectorsCount(); i++ ){
                Selector s = cssSel.getSelector(i);
                if(s instanceof AttributeSelector){
                    AttributeSelector attr = (AttributeSelector)s;
                    if(attr.getName().equalsIgnoreCase("id")){
                        ids++;
                    }else if(attr.getName().equalsIgnoreCase("class")){
                        attrs++;
                    }
                }
            }
        }

        specificty = (ids*100) + (attrs*10) + tags;

    }

    public CssRule(String selector) {
        setSelector(selector);
    }

    public CssRule(String selector, List<StyleProperty> styles) {
        setSelector(selector);
        this.styles = styles;
    }

    String selector;

    public List<StyleProperty> getStyles() {
        return styles;
    }

    public void setStyles(List<StyleProperty> styles) {
        this.styles = styles;
    }

    List<StyleProperty> styles = new ArrayList<StyleProperty>();

    public int getSpecificty() {
        return specificty;
    }

    int specificty = 0;

    @Override
    public int compareTo(CssRule o) {
        return  specificty-o.getSpecificty();
    }

    @Override
    public String toString() {
        return selector;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
