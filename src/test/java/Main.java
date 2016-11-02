import javax.websocket.Session;
import java.util.*;

public class Main {
    public static void main(String[] args) {
//        Queue<Integer> set = new LinkedList<Integer>();
//        set.add(1);
//        set.add(2);
//        set.add(3);
//        set.add(4);

        List<Integer> set = new ArrayList<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);

        System.out.println(set.get(0));
        set.remove(0);
        System.out.println(set.get(0));

//
//        set.remove(2);
//        set.remove(2);
//
//        String[] arr = new String[2];
//        arr[0] = "one";
//        arr[1] = "two";
//
//        arr[0] = "3";
//        arr[1] = "4";
//
//        System.out.println(Arrays.toString(arr));

//        Integer myInteger;
//        myInteger = set.poll();
//        System.out.println(myInteger);
//        System.out.println(set.element());
//        System.out.println("size = " + set.size());

//        HashMap<Session[], Long> dialogs = new HashMap<Session[], Long>();
//        HashMap<Long, String[]> dialogs = new HashMap<Long, String[]>();
//        String[] strs = new String[2];
//        strs[0] = "yarik";
//        strs[1] = "leha";
//        dialogs.put(1L, strs);
//
//        String[] strs2 = new String[2];
//        strs2[0] = "yarik";
//        strs2[1] = "leha";
//        dialogs.put(2L, strs2);
//
//        System.out.println(Arrays.toString(dialogs.get(3L)));


//        List<String> list1 = new ArrayList<String>();
//        List<String> list2 = new LinkedList<String>();
//// Заполнение коллекций
//// попробуйте 10, 100, 1000, 10000, 100000
//        for (int i=0; i<100; i++) {
//            list1.add("" + i);
//            list2.add("" + i);
//        }
//// Random access test
//        long t0=System.nanoTime(); // get list1
//        for (int i=0; i<list1.size(); i++) {
//            list1.remove(i);
//        }
//        long t1=System.nanoTime(); // get list2
//        for (int i=0; i<list2.size(); i++) {
//            list2.remove(i);
//        }
//        long t2=System.nanoTime();
//// Результаты
//        System.out.println("RandomAccess :" + (list1 instanceof RandomAccess) + " "
//                + (list2 instanceof RandomAccess));
//        System.out.println("get(i) :" + (t1-t0) + " " + (t2-t1));


    }
}
