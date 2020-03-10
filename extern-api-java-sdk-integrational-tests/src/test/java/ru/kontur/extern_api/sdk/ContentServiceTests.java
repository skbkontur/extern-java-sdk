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
    private final UUID contentId = fromString("0e9efa9b-9c82-4d8e-86f8-8c14ed66b8d8");    //50mb
    private final Integer size = 51146377;
    //private final UUID contentId = fromString("16bdce54-0c8f-42de-b669-2f7e4f502d26");  //114mb
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
