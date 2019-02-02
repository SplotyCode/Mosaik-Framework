package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.InvokeStartUp;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class StringManipulatorTest {

    @Test
    void testGetResult() {
        InvokeStartUp.start();
        StringManipulator manipulator = new StringManipulator("Hallo my friend $name$");
        manipulator.getReplacements().add(new StringManipulator.Replacement(2, 3, "no"));
        Assertions.assertEquals(manipulator.getResult(), "Hanolo my friend $name$");
        manipulator.reset();
        manipulator.getReplacements().add(new StringManipulator.Replacement(4, 8, "ok"));
        Assertions.assertEquals(manipulator.getResult(), "Hallok friend $name$");
        manipulator.reset();
        manipulator.getReplacements().add(new StringManipulator.Replacement(2, 3, "no"));
        manipulator.getReplacements().add(new StringManipulator.Replacement(4, 8, "ok"));
        Assertions.assertEquals(manipulator.getResult(), "Hanolok friend $name$");
    }

    @Test
    void testVariables() {
        InvokeStartUp.start();
        StringManipulator manipulator = new StringManipulator("Hallo my friend $name$");
        manipulator.variable("name", 33);
        Assertions.assertEquals(manipulator.getResult(), "Hallo my friend 33");
    }

    @Test
    void testMultipleVariables() {
        InvokeStartUp.start();
        StringManipulator manipulator = new StringManipulator("Hallo my friend $name$, $name1$, $name2$ and $name3$");
        manipulator.variable("name", "hallo");
        manipulator.variable("name1", "duuu222222");
        manipulator.variable("name2", "anntonn");
        manipulator.variable("name3", "ahllooouuuuuu");
        Assertions.assertEquals(manipulator.getResult(), "Hallo my friend hallo, duuu222222, anntonn and ahllooouuuuuu");
    }

    @AllArgsConstructor
    @NoArgsConstructor
    class Pat {
        String name = "david";
        int age = 14;
    }

    @Test
    void testPatterns() {
        InvokeStartUp.start();
        StringManipulator manipulator = new StringManipulator("hallo$@pat$ dasd $name$ 333 $age$ $@@$hasdasdas");
        manipulator.pattern(new Pat());
        Assertions.assertEquals(manipulator.getResult(), "hallo dasd david 333 14 hasdasdas");
    }

    @Test
    void testPatternList() {
        InvokeStartUp.start();
        StringManipulator manipulator = new StringManipulator("hallo$@pat$ Name: $name$ <br> Age: $age$ <br><br> $@@$hasdasdas");
        List<Pat> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(new Pat("david", i + 10));
        }
        manipulator.patternList(data);
        Assertions.assertEquals(manipulator.getResult(), "hallo Name: david <br> Age: 15 <br><br>  Name: david <br> Age: 20 <br><br>  Name: david <br> Age: 13 <br><br>  Name: david <br> Age: 21 <br><br>  Name: david <br> Age: 14 <br><br>  Name: david <br> Age: 19 <br><br>  Name: david <br> Age: 10 <br><br>  Name: david <br> Age: 12 <br><br>  Name: david <br> Age: 18 <br><br>  Name: david <br> Age: 17 <br><br>  Name: david <br> Age: 16 <br><br>  Name: david <br> Age: 11 <br><br> hasdasdas");
    }


}