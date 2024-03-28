package com.shop.core.address.acceptance;

import com.shop.common.util.AcceptanceTest;
import com.shop.core.address.presentation.dto.AddressRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.core.auth.step.AuthSteps.회원생성_후_토큰_발급;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주소록 관련 인수 테스트")
public class AddressAcceptanceTest extends AcceptanceTest {

    String 정상적인_회원의_토큰;

    @BeforeEach
    void 사전_회원생성_후_토큰_발급() {
        정상적인_회원의_토큰 = 회원생성_후_토큰_발급(스미스);
    }

    @Nested
    class 주소록_생성 {


        @Nested
        class 성공 {

            /**
             * Given 회원을 생성하고 토큰을 발급한다.
             * When  토큰을 통해 주소록을 등록할 경우
             * Then  정상적으로 주소록이 등록된다.
             */
            @Test
            void 주소록_등록_성공() {
                // when
                AddressRequest 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);


                ExtractableResponse<Response> 주소록_등록_요청_응답 = given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(주소록_등록_요청_정보)
                        .when()
                        .post("/addresses")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // then
                given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get(String.format("/addresses/%d", getCreatedLocationId(주소록_등록_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();
            }
        }

        @Nested
        class 실패 {

            /**
             * Given 회원을 생성하고 토큰을 발급한다.
             * When  토큰 없이 주소록을 등록할 경우
             * Then  주소록이 등록되지 않는다.
             */
            @Test
            void 주소록_등록_실패() {
                // when
                AddressRequest 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);

                // when, then
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(주소록_등록_요청_정보)
                        .when()
                        .post("/addresses")
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }

    @Nested
    class 주소록_수정 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성하고 토큰을 발급한다.
             * And   토큰을 통해 주소록을 등록한다.
             * When  토큰을 통해 이전에 등록했던 주소록을 수정한다.
             * Then  정상적으로 주소록이 수정된다.
             */
            @Test
            void 주소록_수정_성공() {
                // given
                Map<String, String> 주소록_등록_요청_정보 = new HashMap<>();
                주소록_등록_요청_정보.put("address", "서울특별시 강남구 역삼동 123-45");
                주소록_등록_요청_정보.put("detailed_address", "역삼타워빌딩 5층 501호");
                주소록_등록_요청_정보.put("description", "회사 주소");
                주소록_등록_요청_정보.put("is_default", "true");

                ExtractableResponse<Response> 주소록_등록_요청_응답 = given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(주소록_등록_요청_정보)
                        .when()
                        .post("/addresses")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // when
                Map<String, String> 주소록_수정_요청_정보 = new HashMap<>();
                주소록_수정_요청_정보.put("address", "대구광역시 중구 삼덕동 123-4");
                주소록_수정_요청_정보.put("detailed_address", "대구타워 10층 1001호");
                주소록_수정_요청_정보.put("description", "최근 바뀐 회사 주소");
                주소록_수정_요청_정보.put("is_default", "true");

                given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(주소록_등록_요청_정보)
                        .when()
                        .put("/addresses/", getCreatedLocationId(주소록_등록_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .jsonPath();

                JsonPath jsonPath = given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/addresses")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath();

                // then
                assertThat(jsonPath.getList("address", String.class)).containsExactly("대구광역시 중구 삼덕동 123-4");
                assertThat(jsonPath.getList("detailed_address", String.class)).containsExactly("대구타워 10층 1001호");
                assertThat(jsonPath.getList("description", String.class)).containsExactly("최근 바뀐 회사 주소");

            }

        }


        @Nested
        class 실패 {

            /**
             * Given 회원을 생성하고 토큰을 발급한다.
             * And   토큰을 통해 주소록을 등록한다.
             * When  토큰 없이 이전에 등록했던 주소록을 수정한다.
             * Then  주소록이 수정되지 않는다.
             */
            @Test
            void 주소록_수정_실패() {
                // given
                Map<String, String> 주소록_등록_요청_정보 = new HashMap<>();
                주소록_등록_요청_정보.put("address", "서울특별시 강남구 역삼동 123-45");
                주소록_등록_요청_정보.put("detailed_address", "역삼타워빌딩 5층 501호");
                주소록_등록_요청_정보.put("description", "회사 주소");
                주소록_등록_요청_정보.put("is_default", "true");

                ExtractableResponse<Response> 주소록_등록_요청_응답 = given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(주소록_등록_요청_정보)
                        .when()
                        .post("/addresses")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // when
                Map<String, String> 주소록_수정_요청_정보 = new HashMap<>();
                주소록_수정_요청_정보.put("address", "대구광역시 중구 삼덕동 123-4");
                주소록_수정_요청_정보.put("detailed_address", "대구타워 10층 1001호");
                주소록_수정_요청_정보.put("description", "최근 바뀐 회사 주소");
                주소록_수정_요청_정보.put("is_default", "true");

                given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(주소록_등록_요청_정보)
                        .when()
                        .put("/addresses/", getCreatedLocationId(주소록_등록_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                JsonPath jsonPath = given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/addresses")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath();

                // then
                assertThat(jsonPath.getList("address", String.class)).containsExactly("서울특별시 강남구 역삼동 123-45");
                assertThat(jsonPath.getList("detailed_address", String.class)).containsExactly("역삼타워빌딩 5층 501호");
                assertThat(jsonPath.getList("description", String.class)).containsExactly("회사 주소");
            }
        }
    }

    @Nested
    class 기본_주소록_변경 {

        ExtractableResponse<Response> 첫번째_주소록_등록_요청_응답;
        ExtractableResponse<Response> 두번째_주소록_등록_요청_응답;
        ExtractableResponse<Response> 세번째_주소록_등록_요청_응답;

        @BeforeEach
        void 사전_주소록_등록() {
            Map<String, String> 첫번째_주소록_등록_요청_정보 = new HashMap<>();
            첫번째_주소록_등록_요청_정보.put("address", "서울특별시 강남구 역삼동 123-45");
            첫번째_주소록_등록_요청_정보.put("detailed_address", "역삼타워빌딩 5층 501호");
            첫번째_주소록_등록_요청_정보.put("description", "회사 주소");
            첫번째_주소록_등록_요청_정보.put("is_default", "true");

            첫번째_주소록_등록_요청_응답 = given().log().all()
                    .header("Authorization", 정상적인_회원의_토큰)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(첫번째_주소록_등록_요청_정보)
                    .when()
                    .post("/addresses")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract();

            Map<String, String> 두번째_주소록_등록_요청_정보 = new HashMap<>();
            두번째_주소록_등록_요청_정보.put("address", "대구광역시 중구 삼덕동 123-4");
            두번째_주소록_등록_요청_정보.put("detailed_address", "대구타워 10층 1001호");
            두번째_주소록_등록_요청_정보.put("description", "최근 바뀐 회사 주소");
            두번째_주소록_등록_요청_정보.put("is_default", "true");

            두번째_주소록_등록_요청_응답 = given().log().all()
                    .header("Authorization", 정상적인_회원의_토큰)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(두번째_주소록_등록_요청_정보)
                    .when()
                    .post("/addresses")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract();

            Map<String, String> 세번째_주소록_등록_요청_정보 = new HashMap<>();
            두번째_주소록_등록_요청_정보.put("address", "부산광역시 해운대구 우동 543-21");
            두번째_주소록_등록_요청_정보.put("detailed_address", "해운대 마린시티 3동 1202호");
            두번째_주소록_등록_요청_정보.put("description", "휴가용 숙소");
            두번째_주소록_등록_요청_정보.put("is_default", "true");

            세번째_주소록_등록_요청_응답 = given().log().all()
                    .header("Authorization", 정상적인_회원의_토큰)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(세번째_주소록_등록_요청_정보)
                    .when()
                    .post("/addresses")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract();
        }


        @Nested
        class 성공 {

            /**
             * Given 회원을 생성하고 토큰을 발급한다.
             * And   토큰을 통해 주소록을 여러개 등록한다.
             * When  토큰을 통해 기본 주소록을 변경한다.
             * Then  정상적으로 기본 주소록이 변경된다.
             */
            @Test
            void 기본_주소록_변경_성공() {
                // when
                Map<String, String> 기본_주소록_변경_정보 = new HashMap<>();
                기본_주소록_변경_정보.put("is_default", "true");

                given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(기본_주소록_변경_정보)
                        .when()
                        .put("/address-book/default-address/", getCreatedLocationId(두번째_주소록_등록_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // then
                JsonPath jsonPath = given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/addresses/", getCreatedLocationId(첫번째_주소록_등록_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath();

                assertThat(jsonPath.getList("is_default", String.class)).containsExactly("true");

            }

        }

        @Nested
        class 실패 {

            /**
             * Given 회원을 생성하고 토큰을 발급한다.
             * And   토큰을 통해 주소록을 여러개 등록한다.
             * When  토큰 없이 기본 주소록을 변경한다.
             * Then  기본 주소록이 변경되지 않는다.
             */
            @Test
            void 기본_주소록_변경_실패() {
                // when, then
                Map<String, String> 기본_주소록_변경_정보 = new HashMap<>();
                기본_주소록_변경_정보.put("is_default", "true");

                given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(기본_주소록_변경_정보)
                        .when()
                        .put("/address-book/default-address/", getCreatedLocationId(두번째_주소록_등록_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();
            }
        }
    }
}
