import io.github.splotycode.mosaik.networking.store.LoadShare;

import java.util.Arrays;

public class LoadShareTest {

    public static void main(String[] args) {
        testDefault(0, 100, 10);
        testDefault(100, 200, 10);
    }

    private static void testDefault(int min, int max, int size) {
        LoadShare share = new LoadShare(min, max, size);
        float[] wights = share.getWeights().clone();
        System.out.println(Arrays.toString(share.getWeights()));
        share.resize(5);
        System.out.println(Arrays.toString(share.getWeights()));
        share.resize(10);
        System.out.println(Arrays.toString(share.getWeights()));
        assert Arrays.equals(wights, share.getWeights());
    }

}
