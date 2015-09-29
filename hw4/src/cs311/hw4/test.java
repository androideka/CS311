package cs311.hw4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by androideka on 9/21/15.
 */
public class test
{
    public static void main(String[] args) {
        InsertionSort insertionSort = new InsertionSort();

        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        };

        ArrayList<Integer[]> arrays = new ArrayList<Integer[]>();

        for (int i = 1; i <= 50000; i*=2) {
            Integer[] array = new Integer[i];
            randomizeArray(array);
            arrays.add(array);
        }

        for (int i = 0; i < arrays.size(); i++)
        {
            Integer[] arr = arrays.get(i);
            insertionSort.sort(arr, 0, arr.length, comp);
        }
    }

    private static void randomizeArray(Integer[] arr)
    {
        Random random = new Random();
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = random.nextInt(arr.length);
        }
    }
}
