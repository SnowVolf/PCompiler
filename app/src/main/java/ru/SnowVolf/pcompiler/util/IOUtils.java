package ru.SnowVolf.pcompiler.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ru.SnowVolf.pcompiler.settings.Preferences;

/**
 * Created by Snow Volf on 25.08.2017, 3:29
 */

public class IOUtils {
    private static int BUFFER = 0x1000; //4K

    public static File saveTemporaryZipFile(String filename, List<File> files) {
        try {
            return saveTemporaryZipFileAndThrow(filename, files);
        } catch (IOException e) {
            Log.e(Constants.TAG, e.getMessage());
        }
        return null;
    }

    private static File saveTemporaryZipFileAndThrow(String filename, List<File> files) throws IOException {
        File zipFile = new File(Preferences.getPatchOutput(), filename);

        ZipOutputStream output = null;
        try {
            output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), BUFFER));

            for (File file : files) {
                FileInputStream fi = new FileInputStream(file);
                BufferedInputStream input = null;
                try {
                    input = new BufferedInputStream(fi, BUFFER);

                    ZipEntry entry = new ZipEntry(file.getName());
                    output.putNextEntry(entry);
                    copy(input, output);
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }

            }
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return zipFile;
    }

    /**
     * Copies all bytes from the input stream to the output stream. Does not
     * close or flush either stream.
     * <p/>
     * Taken from Google Guava ByteStreams.java
     *
     * @param from the input stream to read from
     * @param to   the output stream to write to
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs
     */
    private static long copy(InputStream from, OutputStream to) throws IOException {
        byte[] buf = new byte[BUFFER];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }
}
