package io.swagger.util.supplier.filer;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to manage Temp directories
 * @author dedece35
 */
public final class DirectoryTempHelper {

    private static Logger LOGGER = LoggerFactory.getLogger(DirectoryTempHelper.class);

    public static final String GENERATION_TMP_DIR_PREFIX = "swagger-core-";

    private DirectoryTempHelper() {
    }

    /**
     * Create a Temp irectory with a prefix and random long
     * @return Fiel object representing Temp directory
     * @throws IOException
     */
    public static File createTmpDir() throws IOException {
        String tmpDirName = System.getProperty("java.io.tmpdir");
        if (tmpDirName == null) {
            tmpDirName = "";
        } else {
            tmpDirName += File.separator;
        }
        Random r = new Random();
        String name = tmpDirName + GENERATION_TMP_DIR_PREFIX + r.nextLong();
        File rootDir = new File(name);
        if (!rootDir.mkdir()) {
            throw new IOException("Unable to make Temp directory " + rootDir.getAbsolutePath());
        }
        return rootDir;
    }

    /**
     * Delete recursively a Temp directory and its content
     * @param rootDir File Object representing a Temp root directory to delete
     */
    public static void deleteDir(final File rootDir) {
        for (File f : rootDir.listFiles()) {
            if (f.isDirectory()) {
                deleteDir(f);
            } else {
                if (!f.delete()) {
                    LOGGER.error("Unable to delete file '{}'", f.getAbsolutePath());
                }
            }
        }
        if (!rootDir.delete()) {
            LOGGER.error("Unable to delete directory '{}'" + rootDir.getAbsolutePath());
        }
    }

}
