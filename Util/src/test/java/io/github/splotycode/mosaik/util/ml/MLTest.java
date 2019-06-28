package io.github.splotycode.mosaik.util.ml;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class MLTest {

    @Test
    public void test() {
        Random random = new Random();
        int[][] xorInputs = { {0, 0}, {0, 1}, {1, 0}, {1, 1} };
        for(int i = 0; i < xorInputs.length * 2000; i++)
        {
            int[] arr = xorInputs[i % 4];
            double[] inputs = { arr[0], arr[1] };
            double[] outputs = { arr[0] ^ arr[1] };
            System.out.println(Arrays.toString(inputs) + " " + Arrays.toString(outputs));
        }
    }

}
