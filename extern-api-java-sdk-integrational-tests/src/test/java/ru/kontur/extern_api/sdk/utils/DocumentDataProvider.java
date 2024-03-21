package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.TestData;

class DocumentDataProvider {

    private final String ifnsCode;

    DocumentDataProvider(String ifnsCode) {
        this.ifnsCode = ifnsCode;
    }

    String getSubmissionData(TestData testData, UUID baseFileId, UUID childFileId) {
        return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
                + "<Файл ИдФайл=\"ON_DOCNPNO_" + ifnsCode + "_" + ifnsCode + "_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_20231201_"
                + UUID.randomUUID()
                + "\" ВерсПрог =\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 16.0\" ВерсФорм=\"5.02\">"
                + "<Документ КНД=\"1184002\" ДатаДок=\"01.12.2023\">"
                + "<СвОтпрДок>"
                + "<ОтпрЮЛ НаимОрг=\"ООО «Первая Ижевская тестовая»\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\" />"
                + "</СвОтпрДок>"
                + "<СвПолДок>"
                + "<ОтпрНО КодНО=\"" + ifnsCode + "\" НаимНО=\"Тестовый КО (положительный ответ)\" />"
                + "</СвПолДок>"
                + "<СвНП>"
                + "<НПЮЛ НаимОрг=\"ООО «Первая Ижевская тестовая»\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\" />"
                + "</СвНП>"
                + "<Подписант ПрПодп=\"3\" Должн =\"Руководитель\" Тлф=\"8800\" E-mail=\"ruk@skbkontur.ru\" ИННФЛ=\"227632864503\">"
                + "<ФИО Фамилия=\""
                + testData.getClientInfo().getSender().getFio().getSurname()
                + "\" Имя=\""
                + testData.getClientInfo().getSender().getFio().getName()
                + "\" Отчество=\""
                + testData.getClientInfo().getSender().getFio().getPatronymic()
                + "\" />"
                + "</Подписант>"
                + "<ДокПредстНО КолФайл=\"1\">"
                + "<ИдФайлОсн>1165013_" + ifnsCode + "_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_"
                + UUID.randomUUID()
                + "_20170807_"
                + UUID.randomUUID()
                + "</ИдФайлОсн>"
                + "<ДокСкан ПорНомДок=\"1.01\" КодДок=\"0510041\" НаимДок=\"Фото_1\" >"
                + "<ИмяФайл>"
                + getAttachmentFileName(testData, baseFileId, childFileId)
                + "</ИмяФайл>"
                + "</ДокСкан>"
                + "</ДокПредстНО>"
                + "</Документ>"
                + "</Файл>";
    }


    String getLetterData(TestData testData) {
        return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
                + "<Файл ИдФайл=\"IU_OBRNP_"
                + ifnsCode
                + "_"
                + ifnsCode
                + "_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_20180301_"
                + UUID.randomUUID()
                + "\" ВерсПрог=\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 13.0\" ВерсФорм=\"5.03\">"
                + "<Документ КНД=\"1166102\" ДатаДок=\"01.03.2018\">"
                + "<СвНП>"
                + "<НПЮЛ НаимОрг=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getOrgName()
                + "\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\"/>"
                + "<АдрРФ КодРегион=\"66\"/>"
                + "</СвНП>"
                + "<СвОтпр>"
                + "<СвОтпрЮЛ НаимОрг=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getOrgName()
                + "\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\"/>"
                + "</СвОтпр>"
                + "<Подписант ПрПодп=\"1\">"
                + "<ФИО Фамилия=\""
                + testData.getClientInfo().getSender().getFio().getSurname()
                + "\" Имя=\""
                + testData.getClientInfo().getSender().getFio().getName()
                + "\" Отчество=\""
                + testData.getClientInfo().getSender().getFio().getPatronymic()
                + "\" />"
                + "</Подписант>"
                + "<ОбращИнф ИФНС=\"" + ifnsCode + "\">"
                + "<ОбращТекст><![CDATA[с]]></ОбращТекст>"
                + "<Прил КолФайл=\"0\"/>"
                + "</ОбращИнф>"
                + "</Документ>"
                + "</Файл>";
    }

    String getAttachmentFileName(TestData testData, UUID baseFileId, UUID childFileId) {
        return "0510041_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_"
                + ifnsCode
                + "_"
                + baseFileId
                + "_"
                + childFileId
                + ".jpg";
    }
}
