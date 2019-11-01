/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Aleksey Sukhorukov
 */

package ru.kontur.extern_api.sdk.model.pfr;

import java.util.List;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.LinksHolder;

/**
 * Ответ в документообороте ПФР.
 */
public class PfrReply implements LinksHolder {
    private String id;
    private PfrReplyDocument[] Documents;
    private String docflowId;
    private List<Link> links;

    public String getId() {
        return id;
    }

    public PfrReplyDocument[] getDocuments() {
        return Documents;
    }

    public String getDocflowId() {
        return docflowId;
    }

    public List<Link> getLinks() {
        return links;
    }

    private Link getLink(String name){
        return links
                .stream()
                .filter(l -> l.getRel().equals(name))
                .findAny()
                .orElse(null);
    }
}
