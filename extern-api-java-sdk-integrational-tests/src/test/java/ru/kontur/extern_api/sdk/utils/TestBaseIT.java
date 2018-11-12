package ru.kontur.extern_api.sdk.utils;

import org.junit.jupiter.api.BeforeAll;
import ru.kontur.extern_api.sdk.ExternEngine;

public class TestBaseIT {

    protected static ExternEngine engine;

    @BeforeAll
    protected static void setUpClass() throws Exception {
        engine = TestSuite.Load().engine;
    }

}
