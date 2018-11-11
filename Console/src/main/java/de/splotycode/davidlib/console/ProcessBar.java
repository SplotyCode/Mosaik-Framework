package de.splotycode.davidlib.console;

import java.io.PrintStream;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ProcessBar {

    private int max;
    private PrintStream stream;
    private String prefix;
    private int value;

    private long startTime;

    public ProcessBar(int max, PrintStream stream, String prefix, int value) {
        this.max = max;
        this.stream = stream;
        this.prefix = prefix;
        this.value = value;
        reset();
    }

    private void draw() {
        String eta = getETaAsString();
        StringBuilder string = new StringBuilder();
        int percent = (int) (value * 100 / max);
        string
                .append('\r')
                .append(prefix)
                .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
                .append(String.format(" %d%% [", percent))
                .append(String.join("", Collections.nCopies(percent, "=")))
                .append('>')
                .append(String.join("", Collections.nCopies(100 - percent, " ")))
                .append(']')
                .append(String.join("", Collections.nCopies((int) (Math.log10(max)) - (int) (Math.log10(value)), " ")))
                .append(String.format(" %d/%d, %s", value, max, eta));

        System.out.print(string);
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }

    public void step() {
        step(1);
    }

    public void step(int n) {
        value = Math.min(max, value + n);
        draw();
    }

    public long getEta() {
        return value == 0 ? 0 : (max - value) * (System.currentTimeMillis() - startTime) / value;
    }

    public String getETaAsString() {
        long eta = getEta();
        return value == 0 ? "N/A" :
                String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                        TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void stop() {
        stream.println();
    }

}
