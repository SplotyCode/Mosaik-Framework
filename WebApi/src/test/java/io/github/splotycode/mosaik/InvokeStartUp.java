package io.github.splotycode.mosaik;

import io.github.splotycode.mosaik.startup.Main;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class InvokeStartUp extends RunListener {

    public static void start() {
        Main.mainIfNotInitialised(new String[] {"-mosaik.appname", "tests"});
    }

    @Override
    public void testRunStarted(Description description) {
        start();
    }

}
