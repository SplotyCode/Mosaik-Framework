package io.github.splotycode.mosaik.util.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelIterableTest {

    @Test
    void test() {
        int i = 0;
        ArrayList<ArrayList> list = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            ArrayList<ArrayList<String>> list2 = new ArrayList<>();
            for (int k = 0; k < 3; k++) {
                ArrayList<String> arrayList = new ArrayList<>(3);
                for (int l = 0; l < 3; l++) {
                    arrayList.add(String.valueOf(++i));
                }
                list2.add(arrayList);
            }
            list.add(list2);
        }
        int check = 0;
        LevelIterable<String> iterable = new LevelIterable<>(list);
        for (String str : iterable) {
            assertEquals(++check, (int) Integer.valueOf(str));
        }
        assertEquals(check, 27);
    }


}