package io.github.iostreamsinspark.out;

import io.github.iostreamsinspark.util.ManualClock;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @author wuyi
 * @date 2018/4/21
 *
 * Note: set -ea option to enable JVM to use assert()
 */
public class TimeTrackerOutputStreamSuite {
    private static ManualClock clock = new ManualClock(0);
    private TimeTrackingOutputStream timeTrackingStream;
    private class MyOutputStream extends OutputStream {

        public MyOutputStream() {}

        @Override
        public void write(int b) throws IOException {
            // we pretend to write something here...
            // writing...
            // finish...
            // and 1000 millis pass away...
            clock.advance(1000);
        }

        @Override
        public void write(byte[] b) throws IOException {
            // we pretend to write something here...
            // writing...
            // finish...
            // and 1000 millis pass away...
            clock.advance(2000);
        }

        // in order to be simplify, we do not simulate other write methods...

        public void close() throws IOException {
            super.close();
        }
    }

    @Before
    public void setUp() {
        timeTrackingStream = new TimeTrackingOutputStream(clock, new MyOutputStream());
    }

    @After
    public void tearDown() {
        // do nothing
    }

    @Test
    public void test() {
        int WHATEVER = 51;
        try {
            assert(clock.getTimeMillis() == 0);
            timeTrackingStream.write(WHATEVER);
            assert(timeTrackingStream.getTimeCounter() == 1000);
            assert(clock.getTimeMillis() == 1000);
            // reset our timeCounter
            timeTrackingStream.reset();
            timeTrackingStream.write("Hello World!".getBytes());
            assert(clock.getTimeMillis() == 3000);
            // 3000 - 1000 = 2000
            assert(timeTrackingStream.getTimeCounter() == 2000);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                timeTrackingStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
