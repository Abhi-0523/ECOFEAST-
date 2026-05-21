package com.ecofeast;

import java.io.File;
import java.net.BindException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.catalina.startup.Tomcat;

/**
 * Local launcher for running the web app directly from IntelliJ without an external Tomcat install.
 */
public final class EcoFeastApplication {
    private static final int MAX_PORT_TRIES = 40;

    private EcoFeastApplication() {
    }

    public static void main(String[] args) throws Exception {
        int preferredPort = Integer.parseInt(System.getProperty("port", "8081"));
        File webAppDir = resolveExistingPath("src/main/webapp", "EcoFeast/src/main/webapp");
        File classesDir = resolveExistingPath("target/classes", "out/production/classes", "EcoFeast/target/classes");

        LifecycleException lastFailure = null;
        for (int offset = 0; offset < MAX_PORT_TRIES; offset++) {
            int port = preferredPort + offset;
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

            try {
                tomcat.start();
                if (offset > 0) {
                    System.out.println("Note: port " + preferredPort + " was busy; using " + port + " instead.");
                }
                System.out.println("EcoFeast running at http://localhost:" + port + "/ecofeast/");
                tomcat.getServer().await();
                return;
            } catch (LifecycleException e) {
                lastFailure = e;
                try {
                    tomcat.stop();
                    tomcat.destroy();
                } catch (Exception ignored) {
                    // best-effort cleanup before retry
                }
                if (!isBindException(e) || offset == MAX_PORT_TRIES - 1) {
                    throw e;
                }
                System.err.println("Port " + port + " unavailable (" + rootCauseMessage(e) + "), trying " + (port + 1) + "...");
            }
        }
        if (lastFailure != null) {
            throw lastFailure;
        }
    }

    private static boolean isBindException(Throwable t) {
        while (t != null) {
            if (t instanceof BindException) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private static String rootCauseMessage(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t.getClass().getSimpleName() + ": " + t.getMessage();
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
