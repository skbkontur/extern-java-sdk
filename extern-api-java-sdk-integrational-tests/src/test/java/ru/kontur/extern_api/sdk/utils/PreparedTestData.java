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

package ru.kontur.extern_api.sdk.utils;

import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.Taxpayer;
import ru.kontur.extern_api.sdk.model.AdditionalClientInfo.SignerTypeEnum;
import ru.kontur.extern_api.sdk.model.ion.ClientInfo;
import ru.kontur.extern_api.sdk.model.ion.*;

import java.util.ArrayList;
import java.util.Date;

public class PreparedTestData {

    public static UsnServiceContractInfo usnV2(Certificate certificate, OrganizationRequest org) {

        UsnFormatPeriod period = new UsnFormatPeriod();
        period.setYear(2017);

        UsnServiceContractInfo usn = new UsnServiceContractInfo();
        usn.setAdditionalOrgInfo(createAdditionalClientInfo(certificate.getFio(), org.getOrgName(), true));
        usn.setData(getUsnV2Data());
        usn.setPeriod(period);
        usn.setVersion(2);

        return usn;
    }

    private static AdditionalClientInfo createAdditionalClientInfo(String fio, String orgName, boolean isChief) {
        SignerTypeEnum signerType = isChief ? SignerTypeEnum.CHIEF : SignerTypeEnum.REPRESENTATIVE;

        Taxpayer payer = new Taxpayer();
        payer.setTaxpayerChiefFio(fio);
        payer.setTaxpayerFullName(orgName);
        payer.setTaxpayerOkved("47");
        payer.setTaxpayerPhone("777777");

        if (!isChief) {
            PassportInfo passport = new PassportInfo();
            passport.setCode("21");
            passport.setSeriesNumber("1111 441144");
            passport.setIssuedBy("string");
            passport.setIssuedDate(new Date());

            Representative representative = new Representative();
            representative.setPassport(passport);
            representative.setRepresentativeDocument("1");

            payer.setRepresentative(representative);
        }

        DocumentSender documentSender = new DocumentSender();
        documentSender.setSenderFullName(fio);

        AdditionalClientInfo aci = new AdditionalClientInfo();
        aci.setDocumentSender(documentSender);
        aci.setSignerType(signerType);
        aci.setTaxpayer(payer);

        return aci;
    }

    private static ClientInfo createIonClientInfo(String fio, String orgName, boolean isChief) {
        SignerType signerType = isChief ? SignerType.CHIEF : SignerType.REPRESENTATIVE;

        ru.kontur.extern_api.sdk.model.ion.Taxpayer payer = new ru.kontur.extern_api.sdk.model.ion.Taxpayer();
        payer.setTaxpayerChiefFio(fio);
        payer.setTaxpayerFullName(orgName);
        payer.setTaxpayerOkved("47");
        payer.setTaxpayerPhone("777777");
        payer.setAddress(new Address());

        if (!isChief) {
            PassportInfo passport = new PassportInfo();
            passport.setCode("21");
            passport.setSeriesNumber("1111 441144");
            passport.setIssuedBy("string");
            passport.setIssuedDate(new Date());

            Representative representative = new Representative();
            representative.setPassport(passport);
            representative.setRepresentativeDocument("1");

            payer.setRepresentative(representative);
        }

        return new ClientInfo(fio, signerType, payer);
    }

    public static IonRequestContract buildIon(BuildDocumentType ionType, Certificate certificate, OrganizationRequest org) {
        return IonBuilder.Create()
                .setClientInfo(createIonClientInfo(certificate.getFio(), org.getOrgName(), true))
                .setIonData(getIonRequestData(ionType))
                .setIonPeriod(new IonPeriod(2018))
                .build();
    }

    private static IonRequestData getIonRequestData(BuildDocumentType ionType) {
        switch (ionType) {
            case ION1:
                return new Ion1RequestData(
                        IonRequestContract.RequestType.ALL_KPPS.index(),
                        IonRequestContract.AnswerFormat.XML.index(),
                        "01.11.2018");
            case ION2:
                return new Ion2RequestData(
                        IonRequestContract.RequestType.ALL_KPPS.index(),
                        IonRequestContract.AnswerFormat.XML.index(),
                        IonRequestContract.ReportGenerationCondition.GROUP_BY_ALL_PAYMENT_TYPES.index(),
                        "2018",
                        null);
            case ION3:
                return new Ion3RequestData(
                        IonRequestContract.RequestType.ALL_KPPS.index(),
                        IonRequestContract.AnswerFormat.XML.index(),
                        "01.01.2018",
                        "31.12.2019",
                        IonRequestContract.ReportSelectionCondition.ALL_REPORT_TYPES.index());
            case ION4:
                return new Ion4RequestData(
                        IonRequestContract.RequestType.ALL_KPPS.index(),
                        IonRequestContract.AnswerFormat.XML.index(),
                        "01.11.2018",
                        "2018",
                        new ArrayList<RequestingTax>() {{
                            add(new RequestingTax("Налог на прибыль", "18210102010012100110", "45305000"));
                        }}
                );
            case ION5:
                return new Ion5RequestData(
                        IonRequestContract.RequestType.ALL_KPPS.index(),
                        IonRequestContract.AnswerFormat.XML.index(),
                        "01.11.2018");
            default:
                return new IonRequestData(IonRequestContract.RequestType.ALL_KPPS.index(), IonRequestContract.AnswerFormat.XML.index());
        }
    }

    private static UsnDataV2 getUsnV2Data() {
        UsnDataV2 data = new UsnDataV2();
        data.setIschislMin("3681");
        data.setNomKorr(1);
        data.setPoMestu(120);
        data.setUbytPred("3681");

        data.setZaKv(createPeriodIndicators(
                "560073",
                "560073",
                "560073",
                "560073",
                "50701000",
                "560073"
        ));
        data.setZaPg(createPeriodIndicators(
                "31931",
                "1092253",
                "1092253",
                "1092253",
                "08701000",
                "1092253"
        ));
        data.setZa9m(createPeriodIndicators(
                "32688",
                "1637046",
                "1637046",
                "1637046",
                "08701000",
                "1637046"
        ));
        data.setZaNalPer(createTaxPeriodIndicators());

        return data;
    }

    private static PeriodIndicators createPeriodIndicators(
            String avPu, String dohod,
            String ischisl, String nalBazaUbyt,
            String oktmo, String rashod) {

        PeriodIndicators p = new PeriodIndicators();

        MerchantTax rts = new MerchantTax();
        rts.setDohod("0");
        rts.setIschisl("0");
        rts.setTorgSborFact("0");
        rts.setTorgSborUmen("0");

        p.setAvPu(avPu);
        p.setDohod(dohod);
        p.setIschisl(ischisl);
        p.setNalBazaUbyt(nalBazaUbyt);
        p.setOktmo(oktmo);
        p.setRaschTorgSbor(rts);
        p.setRashod(rashod);

        p.setStavka("7.0");
        p.setUmenNal("0");

        return p;
    }

    private static TaxPeriodIndicators createTaxPeriodIndicators() {
        return new TaxPeriodIndicators("0",
                createPeriodIndicators("0", "1637046", "1637046", "1637046", "08701000", "1637046")
        );
    }

}
