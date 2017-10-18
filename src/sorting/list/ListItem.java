package sorting.list;

public class ListItem<E> {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ListItem<E> getNext() {
        return next;
    }

    private ListItem<E> next;

    /**
     * @param data the element held in the list item
     **/

    ListItem(Object data) {this.data = data;}


    public String toString(){return "" + data;}
    /**
     * @param next the value to
     * update the <code>next</code> reference
     **/

    protected void setNext(ListItem<E> next) {this.next = next;}

    /**
     * Moves the list item after
     * this one so that it immediately follows the destination
     * @param destination a reference to a list item
     **/

    protected boolean moveNextAfter(ListItem<E> destination) {
        if (destination != next && destination != this) {
            ListItem<E> temp = next;
            this.setNext(temp.next);
            temp.setNext(destination.next);
            destination.setNext(temp);
            return true;
        }
        return false;
    }


}
