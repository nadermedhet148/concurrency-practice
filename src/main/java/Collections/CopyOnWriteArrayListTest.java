package Collections;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list
                = new CopyOnWriteArrayList<>();

        Iterator itr = list.iterator();
        list.add("GfG");
        // any new element after create iterator will be ignored
        System.out.println("List contains: ");
        while (itr.hasNext())
            System.out.println(itr.next());

        // iterator after adding an element
        itr = list.iterator();
        System.out.println("List contains:");
        while (itr.hasNext())
            System.out.println(itr.next());
    }
}
