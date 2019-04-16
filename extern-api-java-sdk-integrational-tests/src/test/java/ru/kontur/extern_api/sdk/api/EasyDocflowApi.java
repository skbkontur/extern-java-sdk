package ru.kontur.extern_api.sdk.api;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.utils.DemandRequestDto;
import ru.kontur.extern_api.sdk.utils.DemandResponseDto;

@JsonSerialization(GsonProvider.IDENTITY)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface EasyDocflowApi {

    @POST("test-tools/v1/generate-demand")
    CompletableFuture<DemandResponseDto> getDemand(@Body DemandRequestDto demandRequestDto);
}
