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

import java.util.Date;
import ru.kontur.extern_api.sdk.model.AdditionalClientInfo;
import ru.kontur.extern_api.sdk.model.AdditionalClientInfo.SignerTypeEnum;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.DocumentSender;
import ru.kontur.extern_api.sdk.model.MerchantTax;
import ru.kontur.extern_api.sdk.model.OrganizationRequest;
import ru.kontur.extern_api.sdk.model.PassportInfo;
import ru.kontur.extern_api.sdk.model.PeriodIndicators;
import ru.kontur.extern_api.sdk.model.Representative;
import ru.kontur.extern_api.sdk.model.SenderRequest;
import ru.kontur.extern_api.sdk.model.TaxPeriodIndicators;
import ru.kontur.extern_api.sdk.model.Taxpayer;
import ru.kontur.extern_api.sdk.model.UsnDataV2;
import ru.kontur.extern_api.sdk.model.UsnFormatPeriod;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;

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

        if (!isChief){
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
