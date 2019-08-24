import io.github.splotycode.mosaik.networking.store.LoadShare;

import java.util.Arrays;

public class LoadShareTest {

    public static void main(String[] args) {
        testResize(0, 100, 10);
        testResize(100, 200, 10);
        LoadShare share = new LoadShare(0, 100, 4, 0, false, 0, false);
        float[] wights = share.getWeights().clone();
        share.update(new long[] {25, 25, 40, 10});
        assert Arrays.equals(wights, share.getWeights());
    }

    private static void testResize(int min, int max, int size) {
        LoadShare share = new LoadShare(min, max, size);
        float[] wights = share.getWeights().clone();
        share.resize(5);
        share.resize(10);
        assert Arrays.equals(wights, share.getWeights());
    }

}
