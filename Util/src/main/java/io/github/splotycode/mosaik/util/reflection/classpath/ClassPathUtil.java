package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.StringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassPathUtil {

    private static final String MANIFEST_ATTRIBUTE_CLASS_PATH = Attributes.Name.CLASS_PATH.toString();

    public interface JarMetaPathCollector {

        void accept(File metaPath) throws IOException;

    }

    public static Set<File> getJarMetaPaths(File file, JarFile jarFile) throws IOException {
        Set<File> classpathFiles = new HashSet<>();
        collectJarMetaPaths(file, jarFile, classpathFiles::add);
        return classpathFiles;
    }

    public static void collectJarMetaPaths(File file, JarFile jarFile, JarMetaPathCollector collector) throws IOException {
        String classpathAttribute = jarFile.getManifest()
                .getMainAttributes().getValue(MANIFEST_ATTRIBUTE_CLASS_PATH);
        if (classpathAttribute == null) return ;
        for (String path : classpathAttribute.split(" ")) {
            if (!StringUtil.isEmptyDeep(path)) {
                URL url = new URL(file.toURI().toURL(), path);
                collector.accept(new File(url.getFile()));
            }
        }
    }

}
