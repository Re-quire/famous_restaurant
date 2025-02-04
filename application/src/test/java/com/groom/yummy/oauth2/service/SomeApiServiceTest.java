package com.groom.yummy.oauth2.service;

import com.groom.yummy.store.Store;
import com.groom.yummy.store.StoreService;
import com.groom.yummy.webclient.SomeApiService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@ActiveProfiles("test")
@Import(SomeApiServiceTest.TestConfig.class)
class SomeApiServiceTest {

    @Autowired
    private SomeApiService someApiService;

    @MockitoBean
    private StoreService storeService;

    private static MockWebServer mockWebServer;


    @BeforeAll
    static void setUpServer() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("특정 지역 요청 시 반환된 식당 정보 저장 테스트")
    void 특정지역_식당정보_저장테스트() throws Exception {
        // GIVEN
        String mockResponseBody = """
    {
        "data": {
            "stores": [
                {
                    "storeId": 1,
                    "name": "맛집가게",
                    "regionId": 2,
                    "category": "001"
                },
                {
                    "storeId": 2,
                    "name": "맛집가게2",
                    "regionId": 2,
                    "category": "002"
                }
            ]
        },
        "message": "가게 목록이 성공적으로 조회되었습니다."
    }
    """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponseBody)
                .addHeader("Content-Type", "application/json"));

        // WHEN
        String regionCode = "1111000000";
        someApiService.fetchStoresFromApi(regionCode);

        // THEN
        ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);
        verify(storeService, times(2)).createStore(storeCaptor.capture());

        List<Store> capturedStores = storeCaptor.getAllValues();

        // 첫 번째 Store 검증
        Store store1 = capturedStores.get(0);
        assertEquals("맛집가게", store1.getName());
        assertEquals("001", store1.getCategory().getApiCode());
        assertEquals(2L, store1.getRegionId());

        // 두 번째 Store 검증
        Store store2 = capturedStores.get(1);
        assertEquals("맛집가게2", store2.getName());
        assertEquals("002", store2.getCategory().getApiCode());
        assertEquals(2L, store2.getRegionId());

        // Mock 서버 요청 검증
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/api/v1/stores?regionCode=1111000000", recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Configuration
    static class TestConfig {
        @Bean
        public SomeApiService someApiService(StoreService storeService) {
            String baseUrl = mockWebServer.url("/").toString();
            return new SomeApiService(WebClient.builder().baseUrl(baseUrl), storeService);
        }

        @Bean
        public StoreService storeService() {
            return mock(StoreService.class);
        }
    }

}
