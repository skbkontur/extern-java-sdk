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
package ru.kontur.extern_api.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.CheckTaskInfo;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.PrepareTaskInfo;
import ru.kontur.extern_api.sdk.model.SendTaskInfo;
import ru.kontur.extern_api.sdk.model.TaskState;
import ru.kontur.extern_api.sdk.model.TaskType;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DraftTestPack;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;


@Execution(ExecutionMode.SAME_THREAD)
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
    @DisplayName("StartSend")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartSend(Draft draft) {
        SendTaskInfo startSend = engine.getTaskService(draft.getId())
                .startSendAsync()
                .join();

        assertEquals(startSend.getTaskState(), TaskState.RUNNING);
        assertEquals(startSend.getTaskType(), TaskType.SEND);
    }


    @ParameterizedTest
    @DisplayName("GetSendResult")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetSendResult(Draft draft) {
        SendTaskInfo startSend = engine.getTaskService(draft.getId())
                .startSendAsync()
                .join();

        Docflow docflow = engine.getTaskService(draft.getId()).getSendResult(startSend).join();
        assertEquals(docflow.getStatus(), DocflowStatus.SENT);
    }

    @ParameterizedTest
    @DisplayName("GetSendResult")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetTaskState(Draft draft) {
        SendTaskInfo startSend = engine.getTaskService(draft.getId())
                .startSendAsync()
                .join();

        TaskState taskState = engine.getTaskService(draft.getId()).getTaskStatus(startSend).join();
        assertEquals(taskState, TaskState.RUNNING);
    }

    @ParameterizedTest
    @DisplayName("StartPrepare")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartPrepare(Draft draft) {
        PrepareTaskInfo startPrepare = engine.getTaskService(draft.getId())
                .startPrepareAsync()
                .join();

        assertEquals(startPrepare.getTaskState(), TaskState.RUNNING);
        assertEquals(startPrepare.getTaskType(), TaskType.PREPARE);
    }

    @ParameterizedTest
    @DisplayName("GetPrepareResult")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetPrepareResult(Draft draft) {
        PrepareTaskInfo startPrepare = engine.getTaskService(draft.getId())
                .startPrepareAsync()
                .join();

        PrepareResult prepareResult = engine.getTaskService(draft.getId()).getPrepareResult(startPrepare)
                .join();

        Status status = prepareResult.getStatus();
        assertTrue(status == Status.OK || status == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);
    }

    @ParameterizedTest
    @DisplayName("StartCheck")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testStartCheck(Draft draft) {
        CheckTaskInfo startCheck = engine.getTaskService(draft.getId())
                .startCheckAsync()
                .join();

        assertEquals(startCheck.getTaskState(), TaskState.RUNNING);
        assertEquals(startCheck.getTaskType(), TaskType.CHECK);
    }

    @ParameterizedTest
    @DisplayName("GetCheckResult")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testGetCheckResult(Draft draft) {
        CheckTaskInfo startCheck = engine.getTaskService(draft.getId())
                .startCheckAsync()
                .join();

        CheckResultData checkResult = engine.getTaskService(draft.getId()).getCheckResult(startCheck)
                .join();

        assertTrue(checkResult.hasNoErrors());
    }
}
