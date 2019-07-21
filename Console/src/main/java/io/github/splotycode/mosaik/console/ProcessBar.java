package io.github.splotycode.mosaik.console;

import io.github.splotycode.mosaik.iui.INamedTaskBar;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * Creates a Process Bar in the console
 */
public class ProcessBar implements INamedTaskBar {

    private int max;
    private Logger logger;
    private String prefix;
    private int value;
    @Setter private boolean sameLine = true;

    private long startTime;

    /**
     * Creates an new progressbar
     * @param max the maximum value
     * @param logger the logger in that the progressbar should be written in
     * @param prefix the prefix for the progressbar
     * @param value the default value
     */
    public ProcessBar(int max, Logger logger, String prefix, int value) {
        this.max = max;
        this.logger = logger;
        this.prefix = prefix;
        this.value = value;
        reset();
    }

    private void draw() {
        String eta = getETaAsString();
        int percent = value * 100 / max;
        String prefix = this.prefix;
        if (sameLine) {
            prefix = '\r' + prefix;
        }
        String string = prefix +
                        StringUtil.repeat(" ", percent == 0 ? 2 : 2 - (int) (Math.log10(percent))) +
                        String.format(" %d%% [", percent) +
                        StringUtil.repeat("=", percent) +
                        '>' +
                        StringUtil.repeat(" ", 100 - percent) +
                        ']' +
                        StringUtil.repeat(" ", (int) (Math.log10(max)) - (int) (Math.log10(value))) +
                        String.format(" %d/%d, %s", value, max, eta);
        logger.info(string);
    }

    /**
     * Resets the ProcessBar
     */
    public void reset() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Goes 1 step further
     */
    public void step() {
        step(1);
    }

    /**
     * Goes n step further
     * @param n the number of steps
     */
    public void step(int n) {
        value = Math.min(max, value + n);
        draw();
    }

    /**
     * Get ela as long
     * @return Get the eta as a long
     */
    public long getEta() {
        return value == 0 ? 0 : (max - value) * (System.currentTimeMillis() - startTime) / value;
    }

    /**
     * Get ela as formatted String
     * @return Get the eta as a formatted String
     */
    public String getETaAsString() {
        long eta = getEta();
        return value == 0 ? "N/A" :
                String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                        TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));
    }

    /**
     * Stops The ProcessBar
     */
    public void stop() {
        logger.info("");
    }

    @Override
    public String name() {
        return prefix;
    }

    @Override
    public int max() {
        return max;
    }

    @Override
    public int value() {
        return value;
    }

}
