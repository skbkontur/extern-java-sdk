/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.skbkontur.sdk.extern.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.DocumentContents;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.FnsRecipient;
import ru.skbkontur.sdk.extern.model.Organization;
import ru.skbkontur.sdk.extern.model.Sender;
import ru.skbkontur.sdk.extern.providers.LoginAndPasswordProvider;
import ru.skbkontur.sdk.extern.providers.auth.AuthenticationProviderByPass;
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 * @author Sukhorukov A.D.
 * 
 * Для запуска примера необходимо в командной строке передать путь к файлу:
 * 
 * SendDocForYourself.properties 
 * 
 * со следующим содержимым:
 * 
 * # URI Экстерн сервиса
 * service.base.uri = http://extern-api.testkontur.ru
 * # идентификатор аккаунта
 * account.id = XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
 * # идентификатор внешнего сервиса
 * api.key = XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
 * # URI сервиса аутентификации
 * auth.base.uri = http://api.testkontur.ru/auth
 * # логин для аутентификации по логину и паролю
 * auth.login = *****
 * # пароль для аутентификации по логину и паролю
 * auth.pass = *****
 * # ИНН отправителя
 * sender.inn = **********
 * # КПП отправителя
 * sender.kpp = *********
 * # IP отправителя
 * sender.ip = XX.XXX.XXX.XXX
 * # отпечаток сертификата отправителя
 * sender.thumbprint = XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 * # код ФНС, куда отправляется документ
 * ifns.code = 0087
 * # ИНН организации, для которой отправляется документ
 * company.inn = **********
 * # КПП организации, для которой отправляется документ
 * company.kpp = *********
 * # путь к документу, который необходимо отправить
 * document.path = X:\\path documents\\NO_SRCHIS_0087_0087_XXXXXXXXXXXXXXXXXXX_20180126_XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX.xml
 *  
 * 
 */
public class SendDocForYourself {

	private static ExternEngine engine;

	public static void main(String[] args) throws IOException, CryptoException, InterruptedException, ExecutionException {
		// необходимо передать путь
		if (args.length == 0) {
			System.out.println("There is no path to the properter file in the command line.");
			return;
		}
		
		File parameterFile = new File(args[0]);
		if (!parameterFile.exists() || !parameterFile.isFile()) {
			System.out.println("Parameter file not found: " + args[0]);
			return;
		}
		
		// создаем экземляр движка для работы с API Экстерна
		engine = new ExternEngine();
		// загружаем параметры для отправки документа на портал Экстерна
		Properties parameters = new Properties();
		try (InputStream is = new FileInputStream(parameterFile)) {
			parameters.load(is);
		}
		
		// КОНФИГУРИРОВАНИЕ ДВИЖКА
		
		// устанавливаем URI для Экстерн API
		engine.setServiceBaseUriProvider(() -> parameters.getProperty("service.base.uri"));
		// устанавливаем идентификатор аккаунта
		engine.setAccountProvider(() -> UUID.fromString(parameters.getProperty("account.id")));
		// устанавливаем идентификатор внешнего сервиса
		engine.setApiKeyProvider(() -> parameters.getProperty("api.key"));
		// устанавливаем провайдер для аутентификации по логину и паролю
		engine.setAuthenticationProvider(
			new AuthenticationProviderByPass(
				() -> parameters.getProperty("auth.base.uri"),
				new LoginAndPasswordProvider() {
					@Override
					public String getLogin() { return parameters.getProperty("auth.login");}
					@Override
					public String getPass() { return parameters.getProperty("auth.pass");}
				},
				engine.getApiKeyProvider()
			)
		);
		
		engine.configureServices();
		
		// СОЗДАНИЕ ЧЕРНОВИКА И ОТПРАВКА ДОКУМЕНТА
		
		// сервис для черновиков
		DraftService draft = engine.getDraftService();
		
		// ключ подписи отправителя
		Key senderKey = findKeyByThumbprint(parameters.getProperty("sender.thumbprint"));
		
		// отправитель
		Sender sender = new Sender();
		// ИНН отправителя
		sender.setInn(parameters.getProperty("sender.inn"));
		// КПП отправителя
		sender.setKpp(parameters.getProperty("sender.kpp"));
		// IP адресс отправителя
		sender.setIpaddress(parameters.getProperty("sender.ip"));
		// сертификат отправителя в кодировке BASE64
		sender.setCertificate(senderKey == null ? null : IOUtil.encodeBase64(senderKey.getX509ctx()));

		// получатель
		FnsRecipient recipient = new FnsRecipient();
		// ИНН отправителя
		recipient.setIfnsCode(parameters.getProperty("ifns.code"));

		// получатель
		Organization organization = new Organization(parameters.getProperty("company.inn"),parameters.getProperty("company.kpp"));

		// путь к документу, который будем отправлять
		String docPath = parameters.getProperty("document.path");
		// файл документа для отправки
		File docFile = new File(docPath);
		
		byte[] docContent = IOUtil.readFileContent(docFile);
		// отправлять документ необходимо в кодировке BASE64
		String docBase64 = IOUtil.encodeBase64(docContent);
		// вычисляем подпись
		byte[] sign = sign(senderKey,docContent);
		// отправлять подпись необходимо в кодировке BASE64
		String signBase64 = null;
		if (sign != null)
			signBase64 = IOUtil.encodeBase64(sign);
		
		// создаем описание документа
		DocumentDescription dd = new DocumentDescription();
		dd.setContentType("application/xml");
		dd.setFilename(docFile.getName());

		// создаем контекст документа
		DocumentContents dc = new DocumentContents();
		dc.setDocumentDescription(dd);
		dc.setBase64Content(docBase64);
		dc.setSignature(signBase64);
		
		// 1) создаем черновик
		// 2) добавляем в черновик документ и подпись
		// 3) перед отправкой выполняем проверку для получения дополнительной диагностики
		// 4) отправляем черновик
		QueryContext<List<Docflow>> sendCxt = draft
			.createAsync(sender, recipient, organization)
			.thenApply(c->draft.addDecryptedDocument(c.setDocumentContents(dc)))
			.thenApply(draft::check)
			.thenApply(draft::send)
			.get();

		// проверяем результат отправки 
		if (sendCxt.isFail()) {
			// ошибка отправки документа
			// регистрируем ошибку в лог файл
			System.out.println("Error sending document.\n" + sendCxt.getServiceError().toString());
			
			return;
		}
		
		// документ был отправлен
		// в результате получаем список документооборотов (ДО)
		// иногда один документ может вызвать несколько ДО
		System.out.println(MessageFormat.format("The document [{0}] was sent.",docFile.getName()));
	}
	
	private static byte[] sign(Key key, byte[] content) throws CryptoException {
		byte[] signature = null;
		// криптосервис для ГОСТ алгоритмов
		CryptoService cryptoService = new MSCapi();
		// PKCS#7 криптопровайдер 
		PKCS7 p7 = new PKCS7(cryptoService);
		// если ключ найден вычисляем подпись документа в формате PKCS#7
		if (key != null) 
			signature = p7.sign(key, null, content, false);
		
		return signature;
	}
	
	private static Key findKeyByThumbprint(String thumbprint) throws CryptoException {
		// криптосервис для ГОСТ алгоритмов
		CryptoService cryptoService = new MSCapi();
		// получаем ключ подписи по отпечатку сертификата открытого ключа
		Key key = null;
		// получаем список доступных ключей
		Key[] keys = cryptoService.getKeys();
		if(keys != null) {
			// преобразуем hex  в массив байт
			byte[] tp = IOUtil.hexToBytes(thumbprint);
			// ищем ключ с нужным отпечатком
			key = Stream.of(keys).filter(k->Arrays.equals(tp, k.getThumbprint())).findAny().orElse(null);
		}
		return key;
	}
}
