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
package ru.kontur.extern_api.sdk.provider.crypt.mscapi;

import com.argos.asn1.Asn1Exception;
import com.argos.asn1.OIDLib;
import com.argos.asn1.ObjectIdentifier;
import com.argos.asn1.OctetString;
import com.argos.asn1.Set;
import com.argos.asn1.UniversalTime;
import com.argos.cipher.asn1ext.Asn1ExtException;
import com.argos.cipher.asn1ext.RecipientInfo;
import com.argos.cipher.asn1ext.SignerInfo;
import com.argos.cipher.asn1ext.X509certificate;
import com.argos.cipher.asn1ext.notions.Attribute;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Errors;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.PublicKey;
import ru.argosgrp.cryptoservice.SessionKey;
import ru.argosgrp.cryptoservice.pkcs7.EnvelopedDataInputStream;
import ru.argosgrp.cryptoservice.pkcs7.EnvelopedDataOutputStream;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;

/**
 * See ru.argosgrp 5.0.22 authors to update this class.
 * @See ru.argosgrp.cryptoservice.pkcs7.PKCS7
 */
public class PKCS7KeAPi {

    /** иденификатор алгоритма экспорта/импорта сессионного ключа, rfc 4357 VKO GOST R 34.10-2001 */
    public static final int CALG_PRO_EXPORT = 0x661f; // 26143
    /** иденификатор алгоритма экспорта/импорта сессионного ключа, используется для криптографии PKCS#7 */
    public static final int CALG_PRO12_EXPORT = 0x6621; // 26145

    //	private static final Logger log = Logger.getLogger(PKCS7.class);
    private static final int BLOCK_SIZE = 0x1000;
    private static final byte[] BLOCK = new byte[BLOCK_SIZE];

    private CryptoService cryptoService;

    public PKCS7KeAPi(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public void setCryptoService(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public byte[] sign(Key key, String pass, byte[] data, boolean isAttach) throws CryptoException {
        try {
            if (key.getX509ctx() == null) {
                key.setX509ctx(cryptoService.exportX509(key));
            }
            // определяем идентификатор алгоритм по сертификату ключа
            X509certificate cert = new X509certificate(key.getX509ctx());
            // идентификатор алгоритма подписи
            int algId = getAlgIdBy(cert);
            // вычисления хеша ГОСТ 34.11-94
            byte[] hash = cryptoService.hash(data, algId);
            // вычисление ЭЦП ГОСТ 34.10-%algId%
            byte[] signature = IOUtil.swap(cryptoService.signHash(key, pass, hash));
            // если необходимо добавить подписываемые данные
            byte[] attached = isAttach ? data : null;
            // упаковываем подпись в формат PKCS#7
            return createSignCMS(getX509certificate(key), signature, attached);
        } catch (Asn1Exception x) {
            throw new CryptoException(Errors.X509_ERROR, x);
        }
    }

    public byte[] sign(Key key, String pass, InputStream is) throws CryptoException, IOException {
        try {
            if (key.getX509ctx() == null) {
                key.setX509ctx(cryptoService.exportX509(key));
            }
            // определяем идентификатор алгоритм по сертификату ключа
            X509certificate cert = new X509certificate(key.getX509ctx());
            // идентификатор алгоритма подписи
            int algId = getAlgIdBy(cert);
            // дискриптор хеша
            Object handler = cryptoService.initHash(algId);
            // вычисления хеша ГОСТ 34.11-94
            int size = 0;
          while ((size = is.read(BLOCK)) > 0) {
            cryptoService.hash(handler, Arrays.copyOfRange(BLOCK, 0, size), false);
          }
            // извлекаем хеш
            byte[] hash = cryptoService.hash(handler, null, true);
            // вычисление ЭЦП ГОСТ 34.10-2001
            byte[] signature = IOUtil.swap(cryptoService.signHash(key, pass, hash));
            // упаковываем подпись в формат PKCS#7
            return createSignCMS(getX509certificate(key), signature, null);
        } catch (Asn1Exception x) {
            throw new CryptoException(Errors.X509_ERROR, x);
        }
    }

    /*
      public boolean verify(byte[] p7) {

      }
      */
    public static byte[] createSignCMS(X509certificate x509, byte[] signature, byte[] data)
            throws CryptoException {
        try {
            // идентификатор алгоритма шифрования подписи subjectPubKeyInfo
            String encryptOID = getEncryptOID(x509);
            // OID хеша для алгоритма подписи
            String encryptHashOID = encryptOID;
            // если это RSA подпись
            if (OIDLib.OID_RSA_ENCRYPTION.equals(encryptOID)) {
                // то идентификатор алгоритма берем из CertificateInfo.AlgorithmIdentifier
                encryptHashOID = x509.getCertificateInfo().getAlgorithmIdentifier().getAlgorithm()
                        .toPrintableString();
            }
            // идентификатор алгоритма хеширования
            String hashOID = getHashOID(encryptHashOID);
            // структура PKCS#7
            com.argos.cipher.asn1ext.PKCS7 p7 = new com.argos.cipher.asn1ext.PKCS7(com.argos.asn1.OIDLib.OID_SIGNED_DATA,
                                                                                   hashOID,
                                                                                   data);
            p7.getSignedData().addCertificate(x509);
//      p7.getSignedData().addSignerInfo(new SignerInfo(x509,com.argos.asn1.OIDLib.OID_HASH_GOST_R3411,OID_ENCRYPT_GOST_R3410,signature));
            p7.getSignedData().addSignerInfo(new SignerInfo(x509, hashOID, encryptOID, signature));

            return p7.serialize();
        } catch (Exception x) {
            throw new CryptoException(Errors.PKCS7_CREATION_ERROR, x);
        }
    }

    public static byte[] createSignCMS(
            byte[] x509Der,
            byte[] signature,
            byte[] authenticatedAttributes,
            byte[] data
    ) throws CryptoException {
        try {
            X509certificate x509 = new X509certificate(x509Der);
            // идентификатор алгоритма шифрования подписи subjectPubKeyInfo
            String encryptOID = getEncryptOID(x509);
            // OID хеша для алгоритма подписи
            String encryptHashOID = encryptOID;
            // если это RSA подпись
            if (OIDLib.OID_RSA_ENCRYPTION.equals(encryptOID)) {
                // то идентификатор алгоритма берем из CertificateInfo.AlgorithmIdentifier
                encryptHashOID = x509.getCertificateInfo().getAlgorithmIdentifier().getAlgorithm()
                        .toPrintableString();
            }
            // идентификатор алгоритма хеширования
            String hashOID = getHashOID(encryptHashOID);
            // структура PKCS#7
            com.argos.cipher.asn1ext.PKCS7 p7 = new com.argos.cipher.asn1ext.PKCS7(com.argos.asn1.OIDLib.OID_SIGNED_DATA,
                                                                                   hashOID,
                                                                                   data);
            p7.getSignedData().addCertificate(x509);
            p7.getSignedData().addSignerInfo(new SignerInfo(
                    x509,
                    hashOID,
                    encryptOID,
                    signature,
                    authenticatedAttributes
            ));

            return p7.serialize();
        } catch (Exception x) {
            throw new CryptoException(Errors.PKCS7_CREATION_ERROR, x);
        }
    }

    public static byte[] createRSAAuthenticatedAttributes(byte[] digest) throws Asn1Exception {
        // signed attributes
        Set authenticatedAttributes = new Set();
        // rsa content type
        authenticatedAttributes.add(new Attribute(
                "1.2.840.113549.1.9.3",
                new ObjectIdentifier("1.2.840.113549.1.7.1")
        ));
        // signature time
        authenticatedAttributes.add(new Attribute(
                "1.2.840.113549.1.9.5",
                new UniversalTime(new java.util.Date())
        ));
        // message digest
        authenticatedAttributes.add(new Attribute("1.2.840.113549.1.9.4", new OctetString(digest)));

        return authenticatedAttributes.serialize();
    }

    public static byte[] createSignCMS(X509Certificate cert, byte[] signature, byte[] data)
            throws CryptoException {
        try {
            X509certificate x509 = new X509certificate(cert.getEncoded());
            return createSignCMS(x509, signature, data);
        } catch (Exception x) {
            throw new CryptoException(Errors.PKCS7_CREATION_ERROR, x);
        }
    }

    /**
     * Проверка присоединенной подписи
     *
     * @param sign byte[] массив байт присоединенной подписи в формате {@link PKCS7}
     * @return boolean true - подпись верна, false - подпись не верна
     * @throws CryptoException брасает ошибку криптопровайдера
     */
    public boolean verify(byte[] sign) throws CryptoException {
        com.argos.cipher.asn1ext.PKCS7 p7 = null;
        // загружаем подпись
        try {
            p7 = new com.argos.cipher.asn1ext.PKCS7(sign);
        } catch (Exception x) {
            throw new CryptoException(
                    Errors.PKCS7_ERROR, x);
        }
        // извлекаем данные из подписи
        byte[] data = p7.getSignedData().getContentInfo().getData();
        // если данных в подписи нет, то формируем ошибку
      if (data == null) {
        throw new CryptoException(Errors.VERIFY_ERROR, " Отсутствуют данные для проверки.");
      }
        // проверяем подписи для всех подписантов
        return verify(data, p7);
    }

    /**
     * Проверка отсоединенной подписи
     *
     * @param data byte[] данные для проверки подписи
     * @param sign byte[] массив байт отсоединенной подписи в формате {@link PKCS7}
     * @return boolean true - подпись верна, false - подпись не верна
     * @throws CryptoException брасает ошибку криптопровайдера
     */
    public boolean verify(byte[] data, byte[] sign) throws CryptoException {
        com.argos.cipher.asn1ext.PKCS7 p7 = null;
        // загружаем подпись
        try {
            p7 = new com.argos.cipher.asn1ext.PKCS7(sign);
        } catch (Exception x) {
            throw new CryptoException(
                    Errors.PKCS7_ERROR, x);
        }
        // проверяем подписи для всех подписантов
        return verify(data, p7);
    }

    /**
     * Проверка отсоединенной подписи
     *
     * @param is {@link InputStream} поток данных, для которых проверяется подпись
     * @param blockSize int размер блока данных для чтения из входного потока
     * @param sign byte[] массив байт отсоединенной подписи в формате {@link PKCS7}
     * @return boolean true - подпись верна, false - подпись не верна
     * @throws CryptoException брасает ошибку криптопровайдера
     */
    public boolean verify(InputStream is, int blockSize, byte[] sign) throws CryptoException {
        com.argos.cipher.asn1ext.PKCS7 p7 = null;
        // загружаем подпись
        try {
            p7 = new com.argos.cipher.asn1ext.PKCS7(sign);
        } catch (Exception x) {
            throw new CryptoException(
                    Errors.PKCS7_ERROR, x);
        }
        // проверяем подписи для всех подписантов
        return verify(is, blockSize, p7);
    }

    private boolean verify(byte[] data, com.argos.cipher.asn1ext.PKCS7 p7) throws CryptoException {
        boolean result = false;
        // проверяем подписи для всех подписантов
        try {
            // цикл по всем подписантам
            for (int i = 0; i < p7.getSignedData().getSignerInfosSize(); i++) {
                SignerInfo si = p7.getSignedData().getSignerInfo(i);
                X509certificate c = p7.findCertificate(si.getIssuer(), si.getIssuerSerialNumber());
              if (c == null)
              // сертификат не найден
              {
                throw new CryptoException(Errors.VERIFY_ERROR, " Сертификат не найден");
              }
                // вычислим дайджест сообщения по исходному документу
                byte[] srcDigest = this.cryptoService.hash(data, getAlgIdBy(c));
                // извлекаем открытый ключ из сертификата
                byte[] pubkeyblob = cryptoService.exportPubKeyFromX509(c.serialize());
                // извлекаем подпись
                byte[] signature = IOUtil.swap(si.getEncryptedDigest());
                // извлекаем набор подписываемых атрибутов: rfc 5652
                com.argos.asn1.Set signedAttrs = si.getSignedAttributes();
                // если атрибуты присутсвуют
                if (signedAttrs != null) {
                    // извлекаем хеш (дайджест) документа
                    com.argos.asn1.Set s = com.argos.cipher.asn1ext.notions.Attributes.getAttributeValue(
                            signedAttrs,
                            com.argos.cipher.asn1ext.notions.Attributes.OID_MESSAGE_DIGEST
                    );
                    // если не найден
                  if (s == null || s.size() < 1)
                  // то кидаем ошибку
                  {
                    throw new CryptoException(Errors.VERIFY_ERROR, "не найден дайджест сообщения");
                  }
                    // тип дайджеста сообщения из PKCS#7 должен быть OCTETSTRING
                  if (s.get(0).getType() != com.argos.asn1.Types.OCTETSTRING) {
                    throw new CryptoException(
                            Errors.VERIFY_ERROR,
                            "не верный тип данных для дайджеста сообщения"
                    );
                  }
                    // дайджест сообщения из PKCS#7
                    byte[] p7Digest = ((com.argos.asn1.OctetString) s.get(0)).getOctetString();
                    // два дайджеста должны быть равны
                  if (!Arrays.equals(p7Digest, srcDigest))
                  // иначе подпись неверна;
                  {
                    return false;
                  }
                    // подпись проверяется для signedAttrs
                    data = signedAttrs.serialize();
                }
                // проверка подписи
              if (!(result = cryptoService.verify(pubkeyblob, data, signature))) {
                break;
              }
            }
        } catch (CryptoException x) {
            throw new CryptoException(Errors.VERIFY_ERROR, x);
        } catch (Exception x) {
            throw new CryptoException(Errors.VERIFY_ERROR, x);
        }
        return result;
    }

    private boolean verify(InputStream is, int blockSize, com.argos.cipher.asn1ext.PKCS7 p7)
            throws CryptoException {
        boolean result = false;
        // проверяем подписи для всех подписантов
        try {
            // проверка всех подписей
            for (int i = 0; i < p7.getSignedData().getSignerInfosSize(); i++) {
                SignerInfo si = p7.getSignedData().getSignerInfo(i);
                X509certificate c = p7.findCertificate(si.getIssuer(), si.getIssuerSerialNumber());
              if (c == null)
              // сертификат не найден
              {
                throw new CryptoException(Errors.VERIFY_ERROR, " Сертификат не найден");
              }
                // извлекаем открытый ключ из сертификата
                byte[] pubkeyblob = cryptoService.exportPubKeyFromX509(c.serialize());
                // извлекаем подпись
                byte[] signature = IOUtil.swap(si.getEncryptedDigest());
                // извлекаем набор подписываемых атрибутов: rfc 5652
                com.argos.asn1.Set signedAttrs = si.getSignedAttributes();
                // если атрибуты присутсвуют
                if (signedAttrs != null) {
                    // извлекаем хеш (дайджест) документа
                    com.argos.asn1.Set s = com.argos.cipher.asn1ext.notions.Attributes.getAttributeValue(
                            signedAttrs,
                            com.argos.cipher.asn1ext.notions.Attributes.OID_MESSAGE_DIGEST
                    );
                    // если не найден
                  if (s == null || s.size() < 1)
                  // то кидаем ошибку
                  {
                    throw new CryptoException(Errors.VERIFY_ERROR, "не найден дайджест сообщения");
                  }
                    // дайджест сообщения из PKCS#7
                  if (s.get(0).getType() != com.argos.asn1.Types.OCTETSTRING) {
                    throw new CryptoException(Errors.VERIFY_ERROR, " Не найден дайджест сообщения");
                  }
                    byte[] p7Digest = ((com.argos.asn1.OctetString) s.get(0)).getOctetString();
                    // вычислим дайджест сообщения по исходному документу
                    byte[] srcDigest = this.cryptoService.hash(is, blockSize, getAlgIdBy(c));
                    // два дайджеста должны быть равны
                  if (!Arrays.equals(p7Digest, srcDigest))
                  // иначе подпись неверна;
                  {
                    return false;
                  }
                    // подпись проверяется для signedAttrs
                  if (!(result = cryptoService.verify(pubkeyblob, signedAttrs.serialize(), signature))) {
                    break;
                  }
                } else {
                    // проверка подписи
                  if (!(result = cryptoService.verify(pubkeyblob, is, blockSize, signature))) {
                    break;
                  }
                }
            }
        } catch (CryptoException x) {
            throw new CryptoException(Errors.VERIFY_ERROR, x);
        } catch (Exception x) {
            throw new CryptoException(Errors.VERIFY_ERROR, x);
        }
        return result;
    }

    /**
     * Зашифровывает данные для получателей.
     * Используется потоковое шифрования. Данные для шифрования читаются блоками по 1024 байт. Такой же
     * размер буфера используется в функции шифрования КриптоПро.
     *
     * @param recipientDerCerts byte[][] массивы байт сертификатов получателей в der-кодировки
     * @param is java.io.InputStream поток байт данных для шифрования
     * @param os java.io.OutputStream выходной поток, содержащий зашифрованный данные в формате PKCS7
     * @throws CryptoException брасает ошибку криптопровайдера
     */
    public void encrypt(
            byte[][] recipientDerCerts,
            InputStream is, java.io.OutputStream os
    ) throws CryptoException {
        // буфер блока данных
        final byte[] BUFFER = new byte[BLOCK_SIZE << 1];
        // буфер для последующего блока данных
        final byte[] BUFFER_AHEAD = new byte[BLOCK_SIZE];
        // считывваем данные из буферизированного потока
        // массив с открытыми ключами получателей
        Object[] pubKeys = new Object[recipientDerCerts.length];
        // параметр шифрования контекста, определяющий OID таблицы замен
        // по умолчанию используется рекомендация ТК-26
        String encryptParamOID = cryptoService.getAlgId() == CryptoService.GOST_2001_ALG_ID
                ? OIDLib.OID_PARAM_A_ENCRYPT_GOST_28147_89 : OIDLib.OID_PARAM_TC26_Z_ENCRYPT_GOST_28147_89;
        // заполняем массив с открытыми ключами получателей
        for (int i = 0; i < pubKeys.length; i++) {
            byte[] publicKeyBlob = cryptoService.exportPubKeyFromX509(recipientDerCerts[i]);
            String encryptOID = getEncryptOID(recipientDerCerts[i]);
            if (encryptParamOID.equals(OIDLib.OID_PARAM_TC26_Z_ENCRYPT_GOST_28147_89) && encryptOID.equals(
                    OIDLib.OID_ENCRYPT_GOST_R3410)) {
                encryptParamOID = OIDLib.OID_PARAM_A_ENCRYPT_GOST_28147_89;
            }
            PublicKey pk = new PublicKey(publicKeyBlob, encryptOID);
            pubKeys[i] = pk;
        }
        // получаем дескриптор для зашифрования данных
        Object eh = cryptoService.initEncrypt(null, pubKeys, CALG_PRO_EXPORT, encryptParamOID);
        // получаем массив зашифрованных ключей получателей
        SessionKey[] sessionKeys = cryptoService.getSessionKeys(eh);
        // создаем объект PKCS#7 для записи данных в поток
        try {
            EnvelopedDataOutputStream pkcs7OutputStream = new EnvelopedDataOutputStream(
                    recipientDerCerts,
                    sessionKeys,
                    cryptoService.getIv(eh),
                    encryptParamOID,
                    os
            );
            // считываем первый блок данных для зашифрования
            int size = is.read(BUFFER, 0, BLOCK_SIZE), size_ahead;
            // выполняем шифрования до конца потока
            while (size > 0) {
                // считываем следующий блок данных, если он есть,
                // иначе будет считаться, что это последний блок.
                // в метод шифрования надо передать признак последнего блока данных - size_ahead<=0
                size_ahead = is.read(BUFFER_AHEAD);
                if (size_ahead > 0 && size_ahead < BLOCK_SIZE) {
                    System.arraycopy(BUFFER_AHEAD, 0, BUFFER, size, size_ahead);
                    size += size_ahead;
                    size_ahead = 0;
                }
                // формируем массив байт только с загруженными данными и передаем в метод шифрования
                byte[] encrypted = cryptoService.encrypt(eh, Arrays.copyOf(BUFFER, size), size_ahead <= 0);
              if (encrypted != null)
              // сохраняем зашифрованный блок в потоке
              {
                pkcs7OutputStream.write(encrypted);
              }
                // переходим к следующему блоку данных, если он есть - size_ahead > 0
                size = -1;
                if (size_ahead > 0) {
                    System.arraycopy(BUFFER_AHEAD, 0, BUFFER, 0, size_ahead);
                    size = size_ahead;
                }
            }
            // закрываем поток
            pkcs7OutputStream.close();
        } catch (IOException x) {
            throw new CryptoException(Errors.USER_DATA_READ_ERROR, x);
        } catch (Asn1ExtException x) {
            throw new CryptoException(Errors.ASN1_ENCODING_ERROR, x);
        } finally {
            // освобождаем ресурсы криптографии
            cryptoService.releaseEncrypt(eh);
        }
    }

    /**
     * Зашифровывает данные для получателей.
     * Используется потоковое шифрования. Данные для шифрования читаются блоками по 1024 байт. Такой же
     * размер буфера используется в функции шифрования КриптоПро.
     *
     * @param recipientDerCerts byte[][] массивы байт сертификатов получателей в der-кодировки
     * @param data java.io.InputStream поток байт данных для шифрования
     * @return byte[] массив байт зашифрованных данных в формате {@link PKCS7}
     * @throws CryptoException брасает ошибку криптопровайдера
     */
    public byte[] encrypt(byte[][] recipientDerCerts, byte[] data) throws CryptoException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        encrypt(recipientDerCerts, bis, bos);
        return bos.toByteArray();
    }

    /**
     * Расшифровывает сообщение
     *
     * @param key {@link Key} закрытый ключ для расшифрования
     * @param pass String пароль (пин-код) для ключевого контейнера
     * @param pkcs7der byte[] массив байт с закодированной структурой PKCS#7
     * @return byte[] массив байт с расшифрованным сообщением
     * @throws CryptoException брасает ошибку криптопровайдера
     */
    public byte[] decrypt(Key key, String pass, byte[] pkcs7der) throws CryptoException {
        // поток для расшифрованного сообщения
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // PKCS#7 структура
        com.argos.cipher.asn1ext.PKCS7 p7;
        // дескриптор для расшифрования данных
        Object decryptHandler = null;

        try {
            // разбираем зашифрованные данные
            p7 = new com.argos.cipher.asn1ext.PKCS7(pkcs7der);
            // ищем получателя по открытому ключа (containerName)
            RecipientInfo ri = p7.getRecipientInfoByX509(getX509certificate(key));
          if (ri == null) {
            throw new CryptoException(
                    Errors.DECRYPTING_ERROR,
                    "пакет не содержит информацию о сертификате получателя"
            );
          }
            // извлекаем вектор инициализации
            byte[] iv = p7.getEnvelopedData().getEncryptedContentInfo_iv();
            // OID таблицы замен для зашифрованного контента
            String encryptParamOID = p7.getEnvelopedData().getEncryptedContentInfo_algorithmOID();
            // извлекаем зашифрованные данные
            java.util.List<byte[]> encryptedData = p7.getEnvelopedData()
                    .getEncryptedContentInfo_encryptedContent();
            // зашифрованный сессионный ключ
            SessionKey sessionKey = cryptoService.getSessionKey(ri.getKeyTransport().serialize());
            // получаем дескриптор для расшифрования данных
            decryptHandler = cryptoService.initDecrypt(key, pass, iv, sessionKey, encryptParamOID);
            if (decryptHandler == null) {
                throw new CryptoException(
                        "Can't init decrypt handler, null returned, try use singleton instance of this class! "
                                + encryptParamOID);
            }
            // номер последнего блока
            int last = encryptedData.size() - 1;
            // для всех блоков данных
            for (int i = 0; i < encryptedData.size(); i++) {
                // расшифрованный блок данных
                byte[] decrypted;
                // производим расшифрование
                if ((decrypted = cryptoService.decrypt(decryptHandler, encryptedData.get(i), i == last))
                        != null) {
                    // сохраняем в выходной поток
                    bos.write(decrypted);
                }
            }
        } catch (com.argos.asn1.Asn1Exception x) {
            throw new CryptoException(Errors.DECRYPT_ERROR, x);
        } catch (IOException x) {
            throw new CryptoException(Errors.DECRYPT_ERROR, x);
        } finally {
          if (decryptHandler != null) {
            cryptoService.releaseDecrypt(decryptHandler);
          }
        }
        return bos.toByteArray();
    }

    /**
     * Расшифровывает поток данных. Расшифрованные данные сохраняются в выходном потоке.
     *
     * @param key {@link Key} закрытый ключ для расшифрования
     * @param pass String пароль (пин-код) для ключевого контейнера
     * @param is java.io.InputStream поток зашифрованных данных
     * @param os java.io.OutputStream поток расшифрованных данных
     * @throws CryptoException брасает ошибку криптопровайдера
     * @throws IOException ошибки ввода, вывода
     */
    public void decrypt(Key key, String pass, InputStream is, java.io.OutputStream os)
            throws CryptoException, IOException {
        // дескриптор расшифрования
        Object decryptHandler = null;

//  	long start = System.currentTimeMillis();
//  	long finish = start;
        try {
//			loginfo(String.format("%1$d,%2$d,%3$s,%4$d",start,1,"Start decrypt",0));
            // сохраняем указатель текущей позиции потока
            is.mark(32);
            // разбирает структуру PKCS#7 с зашифрованными данными
            EnvelopedDataInputStream p7 = new EnvelopedDataInputStream(is);
            // извлекает информацию о получателе по его сертификату
            RecipientInfo ri = p7.getRecipientInfoByX509(getX509certificate(key));
          if (ri == null) {
            throw new CryptoException(
                    Errors.DECRYPTING_ERROR,
                    "пакет не содержит информацию о сертификате получателя"
            );
          }
            // извлекаем вектор инициализации
            byte[] iv = p7.getEncryptedContentInfo_iv();
            // OID таблицы замен для зашифрованного контента
            String encryptParamOID = p7.getEncryptedContentInfo_algorithmOID();
            // зашифрованный сессионный ключ
            SessionKey sessionKey = cryptoService.getSessionKey(ri.getKeyTransport().serialize());
//      finish = System.currentTimeMillis();
//      loginfo(String.format("%1$d,%2$d,%3$s,%4$d",finish,2,"Parsed PKCS7",finish-start));
            // расшифровка
            try {
//        start = finish;
                // получаем дискриптор расшифрования
                decryptHandler = cryptoService.initDecrypt(key, pass, iv, sessionKey, encryptParamOID);
//        finish = System.currentTimeMillis();
//        loginfo(String.format("%1$d,%2$d,%3$s,%4$d",finish,3,"Initialize decrypt",finish-start));
//        start = finish;
                // считываем первый блок
                byte[] dataBlock = p7.hasNextBlock() ? p7.nextBlock() : null;
                // считываем поблочно данные из потока
                while (dataBlock != null) {
                    // считываем блок вперед для определения последнего блока
                    byte[] aheadDataBlock = p7.hasNextBlock() ? p7.nextBlock() : null;
                    // расшифровываем блок
                    byte[] decrypted = cryptoService.decrypt(
                            decryptHandler,
                            dataBlock,
                            aheadDataBlock == null
                    );
                    // если блок был расшифрован в этой фазе
                  if (decrypted != null)
                  // то сохраняем в выходном потоке
                  {
                    os.write(decrypted);
                  }
                    // загружаем следующий блок
                    dataBlock = aheadDataBlock;
                }
            } catch (Asn1Exception x) {
                // может быть невозможно разобрать DER структуру в потоковом режиме
                if (decryptHandler != null) {
                    cryptoService.releaseDecrypt(decryptHandler);
                    decryptHandler = null;
                }
                // попробуем расшифровать как монолит
                try {
                    // получаем дискриптор расшифрования
                    decryptHandler = cryptoService.initDecrypt(key, pass, iv, sessionKey, encryptParamOID);
                    // читаем поток сначала
                    is.reset();
                    // загружаем данные из потока в массив байт
                    // поток для массива байт
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(BLOCK_SIZE);
                    int len;
                  while ((len = is.read(BLOCK)) > 0) {
                    bos.write(BLOCK, 0, len);
                  }
                    // расшифровываем
                    byte[] decrypted = cryptoService.decrypt(decryptHandler, bos.toByteArray(), false);
                    // сохраняем данные в поток
                    os.write(decrypted);
                } finally {
                  if (decryptHandler != null) {
                    cryptoService.releaseDecrypt(decryptHandler);
                  }
                }
            }
        } catch (Asn1Exception x) {
            throw new CryptoException(Errors.ASN1_BAD_FORMAT, x);
        }
//    finish = System.currentTimeMillis();
//    loginfo(String.format("%1$d,%2$d,%3$s,%4$d",finish,3,"Decrypt",finish-start));
    }

    private X509certificate getX509certificate(Key key) throws CryptoException, Asn1Exception {
        // сертификат подписанта
        byte[] x509Der = cryptoService.exportX509(key);
        // десериализация сертификата
        return new X509certificate(x509Der);
    }

    /**
     * Возвращает идентификатор алгоритма подписи по сертификату ключа
     *
     * @param x509 массив байт сертификата ключа подписи
     * @return идентификатор алгоритма подписи: CryptoService.GOST_2001_ALG_ID |
     *         CryptoService.GOST_2012_256_ALG_ID | CryptoService.GOST_2012_512_ALG_ID
     */
    public int getAlgIdBy(X509certificate x509) {
        switch (getEncryptOID(x509)) {
            case OIDLib.OID_ENCRYPT_GOST_R3410:
                return CryptoService.GOST_2001_ALG_ID;
            case OIDLib.OID_ENCRYPT_GOST_R3410_12_256:
                return CryptoService.GOST_2012_256_ALG_ID;
            case OIDLib.OID_ENCRYPT_GOST_R3410_12_512:
                return CryptoService.GOST_2012_512_ALG_ID;
            default:
                return CryptoService.GOST_2001_ALG_ID;
        }
    }

    /**
     * Извлекает OID алгоритма шифрования подписи из сертификата
     *
     * @param x509 X509certificate сертификат подписи
     * @return String OID алгоритма шифрования подписи
     */
    public static String getEncryptOID(X509certificate x509) {
        // информация извлекается из Public Key Identifier
        return x509.getCertificateInfo().getPublicKeyIdentifiers().getAlgorithm().toPrintableString();
    }

    public static String getEncryptOID(byte[] x509der) throws CryptoException {
        try {
            return new X509certificate(x509der).getCertificateInfo().getPublicKeyIdentifiers().getAlgorithm()
                    .toPrintableString();
        } catch (Exception x) {
            throw new CryptoException(x.getMessage(), x);
        }
    }

    private int getEphemAlgId(byte[] x509der) throws CryptoException {
        try {
            switch (getEncryptOID(new X509certificate(x509der))) {
                case OIDLib.OID_ENCRYPT_GOST_R3410:
                    return CryptoService.EPHEM_2001_ALG_ID;
                case OIDLib.OID_ENCRYPT_GOST_R3410_12_256:
                    return CryptoService.EPHEM_2012_256_ALG_ID;
                case OIDLib.OID_ENCRYPT_GOST_R3410_12_512:
                    return CryptoService.EPHEM_2012_512_ALG_ID;
                default:
                    return CryptoService.EPHEM_2001_ALG_ID;
            }
        } catch (Asn1Exception x) {
            throw new CryptoException(x.getMessage(), x);
        }
    }

    /**
     * Возвращает OID алгоритма хеширования по OIDу алгоритма шифрования подписи
     * Поддерживает следующие алгоритмы:
     * SHA-1 with encryption: OID_SHA1_WITH_ENCRYPTION = "1.2.840.113549.1.1.5";
     * SHA-1 with encryption alternate: OID_SHA1_WITH_ENCRYPTION_ALT = "1.3.14.3.2.29";
     * SHA-224 with encryption: OID_SHA224_WITH_ENCRYPTION = "1.2.840.113549.1.1.14";
     * SHA-256 with encryption: OID_SHA256_WITH_ENCRYPTION = "1.2.840.113549.1.1.11";
     * SHA-384 with encryption: OID_SHA384_WITH_ENCRYPTION = "1.2.840.113549.1.1.12";
     * SHA-512 with encryption: OID_SHA512_WITH_ENCRYPTION = "1.2.840.113549.1.1.13";
     *
     * @param encryptOID String OID алгоритма шифровании подписи
     * @return String OID алгоритма хеширования
     */
    public static String getHashOID(String encryptOID) {
        switch (encryptOID) {
            case OIDLib.OID_ENCRYPT_GOST_R3410:
                return OIDLib.OID_HASH_GOST_R3411;
            case OIDLib.OID_ENCRYPT_GOST_R3410_12_256:
                return OIDLib.OID_HASH_GOST_R3411_12_256;
            case OIDLib.OID_ENCRYPT_GOST_R3410_12_512:
                return OIDLib.OID_HASH_GOST_R3411_12_512;
            case OIDLib.OID_SHA1_WITH_ENCRYPTION:
                return OIDLib.OID_SHA1;
            case OIDLib.OID_SHA1_WITH_ENCRYPTION_ALT:
                return OIDLib.OID_SHA1;
            case OIDLib.OID_SHA224_WITH_ENCRYPTION:
                return OIDLib.OID_SHA224;
            case OIDLib.OID_SHA256_WITH_ENCRYPTION:
                return OIDLib.OID_SHA256;
            case OIDLib.OID_SHA384_WITH_ENCRYPTION:
                return OIDLib.OID_SHA384;
            case OIDLib.OID_SHA512_WITH_ENCRYPTION:
                return OIDLib.OID_SHA512;
            default:
                return "";
        }
    }

    public static String getCertificateAlgorithmOID(byte[] x509der) throws Asn1Exception {
        return new X509certificate(x509der).getCertificateInfo().getAlgorithmIdentifier().getAlgorithm()
                .toPrintableString();
    }
}