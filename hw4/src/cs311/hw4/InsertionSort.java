package cs311.hw4;

/**
 * Created by androideka on 9/21/15.
 */
import java.util.Comparator;

public class InsertionSort<T> implements ISort<T>
{
    public void sort(T[] arr, int start, int end, Comparator<T> comp)
    {
        long startTime = System.currentTimeMillis();

        if( arr == null )
        {
            throw new NullPointerException("The array must not be null. Please try again with valid data.");
        }

        int i = start + 1;

        while( i < end )
        {
            if( comp.compare(arr[i-1], arr[i]) > 0 )
            {
                int j = i;
                while( j > 0 && comp.compare(arr[j-1], arr[j]) >= 0 )
                {
                    swap( arr, j-1, j );
                    j--;
                }
            }
            i++;
        }
        System.out.println("Array size " + arr.length + " duration: " + (System.currentTimeMillis() - startTime)
                + " milliseconds");
    }

    public void swap( T[] arr, int first, int second )
    {
        T one = arr[first];
        T two = arr[second];
        T temp = one;
        arr[first] = two;
        arr[second] = temp;
    }
}


