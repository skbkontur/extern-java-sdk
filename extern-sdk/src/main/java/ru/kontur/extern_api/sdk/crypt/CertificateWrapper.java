/*
 * Copyright (c) 2018 SKB Kontur
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

package ru.kontur.extern_api.sdk.crypt;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.utils.Lazy;

public class CertificateWrapper {

    private final X509Certificate cert;

    private final String thumbprint;

    private final Lazy<Map<String, String>> lIssuerFields;
    private final Lazy<Map<String, String>> lSubjectFields;

    public CertificateWrapper(X509Certificate cert, String thumbprint) {
        this.cert = cert;
        this.thumbprint = thumbprint;

        lIssuerFields = Lazy.of(() -> parseCommaSeparated(cert.getIssuerX500Principal().toString()));
        lSubjectFields = Lazy.of(() -> parseCommaSeparated(cert.getSubjectX500Principal().toString()));
    }

    public X509Certificate getCert() {
        return cert;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public Map<String, String> getIssuerFields() {
        return lIssuerFields.get();
    }

    public Map<String, String> getSubjectFields() {
        return lSubjectFields.get();
    }

    private static Map<String, String> parseCommaSeparated(String string) {
        return Arrays.stream(string.replaceAll("=\"([^\"]*)(, )([^\"]*)\"", "=$1_DEAD_$3").split(", "))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(o -> o[0], o -> o[1].replace("_DEAD_", ", ")));
    }
}
