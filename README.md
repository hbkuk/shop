# Shop

---

## ERD
- [ERD Cloud](https://www.erdcloud.com/d/cpjXZMHGShd2YD6cQ)

## Code Convention

### 객체 지향 생활 체조 원칙

```
- 한 메서드에 오직 한 단계의 들여쓰기만 한다.
- else 예약어를 쓰지 않는다.
- 모든 원시 값과 문자열을 포장한다.
- 한 줄에 점을 하나만 찍는다.
- 줄여 쓰지 않는다(축약 금지).
- 모든 엔티티를 작게 유지한다.
- 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
- 일급 컬렉션을 쓴다.
- getter/setter/프로퍼티를 쓰지 않는다.
```

### 묻지 말고 시켜라(Tell, Don’t Ask)
```
- 밀접하게 연관된 정보와 행동을 함께 가지는 객체를 만들 수 있다.
- 객체의 정보를 이용하는 행동을 객체의 외부가 아닌 내부에 위치시키기 때문에 자연스럽게 정보와 행동을 동일한 클래스 안에 두게 된다.
- 보다 자연스럽게 정보 전문가에게 책임을 할당하게 되고 높은 응집도를 가진 클래스를 얻을 확률이 높아진다.
```

[참고 자료](https://origogi.github.io/oop/%EB%AC%BB%EC%A7%80-%EB%A7%90%EA%B3%A0-%EC%8B%9C%EC%BC%9C%EB%9D%BC/)



## Commit Convention

```
feat : 새로운 기능 추가
Fix : 버그 수정
Docs : 문서 수정
Style : 코드 수정 없이 코드 포맷 변경, 세미콜론 추가
Refactor : 리팩토링
Test : 테스트 코드 추가
Chore : 빌드 업무 및 패키지 매니저 수정
Design : UI 디자인 변경
Rename : 파일명 혹은 폴더명 수정
Remove : 코드 혹은 파일 삭제 
```