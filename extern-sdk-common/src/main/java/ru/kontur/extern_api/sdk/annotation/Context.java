/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
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
package ru.kontur.extern_api.sdk.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author alexs
 */
public class Context {

    private static final String BEAN = "bean";
    private static final String SCOPE_SINGLETON = "singleton";
    private static final String SCOPE_PROTOTYPE = "prototype";

    private final Map<String, Bean> BEAN_META = new ConcurrentHashMap<>();
    private final Map<String, Object> BEANS = new ConcurrentHashMap<>();

    protected final void load(String contextPath) {
        try (InputStream is = Context.class.getResourceAsStream(contextPath)) {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            NodeList beanNodes = dom.getElementsByTagName(BEAN);
            for (int i = 0; i < beanNodes.getLength(); i++) {
                Bean bean = new Bean((Element) beanNodes.item(i));
                BEAN_META.put(bean.id, bean);
            }
        }
        catch (IOException | ParserConfigurationException | SAXException ignore) {
            System.out.println(MessageFormat.format("================ {0}: {1}", ignore.getMessage(), contextPath));
        }
    }

    public void bind(Object o) {

        Field[] fields = o.getClass().getDeclaredFields();

        for (Field f : fields) {
            Component s = f.getAnnotation(Component.class);

            if (s == null) {
                continue;
            }

            String componentName = s.value();

            Object component = BEANS.get(componentName);

            if (component == null) {

                Bean bean = BEAN_META.get(componentName);

                if (bean != null) {
                    try {
                        component = Class.forName(bean.className).newInstance();
                    }
                    catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignore) {
                        System.out.println(MessageFormat.format("================ {0}: {1}", ignore.getMessage(), componentName));
                    }

                    if (SCOPE_SINGLETON.equals(bean.scope)) {
                        BEANS.put(componentName, component);
                    }
                }
            }
            try {
                f.setAccessible(true);
                f.set(o, component);
                f.setAccessible(false);
            }
            catch (IllegalAccessException ignore) {
                System.out.println(MessageFormat.format("================ {0}: {1}", ignore.getMessage(), componentName));
            }
        }
    }

    private class Bean {

        private String id;
        private String className;
        private String scope;

        Bean(Element e) {
            id = e.getAttribute("id");
            className = e.getAttribute("class");
            scope = e.getAttribute("scope");
            if (scope == null || !SCOPE_PROTOTYPE.equals(scope)) {
                scope = SCOPE_SINGLETON;
            }
        }
    }
}
