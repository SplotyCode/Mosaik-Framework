package me.david.davidlib.event;

import lombok.Getter;
import lombok.Setter;

public class CancelableEvent extends Event {

    @Getter @Setter private boolean canceled;

}
