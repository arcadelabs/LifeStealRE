package in.arcadelabs.lifesteal.utils.backend;

import com.google.common.collect.ImmutableSet;
import lombok.SneakyThrows;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassRegistration {

    public ClassRegistration init(String packageName) {
        for (Class<?> clazz : getClassesInPackage(packageName)) {
            try {
                clazz.newInstance();
            } catch (Exception ignored) {}
        }

        return this;
    }

    @SneakyThrows
    public Collection<Class<?>> getClassesInPackage(String packageName) {
        final Collection<Class<?>> classes = new ArrayList<>();
        final CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
        final URL resource = codeSource.getLocation();

        final String relPath = packageName.replace(".", "/");
        final String resPath = resource.getPath().replace("%20", " ");
        final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

        final JarFile jarFile = new JarFile(jarPath);
        final Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace("/", ".").replace("\\", ".").replace(".class", "");
            }

            if (className != null) {
                classes.add(Class.forName(className));
            }
        }

        jarFile.close();

        return ImmutableSet.copyOf(classes);
    }
}
