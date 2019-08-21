import io.github.splotycode.mosaik.networking.store.LoadShare;

import java.util.Arrays;

public class LoadShareTest {

    public static void main(String[] args) {
        testDefault(0, 100, 10);
        testDefault(100, 200, 10);
        LoadShare share = new LoadShare(0, 100, 4);
        share.update(new long[] {25, 25, 40, 10});
        System.out.println(Arrays.toString(share.getWeights()));
        share = new LoadShare(100, 200, 4);
        share.update(new long[] {25, 25, 40, 10});
        System.out.println(Arrays.toString(share.getWeights()));
    }

    private static void testDefault(int min, int max, int size) {
        LoadShare share = new LoadShare(min, max, size);
        float[] wights = share.getWeights().clone();
        share.resize(5);
        share.resize(10);
        assert Arrays.equals(wights, share.getWeights());
    }

}
