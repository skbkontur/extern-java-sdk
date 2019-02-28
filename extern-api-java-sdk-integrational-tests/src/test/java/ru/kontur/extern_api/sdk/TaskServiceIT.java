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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DraftTestPack;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;

@DisplayName("Task service should be able to")
class TaskServiceIT {

    private static ExternEngine engine;
    private static List<DraftTestPack> tests;
    private static CryptoUtils cryptoUtils;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        String certificate = cryptoUtils.loadX509(engine.getConfiguration().getThumbprint());
        TestData[] testData = TestUtils.getTestData(certificate);
        tests = Arrays
                .stream(testData)
                .map((TestData data) -> new DraftTestPack(data, engine, cryptoUtils))
                .collect(Collectors.toList());

        tests.forEach(DraftTestPack::createNewEmptyDraft);
    }

    @BeforeEach
    void setUp() {
        tests.forEach(DraftTestPack::createNewEmptyDraftIfNecessary);
    }


    private static Stream<Draft> newDraftWithDocumentFactory() {
        return tests.stream().map(DraftTestPack::newDraftWithDocument);
    }


    @ParameterizedTest
    @DisplayName("command \"StartSend\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartSend(Draft draft) {
        QueryContext<TaskInfo<Docflow>> startSend = engine.getTaskService()
                .startSendAsync(draft.getId())
                .join();

        assertNull(startSend.getServiceError());
        assertEquals(startSend.get().getTaskState(), TaskState.RUNNING);
    }


    @ParameterizedTest
    @DisplayName("command \"GetSendResult\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetSendResult(Draft draft) throws InterruptedException, ExecutionException {
        QueryContext<TaskInfo<Docflow>> startSend = engine.getTaskService()
                .startSendAsync(draft.getId())
                .join();

        Docflow docflow = engine.getTaskService().getSendResult(draft.getId(), startSend.get());

    }

    @ParameterizedTest
    @DisplayName("command \"StartPrepare\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartPrepare(Draft draft) {
        QueryContext<TaskInfo<PrepareResult>> startPrepare = engine.getTaskService()
                .startPrepareAsync(draft.getId())
                .join();

        assertNull(startPrepare.getServiceError());
        assertEquals(startPrepare.get().getTaskState(), TaskState.RUNNING);
    }

    @ParameterizedTest
    @DisplayName("command \"StartCheck\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartCheck(Draft draft) {
        QueryContext<TaskInfo<CheckResultData>> startCheck = engine.getTaskService()
                .startCheckAsync(draft.getId())
                .join();

        assertNull(startCheck.getServiceError());
        assertEquals(startCheck.get().getTaskState(), TaskState.RUNNING);
    }
}
