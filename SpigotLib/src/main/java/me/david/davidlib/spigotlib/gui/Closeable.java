package me.david.davidlib.spigotlib.gui;

public class Closeable {

    public static final Closeable ALL = new Closeable(true, true, true);
    public static final Closeable NONE = new Closeable(false, false, false);

    public Closeable(boolean quit, boolean closeButton, boolean close) {
        this.quit = quit;
        this.closeButton = closeButton;
        this.close = close;
    }

    /**
     * Should the inventory close if the player leaves?
     */
    private final boolean quit;

    /**
     * Should the inventory close if the player pressed the close button
     */
    private final boolean closeButton;

    /**
     * Should the inventory close if the player closed the Inventory
     */
    private final boolean close;

    public boolean isQuit() {
        return quit;
    }

    public boolean isCloseButton() {
        return closeButton;
    }

    public boolean isClose() {
        return close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Closeable closeable = (Closeable) o;

        if (quit != closeable.quit) return false;
        if (closeButton != closeable.closeButton) return false;
        return close == closeable.close;
    }

    @Override
    public int hashCode() {
        int result = (quit ? 1 : 0);
        result = 31 * result + (closeButton ? 1 : 0);
        result = 31 * result + (close ? 1 : 0);
        return result;
    }
}
