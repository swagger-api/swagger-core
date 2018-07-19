package io.swagger.util.supplier.filer;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test creation and deletion of Temp Directory
 * @author dedece35
 */
public class DirectoryTempHelperTest {

    @Test
    public void testCreateTmpDir() throws IOException {

        File tmpDir = DirectoryTempHelper.createTmpDir();

        Assert.assertNotNull(tmpDir);
        Assert.assertTrue(tmpDir.exists());
        Assert.assertTrue(tmpDir.isDirectory());
        Assert.assertNotNull(tmpDir.getPath());
        Assert.assertTrue(tmpDir.getPath().startsWith(System.getProperty("java.io.tmpdir") + DirectoryTempHelper.GENERATION_TMP_DIR_PREFIX));

    }

    @Test
    public void testDeleteTmpDir() throws IOException {

        // preparing Tmp Dir
        File tmpDir = DirectoryTempHelper.createTmpDir();
        Assert.assertNotNull(tmpDir);
        Assert.assertTrue(tmpDir.exists());
        Assert.assertTrue(tmpDir.isDirectory());

        // adding one file to tmp dir
        File fileTmp = new File(tmpDir.getPath() + "/myfile.txt");
        fileTmp.createNewFile();
        Assert.assertNotNull(fileTmp);
        Assert.assertTrue(fileTmp.exists());
        Assert.assertTrue(fileTmp.isFile());

        // adding one child dir to tmp dir
        File childDirTmp = new File(tmpDir.getPath() + "/mydir");
        childDirTmp.mkdir();
        Assert.assertNotNull(childDirTmp);
        Assert.assertTrue(childDirTmp.exists());
        Assert.assertTrue(childDirTmp.isDirectory());

        // deleting Tmp dir
        DirectoryTempHelper.deleteDir(tmpDir);
        Assert.assertFalse(tmpDir.exists());
        Assert.assertFalse(fileTmp.exists());

    }

}
