package io.github.splotycode.mosaik.networking.packet;

import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Getter
public abstract class PostPacket<R, W> implements ManuelPacket<R, W> {

    protected R unresolvedBody;
    protected boolean initialRead;
    protected boolean alreadyFilled;

    public PostPacket(R unresolvedBody) {
        this.unresolvedBody = Objects.requireNonNull(unresolvedBody, "unresolvedBody");
        alreadyFilled = true;
    }

    public void checkResolvable() {
        if (alreadyFilled) {
            throw new IllegalStateException("Can not resolve packet tat was filled manually");
        }
        checkState();
    }

    protected void checkState() {
        if (!initialRead) {
            throw new IllegalStateException("Can not mark as resolved if not read");
        }
        if (unresolvedBody == null) {
            throw new IllegalStateException("Already resolved");
        }
    }

    public void resolveDone() {
        ReferenceCountUtil.release(unresolvedBody);
        unresolvedBody = null;
    }

    public void requireResolved() {
        if (!isResolved()) {
            throw new IllegalStateException("Packet need to be post resolved before read");
        }
    }

    public boolean isResolved() {
        return (initialRead || alreadyFilled) && unresolvedBody == null;
    }

    @Override
    public void read(R packet) throws Exception {
        ReferenceCountUtil.retain(packet);
        unresolvedBody = packet;
        initialRead = true;
    }
}
