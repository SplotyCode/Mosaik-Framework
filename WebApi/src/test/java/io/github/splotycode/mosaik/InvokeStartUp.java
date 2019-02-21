package io.github.splotycode.mosaik;

import io.github.splotycode.mosaik.startup.Main;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class InvokeStartUp extends RunListener {

    public static void start() {
        try {
            Main.mainIfNotInitialised(new String[] {"-mosaik.appname", "tests", "-debug:log_file"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testRunStarted(Description description) {
        start();
    }

}
