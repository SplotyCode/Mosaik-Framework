package io.github.splotycode.mosaik.valuetransformer;

public class TransformerNotFoundException extends TransformException {

    public TransformerNotFoundException() {
    }

    public TransformerNotFoundException(String s) {
        super(s);
    }

    public TransformerNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TransformerNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public TransformerNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
