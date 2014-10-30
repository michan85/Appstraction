package appstraction.app.style.dom;



/**
 * Created by Michael.Hancock on 7/30/2014.
 */
public class Walker {

    public int process(Node v){
        return EXIT;
    }




    public static final int CONTINUE = 0;
    public static final int EXIT = 1;
    public static final int SKIP_CHILDREN = 2;
    private static final int SKIP_PARENT = 3;

    public int level = 0;
    public int walk(Node el) {

        level++;
        for (int i = 0; i < el.getChildNodesCount(); i++) {
            Node v = el.getChild(i);
            int result = process(v);
            switch (result) {
                case CONTINUE:

                    result = walk(v);

                    if (result == EXIT)
                        return EXIT;
                    break;
                case EXIT:
                    return EXIT;
                case SKIP_CHILDREN:
                    break;
                case SKIP_PARENT:
                    return CONTINUE;
            }
        }
        level--;

        return CONTINUE;
    }
}
