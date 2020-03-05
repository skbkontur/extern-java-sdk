package ru.kontur.extern_api.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.utils.TestSuite;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Execution(ExecutionMode.SAME_THREAD)
public class ContentServiceTests {
    private static ExternEngine engine;
    private final UUID contentId = fromString("d2ccfe4e-40ec-4aa0-ba6d-5dba54018923");    //50mb
    private final Integer size = 51146377;
    //private final UUID contentId = fromString("3f112178-4d1f-4af9-a54e-3d2ab51c770b");  //114mb
    //private final UUID contentId = fromString("f10425d4-0cdc-42cf-9b0f-f65e128da6e3");  //900mb Exception in thread "OkHttp Dispatcher" java.lang.OutOfMemoryError: Java heap space

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
    }

    @Test
    void main() throws ExecutionException, InterruptedException {

        Integer requestedSize = engine.getContentService().getTotalSizeInBytes(contentId).get();
        assertEquals(size, requestedSize);

        byte[] content = engine.getContentService().downloadAllContent(contentId).get();
        assertNotNull(content);
        assertEquals(size, content.length);

        byte[] partialContent = engine.getContentService().downloadPartialContent(contentId, 0, 40).get();
        assertNotNull(partialContent);
        assertEquals(partialContent.length, 41);

        byte[] partialContentByLength = engine.getContentService().downloadPartialContentByLength(contentId, 41, 50).get();
        assertNotNull(partialContentByLength);
        assertEquals(partialContentByLength.length, 50);
    }
}
