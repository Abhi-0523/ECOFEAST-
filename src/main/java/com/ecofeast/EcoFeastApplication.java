package com.ecofeast;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.catalina.startup.Tomcat;

/**
 * Local launcher for running the web app directly from IntelliJ without an external Tomcat install.
 */
public final class EcoFeastApplication {
    private EcoFeastApplication() {
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("port", "8081"));
        File webAppDir = resolveExistingPath("src/main/webapp", "EcoFeast/src/main/webapp");
        File classesDir = resolveExistingPath("target/classes", "out/production/classes", "EcoFeast/target/classes");

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(createBaseDir().getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addWebapp("/ecofeast", webAppDir.getAbsolutePath());
        context.setParentClassLoader(EcoFeastApplication.class.getClassLoader());

        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(
                resources,
                "/WEB-INF/classes",
                classesDir.getAbsolutePath(),
                "/"));
        context.setResources(resources);

        tomcat.start();
        System.out.println("EcoFeast running at http://localhost:" + port + "/ecofeast/");
        tomcat.getServer().await();
    }

    private static File createBaseDir() throws Exception {
        Path tempDir = Files.createTempDirectory("ecofeast-tomcat");
        File baseDir = tempDir.toFile();
        baseDir.deleteOnExit();
        return baseDir;
    }

    private static File resolveExistingPath(String... candidates) {
        for (String candidate : candidates) {
            File file = new File(candidate);
            if (file.exists()) {
                return file.getAbsoluteFile();
            }
        }
        throw new IllegalStateException("Could not locate required project path. Checked: " + String.join(", ", candidates));
    }
}
