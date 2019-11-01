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

package ru.kontur.extern_api.sdk.model.pfr;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.LinksHolder;

/**
 * // TODO!
 * <p>Объект, содержащий информацию об ответном документе</p>
 * @author Aleksey Sukhorukov
 */
public class PfrReplyDocument implements LinksHolder {
    private String id;
    private String replyId;
    private String docflowId;
    private String documentId;
    private byte[] encryptedContent;
    private byte[] decryptedContent;
    private byte[] signature;
    private List<Link> links;

    /**
     * Возвращает идентификатор ответного документа
     * @return идентификатор ответного документа
     */
    public String getId() {
        return id;
    }

    public String getReplyId() {
        return replyId;
    }

    public String getDocflowId() {
        return docflowId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    public byte[] getDecryptedContent() {
        return decryptedContent;
    }

    public byte[] getSignature() {
        return signature;
    }

    /**
     * Возвращает список ссылок ответного документа
     * @return список ссылок ответного документа
     */
    public List<Link> getLinks() {
        return links;
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

    @Nullable
    public Link getCloudSignLink(){
        return getLink("sign");
    }

    @Nullable
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
