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
public class ContentServiceIT {
    private static ExternEngine engine;
    private final UUID contentId = fromString("cbb658e0-eca2-4258-bf7c-f5ba0eeddbf1");    //50mb
    private final Integer size = 51146377;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;

    }

    @Test
    void main() throws ExecutionException, InterruptedException {

        Integer requestedSize = engine.getContentService().getTotalSizeInBytes(contentId).get();
        assertEquals(size, requestedSize);

        byte[] partialContent = engine.getContentService().getPartialContent(contentId, 0, 40).get();
        assertNotNull(partialContent);
        assertEquals(partialContent.length, 41);

        byte[] partialContentByLength = engine.getContentService().getPartialContentByLength(contentId, 41, 50).get();
        assertNotNull(partialContentByLength);
        assertEquals(partialContentByLength.length, 50);
    }
}
