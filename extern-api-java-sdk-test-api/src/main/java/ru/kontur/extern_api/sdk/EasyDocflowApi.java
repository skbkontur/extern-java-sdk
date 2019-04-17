package ru.kontur.extern_api.sdk;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.model.DemandRequestDto;
import ru.kontur.extern_api.sdk.model.DemandResponseDto;

@JsonSerialization(GsonProvider.IDENTITY)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface EasyDocflowApi {

    @POST("test-tools/v1/generate-demand")
    CompletableFuture<DemandResponseDto> getDemand(@Body DemandRequestDto demandRequestDto);
}
