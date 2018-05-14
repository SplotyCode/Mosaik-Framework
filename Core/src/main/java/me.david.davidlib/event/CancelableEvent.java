package me.david.davidlib.event;

import lombok.Getter;
import lombok.Setter;

public class CancelableEvent extends Event implements Cancelable {

    @Getter @Setter private boolean canceled;

}
