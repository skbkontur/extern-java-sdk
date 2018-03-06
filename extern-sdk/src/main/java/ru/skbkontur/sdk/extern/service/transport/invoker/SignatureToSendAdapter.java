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
import ru.skbkontur.sdk.extern.service.transport.swagger.model.SignatureToSend;

/**
 *
 * @author AlexS
 */
public class SignatureToSendAdapter extends TypeAdapter<SignatureToSend> {

	private static final java.util.Base64.Encoder ENCODER = java.util.Base64.getEncoder();
	private static final java.util.Base64.Decoder DECODER = java.util.Base64.getDecoder();

	@Override
	public void write(JsonWriter out, SignatureToSend s) throws IOException {
		if (s == null) {
			out.nullValue();
		}
		else {
			out.beginObject();
			if (s.getId() != null) {
				out.name("id").value(s.getId().toString());
			}
			if (s.getContentData() != null) {
				out.name("content-data").value(ENCODER.encodeToString(s.getContentData()));
			}
			out.endObject();
		}
	}

	@Override
	public SignatureToSend read(JsonReader in) throws IOException {
		if (in.peek() != JsonToken.BEGIN_OBJECT) {
			return null;
		}

		SignatureToSend s = new SignatureToSend();

		while (in.hasNext()) {
			String name = in.nextName();
			switch (name) {
				case "id":
					s.setId(UUID.fromString(in.nextString()));
					break;
				case "content-data":
					s.setContentData(DECODER.decode(in.nextString()));
					break;
			}
		}

		return s;
	}

}
