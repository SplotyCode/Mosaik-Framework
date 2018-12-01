package me.david.davidlib.spigotlib.gui;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class Closeable {

    public static final Closeable ALL = new Closeable(true, true, true);
    public static final Closeable NONE = new Closeable(false, false, false);


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

}
