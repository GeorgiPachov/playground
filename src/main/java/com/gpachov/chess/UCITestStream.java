package com.gpachov.chess;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class UCITestStream  extends BufferedInputStream {
    private Runnable callback;
    public UCITestStream(InputStream in, Runnable callback) {
        super(in);
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = super.read(b);
        if (read == -1) {
            invokeCallback();
        }
        return read;
    }

    private void invokeCallback() {
        Optional.ofNullable(callback).ifPresent(Runnable::run);
    }

    @Override
    public synchronized int read() throws IOException {
        int read = super.read();
        if (read == -1) {
            invokeCallback();
        }
        return read;
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int read =  super.read(b, off, len);
        if (read == -1) {
            invokeCallback();
        }
        return read;
    }
}
