/*
 * The MIT License
 *
 * Copyright 2018 alexs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.ServicesFactory;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;

/**
 *
 * @author alexs
 */
public class DefaultServicesFactory implements ServicesFactory {

    private static final String BEAN = "bean";
    private static final String SCOPE_SINGLETON = "singleton";
    private static final String SCOPE_PROTOTYPE = "prototype";

    private enum Service {
        ACCOUNT("accountsAdaptor"),
        CERTIFICATE("certificatesAdaptor"),
        DOCFLOW("docflowsAdaptor"),
        DRAFT("draftsAdaptor"),
        EVENT("eventsAdaptor"),
        HTTPCLIENT("httpClient");

        private final String adaptorId;

        Service(String adaptorId) {
            this.adaptorId = adaptorId;
        }

        String getAdaptorId() {
            return adaptorId;
        }

    };

    private static final String ADAPTOR_CONTEXT = "/AdaptorContext.xml";

//	private final String adaptorContextPath;
    private static final Map<String, AdaptorMeta> ADAPTOR_META = new ConcurrentHashMap<>();

    private static final Map<String, Object> ADAPTORS = new ConcurrentHashMap<>();
    
    private static volatile DefaultServicesFactory self = null;

    private AccountService accountService;

    private CertificateService certificateService;

    private DocflowService docflowService;

    private DraftService draftService;

    private EventService eventService;

    private HttpClient httpClient;
    
    public DefaultServicesFactory(String adaptorContextPath) {
        load(adaptorContextPath);
    }

    public DefaultServicesFactory() {
        this(ADAPTOR_CONTEXT);
    }

    @Override
    public AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountServiceImpl(getAdaptor(Service.ACCOUNT));
        }
        return accountService;
    }

    @Override
    public CertificateService getCertificateService() {
        if (certificateService == null) {
            certificateService = new CertificateServiceImpl(getAdaptor(Service.CERTIFICATE));
        }
        return certificateService;
    }

    @Override
    public DocflowService getDocflowService() {
        if (docflowService == null) {
            docflowService = new DocflowServiceImpl(getAdaptor(Service.DOCFLOW));
        }
        return docflowService;
    }

    @Override
    public DraftService getDraftService() {
        if (draftService == null) {
            draftService = new DraftServiceImpl(getAdaptor(Service.DRAFT));
        }
        return draftService;
    }

    @Override
    public EventService getEventService() {
        if (eventService == null) {
            eventService = new EventServiceImpl(getAdaptor(Service.EVENT));
        }
        return eventService;
    }

    @Override
    public HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = getAdaptor(Service.HTTPCLIENT);
        }
        return httpClient;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAdaptor(Service service) {

        T adaptor = (T) ADAPTORS.get(service.getAdaptorId());

        if (adaptor == null) {
            AdaptorMeta m = ADAPTOR_META.get(service.getAdaptorId());
            if (m != null) {
                try {
                    adaptor = (T) Class.forName(m.className).newInstance();
                    if (SCOPE_SINGLETON.equals(m.scope)) {
                        ADAPTORS.put(service.getAdaptorId(), adaptor);
                    }
                }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignore) {
                    System.out.println(MessageFormat.format("================ {0}: {1}", ignore.getMessage(), service.getAdaptorId()));
                }
            }
        }

        return adaptor;
    }

    protected final void load(String adaptorContextPath) {
        try (InputStream is = this.getClass().getResourceAsStream(adaptorContextPath)) {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            NodeList beanNodes = dom.getElementsByTagName(BEAN);
            for (int i = 0; i < beanNodes.getLength(); i++) {
                AdaptorMeta adaptor = new AdaptorMeta((Element) beanNodes.item(i));
                ADAPTOR_META.put(adaptor.id, adaptor);
            }
        }
        catch (IOException | ParserConfigurationException | SAXException ignore) {
            System.out.println(MessageFormat.format("================ {0}: {1}", ignore.getMessage(), adaptorContextPath));
        }
    }

    private class AdaptorMeta {

        private String id;
        private String className;
        private String scope;

        AdaptorMeta(Element e) {
            id = e.getAttribute("id");
            className = e.getAttribute("class");
            scope = e.getAttribute("scope");
            if (scope == null || !SCOPE_PROTOTYPE.equals(scope)) {
                scope = SCOPE_SINGLETON;
            }
        }
    }
}
