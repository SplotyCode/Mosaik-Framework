package io.github.splotycode.mosaik.networking.packet.handle;

public class AnnotationStructureExcpetion extends RuntimeException {

    public AnnotationStructureExcpetion() {
    }

    public AnnotationStructureExcpetion(String s) {
        super(s);
    }

    public AnnotationStructureExcpetion(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AnnotationStructureExcpetion(Throwable throwable) {
        super(throwable);
    }

    public AnnotationStructureExcpetion(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
