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

package ru.kontur.extern_api.sdk.validator;

import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.LinksHolder;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.service.transport.adaptor.Query;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.text.MessageFormat;
import java.util.Collection;

public class LinkExists<R> implements Query<R> {
    private Query<R> query;
    private String linkName;
    private LinksHolder linksHolder;

    public LinkExists(String linkName, LinksHolder linksHolder, Query<R> query) {
        this.query = query;
        this.linkName = linkName;
        this.linksHolder = linksHolder;
    }

    @Override
    public QueryContext<R> apply(QueryContext<?> cxt) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        Link link = getLink(linksHolder.getLinks(), linkName);
        if (link == null) {
            return new QueryContext<R>(cxt, cxt.getEntityName()).setServiceError(MessageFormat.format("The reply does not contain a {0} reference.", linkName));
        }
        cxt.set(linkName + "-link", link);
        return query.apply(cxt);
    }

    private Link getLink(Collection<Link> links, String linkRel) {
        return links
                .stream()
                .filter(l -> l.getRel().equals(linkRel))
                .findAny()
                .orElse(null);
    }
}
