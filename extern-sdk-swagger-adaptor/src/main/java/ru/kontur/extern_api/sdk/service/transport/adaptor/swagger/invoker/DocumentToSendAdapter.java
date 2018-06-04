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

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.LinkDto;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.Link;


/**
 * @author AlexS
 */
public class DocumentToSendAdapter extends TypeAdapter<DocumentToSend> {

    private static final java.util.Base64.Encoder ENCODER = java.util.Base64.getEncoder();
    private static final java.util.Base64.Decoder DECODER = java.util.Base64.getDecoder();

    private final SignatureToSendAdapter signatureToSendAdapter = new SignatureToSendAdapter();

    @Override
    public void write(final JsonWriter out, final DocumentToSend d) throws IOException {
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
    public DocumentToSend read(final JsonReader in) throws IOException {
        in.beginObject();
        final DocumentToSend d = new DocumentToSend();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    d.setId(UUID.fromString(in.nextString()));
                    break;
                case "content":
                    d.setContent(DECODER.decode(in.nextString()));
                    break;
                case "print-form-content":
                    d.setPrintFormContent(DECODER.decode(in.nextString()));
                    break;
                case "filename":
                    d.setFilename(in.nextString());
                    break;
                case "signature":
                    d.setSignature(signatureToSendAdapter.read(in));
                    break;
                case "sender-ip":
                    d.setSenderIp(in.nextString());
                    break;
                case "links":
                    in.beginArray();
                    final List<Link> links = new ArrayList<>();
                    while (in.hasNext()) {
                        in.beginObject();
                        Map<String, Object> map = new HashMap<>();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "href":
                                    map.put("href", in.nextString());
                                    break;
                                case "rel":
                                    map.put("rel", in.nextString());
                                    break;
                                case "name":
                                    map.put("name", in.nextString());
                                    break;
                                case "title":
                                    map.put("title", in.nextString());
                                    break;
                                case "profile":
                                    map.put("profile", in.nextString());
                                    break;
                                case "templated":
                                    map.put("href", in.nextBoolean());
                                    break;
                            }
                        }
                        in.endObject();
                        LinkDto linkDto = new LinkDto();
                        links.add(linkDto.toDto(linkDto.fromDto(map)));
                    }
                    in.endArray();
                    d.setLinks(links);
                    break;
            }
        }
        in.endObject();

        return d;
    }

}
