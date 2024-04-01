package com.shop.core.address.acceptance;

import com.shop.common.util.AcceptanceTest;
import com.shop.core.address.presentation.dto.AddressRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.core.address.step.AddressSteps.*;
import static com.shop.core.auth.step.AuthSteps.회원생성_후_토큰_발급;
import static com.shop.core.member.fixture.MemberFixture.스미스;

@DisplayName("주소록 관련 인수 테스트")
public class AddressAcceptanceTest extends AcceptanceTest {

    String 정상적인_회원의_토큰;

    @BeforeEach
    void 사전_회원생성_후_토큰_발급() {
        정상적인_회원의_토큰 = 회원생성_후_토큰_발급(스미스);
    }

    @Nested
    class 주소록_등록 {


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
                var 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                var 주소록_등록_요청_응답 = 주소록_등록_요청_토큰_포함(주소록_등록_요청_정보, 정상적인_회원의_토큰);

                // then
                주소록_정보_확인(주소록_등록_요청_정보, 정상적인_회원의_토큰);
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
                    var 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);

                    // when, then
                    주소록_등록_요청_토큰_미포함(주소록_등록_요청_정보);
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
                    var 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                    var 주소록_등록_요청_응답 = 주소록_등록_요청_토큰_포함(주소록_등록_요청_정보, 정상적인_회원의_토큰);

                    // when
                    var 주소록_수정_요청_정보 = AddressRequest.of("대구광역시 중구 삼덕동 123-4", "대구타워 10층 1001호", "최근 바뀐 회사 주소", false);
                    주소록_수정_요청_토큰_포함(주소록_수정_요청_정보, 주소록_등록_요청_응답, 정상적인_회원의_토큰);

                    // then
                    주소록_수정_확인(주소록_수정_요청_정보, 정상적인_회원의_토큰);
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
                    var 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                    var 주소록_등록_요청_응답 = 주소록_등록_요청_토큰_포함(주소록_등록_요청_정보, 정상적인_회원의_토큰);

                    // when
                    var 주소록_수정_요청_정보 = AddressRequest.of("대구광역시 중구 삼덕동 123-4", "대구타워 10층 1001호", "최근 바뀐 회사 주소", true);
                    주소록_수정_요청_토큰_미포함(주소록_수정_요청_정보, 주소록_등록_요청_응답);

                    // then
                    주소록_정보_확인(주소록_등록_요청_정보, 정상적인_회원의_토큰);
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
                AddressRequest 첫번째_주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", false);
                첫번째_주소록_등록_요청_응답 = 주소록_등록_요청_토큰_포함(첫번째_주소록_등록_요청_정보, 정상적인_회원의_토큰);

                AddressRequest 두번째_주소록_등록_요청_정보 = AddressRequest.of("대구광역시 중구 삼덕동 123-4", "대구타워 10층 1001호", "최근 바뀐 회사 주소", false);
                두번째_주소록_등록_요청_응답 = 주소록_등록_요청_토큰_포함(두번째_주소록_등록_요청_정보, 정상적인_회원의_토큰);

                AddressRequest 세번째_주소록_등록_요청_정보 = AddressRequest.of("부산광역시 해운대구 우동 543-21", "해운대 마린시티 3동 1202호", "휴가용 숙소", false);
                세번째_주소록_등록_요청_응답 = 주소록_등록_요청_토큰_포함(세번째_주소록_등록_요청_정보, 정상적인_회원의_토큰);
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
                    기본_주소록_변경_요청_토큰_포함(두번째_주소록_등록_요청_응답, 정상적인_회원의_토큰);

                    // then
                    기본_주소록_확인(두번째_주소록_등록_요청_응답, 정상적인_회원의_토큰);
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
                    // when
                    기본_주소록_변경_요청_토큰_미포함(두번째_주소록_등록_요청_응답);

                    // then
                    기본_주소록_아님_확인(두번째_주소록_등록_요청_응답, 정상적인_회원의_토큰);
                }
            }
        }
    }
}