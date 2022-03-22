package io.swagger.v3.plugin.maven.jakarta;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Transforms swagger-maven-plugin jar file to be used with Jakarta namespace using artifactId swagger-maven-plugin-jakarta
 *
 */
public class JakartaTransformer {


    /*
     *  Substitution tokens + regex
     */
    private static Set<String> files =
            Stream.of(
                    // "META-INF/MANIFEST.MF",
                    "META-INF/maven/plugin.xml",
                    "META-INF/maven/io.swagger.core.v3/swagger-maven-plugin/plugin-help.xml",
                    // "META-INF/plexus/components.xml",
                    "META-INF/maven/io.swagger.core.v3/swagger-maven-plugin/pom.xml",
                    "META-INF/maven/io.swagger.core.v3/swagger-maven-plugin/pom.properties")
                    .collect(Collectors.toCollection(HashSet::new));

    private static final String jakartaXmlBindDep = "<dependency>\n" +
            "                <groupId>jakarta.xml.bind</groupId>\n" +
            "                <artifactId>jakarta.xml.bind-api</artifactId>\n" +
            "                <version>VERSION</version>\n" +
            "            </dependency>\n";

    private static final String jakartaXmlBindRegex = "^.*(<dependency>.*jakarta\\.xml\\.bind((?!</dependency>).)*</dependency>).*$";

    private static final String jakartaValidationDep = "<dependency>\n" +
            "                <groupId>jakarta.validation</groupId>\n" +
            "                <artifactId>jakarta.validation-api</artifactId>\n" +
            "                <version>VERSION</version>\n" +
            "            </dependency>\n";

    private static final String jakartaValidationRegex = "^.*(<dependency>.*jakarta\\.validation((?!</dependency>).)*</dependency>).*$";

    private static final String jacksonJsonDep = "<dependency>\n" +
            "                <groupId>com.fasterxml.jackson.jakarta.rs</groupId>\n" +
            "                <artifactId>jackson-jakarta-rs-json-provider</artifactId>\n" +
            "                <version>VERSION</version>\n" +
            "                <exclusions>\n" +
            "                    <exclusion>\n" +
            "                        <groupId>jakarta.activation</groupId>\n" +
            "                        <artifactId>jakarta.activation-api</artifactId>\n" +
            "                    </exclusion>\n" +
            "                    <exclusion>\n" +
            "                        <groupId>jakarta.xml.bind</groupId>\n" +
            "                        <artifactId>jakarta.xml.bind-api</artifactId>\n" +
            "                    </exclusion>\n" +
            "                </exclusions>\n" +
            "            </dependency>";

    private static final String jacksonJsonRegex = "^.*(<dependency>.*jackson\\-jaxrs\\-json((?!</dependency>).)*</dependency>).*$";

    private static final String jacksonBaseDep = "<dependency>\n" +
            "                <groupId>com.fasterxml.jackson.jakarta.rs</groupId>\n" +
            "                <artifactId>jackson-jakarta-rs-base</artifactId>\n" +
            "                <version>VERSION</version>\n" +
            "            </dependency>";

    private static final String jacksonBaseRegex = "^.*(<dependency>.*jackson\\-jaxrs\\-base((?!</dependency>).)*</dependency>).*$";

    private static final String jacksonJaxbDep = "<dependency>\n" +
            "                <groupId>com.fasterxml.jackson.module</groupId>\n" +
            "                <artifactId>jackson-module-jakarta-xmlbind-annotations</artifactId>\n" +
            "                <version>VERSION</version>\n" +
            "                <exclusions>\n" +
            "                    <exclusion>\n" +
            "                        <groupId>jakarta.activation</groupId>\n" +
            "                        <artifactId>jakarta.activation-api</artifactId>\n" +
            "                    </exclusion>\n" +
            "                    <exclusion>\n" +
            "                        <groupId>jakarta.xml.bind</groupId>\n" +
            "                        <artifactId>jakarta.xml.bind-api</artifactId>\n" +
            "                    </exclusion>\n" +
            "                    <exclusion>\n" +
            "                        <groupId>com.sun.activation</groupId>\n" +
            "                        <artifactId>jakarta.activation</artifactId>\n" +
            "                    </exclusion>" +
            "                </exclusions>\n" +
            "            </dependency>";

    private static final String jacksonJaxbRegex = "^.*(<dependency>.*jackson\\-module\\-jaxb((?!</dependency>).)*</dependency>).*$";

    private static final String jakartaActivationDep = "<dependency>\n" +
            "                <groupId>jakarta.activation</groupId>\n" +
            "                <artifactId>jakarta.activation-api</artifactId>\n" +
            "                <version>VERSION</version>\n" +
            "            </dependency>";

    private static final String jakartaActivationRegex = "^.*(<dependency>.*jakarta\\.activation((?!</dependency>).)*</dependency>).*$";

    /**
     *
     * Extracts Jar into temp directory
     *
     */
    private static void extract(String inPath, String outPath) throws Exception {

        Path target = Paths.get(outPath);
        Path source = Paths.get(inPath);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                Path path = target.resolve(zipEntry.getName()).normalize();

                if (!path.startsWith(target + File.separator)) {
                    throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
                }

                if (zipEntry.isDirectory()) {
                    if (!Files.isDirectory(path)) {
                        Files.createDirectories(path);
                    }
                } else {
                    // fix for Windows-created archives
                    Path parent = path.getParent();
                    if (!Files.isDirectory(parent)) {
                        Files.createDirectories(parent);
                    }

                    // write file content
                    try (OutputStream os = Files.newOutputStream(path)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    /**
     *
     * Compress temp directory back into Jar after transformed
     *
     */
    public static void jar(Path sourcePath, String outJarPath) throws IOException {

        URI uri = URI.create("jar:" + Paths.get(outJarPath).normalize().toUri());

        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attributes) {

                if (attributes.isSymbolicLink()) {
                    return FileVisitResult.CONTINUE;
                }

                Map<String, String> env = new HashMap<>();
                env.put("create", "true");

                try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

                    Path targetFile = sourcePath.relativize(file);
                    Path pathInZipfile = zipfs.getPath(targetFile.toString());

                    if (pathInZipfile.getParent() != null) {
                        Files.createDirectories(pathInZipfile.getParent());
                    }

                    CopyOption[] options = {
                            StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.COPY_ATTRIBUTES,
                            LinkOption.NOFOLLOW_LINKS
                    };
                    Files.copy(file, pathInZipfile, options);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                //
                return FileVisitResult.CONTINUE;
            }

        });

    }

    /**
     *
     * Replaces swagger artifact adding `-jakarta` suffix
     *
     */
    private static void replaceInJar(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zis = new ZipInputStream(bis);
             ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filePath + ".jar"))) {

            for (ZipEntry zipEntry; (zipEntry = zis.getNextEntry()) != null; ) {
                out.putNextEntry(new ZipEntry(zipEntry.getName()));

                if (files.contains(zipEntry.getName())) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    for (String line; (line = reader.readLine()) != null; ) {
                        line = line.replace("swagger-maven-plugin", "swagger-maven-plugin-jakarta");
                        line = line.replace("swagger-project", "swagger-project-jakarta");
                        line = line.replace("swagger-jaxrs2", "swagger-jaxrs2-jakarta");
                        line = line.replace("swagger-models", "swagger-models-jakarta");
                        line = line.replace("swagger-annotations", "swagger-annotations-jakarta");
                        line = line.replace("swagger-core", "swagger-core-jakarta");
                        line = line.replace("swagger-integration", "swagger-integration-jakarta");
                        writer.println(line);
                    }
                    writer.flush();
                } else {
                    byte[] buf = new byte[8192];
                    int length;
                    while ((length = zis.read(buf)) > 0) {
                        out.write(buf, 0, length);
                    }
                }

            }
        }
        Files.delete(Paths.get(filePath));
        Files.move(Paths.get(filePath + ".jar"), Paths.get(filePath));

    }

    public static void main(String[] args) throws Exception {
        String filePath = args[0];
        String outPath = args[1];
        String jakartaVersion = args[2];
        String jakartaActivationVersion = args[3];
        String jacksonVersion = args[4];

        replaceInJar(filePath);
        extract(filePath, outPath);

        // rename dir
        Files.move(Paths.get(outPath + File.separator +
                        "META-INF" + File.separator +
                        "maven" + File.separator +
                        "io.swagger.core.v3" + File.separator +
                        "swagger-maven-plugin"),
                Paths.get(outPath + File.separator +
                        "META-INF" + File.separator +
                        "maven" + File.separator +
                        "io.swagger.core.v3" + File.separator +
                        "swagger-maven-plugin-jakarta")
        );

        Path pluginXmlPath = Paths.get(
                outPath +
                        File.separator + "META-INF" +
                        File.separator + "maven" +
                        File.separator + "plugin.xml");
        String pluginXml = new String(Files.readAllBytes(pluginXmlPath));
        Matcher m = Pattern.compile(jakartaXmlBindRegex, Pattern.DOTALL).matcher(pluginXml);
        while (m.find()) {
            pluginXml = pluginXml.substring(m.start(), m.start(1)) +
                    jakartaXmlBindDep.replace("VERSION", jakartaVersion) +
                    pluginXml.substring(m.end(1), m.end());
        }

        m = Pattern.compile(jakartaValidationRegex, Pattern.DOTALL).matcher(pluginXml);
        while (m.find()) {
            pluginXml = pluginXml.substring(m.start(), m.start(1)) +
                    jakartaValidationDep.replace("VERSION", jakartaVersion) +
                    pluginXml.substring(m.end(1), m.end());
        }

        m = Pattern.compile(jacksonJsonRegex, Pattern.DOTALL).matcher(pluginXml);
        while (m.find()) {
            pluginXml = pluginXml.substring(m.start(), m.start(1)) +
                    jacksonJsonDep.replace("VERSION", jacksonVersion) +
                    pluginXml.substring(m.end(1), m.end());
        }

        m = Pattern.compile(jacksonBaseRegex, Pattern.DOTALL).matcher(pluginXml);
        while (m.find()) {
            pluginXml = pluginXml.substring(m.start(), m.start(1)) +
                    jacksonBaseDep.replace("VERSION", jacksonVersion) +
                    pluginXml.substring(m.end(1), m.end());
        }

        m = Pattern.compile(jacksonJaxbRegex, Pattern.DOTALL).matcher(pluginXml);
        while (m.find()) {
            pluginXml = pluginXml.substring(m.start(), m.start(1)) +
                    jacksonJaxbDep.replace("VERSION", jacksonVersion) +
                    pluginXml.substring(m.end(1), m.end());
        }

        m = Pattern.compile(jakartaActivationRegex, Pattern.DOTALL).matcher(pluginXml);
        while (m.find()) {
            pluginXml = pluginXml.substring(m.start(), m.start(1)) +
                    jakartaActivationDep.replace("VERSION", jakartaActivationVersion) +
                    pluginXml.substring(m.end(1), m.end());
        }

        // write back to plugin.xml file
        Files.write(pluginXmlPath, pluginXml.getBytes());

        // delete the original jar
        Files.delete(Paths.get(filePath));
        // compress the new jar
        jar(Paths.get(outPath), filePath);
        // delete the temp directory
        Files.walk(Paths.get(outPath))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }

}
