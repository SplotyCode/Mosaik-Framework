package io.github.splotycode.mosaik.domparsing.parsing;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class DefaultStringDomParser<O extends Document, R extends DomParser> implements DomParser<O, R> {

    private DomReader<R>[] readers;

    protected String content;
    @Setter protected int index, line;

    @Setter protected boolean skipThis, reHandle;
    @Setter protected DomReader<R> locked;
    protected List<DomReader<R>> activeReaders = new ArrayList<>();

    @SafeVarargs
    protected final void setReaders(DomReader<R>... readers) {
        this.readers = readers;
        Collections.addAll(activeReaders, readers);
    }

    protected abstract O getResult();

    @SuppressWarnings("unchecked")
    protected R self() {
        return (R) this;
    }

    protected void reset() {
        content = null;
        locked = null;
        index = 0;
        line = 1;
        skipThis = reHandle = false;
        activeReaders.clear();

    }

    protected void setContent(DomInput input) {
        if (content != null) {
            reset();
        }

        Objects.requireNonNull(input, "input");
        content = Objects.requireNonNull(input.getString(), "content");
    }

    @Override
    public O parse(DomInput input) {
        setContent(input);

        while (index < content.length()) {
            char c = content.charAt(index);
            if (isLocked()) {
                getLocked().readNext(c, self());
            } else for (DomReader<R> reader : getActiveReaders()) {
                try {
                    reader.readNext(c, self());
                } catch (Throwable throwable) {
                    throw new DomParseException("Exception in readNext() method", throwable);
                }
                if (skipThis) {
                    skipThis = false;
                    break;
                }
            }
            if (reHandle) {
                reHandle = false;
            } else {
                if (c == '\n') line++;
                index++;
            }
        }
        for (DomReader<?> reader : getReaders()) {
            reader.parseDone();
        }
        return getResult();
    }

    @Override
    public char getChar() {
        return content.charAt(index);
    }

    @Override
    public char getChar(int next) {
        return content.charAt(index+next);
    }

    @Override
    public void reHandle() {
        reHandle = true;
    }

    @Override
    public void disableReader(DomReader<R> reader) {
        activeReaders.remove(reader);
    }

    @Override
    public void enableReader(DomReader<R> reader) {
        activeReaders.add(reader);
    }

    @Override
    public void skip(int i) {
        line += content.substring(index, index + i).split("\r\n|\r|\n").length;
        index += i;
    }

    @Override
    public void skip() {
        skip(1);
    }

    @Override
    public boolean skipIfFollow(String next) {
        return skipIfFollow(content, false);
    }

    protected boolean skipIfFollow(String next, boolean ignoreCase) {
        if (index + next.length() > content.length()) {
            return false;
        }
        if (!content.regionMatches(ignoreCase, index, next, 0, next.length())) {
            return false;
        }
        line += StringUtil.countMatches(content, '\n', index, index + next.length());
        index += next.length() - 1;
        return true;
    }

    @Override
    public boolean skipIfFollowIgnoreCase(String text) {
        return skipIfFollow(content, true);
    }

    @Override
    public void stopCurrent() {
        skipThis = true;
    }

    @Override
    public boolean isLocked() {
        return locked != null;
    }

    @Override
    public DomReader<R> getInstance(Class<DomReader<R>> clazz) {
        for (DomReader<R> reader : readers) {
            if (clazz.isInstance(reader)) {
                return reader;
            }
        }
        return null;
    }
}
