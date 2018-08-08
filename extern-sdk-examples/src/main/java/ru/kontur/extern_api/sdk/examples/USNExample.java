/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.AdditionalClientInfo;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.PassportInfo;
import ru.kontur.extern_api.sdk.model.PeriodIndicators;
import ru.kontur.extern_api.sdk.model.Representative;
import ru.kontur.extern_api.sdk.model.TaxPeriodIndicators;
import ru.kontur.extern_api.sdk.model.Taxpayer;
import ru.kontur.extern_api.sdk.model.UsnDataV2;
import ru.kontur.extern_api.sdk.model.UsnFormatPeriod;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

public class USNExample {

    private static final Logger log = LogManager.getLogger(USNExample.class);

    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException {
        // first argument is a path to property file
        if (args.length == 0) {
            log.log(Level.INFO,
                    "There is no path to the property file in the command line.");
            return;
        }

        // настраиваем engine сервис
        ConfiguratorService configuratorService = new ConfiguratorService(getProperties(args[0]));
        ExternEngine externEngine = configuratorService.getExternEngine();

        // 1. создаем черновик
        DraftService draft = externEngine.getDraftService();
        QueryContext<UUID> draftContext = draft
                .createAsync(configuratorService.getSender(), configuratorService.getRecipient(),
                        configuratorService.getOrganization()).get();

        if (draftContext.isFail()) {
            log.log(Level.INFO,
                    "Error creating draft.\r\n" + draftContext.getServiceError().toString());

        }

        // создаем декларацию версии 1. в качестве data используется срока с Json
        QueryContext<DraftDocument> cxt_v1 = new QueryContext<>();
        cxt_v1.setDraftId(draftContext.get());
        cxt_v1.setVersion(1);
        cxt_v1.setUsnServiceContractInfo(createUsnServiceContractInfo_v1());
        QueryContext<DraftDocument> draftDoc_v1 = draft.createAndBuildDeclaration(cxt_v1);
        if (draftDoc_v1.isFail()) {
            log.log(Level.INFO,
                    "Error creating USN.\r\n" + draftDoc_v1.getServiceError().toString());
        }
        // декларация создана

        // создаем декларацию версии 2. в качестве data используется объект
        QueryContext<DraftDocument> cxt_v2 = new QueryContext<>();
        cxt_v2.setDraftId(draftContext.get());
        cxt_v2.setVersion(2);
        cxt_v2.setUsnServiceContractInfo(createUsnServiceContractInfo_v2());
        QueryContext<DraftDocument> draftDoc_v2 = draft.createAndBuildDeclaration(cxt_v2);
        if (draftDoc_v2.isFail()) {
            log.log(Level.INFO,
                    "Error creating USN.\r\n" + draftDoc_v2.getServiceError().toString());
        }
        // декларация создана
    }

    // получаем парамеьры из файла конфигурации
    @Nullable
    private static Properties getProperties(@NotNull String path) throws IOException {
        File parameterFile = new File(path);
        if (!parameterFile.exists() || !parameterFile.isFile()) {
            log.log(Level.INFO, "Parameter file not found: " + path);
            return null;
        }

        // loads properties
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(parameterFile)) {
            properties.load(is);
        }

        log.log(Level.INFO, "Properties loaded");

        return properties;
    }

    private static UsnServiceContractInfo createUsnServiceContractInfo_v1() {
        UsnServiceContractInfo usn = new UsnServiceContractInfo();
        // 1. period
        UsnFormatPeriod period = new UsnFormatPeriod();
        period.setPeriodModifiers(UsnFormatPeriod.PeriodModifiersEnum.LASTPERIODFORTAXREGIME);
        period.setYear(2017);
        usn.setPeriod(period);

        // 2. additional client info
        usn.setAdditionalOrgInfo(createAdditionalClientInfo());

        // 3. data
        usn.setData(String.valueOf(USNExample.class.getResourceAsStream("/docs/income-1.json")));

        return usn;
    }

    private static UsnServiceContractInfo createUsnServiceContractInfo_v2() {
        UsnServiceContractInfo usn = new UsnServiceContractInfo();
        // 1. period
        UsnFormatPeriod period = new UsnFormatPeriod();
        period.setPeriodModifiers(UsnFormatPeriod.PeriodModifiersEnum.LASTPERIODFORTAXREGIME);
        period.setYear(2017);
        usn.setPeriod(period);

        // 2. additional client info
        usn.setAdditionalOrgInfo(createAdditionalClientInfo());

        // 3. data
        usn.setData(getData());

        return usn;
    }


    private static AdditionalClientInfo createAdditionalClientInfo() {
        AdditionalClientInfo aci = new AdditionalClientInfo();
        aci.setSenderFullName("Иванов Иван Иванович");
        aci.setSignerType(AdditionalClientInfo.SignerTypeEnum.REPRESENTATIVE);
        Taxpayer payer = new Taxpayer();
        Representative representative = new Representative();
        PassportInfo passport = new PassportInfo();
        passport.setCode("1234");
        passport.setSeriesNumber("196851");
        passport.setIssuedBy("ТП 62");
        passport.setIssuedDate(new Date());
        representative.setPassport(passport);
        representative.setRepresentativeDocument("паспорт РФ");
        payer.setRepresentative(representative);
        payer.setTaxpayerChiefFio("Петров Петр Петрович");
        payer.setTaxpayerFullName("Рага и Копыта");
        payer.setTaxpayerOkved("123");
        payer.setTaxpayerPhone("03");
        aci.setTaxpayer(payer);

        return aci;
    }

    private static UsnDataV2 getData() {
        UsnDataV2 data = new UsnDataV2();
        data.setIschislMin("3681");
        data.setNomKorr(0);
        data.setPoMestu(120);
        data.setPrizNp(1);
        data.setUbytPred("3681");
        data.setZaKv(
                createPeriodIndicators(
                        "560073", // АвПУ *
                        "560073", // Доход *
                        "560073", // Исчисл *
                        "560073", // НалБазаУбыт *
                        "50701000", // ОКТМО *
                        // РасчТоргСбор
                        "560073" // Расход *
                        // Ставка *
                        // УменНал
                )
        );
        data.setZaPg(
                createPeriodIndicators(
                        "31931", // АвПУ *
                        "1092253", // Доход *
                        "1092253", // Исчисл *
                        "1092253", // НалБазаУбыт *
                        "08701000", // ОКТМО *
                        // РасчТоргСбор
                        "1092253" // Расход *
                        // Ставка *
                        // УменНал
                )
        );
        data.setZaNalPer(
                createTaxPeriodIndicators(
                        // НалПУМин *
                        // АвПУ *
                        // Доход *
                        // Исчисл *
                        // НалБазаУбыт *
                        // ОКТМО *
                        // РасчТоргСбор
                        // Расход *
                        // Ставка *
                        // УменНал
                )
        );

        return data;
    }

    private static PeriodIndicators createPeriodIndicators(String avPu, String dohod,
            String ischisl, String nalBazaUbyt, String oktmo, String rashod) {
        PeriodIndicators p = new PeriodIndicators();

        p.setAvPu(avPu);
        p.setDohod(dohod);
        p.setIschisl(ischisl);
        p.setNalBazaUbyt(nalBazaUbyt);
        p.setOktmo(oktmo);
        p.setRaschTorgSbor(null);
        p.setRashod(rashod);
        p.setStavka("7.0");
        p.setUmenNal(null);

        return p;
    }

    private static TaxPeriodIndicators createTaxPeriodIndicators() {
        return new TaxPeriodIndicators("0",
                createPeriodIndicators("0", "1637046", "1637046", "1637046", "08701000", "1637046")
        );
    }
}
