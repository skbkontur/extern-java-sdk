/*
 * MIT License
 *
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
 */

package ru.skbkontur.sdk.extern.service.transport.invoker;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend;

import java.io.IOException;
import java.util.UUID;


/**
 * @author AlexS
 */
public class DocumentToSendAdapter extends TypeAdapter<DocumentToSend> {

    private static final java.util.Base64.Encoder ENCODER = java.util.Base64.getEncoder();
    private static final java.util.Base64.Decoder DECODER = java.util.Base64.getDecoder();

    private final SignatureToSendAdapter signatureToSendAdapter = new SignatureToSendAdapter();

    @Override
    public void write(JsonWriter out, DocumentToSend d) throws IOException {
        if (d == null) {
            out.nullValue();
        } else {
            out.beginObject();
            out.name("id").value(d.getId().toString());
            out.name("content").value(ENCODER.encodeToString(d.getContent()));
            out.name("filename").value(d.getFilename());
            out.name("signature").jsonValue(signatureToSendAdapter.toJson(d.getSignature()));
            out.name("sender-ip").value(d.getSenderIp());
            out.endObject();
        }
    }

    @Override
    public DocumentToSend read(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.BEGIN_OBJECT) {
            return null;
        }

        DocumentToSend d = new DocumentToSend();

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    d.setId(UUID.fromString(in.nextString()));
                    break;
                case "content":
                    d.setContent(DECODER.decode(in.nextString()));
                    break;
                case "filename":
                    d.setFilename(in.nextString());
                    break;
                case "signature":
                    d.setSignature(signatureToSendAdapter.read(in));
                    break;
            }
        }

        return d;
    }

}
