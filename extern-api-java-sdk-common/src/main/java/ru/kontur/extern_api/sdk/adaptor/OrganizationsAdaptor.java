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
package ru.kontur.extern_api.sdk.adaptor;

import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.OrgFilter;

/**
 * @author Aleksey Sukhorukov
 */
public interface OrganizationsAdaptor {

    /**
     * Lookups an organization by an identifier
     *
     * @param cxt QueryContext&lt;Company&gt; a context of query which mast contains an organization identifier
     *
     * @return QueryContext&lt;Company&gt; a context of query with an organization object
     */
    QueryContext<Company> lookup(QueryContext<?> cxt);

    /**
     * Creates an organization
     *
     * @param cxt QueryContext&lt;CompanyGeneral&gt; a context of query which mast contains a CompanyGeneral object
     *
     * @return QueryContext&lt;Company&gt; a context of query with an organization object
     */
    QueryContext<Company> create(QueryContext<?> cxt);

    /**
     * Updates a name of an organization
     *
     * @param cxt QueryContext&lt;Company&gt; a context of query which mast contains a name of organization
     *
     * @return QueryContext&lt;Company&gt; a context of query with an organization object
     */
    QueryContext<Company> update(QueryContext<?> cxt);

    /**
     * Deletes an organization by an identifier
     *
     * @param cxt QueryContext&lt;Company&gt; a context of query which mast contains an organization identifier
     *
     * @return QueryContext&lt;Void&gt; a context of query. Needs check isFail result.
     */
    QueryContext<Void> delete(QueryContext<?> cxt);

    /**
     * Searches organizations by critaries: inn, kpp
     *
     * @param cxt QueryContext&lt;CompanyBatch&gt; a context of query whick may contains inn, kpp, skip and take parameters
     *
     * @return QueryContext&lt;CompanyBatch&gt; a context with a result
     */
    QueryContext<CompanyBatch> search(QueryContext<?> cxt, OrgFilter filter);
}
