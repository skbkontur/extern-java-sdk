/*
 * Copyright (c) 2019 SKB Kontur
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
 */

package ru.kontur.extern_api.sdk.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.argosgrp.cryptoservice.utils.XMLUtil;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class DemandDataProvider {


    private final String fufTemplateName;
    private final String inn;
    private final String kpp;
    private final String cuCode;
    private Date fileDate = new Date();
    private String applicationFilenameId = UUID.randomUUID().toString();
    private String fufId = UUID.randomUUID().toString();

    private static final String DATE_PATH = "/Файл/Докумет/@ДатаДок";
    private static final String DEMAND_FILE_NAME = "/Файл/Докумет/ДокНапрИзНО/ИнфСообДок/ИмяФайл";
    private static final String DEMAND_PACKAGE_NAME = "/Файл/@ИдФайл";
    private static final String DATE_FOR_FILENAME_FORMAT = "yyyyMMdd";
    private static final String DATE_FOR_XML_FORMAT = "dd.MM.yyyy";
    private static final String FILENAME_SEPARATOR = "_";
    private static final String FILENAME_PREFIX = "ON_DOCNPNO";
    private static final String APPLICATION_FILENAME_PREFIX = "1165013";
    private static final String APPLICATION_FILENAME_EXTENSION = ".pdf";
    private static final String DESCRIPTION_FILENAME = "TR_INFSOOB.xml";

    public DemandDataProvider() {
        fufTemplateName = "ON_DOCNPNO_6653000832665325934_6653000832665325934_0087.xml";
        inn = "66530008326";
        kpp = "665325934";
        cuCode = "0087";
    }

    public DemandDataProvider(String fufTemplateName, String inn, String kpp, String cuCode) {
        this.fufTemplateName = fufTemplateName;
        this.inn = inn;
        this.kpp = kpp;
        this.cuCode = cuCode;
    }

    public DemandDataProvider setFufDate(Date fufDate) {
        fileDate = fufDate;
        return this;
    }

    public DemandDataProvider setApplicationFilenameId(UUID applicationFilenameId) {
        this.applicationFilenameId = applicationFilenameId.toString();
        return this;
    }

    public DemandDataProvider setFufGuid(UUID fileId) {
        this.fufId = fileId.toString();
        return this;
    }

    public DcDemandData getFufData() {
        return new DcDemandData(
                getDocument(),
                getDescription(),
                new DcDocument[]{getApplication()}
        );
    }

    public static DcDemandData getDefaultFufData() {
        return (new DemandDataProvider())
                .getFufData();
    }

    @NotNull
    @Contract(" -> new")
    private DcDocument getApplication() {
        byte[] applicationContent = null;
        return new DcDocument(
                getIncludedFilename(getFormattedDate(DATE_FOR_FILENAME_FORMAT, fileDate), this.fufId, this.applicationFilenameId),
                DcDocType.APPLICATION,
                DcContentType.PDF,
                applicationContent
        );
    }

    @NotNull
    @Contract(" -> new")
    private DcDocument getDescription() {
        try {
            String descriptionContent = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n" +
                    "<ОписДок>\n" +
                    "  <КНДДок>1184002</КНДДок>\n" +
                    "  <КодНО>" + cuCode + "</КодНО>\n" +
                    "  <КодНО2>" + cuCode + "</КодНО2>\n" +
                    "  <НП>" + inn + kpp + "</НП>\n" +
                    "  <КолФайлПрилож>1</КолФайлПрилож>\n" +
                    "  <ИмяФайлПрилож>" + getIncludedFilename(getFormattedDate(DATE_FOR_FILENAME_FORMAT, fileDate), this.fufId, this.applicationFilenameId) + "</ИмяФайлПрилож>\n" +
                    "</ОписДок>";

            return new DcDocument(
                    DESCRIPTION_FILENAME,
                    DcDocType.DESCRIPTION,
                    DcContentType.TEXT_XML,
                    descriptionContent.getBytes("windows-1251")
            );
        } catch (Throwable ignored) {
            throw new RuntimeException("windows-1251 encoding is unsupported");
        }
    }

    private DcDocument getDocument() {
        try (InputStream input = TestUtils.class.getResourceAsStream(fufTemplateName)) {
            Document fufTemplate = XMLUtil.deserialize(input);
            setPathValue(fufTemplate, DATE_PATH, getFormattedDate(DATE_FOR_XML_FORMAT, fileDate));
            setPathValue(fufTemplate, DEMAND_FILE_NAME, getIncludedFilename(getFormattedDate(DATE_FOR_FILENAME_FORMAT, fileDate), fufId, applicationFilenameId));
            setPathValue(fufTemplate, DEMAND_PACKAGE_NAME, getFilename(getFormattedDate(DATE_FOR_FILENAME_FORMAT, fileDate), fufId));


            return null;

        } catch (Throwable ignored) {
            throw new IllegalArgumentException("Bad fuf path or bad FUF for demand");
        }
    }

    private static void setPathValue(Document fufTemplate, String XPath, String value) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList dateNode = (NodeList) xpath.compile(XPath).evaluate(fufTemplate, XPathConstants.NODE);
            if (dateNode.getLength() == 0) {
                throw new IllegalArgumentException("Bad fuf path or bad FUF for demand");
            }
            dateNode.item(0).setTextContent(value);
        } catch (Throwable ignored) {
            throw new IllegalArgumentException("Bad fuf path or bad FUF for demand");
        }
    }

    @NotNull
    @Contract(pure = true)
    private String getFilename(String formattedDate, String fufUUID) {
        return FILENAME_PREFIX + FILENAME_SEPARATOR + inn + kpp + FILENAME_SEPARATOR
                + inn + kpp + FILENAME_SEPARATOR + cuCode
                + FILENAME_SEPARATOR + formattedDate + FILENAME_SEPARATOR + fufUUID;
    }


    @NotNull
    @Contract(pure = true)
    private String getIncludedFilename(String formattedDate, String fufUUID, String includedFilename) {
        return APPLICATION_FILENAME_PREFIX + FILENAME_SEPARATOR + cuCode + FILENAME_SEPARATOR + inn + kpp
                + FILENAME_SEPARATOR + formattedDate + FILENAME_SEPARATOR + fufUUID
                + FILENAME_SEPARATOR + includedFilename + APPLICATION_FILENAME_EXTENSION;
    }


    @NotNull
    private String getFormattedDate(String dateFormat, Date date) {
        return (new SimpleDateFormat(dateFormat)).format(date);
    }

}
