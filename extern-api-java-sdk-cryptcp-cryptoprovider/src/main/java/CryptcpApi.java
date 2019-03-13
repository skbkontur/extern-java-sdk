/*
 * Copyright (c) 2019 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class CryptcpApi {

    private final String executablePath;

    CryptcpApi(String executablePath) {
        this.executablePath = executablePath;
    }

    public byte[] decrypt(String thumbprint, byte[] content) throws IOException {
        return doWithIOTempFiles((in, out) -> {
            Files.write(in, content, StandardOpenOption.CREATE);

            run(String.format("-decr -thumbprint %s %s %s", thumbprint, in, out));
            return Files.readAllBytes(out);
        });
    }

    public byte[] sign(String thumbprint, byte[] signData) throws IOException {
        return doWithIOTempFiles((in, out) -> {
            Files.write(in, signData, StandardOpenOption.CREATE);
            run(String.format("-sign -thumbprint %s %s %s", thumbprint, in, out));
            return Files.readAllBytes(out);
        })
    }

    private <T> T doWithIOTempFiles(ThrowableBiFunction<Path, Path, T, IOException> function)
            throws IOException {
        Path in = Files.createTempFile("CryptcpApi", ".in");
        Path out = Files.createTempFile("CryptcpApi", ".out");

        try {
            return function.apply(in, out);
        } finally {
            Files.delete(in);
            Files.delete(out);
        }
    }

    private int run(String arguments) throws IOException {
        Process exec = Runtime.getRuntime().exec(executablePath + " " + arguments);
        try {
            exec.waitFor();
            if (exec.exitValue() != 0) {
                String result = readAll(exec.getInputStream());
                throw new IOException(result);
            }
            return exec.exitValue();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } finally {
            exec.destroy();
        }
    }

    private String readAll(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
    }
}
