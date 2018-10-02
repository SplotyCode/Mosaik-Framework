package me.david.davidlib.gamesngine.gameloop;

/**
 * This Interface is for handling a Game Loop Overload
 */
public interface Overloadable {

    /**
     * Called when a GameLoop can not keep up
     * @param normal the normal or maximum delay for an tick
     * @param current the time the current tick toked
     */
    void cantKeepUp(long normal, long current);

}
