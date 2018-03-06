/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.invoker;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;
import ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend;

/**
 *
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
		}
		else {
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
