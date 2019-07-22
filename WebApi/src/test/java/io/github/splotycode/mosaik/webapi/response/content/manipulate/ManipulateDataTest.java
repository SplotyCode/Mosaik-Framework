package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;
import io.github.splotycode.mosaik.util.prettyprint.PrettyPrint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManipulateDataTest {

    private static final String EXPECTED = "[pat=ManipulatePattern[\n" +
            "    start: 5\n" +
            "    end: 38\n" +
            "    content:  dasd $name$ 333 $age$ \n" +
            "    name: pat\n" +
            "    childs: []\n" +
            "    parent: Disabled\n" +
            "    variables: [age=[ManipulateVariable[\n" +
            "    start: 17\n" +
            "    end: 22\n" +
            "]], name=[ManipulateVariable[\n" +
            "    start: 6\n" +
            "    end: 12\n" +
            "]]]\n" +
            "]]";
    private static final String EXPECTED_VAR = "[name1=[ManipulateVariable[\n" +
            "    start: 24\n" +
            "    end: 31\n" +
            "]], name2=[ManipulateVariable[\n" +
            "    start: 33\n" +
            "    end: 40\n" +
            "]], name3=[ManipulateVariable[\n" +
            "    start: 45\n" +
            "    end: 52\n" +
            "]], name=[ManipulateVariable[\n" +
            "    start: 16\n" +
            "    end: 22\n" +
            "]]]";

    @Test
    void testVariablePositions() {
        if (StartUpInvoke.invokeTestSuite()) {
            StringManipulator manipulator = new StringManipulator("Hallo my friend $name$, $name1$, $name2$ and $name3$");
            Assertions.assertEquals(EXPECTED_VAR, new PrettyPrint(manipulator.getManipulateData().getVariableMap()).prettyPrintType());
        }
    }

    @Test
    void testPatternPositions() {
        if (StartUpInvoke.invokeTestSuite()) {
            StringManipulator manipulator = new StringManipulator("hallo$@pat$ dasd $name$ 333 $age$ $@@$hasdasdas");
            Assertions.assertEquals(EXPECTED, new PrettyPrint(manipulator.getManipulateData().getPatternMap()).prettyPrintType());
        }
    }

    @Test
    void testPatternInPatternPositions() {
        if (StartUpInvoke.invokeTestSuite()) {
            StringManipulator manipulator = new StringManipulator("hallo$@pat$ dasd $@pat2$ $adsd$ $eee$ $@@$ $name$ 333 $age$ $@@$hasdasdas");
            System.out.println(new PrettyPrint(manipulator.getManipulateData().getPatternMap()).prettyPrintType());
        }
    }

}
