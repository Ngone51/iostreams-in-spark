package io.github.iostreamsinspark;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

import java.io.*;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;


/**
 * @author wuyi
 * @date 2018/4/11
 */
public class LimitedInputStreamSuite {
    private String userHome = System.getProperty("user.dir");
    private String projectAbsolutePath = userHome.split("iostreams")[0];
    // the testfile include content below (without double quotes):
    // "Hello, World. This is from wuyi."
    // and size of the whole file is 32 bytes.
    private String testFile = "iostreams-in-spark/src/test/resources/testfile";

    private FileInputStream fis = null;
    private LimitedInputStream lis = null;

    @Before
    public void setUp() {
        try {
            fis = new FileInputStream(projectAbsolutePath + testFile);
            // we create a LimitedInputStream, which is limited to read 13 bytes at most.
            lis = new LimitedInputStream(fis, 13, true);
        } catch (FileNotFoundException e) {
            System.err.println("File " + projectAbsolutePath + " " + testFile + " not found!");
        }
    }

    @After
    public void tearDown() {
        try {
            // we already set closeWrappedStream = true, so fis will be closed
            // at the same time we call close() on lis.
            lis.close();
            fis = null;
            lis = null;
        } catch (IOException e) {
            System.err.println(lis.getClass() + "close failed.");
        }

    }

    @Test
    public void limitedInputStream() {
        FileChannel channel = fis.getChannel();
        byte[] bytes = new byte[20];
        try {
            Assert.assertEquals(0, channel.position());
            Assert.assertEquals(32, channel.size());
            // we try to read 20 bytes from lis, but due to a limit we set above,
            // we can only read 13 bytes at most.
            int l = lis.read(bytes, 0, 20);

            Assert.assertEquals(13, l);
            Assert.assertEquals(l, channel.position());
            Assert.assertEquals("Hello, World.", new String(bytes, 0 , l));
            
        } catch (ClosedChannelException cce) {
            System.err.println(channel.getClass() + "already closed.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
