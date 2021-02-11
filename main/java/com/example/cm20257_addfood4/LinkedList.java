package com.example.cm20257_addfood4;
import android.util.Log;

public class LinkedList {
    //ASD to hold arrays of strings, adds new items to start of list

    ListNode end = new ListNode(new String[]{"null", "null", "null", "null"}, null);
    ListNode start = new ListNode(new String[]{"null","null","null","null"}, end);

    int length = 0;

    public void add(String[] data) {
        ListNode fresh = new ListNode(data, start.next);
        length++;
        start.next = fresh;
    }

    public String[][] get() {
        String[][] q = new String[length][4];
        try {
            ListNode temp = start.next;
            if (temp == null) {
                return null;
            }

            int i = 0;
            while (temp.next != end) {
                q[i] = temp.element;
                i++;
                temp = temp.next;
            }
            return q;
        } catch (Exception e){
            e.printStackTrace();
            return q;
        }
    }

    public void remove(int[] indexes) {
        int indexesLength = indexes.length;
        String names[] = new String[indexesLength];
        try {
            for (int i = 0; i < indexesLength; i++) {
                int index = indexes[i];
                if(index != 9999) {
                    ListNode temp = start.next;
                    while (index > 0) {
                        temp = temp.next;
                        index--;
                    }
                    names[i] = temp.element[0];
                }
            }
            removeString(names);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeString(String[] data){
        int dataLength = data.length;
        for(int i=0; i< dataLength; i++) {
            ListNode temp = start;
            while (temp != null) {
                if (temp.next.element[0].equals(data[i])){
                    temp.next = temp.next.next;
                    temp = end;
                }
                temp = temp.next;
            }
        }
    }

}

