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
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.ExternEngine;
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

        // создаем декларацию
        QueryContext<Void> cxt = new QueryContext<>();
        cxt.setDraftId(draftContext.get());
        cxt.setVersion(2);
        cxt.setContentString(CONTENT_V2);
        QueryContext<Void> usnContext = draft.createUSN(cxt);
        if (usnContext.isFail()) {
            log.log(Level.INFO,
                "Error creating USN.\r\n" + usnContext.getServiceError().toString());
        }

        // декларация успешно создана
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

    @NotNull
    private final static String CONTENT_V2 = ""
        + "{\n"
        + "    //Период за который отправляется декларация\n"
        + "    \"period\":\n"
        + "    {\n"
        + "        \"year\": \"2016\",\n"
        + "        \"period-modifiers\": \"None\"\n"
        + "        //возможные значения periodModifiers:\n"
        + "        //None\n"
        + "        //LiquidationReorganization\n"
        + "        //TaxRegimeChange\n"
        + "        //LastPeriodForTaxRegime\n"
        + "    },\n"
        + "    //Версия контракта\n"
        + "    \"version\": 2,\n"
        + "    //Дополнительные данные об организации\n"
        + "    \"additional-org-info\":\n"
        + "    {\n"
        + "        //Тип подписанта\n"
        + "        \"signer-type\": \"representative\",\n"
        + "        \"document-sender\":\n"
        + "        {\n"
        + "            \"sender-full-name\": \"ОтправительПропатченный\"\n"
        + "        },\n"
        + "        //Данные об налогоплательщике\n"
        + "        \"taxpayer\":\n"
        + "        {\n"
        + "            //Имя руководителя\n"
        + "            \"taxpayer-chief-fio\": \"Иванов Иван Иванович\",\n"
        + "            \"taxpayer-full-name\": \"НалогоплательщикПропатченный\",\n"
        + "            //Данные об представителе\n"
        + "            \"representative\":\n"
        + "            {\n"
        + "                //Паспортные данные\n"
        + "                \"passport\":\n"
        + "                {\n"
        + "                    \"code\": \"123123\",\n"
        + "                    \"serial-number\": \"6565\",\n"
        + "                    \"issue-date\": \"2000-01-01T00:00:00\",\n"
        + "                    \"issued-by\": \"УФМС г. Ревда\"\n"
        + "                },\n"
        + "                //Документ подтверждающий представителя\n"
        + "                \"representative-document\": \"доверенность 23\"\n"
        + "            },\n"
        + "            //Номер телефона\n"
        + "            \"taxpayer-phone\": \"365-65-65\",\n"
        + "            //Оквед\n"
        + "            \"taxpayer-okved\": \"93.04\"\n"
        + "        },\n"
        + "    },\n"
        + "    \"data\":\n"
        + "    {\n"
        + "        //Номер корректировки. Принимает значение: 0 – первичный документ, 1 – 999 – номер корректировки для корректирующего документа\n"
        + "        \"НомКорр\": 0,\n"
        + "        //Код места, по которому представляется документ(120,210,215)\n"
        + "        \"ПоМесту\": 120,\n"
        + "\n"
        + "        //Сумма убытка, полученного в предыдущем (предыдущих) налоговом (налоговых) периоде (периодах), уменьшающая налоговую базу за налоговый период\n"
        + "             \"УбытПред\": \"3681\",\n"
        + "\n"
        + "             //Сумма исчисленного минимального налога за налоговый период (ставка налога 1%)\n"
        + "                \"ИсчислМин\": \"3681\",\n"
        + "\n"
        + "                // Показатели за первый квартал\n"
        + "             //могут отсутствовать\n"
        + "                \"ЗаКв\":\n"
        + "             {\n"
        + "                        //Код по ОКТМО\n"
        + "                     \"ОКТМО\": \"50701000\",\n"
        + "                        //Сумма авансового платежа к уплате по сроку не позднее двадцать пятого апреля отчетного года\n"
        + "                     \"АвПУ\": \"560073\",\n"
        + "                        //Сумма полученных доходов нарастающим итогом\n"
        + "                     \"Доход\": \"560073\",\n"
        + "                        //Сумма произведенных расходов нарастающим итогом\n"
        + "                     \"Расход\": \"560073\",\n"
        + "                        //Налоговая база для исчисления налога (авансового платежа по налогу)/Сумма полученного убытка за истекший налоговый (отчетный) период\n"
        + "                     \"НалБазаУбыт\": \"560073\",\n"
        + "                        //Ставка налога\n"
        + "                     \"Ставка\": \"7.0\",\n"
        + "                        //Сумма исчисленного налога (авансового платежа по налогу)\n"
        + "                     \"Исчисл\": \"560073\"\n"
        + "                },\n"
        + "\n"
        + "                // Показатели за полугодие\n"
        + "             //могут отсутствовать\n"
        + "                \"ЗаПг\":\n"
        + "             {\n"
        + "                        //Код по ОКТМО\n"
        + "                     \"ОКТМО\": \"08701000\",\n"
        + "                        //Сумма авансового платежа к уплате по сроку не позднее двадцать пятого июля отчетного года / Сумма авансового платежа к уменьшению по сроку не позднее двадцать пятого июля отчетного года (со знаком \"-\")\n"
        + "                     \"АвПУ\": \"31931\",\n"
        + "                        //Сумма полученных доходов нарастающим итогом\n"
        + "                     \"Доход\": \"1092253\",\n"
        + "                        //Сумма произведенных расходов нарастающим итогом\n"
        + "                     \"Расход\": \"1092253\",\n"
        + "                        //Налоговая база для исчисления налога (авансового платежа по налогу)/Сумма полученного убытка за истекший налоговый (отчетный) период\n"
        + "                     \"НалБазаУбыт\": \"1092253\",\n"
        + "                        //Ставка налога\n"
        + "                     \"Ставка\": \"7.0\",\n"
        + "                        //Сумма исчисленного налога (авансового платежа по налогу)\n"
        + "                     \"Исчисл\": \"1092253\"\n"
        + "                        },\n"
        + "\n"
        + "                // Показатели за девять месяцев\n"
        + "             //могут отсутствовать\n"
        + "                \"За9м\":\n"
        + "             {\n"
        + "                        //Код по ОКТМО\n"
        + "                     \"ОКТМО\": \"08701000\",\n"
        + "                        //Сумма авансового платежа к уплате по сроку не позднее двадцать пятого октября отчетного года / Сумма авансового платежа к уменьшению по сроку не позднее двадцать пятого октября отчетного года (со знаком \"-\")\n"
        + "                     \"АвПУ\": \"32688\",\n"
        + "                        //Сумма полученных доходов нарастающим итогом\n"
        + "                     \"Доход\": \"1637046\",\n"
        + "                        //Сумма произведенных расходов нарастающим итогом\n"
        + "                     \"Расход\": \"1637046\",\n"
        + "                        //Налоговая база для исчисления налога (авансового платежа по налогу)/Сумма полученного убытка за истекший налоговый (отчетный) период\n"
        + "                     \"НалБазаУбыт\": \"1637046\",\n"
        + "                        //Ставка налога\n"
        + "                     \"Ставка\": \"7.0\",\n"
        + "                        //Сумма исчисленного налога (авансового платежа по налогу)\n"
        + "                     \"Исчисл\": \"1637046\"\n"
        + "                },\n"
        + "\n"
        + "                // Показатели за налоговый период\n"
        + "             \"ЗаНалПер\":\n"
        + "                {\n"
        + "                        //Код по ОКТМО\n"
        + "                     \"ОКТМО\": \"08701000\",\n"
        + "                        //Сумма налога, подлежащая доплате за налоговый период (календарный год) по сроку / Сумма налога к уменьшению за налоговый период (календарный год) по сроку (со знаком \"-\")\n"
        + "                     \"АвПУ\": \"0\",\n"
        + "                        //Сумма минимального налога, подлежащая уплате за налоговый период (календарный год) по сроку\n"
        + "                     //может быть вместо \"НалПУУменПер\"\n"
        + "                        \"НалПУМин\": \"0\",\n"
        + "                     //Сумма полученных доходов нарастающим итогом\n"
        + "                        \"Доход\": \"1637046\",\n"
        + "                     //Сумма произведенных расходов нарастающим итогом\n"
        + "                        \"Расход\": \"1637046\",\n"
        + "                     //Налоговая база для исчисления налога (авансового платежа по налогу)/Сумма полученного убытка за истекший налоговый (отчетный) период\n"
        + "                        \"НалБазаУбыт\": \"1637046\",\n"
        + "                     //Ставка налога\n"
        + "                        \"Ставка\": \"7.0\",\n"
        + "                     //Сумма исчисленного налога (авансового платежа по налогу)\n"
        + "                        \"Исчисл\": \"1637046\"\n"
        + "                }\n"
        + "    }\n"
        + "}";

}