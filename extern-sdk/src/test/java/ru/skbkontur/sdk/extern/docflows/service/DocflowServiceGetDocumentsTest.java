package ru.skbkontur.sdk.extern.docflows.service;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjects;
import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.docflows.DocflowsValidator;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

public class DocflowServiceGetDocumentsTest {

  private static ExternEngine engine;
  private static Server server;

  private final static String DOCUMENT_DESCRIPTION = "{\"type\": \"urn:nss:nid\"," +
      "\"filename\": \"string\"," +
      "\"content-type\": \"string\"}";

  @BeforeClass
  public static void startJetty() throws Exception {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.addServlet(TestServlet.class, "/docflows/*");
    server = new Server(8080);
    server.setHandler(context);
    server.start();
  }

  @AfterClass
  public static void stopJetty() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @BeforeClass
  public static void setUpClass() {
    engine = new ExternEngine();
    engine.setServiceBaseUriProvider(() -> "http://localhost:8080/docflows");
    engine.setAccountProvider(UUID::randomUUID);
    engine.setApiKeyProvider(() -> UUID.randomUUID().toString());
    engine.setAuthenticationProvider(
        new AuthenticationProvider() {
          @Override
          public QueryContext<String> sessionId() {
            return new QueryContext<String>().setResult("1", QueryContext.SESSION_ID);
          }

          @Override
          public String authPrefix() {
            return "auth.sid ";
          }

          @Override
          public void addAuthenticationListener(AuthenticationListener authListener) {
          }

          @Override
          public void removeAuthenticationListener(AuthenticationListener authListener) {
          }

          @Override
          public void raiseUnauthenticated(ServiceError x) {
          }
        });

    engine.configureServices();
  }

  @Test
  public void testGetDocuments_Empty() {
    ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("[]");
    assertNotNull("Documents must not be null!", getDocuments());
    assertNotNull("Documents must not be null!", getDocumentsAsync());
  }

  @Test
  public void testGetDocuments_Document() {
    ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
    ResponseData.INSTANCE
        .setResponseMessage(String.format("[{\"id\": \"%s\"}]", StandardValues.ID));
    List<Document> documents = getDocuments();
    StandardObjectsValidator.validateNotEmptyList(documents, "Documents");
    DocflowsValidator.validateDocument(documents.get(0), false, false, false, false);
    // validate async
    documents = getDocumentsAsync();
    StandardObjectsValidator.validateNotEmptyList(documents, "Documents");
    DocflowsValidator.validateDocument(documents.get(0), false, false, false, false);
  }

  @Test
  public void testGetDocuments_Document_Description() {
    ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("[{" +
        "\"id\": \"" + StandardValues.ID + "\"," +
        "\"description\": " + DOCUMENT_DESCRIPTION +
        "}]");
    DocflowsValidator.validateDocument(getDocuments().get(0), true, false, false, false);
    DocflowsValidator.validateDocument(getDocumentsAsync().get(0), true, false, false, false);
  }

  @Test
  public void testGetDocuments_Document_WithContent() {
    ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("[{" +
        "\"id\": \"" + StandardValues.ID + "\"," +
        "\"description\": " + DOCUMENT_DESCRIPTION + "," +
        "\"content\": {\n" +
        "  \"decrypted\": " + StandardObjects.LINK + "," +
        "  \"encrypted\": " + StandardObjects.LINK +
        "}" +
        "}]");
    DocflowsValidator.validateDocument(getDocuments().get(0), true, true, false, false);
    DocflowsValidator.validateDocument(getDocumentsAsync().get(0), true, true, false, false);
  }

  @Test
  public void testGetDocuments_Document_Signature() {
    ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("[{" +
        "\"id\": \"" + StandardValues.ID + "\"," +
        "\"description\": " + DOCUMENT_DESCRIPTION + "," +
        "\"content\": {\n" +
        "  \"decrypted\": " + StandardObjects.LINK + "," +
        "  \"encrypted\": " + StandardObjects.LINK +
        "}," +
        "\"signatures\": [{\"id\": \"" + StandardValues.ID + "\"}]" +
        "}]");
    DocflowsValidator.validateDocument(getDocuments().get(0), true, true, true, false);
    DocflowsValidator.validateDocument(getDocumentsAsync().get(0), true, true, true, false);
  }

  @Test
  public void testGetDocuments_Document_Links() {
    ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
    ResponseData.INSTANCE.setResponseMessage("[{" +
        "\"id\": \"" + StandardValues.ID + "\"," +
        "\"description\": " + DOCUMENT_DESCRIPTION + "," +
        "\"content\": {\n" +
        "  \"decrypted\": " + StandardObjects.LINK + "," +
        "  \"encrypted\": " + StandardObjects.LINK +
        "}," +
        "\"signatures\": [{\"id\": \"" + StandardValues.ID + "\"}]," +
        "\"links\": [" + StandardObjects.LINK + "]" +
        "}]");
    DocflowsValidator.validateDocument(getDocuments().get(0), true, true, true, true);
    DocflowsValidator.validateDocument(getDocumentsAsync().get(0), true, true, true, true);
  }

  @Test
  public void testGetDocuments_BAD_REQUEST() {
    ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
    checkResponseCode(SC_BAD_REQUEST);
  }

  @Test
  public void testGetDocuments_UNAUTHORIZED() {
    ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
    checkResponseCode(SC_UNAUTHORIZED);
  }

  @Test
  public void testGetDocuments_FORBIDDEN() {
    ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
    checkResponseCode(SC_FORBIDDEN);
  }

  @Test
  public void testGetDocuments_NOT_FOUND() {
    ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
    checkResponseCode(SC_NOT_FOUND);
  }

  @Test
  public void testGetDocuments_INTERNAL_SERVER_ERROR() {
    ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
    checkResponseCode(SC_INTERNAL_SERVER_ERROR);
  }

  private void checkResponseCode(int code) {
    QueryContext<Object> queryContext = new QueryContext<>();
    queryContext.setDocflowId(StandardValues.ID);
    QueryContext<Docflow> docflowQueryContext = engine.getDocflowService()
        .lookupDocflow(queryContext);
    assertNull("documents must be null!", docflowQueryContext.get());
    assertNotNull("ServiceError must not be null!", docflowQueryContext.getServiceError());
    assertEquals("Response code is wrong!", code,
        docflowQueryContext.getServiceError().getResponseCode());
  }

  private List<Document> getDocuments() {
    QueryContext<Docflow> queryContext = new QueryContext<>();
    queryContext.setDocflowId(StandardValues.ID);
    return engine.getDocflowService().getDocuments(queryContext).get();
  }

  private List<Document> getDocumentsAsync() {
    try {
      return engine.getDocflowService().getDocumentsAsync(StandardValues.ID).get().get();
    } catch (InterruptedException | ExecutionException e) {
      fail();
      return Collections.emptyList();
    }
  }
}
