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
 *
 * @author Aleksey Sukhorukov
 */

package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * <p>Объект, содержащий информацию об ответном документе</p>
 * @author Aleksey Sukhorukov
 */
public class ReplyDocument implements LinksHolder {
    private String id;
    private byte[] content;
    private byte[] printContent;
    private String filename;
    private byte[] signature;
    private List<Link> links;
    private String docflowId;
    private String documentId;

    /**
     * Возвращает идентификатор ответного документа
     * @return идентификатор ответного документа
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает массив байт контента ответного документа
     * @return массив байт контента ответного документа
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Возвращает массив байт для печатной формы (PDF) ответного документа
     * @return массив байт для печатной формы (PDF) ответного документа
     */
    public byte[] getPrintContent() {
        return printContent;
    }

    /**
     * Возвращает имя файла ответного документа
     * @return имя файла ответного документа
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Возвращает массив байт подписи в формате PKCS#7 для контента ответного документа
     * @return массив байт подписи в формате PKCS#7 для контента ответного документа
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * станавливает массив байт подписи в формате PKCS#7 для контента ответного документа
     * @param signature массив байт подписи в формате PKCS#7 для контента ответного документа
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Возвращает список ссылок ответного документа
     * @return список ссылок ответного документа
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * @return идентификатор документооборота
     */
    public String getDocflowId() {
        return docflowId;
    }

    /**
     * Возвращает идентификатор документа, для которого был создан ответный документ
     * @return идентификатор документа, для которого был создан ответный документ
     */
    public String getDocumentId() {
        return documentId;
    }

    public Link getDocflowLink(){
        return getLink("docflow");
    }

    public Link getPutSignatureLink(){
        return getLink("save-signature");
    }

    public Link getSendLink(){
        return getLink("send");
    }

    public Link getCloudSignLink(){
        return getLink("sign");
    }

    public Link getCloudSignConfirmLink(){
        return getLink("sign-confirm");
    }

    private Link getLink(String name){
        return links
                .stream()
                .filter(l -> l.getRel().equals(name))
                .findAny()
                .orElse(null);
    }
}
