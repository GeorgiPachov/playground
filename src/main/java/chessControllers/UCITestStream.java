package chessControllers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UCITestStream  extends BufferedInputStream {
    public UCITestStream(InputStream in) {
        super(in);
    }

    public UCITestStream(InputStream in, int size) {
        super(in, size);
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = super.read(b);
        if (read == -1) {
            try {
                Thread.sleep(70*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return read;
    }

    @Override
    public synchronized int read() throws IOException {
        int read = super.read();
        if (read == -1) {
            try {
                Thread.sleep(70*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return read;
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int read =  super.read(b, off, len);
        if (read == -1) {
            try {
                Thread.sleep(70*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return read;
    }
}
