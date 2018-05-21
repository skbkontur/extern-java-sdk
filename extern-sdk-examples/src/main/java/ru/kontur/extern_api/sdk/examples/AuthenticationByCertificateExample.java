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
import java.net.URL;
import java.util.Properties;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

/*
    пример аутентификации по сертификату
 */
public class AuthenticationByCertificateExample {

    public static void main(String[] args) throws IOException {
        // необходимо передать путь
        if (args.length == 0) {
            System.out.println("There is no path to the property file in the command line.");
            return;
        }
        // файл с параметрами должен существовать
        File parameterFile = new File(args[0]);
        if (!parameterFile.exists() || !parameterFile.isFile()) {
            System.out.println("Parameter file not found: " + args[0]);
            return;
        }

        // загружаем параметры
        Properties parameters = new Properties();
        try (InputStream is = new FileInputStream(parameterFile)) {
            parameters.load(is);
        }

        // заполняем параметры
        URL certificateResource = AuthenticationByCertificateExample.class.getClassLoader()
            .getResource("cert.cer");
        if (certificateResource == null) {
            System.out.println("Error! Certificate not found!");
            return;
        }

        // создаем провайдер
        CertificateAuthenticationProvider authenticationProvider = CertificateAuthenticationProvider
            .usingCertificate(certificateResource)
            .setApiKeyProvider(() -> parameters.getProperty("api.key"))
            .setServiceBaseUriProvider(() -> parameters.getProperty("auth.base.uri"))
            .setCryptoProvider(new CryptoProviderMSCapi())
            .setSignatureKeyProvider(() -> parameters.getProperty("sender.thumbprint"))
            .buildAuthenticationProvider();

        // при получении ID происходит инициализация и подтверждение
        QueryContext<String> sessionId = authenticationProvider.sessionId();
        if (sessionId.getServiceError() != null) {
            System.out.println("Error of authentication!");
            return;
        }

        System.out.println("authentication OK!");
    }
}
