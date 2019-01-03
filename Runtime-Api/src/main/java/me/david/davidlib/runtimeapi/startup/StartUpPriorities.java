package me.david.davidlib.runtimeapi.startup;

public final class StartUpPriorities {

    public static final int INDEPENDENT_SETUP = Integer.MAX_VALUE - 100;

    public static final int PRE_LINKBASE = Integer.MAX_VALUE - 500;

    public static final int MANIPULATE_PRE_LINKBASE = Integer.MAX_VALUE - 750;

    public static final int POST_LINKBASE = Integer.MAX_VALUE - 1000;

    public static final int APPLICATION_STUFF = Integer.MAX_VALUE - 20 * 1000;

}
