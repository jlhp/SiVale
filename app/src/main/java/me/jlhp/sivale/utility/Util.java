package me.jlhp.sivale.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by JOSELUIS on 3/8/2015.
 */
public class Util {

    private Util() { }

    public static boolean isStringEmptyOrNull(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static <T> List<T> collection2List(Collection<T> collection) {
        ArrayList<T> items = new ArrayList<>();

        if(collection == null || collection.isEmpty()) return items;

        for(T item : collection) {
            items.add(item);
        }

        return items;
    }
}
