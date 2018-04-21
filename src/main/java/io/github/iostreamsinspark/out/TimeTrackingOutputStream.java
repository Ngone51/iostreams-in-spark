/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.iostreamsinspark.out;

import io.github.iostreamsinspark.util.ManualClock;

import java.io.IOException;
import java.io.OutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Note: We intentionally removed [[org.apache.spark.executor.ShuffleWriteMetrics]] since it's
 * a spark support class. And we introduce [[ManualClock]] to simulate time elapse and
 * [[timeCounter]] for measuring time consuming.
 *
 * Intercepts write calls and tracks total time spent writing in order to update shuffle write
 * metrics. Not thread safe.
 */
public final class TimeTrackingOutputStream extends OutputStream {
    private final ManualClock clock;
    private long timeCounter = 0L;
    private final OutputStream outputStream;

    public TimeTrackingOutputStream(ManualClock clock, OutputStream outputStream) {
        this.clock = clock;
        this.outputStream = outputStream;
    }

    public void reset() {
        timeCounter = 0L;
    }

    public long getTimeCounter() {
        return timeCounter;
    }

    @Override
    public void write(int b) throws IOException {
        final long startTime = clock.getTimeMillis();
        outputStream.write(b);
        timeCounter += clock.getTimeMillis() - startTime;
    }

    @Override
    public void write(byte[] b) throws IOException {
        final long startTime = clock.getTimeMillis();
        outputStream.write(b);
        timeCounter += clock.getTimeMillis() - startTime;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        final long startTime = clock.getTimeMillis();
        outputStream.write(b, off, len);
        timeCounter += clock.getTimeMillis() - startTime;
    }

    @Override
    public void flush() throws IOException {
        final long startTime = clock.getTimeMillis();
        outputStream.flush();
        timeCounter += clock.getTimeMillis() - startTime;
    }

    @Override
    public void close() throws IOException {
        final long startTime = System.nanoTime();
        outputStream.close();
        timeCounter += clock.getTimeMillis() - startTime;
    }
}

