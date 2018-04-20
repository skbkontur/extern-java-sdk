package ru.skbkontur.sdk.extern.drafts.adaptor;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.drafts.DraftsValidator;
import ru.skbkontur.sdk.extern.model.Draft;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

public class DraftsLookupTest {

  private static final String LOCALHOST_DRAFTS = "http://localhost:8080/drafts";
  private static Server server;

  private QueryContext<Draft> queryContext;

  @BeforeClass
  public static void startJetty() throws Exception {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addServlet(TestServlet.class, "/drafts/*");
    server = new Server(8080);
    server.setHandler(context);
    server.start();
  }

  @Before
  public void prepareQueryContext() {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(LOCALHOST_DRAFTS);
    queryContext = new QueryContext<>();
    queryContext.setApiClient(apiClient);
    queryContext.setAccountProvider(UUID::randomUUID);
    queryContext.setDraftId(UUID.randomUUID());
  }

  @AfterClass
  public static void stopJetty() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDraftsLookup_Empty() {
    ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("{}");
    DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
    draftsAdaptor.lookup(queryContext);
    assertNotNull("List of links must not be null!", queryContext.get());
  }

  @Test
  public void testDraftsLookup_Draft() {
    ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("{\n" +
        "\"id\": \"" + StandardValues.ID + "\"," +
        "\"status\": \"new\"" +
        "}");
    DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
    draftsAdaptor.lookup(queryContext);
    DraftsValidator.validateDraft(queryContext.get());
  }

  @Test
  public void testDraftsLookup_BAD_REQUEST() {
    ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
    checkResponseCode(SC_BAD_REQUEST);
  }

  @Test
  public void testDraftsLookup_UNAUTHORIZED() {
    ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
    checkResponseCode(SC_UNAUTHORIZED);
  }

  @Test
  public void testDraftsLookup_FORBIDDEN() {
    ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
    checkResponseCode(SC_FORBIDDEN);
  }

  @Test
  public void testDraftsLookup_NOT_FOUND() {
    ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
    checkResponseCode(SC_NOT_FOUND);
  }

  @Test
  public void testDraftsLookup_INTERNAL_SERVER_ERROR() {
    ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
    checkResponseCode(SC_INTERNAL_SERVER_ERROR);
  }

  private void checkResponseCode(int code) {
    DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
    draftsAdaptor.lookup(queryContext);
    Draft draft = queryContext.get();
    assertNull("draft must be null!", draft);
    ServiceError serviceError = queryContext.getServiceError();
    assertNotNull("ServiceError must not be null!", serviceError);
    assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
  }
}