package io.github.splotycode.mosaik.runtime.startup.condition;

import io.github.splotycode.mosaik.runtime.logging.CurrentLoggerHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ConditionFailAction {
    DO_NOTHING(true) {
        @Override
        public void fail(String fullMessage) {

        }
    },
    RAISE_EXCEPTION {
        @Override
        public void fail(String fullMessage) {
            throw new ConditionFailedException(fullMessage);
        }
    },
    RAISE_ERROR {
        @Override
        public void fail(String fullMessage) {
            throw new ConditionFailedError(fullMessage);
        }
    },
    LOG_INFO {
        @Override
        public void fail(String fullMessage) {
            loggerHolder.getLogger().ifPresent(logger -> logger.info(fullMessage));
        }
    },
    LOG_WARN {
        @Override
        public void fail(String fullMessage) {
            loggerHolder.getLogger().ifPresent(logger -> logger.warn(fullMessage));
        }
    };

    @Getter private boolean skipped;

    ConditionFailAction() {
        this(false);
    }

    private static final CurrentLoggerHolder loggerHolder = new CurrentLoggerHolder(ConditionFailAction.class);

    public abstract void fail(String fullMessage);
}
