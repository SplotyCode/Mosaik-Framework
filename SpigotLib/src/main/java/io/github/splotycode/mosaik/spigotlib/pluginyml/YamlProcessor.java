package io.github.splotycode.mosaik.spigotlib.pluginyml;

import io.github.splotycode.mosaik.spigotlib.SpigotApplicationType;
import io.github.splotycode.mosaik.util.StringUtil;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("me.david.davidlib.spigotlib.pluginyml.GenerateYaml")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class YamlProcessor extends AbstractProcessor {

    private ProcessingEnvironment pEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        pEnvironment = processingEnvironment;
        super.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        for (Element element : environment.getElementsAnnotatedWith(GenerateYaml.class)) {
            if (!(element instanceof TypeElement)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@GenerateYaml is not a TypeElement");
                return false;
            }
            TypeElement mainTypeElement = (TypeElement) element;
            if (!processingEnv.getTypeUtils().isSubtype(mainTypeElement.asType(), processingEnv.getElementUtils().getTypeElement(SpigotApplicationType.class.getName()).asType())) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@GenerateYaml does not implement SpigotPluginApplicationType");
                return false;
            }
            try {
                FileObject descriptionFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml");
                try (Writer writer = descriptionFile.openWriter()) {
                    writer.append("# plugin.yml generated with DavidLib\n");
                    GenerateYaml annotation = mainTypeElement.getAnnotation(GenerateYaml.class);
                    String main = String.valueOf(pEnvironment.getElementUtils().getBinaryName(mainTypeElement));
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Building plugin.yml for " + main);
                    writer.append("main: ").append(main).append("\n");
                    writer.append("name: ").append(annotation.name()).append("\n");
                    writer.append("version: ").append(String.valueOf(annotation.version())).append("\n");
                    writer.append("author: ").append(annotation.author()).append("\n");
                    writer.append("depend: ").append(StringUtil.join(annotation.dependencies(), obj -> " + obj + ", ", ")).append("\n");
                    writer.append("softdepend: ").append(StringUtil.join(annotation.softDependencies(), obj -> " + obj + ", ", ")).append("\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException("Failed to create plugin.yml", ex);
            }
        }
        return true;
    }
}
