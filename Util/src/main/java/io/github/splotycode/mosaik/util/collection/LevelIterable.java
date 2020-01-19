package io.github.splotycode.mosaik.util.collection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@NoArgsConstructor
public class LevelIterable<E> implements Iterable<E> {

    private HashMap<Class, Handler<E, Object>> handlers = new HashMap<>();
    private Consumer<LevelIterator> start;

    public LevelIterable(Iterable base) {
        start = iterator -> iterator.addAll(base);
    }

    public LevelIterable(Supplier<Iterable> base) {
        start = iterator -> iterator.addAll(base.get());
    }

    public LevelIterable(Iterator base) {
        this(iterator -> iterator.addAll(base));
    }

    public LevelIterable(Consumer<LevelIterator> start) {
        this.start = start;
    }

    @Override
    public Iterator<E> iterator() {
        return new LevelIterator();
    }

    public interface Context<E> {

        Context<E> addAll(Iterator iterator);
        Context<E> addAll(Iterable iterable);
        Context<E> add(E element);

    }

    public static final Handler IGNORE_HANDLER = (context, element) -> false;

    @SuppressWarnings("unchecked")
    public static <E, O> Handler<E, O> ignoreHandler() {
        return IGNORE_HANDLER;
    }

    public interface Handler<E, O> {

        boolean process(Context<E> context, O element);

    }

    public <O> LevelIterable<E> on(Class<O> clazz, Handler<E, O> handler) {
        handlers.put(clazz, (Handler<E, Object>) handler);
        return this;
    }

    protected void start(LevelIterator context) {
        if (start != null) {
            start.accept(context);
        }
    }

    protected boolean process(LevelIterator context, Object element) {
        return true;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class LevelIterator extends SimpleIterator<E> implements Context<E> {

        private Queue<E> preferred = new ArrayDeque<>();
        private Stack<Iterator> stack = new Stack<>();

        {
            start.accept(this);
        }

        @Override
        public LevelIterator addAll(Iterator iterator) {
            stack.push(iterator);
            return this;
        }

        @Override
        public LevelIterator addAll(Iterable iterable) {
            return addAll(iterable.iterator());
        }

        @Override
        public LevelIterator add(E element) {
            preferred.add(element);
            return this;
        }


        protected boolean checkHandlers(Object element) {
            Class clazz = element == null ? null : element.getClass();
            Handler handler = handlers.get(clazz);
            if (handler != null) {
                return handler.process(this, element);
            }
            if (clazz == null) {
                return true;
            }
            for (Map.Entry<Class, Handler<E, Object>> entry : handlers.entrySet()) {
                if (entry.getKey() != null && entry.getKey().isAssignableFrom(clazz)) {
                    return entry.getValue().process(this, element);
                }
            }
            return true;
        }

        @Override
        protected boolean provideNext() {
            while (true) {
                if (!preferred.isEmpty()) {
                    next = preferred.remove();
                    return true;
                }

                Iterator current = stack.empty() ? null : stack.peek();
                if (current == null) {
                    return false;
                }
                if (current.hasNext()) {
                    Object element = current.next();
                    if (process(this, element) && checkHandlers(element)) {
                        if (element instanceof Iterator) {
                            stack.push((Iterator) element);
                        } else if (element instanceof Iterable) {
                            stack.push(((Iterable) element).iterator());
                        } else {
                            try {
                                next = (E) element;
                                return true;
                            } catch (ClassCastException ex) {

                            }
                        }
                    }
                } else {
                    stack.pop();
                }
            }
        }

    }

}
