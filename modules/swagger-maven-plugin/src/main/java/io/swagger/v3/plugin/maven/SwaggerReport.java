package io.swagger.v3.plugin.maven;

import org.codehaus.doxia.sink.Sink;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

/**
 * Generates an interactive Swagger UI page from the project's OpenAPI
 * specification and integrates it into the Maven site as a report.
 *
 * <p>By default the report looks for {@code openapi.json} or
 * {@code openapi.yaml} in {@code ${project.build.directory}}.
 * Run the {@code resolve} goal first to generate the spec file.</p>
 *
 * <p>Usage in a consumer POM:</p>
 * <pre>{@code
 * <reporting>
 *   <plugins>
 *     <plugin>
 *       <groupId>io.swagger.core.v3</groupId>
 *       <artifactId>swagger-maven-plugin</artifactId>
 *       <reportSets>
 *         <reportSet>
 *           <reports>
 *             <report>openapi-report</report>
 *           </reports>
 *         </reportSet>
 *       </reportSets>
 *     </plugin>
 *   </plugins>
 * </reporting>
 * }</pre>
 *
 * @since 2.2.48
 */
@Mojo(name = "openapi-report", threadSafe = true)
public class SwaggerReport extends AbstractMojo implements MavenReport {

    private static final String OUTPUT_NAME = "swagger-ui";

    @Parameter(defaultValue = "${project.reporting.outputDirectory}",
               readonly = true, required = true)
    private File reportOutputDirectory;

    /**
     * Path to the OpenAPI spec file.  When unset the report probes
     * {@code ${project.build.directory}/openapi.json} then
     * {@code ${project.build.directory}/openapi.yaml}.
     */
    @Parameter(property = "swagger.report.specFile")
    private File specFile;

    /**
     * Skip generation of the OpenAPI report.
     */
    @Parameter(property = "swagger.report.skip", defaultValue = "false")
    private boolean skip;

    /**
     * Swagger UI version loaded from the unpkg CDN.
     */
    @Parameter(property = "swagger.report.swaggerUiVersion",
               defaultValue = "5.18.2")
    private String swaggerUiVersion;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    // -- AbstractMojo --------------------------------------------------------

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            generate(null, Locale.getDefault());
        } catch (MavenReportException e) {
            throw new MojoExecutionException(
                    "Failed to generate OpenAPI report", e);
        }
    }

    // -- MavenReport ---------------------------------------------------------

    @Override
    public void generate(Sink sink, Locale locale) throws MavenReportException {
        File resolved = resolveSpecFile();
        if (resolved == null) {
            getLog().warn("OpenAPI spec file not found. "
                    + "Run the swagger-maven-plugin:resolve goal first, "
                    + "or set <specFile> to the path of your spec.");
            return;
        }

        File outputDir = getReportOutputDirectory();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new MavenReportException(
                    "Could not create report output directory: " + outputDir);
        }

        try {
            String specFileName = resolved.getName();
            Files.copy(resolved.toPath(),
                    new File(outputDir, specFileName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            String html = buildSwaggerUiPage(specFileName);
            File htmlFile = new File(outputDir, OUTPUT_NAME + ".html");
            Files.write(htmlFile.toPath(),
                    html.getBytes(StandardCharsets.UTF_8));

            getLog().info("OpenAPI report generated at "
                    + htmlFile.getAbsolutePath());
        } catch (IOException e) {
            throw new MavenReportException(
                    "Failed to generate OpenAPI report", e);
        }
    }

    @Override
    public String getOutputName() {
        return OUTPUT_NAME;
    }

    @Override
    public String getCategoryName() {
        return CATEGORY_PROJECT_REPORTS;
    }

    @Override
    public String getName(Locale locale) {
        return "OpenAPI Specification";
    }

    @Override
    public String getDescription(Locale locale) {
        return "Interactive Swagger UI documentation for the project's "
                + "OpenAPI specification.";
    }

    @Override
    public void setReportOutputDirectory(File directory) {
        this.reportOutputDirectory = directory;
    }

    @Override
    public File getReportOutputDirectory() {
        return reportOutputDirectory;
    }

    @Override
    public boolean isExternalReport() {
        return true;
    }

    @Override
    public boolean canGenerateReport() {
        return !skip && resolveSpecFile() != null;
    }

    // -- internal ------------------------------------------------------------

    File resolveSpecFile() {
        if (specFile != null && specFile.isFile()) {
            return specFile;
        }
        File buildDir = new File(project.getBuild().getDirectory());
        File json = new File(buildDir, "openapi.json");
        if (json.isFile()) {
            return json;
        }
        File yaml = new File(buildDir, "openapi.yaml");
        if (yaml.isFile()) {
            return yaml;
        }
        return null;
    }

    private String buildSwaggerUiPage(String specFileName) {
        String projectName = project.getName() != null
                ? project.getName() : project.getArtifactId();
        String title = escapeHtml(projectName) + " \u2013 OpenAPI";

        return "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "<head>\n"
            + "  <meta charset=\"UTF-8\">\n"
            + "  <meta name=\"viewport\""
            + " content=\"width=device-width, initial-scale=1.0\">\n"
            + "  <title>" + title + "</title>\n"
            + "  <link rel=\"stylesheet\""
            + " href=\"https://unpkg.com/swagger-ui-dist@"
            + escapeHtml(swaggerUiVersion) + "/swagger-ui.css\">\n"
            + "  <style>\n"
            + "    html { box-sizing: border-box; overflow-y: scroll; }\n"
            + "    *, *:before, *:after { box-sizing: inherit; }\n"
            + "    body { margin: 0; background: #fafafa; }\n"
            + "  </style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div id=\"swagger-ui\"></div>\n"
            + "  <script src=\"https://unpkg.com/swagger-ui-dist@"
            + escapeHtml(swaggerUiVersion)
            + "/swagger-ui-bundle.js\"></script>\n"
            + "  <script>\n"
            + "    window.onload = function () {\n"
            + "      SwaggerUIBundle({\n"
            + "        url: \"" + escapeJs(specFileName) + "\",\n"
            + "        dom_id: '#swagger-ui',\n"
            + "        deepLinking: true,\n"
            + "        presets: [\n"
            + "          SwaggerUIBundle.presets.apis,\n"
            + "          SwaggerUIBundle.SwaggerUIStandalonePreset\n"
            + "        ],\n"
            + "        layout: \"BaseLayout\"\n"
            + "      });\n"
            + "    };\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>\n";
    }

    static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }

    static String escapeJs(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("'", "\\'")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }
}
