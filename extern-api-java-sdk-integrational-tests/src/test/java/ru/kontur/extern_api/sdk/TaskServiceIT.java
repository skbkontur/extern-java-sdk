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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
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
        DocflowTaskInfo startSend = engine.getTaskService(draft.getId())
                .startSendAsync()
                .join();

        assertEquals(startSend.getTaskState(), TaskState.RUNNING);
        assertEquals(startSend.getTaskType(), TaskType.SEND);
    }


    @ParameterizedTest
    @DisplayName("command \"GetSendResult\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetSendResult(Draft draft) {
        DocflowTaskInfo startSend = engine.getTaskService(draft.getId())
                .startSendAsync()
                .join();

        Docflow docflow = engine.getTaskService(draft.getId()).getSendResult(startSend).join();
        assertEquals(docflow.getStatus(), DocflowStatus.SENT);
    }

    @ParameterizedTest
    @DisplayName("command \"StartPrepare\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartPrepare(Draft draft) {
        PrepareResultTaskInfo startPrepare = engine.getTaskService(draft.getId())
                .startPrepareAsync()
                .join();

        assertEquals(startPrepare.getTaskState(), TaskState.RUNNING);
        assertEquals(startPrepare.getTaskType(), TaskType.PREPARE);
    }

    @ParameterizedTest
    @DisplayName("command \"GetPrepareResult\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetPrepareResult(Draft draft) {

        PrepareResultTaskInfo startPrepare = engine.getTaskService(draft.getId())
                .startPrepareAsync()
                .join();

        PrepareResult prepareResult = engine.getTaskService(draft.getId()).getPrepareResult(startPrepare)
                .join();

        Status status = prepareResult.getStatus();
        assertTrue(status == Status.OK || status == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);
    }

    @ParameterizedTest
    @DisplayName("command \"StartCheck\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartCheck(Draft draft) {
        CheckResultDataTaskInfo startCheck = engine.getTaskService(draft.getId())
                .startCheckAsync()
                .join();

        assertEquals(startCheck.getTaskState(), TaskState.RUNNING);
        assertEquals(startCheck.getTaskType(), TaskType.CHECK);
    }

    @ParameterizedTest
    @DisplayName("command \"GetCheckResult\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetCheckResult(Draft draft) {
        CheckResultDataTaskInfo startCheck = engine.getTaskService(draft.getId())
                .startCheckAsync()
                .join();

        CheckResultData checkResult = engine.getTaskService(draft.getId()).getCheckResult(startCheck)
                .join();

        assertTrue(checkResult.hasNoErrors());
    }
}
