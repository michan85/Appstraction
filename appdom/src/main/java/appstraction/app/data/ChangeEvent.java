package appstraction.app.data;

/**
 * Created by Michael.Hancock on 7/15/2014.
 */
public class ChangeEvent {
    public static final ChangeEvent CLEAR = new ChangeEvent();
    public static final Update UPDATE = new Update();

    public static class Update extends ChangeEvent{

    }

public static class Add extends AddOrRemove{

    public Add(Object o) {
        super(o);
    }

    public Add(Object o, int position) {
        super(o, position);
    }

    public Add(Object o, int position, boolean isCollection) {
        super(o, position, isCollection);
    }
}

    public static class Remove extends AddOrRemove{

        public Remove(Object o) {
            super(o);
        }

        public Remove(Object o, int position) {
            super(o, position);
        }

        public Remove(Object o, int position, boolean isCollection) {
            super(o, position, isCollection);
        }
    }

    public static class AddOrRemove extends Update{
        private final Object item;
        private final int position;
        private final boolean isCollection;

        public boolean isCollection() {
            return isCollection;
        }



        public Object getItem() {
            return item;
        }
        public int getPosition() {
            return position;
        }

        public AddOrRemove(Object o){
            this.item = o;
            position=-1;
            this.isCollection = false;
        }
        public AddOrRemove(Object o, int position){
            this.item = o;
            this.position = position;
            this.isCollection = false;
        }

        public AddOrRemove(Object o, int position, boolean isCollection){
            this.item = o;
            this.position = position;
            this.isCollection = isCollection;
        }

    }


    public static class ItemUpdate extends ChangeEvent {
        public Object getItem() {
            return item;
        }

        private final Object item;

        public int getPosition() {
            return position;
        }

        private final int position;

        public ItemUpdate(Object item, int position) {
            super();
            this.item=item;
            this.position=position;
        }
    }
}
