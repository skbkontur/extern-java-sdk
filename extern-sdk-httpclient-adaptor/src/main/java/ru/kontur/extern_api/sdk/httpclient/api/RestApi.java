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
package ru.kontur.extern_api.sdk.httpclient.api;

import com.google.gson.Gson;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;

/**
 *
 * @author alexs
 */
public class RestApi {

    private static final String CONTENT_TYPE = "Content-Type";

    private static final Map<Class<?>, Map<String, RestQuery>> REST_QUERIES = new ConcurrentHashMap<>();

    private static final Gson JSON = configuredGson();

    protected HttpClient httpClient;

    protected RestApi() {
        buildRestApi(getClass());
    }

    private void buildRestApi(@NotNull Class<?> clazz) {
        Map<String, RestQuery> restApi = REST_QUERIES.get(clazz);
        synchronized (clazz) {
            if (restApi == null) {
                restApi = new HashMap<>();
                REST_QUERIES.put(clazz, restApi);
                Method[] methods = this.getClass().getDeclaredMethods();
                for (Method m : methods) {
                    Path pa = m.getAnnotation(Path.class);
                    
                    if (pa == null) {
                        continue;
                    }
                    
                    RestQuery restQuery = new RestQuery();
                    restApi.put(m.getName(), restQuery);
                    
                    restQuery.setPath(pa.value());
                    
                    if (isAnnotation(m, GET.class)) {
                        restQuery.setHttpMethod("GET");
                    }
                    else if (isAnnotation(m, POST.class)) {
                        restQuery.setHttpMethod("POST");
                    }
                    else if (isAnnotation(m, PUT.class)) {
                        restQuery.setHttpMethod("PUT");
                    }
                    else if (isAnnotation(m, DELETE.class)) {
                        restQuery.setHttpMethod("DELETE");
                    }
                    
                    extractContentType(m, restQuery);
                    
                    Parameter[] methodParams = m.getParameters();
                    
                    List<Param> params = new ArrayList<>();
                    
                    restQuery.setParams(params);
                    
                    for (Parameter p : methodParams) {
                        PathParam ppa = p.getAnnotation(PathParam.class);
                        if (ppa != null) {
                            params.add(new Param(Param.Type.PATH_PARAM, ppa.value()));
                        }
                        QueryParam qpa = p.getAnnotation(QueryParam.class);
                        if (qpa != null) {
                            params.add(new Param(Param.Type.QUERY_PARAM, qpa.value()));
                        }
                        HeaderParam hpa = p.getAnnotation(HeaderParam.class);
                        if (hpa != null) {
                            params.add(new Param(Param.Type.HEADER_PARAM, hpa.value()));
                        }
                    }
                }
            }
        }
    }

    protected String toJson(Object obj) {
        return JSON.toJson(obj);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public HttpClient setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.httpClient.setGson(JSON);
        return this.httpClient;
    }

    protected <T> ApiResponse<T> invoke(String methodName, Object body, Type type, Object... args) throws ApiException {
        Map<String, RestQuery> restApi = REST_QUERIES.get(this.getClass());
        if (restApi != null) {
            RestQuery restQuery = restApi.get(methodName);

            String path = buildRequestPath(restQuery, args);

            Map<String, Object> queryParams = buildParams(restQuery, Param.Type.QUERY_PARAM, args);

            Map<String, String> headerParams = buildParams(restQuery, Param.Type.HEADER_PARAM, args);

            if (restQuery.getContentType() != null) {
                headerParams.put(CONTENT_TYPE, restQuery.getContentType());
            }

            return httpClient.submitHttpRequest(
                path,
                restQuery.getHttpMethod(),
                queryParams,
                body,
                headerParams,
                null,
                type
            );
        }
        throw new ApiException("The method " + methodName + " not found.");
    }

    private String buildRequestPath(RestQuery restQuery, Object args[]) {
        String path = restQuery.getPath();
        if (args != null && args.length > 0) {
            List<Param> params = restQuery.getParams();
            for (int i = 0; i < args.length; i++) {
                if (i < params.size()) {
                    Param p = params.get(i);
                    if (p.getType() == Param.Type.PATH_PARAM && args[i] != null) {
                        path = path.replaceFirst("\\{\\b*" + params.get(i).getName() + "\\b*\\}", args[i].toString());
                    }
                }
            }
        }
        return path;
    }

    @SuppressWarnings("unchecked")
    private <T> Map<String, T> buildParams(RestQuery restQuery, Param.Type type, Object args[]) {
        Map<String, T> result = new HashMap<>();
        if (args != null && args.length > 0) {
            List<Param> params = restQuery.getParams();
            for (int i = 0; i < args.length; i++) {
                if (i < params.size()) {
                    Param p = params.get(i);
                    if (p.getType() == type && args[i] != null) {
                        result.put(p.getName(), (T) args[i]);
                    }
                }
            }
        }
        return result;
    }

    private boolean isAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.getAnnotation(annotationClass) != null;
    }

    private void extractContentType(Method m, RestQuery restQuery) {
        Consumes consumes = m.getAnnotation(Consumes.class);
        if (consumes != null) {
            restQuery.setContentType(consumes.value()[0]);
        }
    }

    private static Gson configuredGson() {
        return GsonProvider.getGson();
    }
}
