package io.github.splotycode.mosaik.util.reflection.classpath;

public interface ClassPathVisitor {
    ClassPathVisitor ACCEPT_ALL = new ClassPathVisitor() {
        @Override
        public VisitorAction visitPackage(String packagePath) {
            return VisitorAction.CONTINUE;
        }

        @Override
        public VisitorAction visitResource(String fullName) {
            return VisitorAction.CONTINUE;
        }
    };

    ClassPathVisitor ONLY_RESOURCES = new ClassPathVisitor() {
        @Override
        public VisitorAction visitPackage(String packagePath) {
            return VisitorAction.CONTINUE;
        }

        @Override
        public VisitorAction visitResource(String fullName) {
            return fullName.endsWith(".class") ? VisitorAction.SKIP : VisitorAction.CONTINUE;
        }
    };

    VisitorAction visitPackage(String packagePath);

    VisitorAction visitResource(String fullName);

}
