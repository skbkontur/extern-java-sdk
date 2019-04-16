package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.model.ClientInfo;

class DemandsGenerator {

    private final static String testToolsPoint = "/test-tools/v1/generate-demand";
    private final static String DEFAULT_KND = "1160001";//"1165013";


    static DemandResponseDto GenerateDemands(@NotNull HttpClient httpClient, UUID accountId, String baseUrl, ClientInfo clientInfo, String orgName) {
        DemandRequestDto requestDto = new DemandRequestDto(accountId, clientInfo, orgName, new String[]{DEFAULT_KND});

//        ApiResponse<DemandResponseDto> demandResponseDto = httpClient
//                .submitHttpRequest(testToolsPoint,"POST",null,requestDto,null, null, DemandResponseDto.class).\;

        DemandResponseDto demandResponseDto = httpClient
                .followPostLink(baseUrl.concat(testToolsPoint),requestDto,DemandResponseDto.class);

        return demandResponseDto;
    }
}
