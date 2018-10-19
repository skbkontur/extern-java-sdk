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

package ru.kontur.extern_api.sdk.portal;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.Raw;
import ru.kontur.extern_api.sdk.portal.model.CertificateAuthenticationQuest;
import ru.kontur.extern_api.sdk.portal.model.DecryptedKey;
import ru.kontur.extern_api.sdk.portal.model.ServiceUserBinding;
import ru.kontur.extern_api.sdk.portal.model.SessionResponse;
import ru.kontur.extern_api.sdk.portal.model.TrustedAuthenticationQuest;


@ApiResponseConverter(PortalResponseConverter.class)
public interface AuthApi {

    String VERSION = "v5.12";

    @POST("sessions/" + VERSION + "/sessions/refresh")
    CompletableFuture<SessionResponse> refreshSession(
            @NotNull @Query("auth.sid") String sessionId,
            @Nullable @Query("refreshToken") String refreshToken
    );

    @POST("auth/" + VERSION + "/authenticate-by-pass")
    @Headers("Content-Type: text/plain")
    CompletableFuture<SessionResponse> passwordAuthentication(
            @NotNull @Query("login") String login,
            @Nullable @Query("api-key") String apiKey,
            @Nullable @Query("auth.sid") String authSid,
            @NotNull @Body String password
    );

    @POST("/auth/" + VERSION + "/authenticate-by-cert")
    @Headers("Content-Type: application/octet-stream")
    CompletableFuture<CertificateAuthenticationQuest> certificateAuthenticationInit(
            @Nullable @Query("api-key") String apiKey,
            @Nullable @Query("auth.sid") String authSid,
            @Nullable @Query("free") Boolean withoutValidation,
            @NotNull @Body @Raw byte[] cert
    );

    @POST("auth/" + VERSION + "/approve-cert")
    CompletableFuture<SessionResponse> certificateAuthenticationConfirm(
            @NotNull @Query("thumbprint") String thumbprint,
            @Nullable @Query("api-key") String apiKey,
            @NotNull @Body @Raw byte[] decryptedKey
    );

    @POST("auth/" + VERSION + "/authenticate-by-truster")
    CompletableFuture<TrustedAuthenticationQuest> trustedAuthenticationInit(
            @NotNull @Query("timestamp") Date timestamp,
            @NotNull @Query("identityKey") String identityKey,
            @NotNull @Query("identityValue") String identityValue,
            @Nullable @Query("serviceUserId") String serviceUserId,
            @Nullable @Query("api-key") String apiKey,
            @NotNull @Body byte[] signature
    );

    @POST("auth/" + VERSION + "/approve-truster")
    CompletableFuture<SessionResponse> trustedAuthenticationConfirm(
            @NotNull @Query("key") String key,
            @NotNull @Query("id") String identityValue,
            @Nullable @Query("api-key") String apiKey
    );

    @POST("auth/" + VERSION + "/register-external-service-id")
    CompletableFuture<Void> registerExternalServiceId(
            @Nullable @Query("api-key") String apiKey,
            @NotNull @Body ServiceUserBinding serviceUserBinding
    );

}
