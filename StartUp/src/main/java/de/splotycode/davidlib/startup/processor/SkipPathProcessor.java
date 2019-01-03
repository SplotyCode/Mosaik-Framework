package de.splotycode.davidlib.startup.processor;

import me.david.davidlib.util.core.startup.SkipPath;
import me.david.davidlib.utils.StringUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SupportedAnnotationTypes("me.david.davidlib.startup.SkipPath")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SkipPathProcessor extends AbstractProcessor {

    private static AtomicLong id = new AtomicLong(1);
    private static Set<String> paths = ConcurrentHashMap.newKeySet();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment environment) {
        List<String> skippedPaths = new ArrayList<>();
        for (Element element : environment.getElementsAnnotatedWith(SkipPath.class)) {
            if (!(element instanceof TypeElement)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@SkipPath is not a TypeElement");
                return false;
            }
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "[SkipPath] Found " + element.getSimpleName());
            TypeElement mainTypeElement = (TypeElement) element;
            SkipPath annotation = mainTypeElement.getAnnotation(SkipPath.class);
            skippedPaths.addAll(Arrays.asList(annotation.value()));
        }
        for (String path : new ArrayList<>(skippedPaths)) {
            if (paths.contains(path)) skippedPaths.remove(path);
            else paths.add(path);
        }
        if (!skippedPaths.isEmpty()) {
            try {
                long id = this.id.incrementAndGet();
                FileObject descriptionFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "disabled_paths_" + id + ".txt");
                try (Writer writer = descriptionFile.openWriter()) {
                    writer.append("# disabled_paths.txt generated with DavidLib\n");
                    writer.append(StringUtil.join(skippedPaths, obj -> obj, "\n"));
                }
            } catch (IOException ex) {
                throw new RuntimeException("Failed to create disabled_paths.txt", ex);
            }
        }
        return true;
    }

}
