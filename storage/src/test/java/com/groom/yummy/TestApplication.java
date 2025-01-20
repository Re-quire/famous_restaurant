package com.groom.yummy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 테스트 환경에서 mock 테스트가 아닌 실제 db(H2 db)에 접근하여 테스트 하기 위해 추가.
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}

/* 컴포넌트 스캔의 범위
   예를 들어, TestApplication 클래스가 src/test/java/com.groom.yummy 패키지에 위치하면:
   스캔 범위: com.groom.yummy와 그 하위 패키지.
   따라서 main 폴더 아래의 com.groom.yummy에 위치한 클래스들도 빈으로 등록됩니다.
 */
