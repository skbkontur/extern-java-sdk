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
package ru.kontur.extern_api.sdk.httpclient.invoker;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author alexs
 */
public final class MediaType {

    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final Pattern TYPE_SUBTYPE = Pattern.compile(TOKEN + "/" + TOKEN);
    private static final Pattern PARAMETER = Pattern.compile(";\\s*(?:" + TOKEN + "=(?:" + TOKEN + "|" + QUOTED + "))?");

    private final String mediaType;
    private final String type;
    private final String subtype;
    private final String charset;

    private MediaType(String mediaType, String type, String subtype, String charset) {
        this.mediaType = mediaType;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }

    /**
     * Returns a media type for {@code string}, or null if {@code string} is not a well-formed media type.
     *
     * @param string String a string for parsing
     * 
     * @return MediaType this object
     */
    public static MediaType parse(String string) {
        Matcher typeSubtype = TYPE_SUBTYPE.matcher(string);
        if (!typeSubtype.lookingAt()) {
            return null;
        }
        String type = typeSubtype.group(1).toLowerCase(Locale.US);
        String subtype = typeSubtype.group(2).toLowerCase(Locale.US);

        String charset = null;
        Matcher parameter = PARAMETER.matcher(string);
        for (int s = typeSubtype.end(); s < string.length(); s = parameter.end()) {
            parameter.region(s, string.length());
            if (!parameter.lookingAt()) {
                return null; // This is not a well-formed media type.
            }
            String name = parameter.group(1);
            if (name == null || !name.equalsIgnoreCase("charset")) {
                continue;
            }
            String charsetParameter = parameter.group(2) != null
                ? parameter.group(2) // Value is a token.
                : parameter.group(3); // Value is a quoted string.
            if (charset != null && !charsetParameter.equalsIgnoreCase(charset)) {
                throw new IllegalArgumentException("Multiple different charsets: " + string);
            }
            charset = charsetParameter;
        }

        return new MediaType(string, type, subtype, charset);
    }

    /**
     * Returns the high-level media type, such as "text", "image", "audio", "video", or "application".
     * @return String a media type
     */
    public String type() {
        return type;
    }

    /**
     * Returns a specific media subtype, such as "plain" or "png", "mpeg", "mp4" or "xml".
     * @return String a subtype of media type
     */
    public String subtype() {
        return subtype;
    }

    /**
     * Returns the charset of this media type, or null if this media type doesn't specify a charset.
     * @return Charset a charset of media data
     */
    public Charset charset() {
        return charset != null ? Charset.forName(charset) : null;
    }

    /**
     * Returns the charset of this media type, or {@code defaultValue} if this media type doesn't specify a charset.
     * @param defaultValue Charset a default charset of the media data
     * @return the charset of this media type, or {@code defaultValue} if this media type doesn't specify a charset.
     */
    public Charset charset(Charset defaultValue) {
        return charset != null ? Charset.forName(charset) : defaultValue;
    }

    /**
     * Returns the encoded media type, like "text/plain; charset=utf-8", appropriate for use in a Content-Type header.
     * @return String
     */
    @Override
    public String toString() {
        return mediaType;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MediaType && ((MediaType) o).mediaType.equals(mediaType);
    }

    @Override
    public int hashCode() {
        return mediaType.hashCode();
    }
}
