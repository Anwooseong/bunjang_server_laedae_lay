## 230304 개발일지
1. 기획서
  + 홈화면은 최근 본 상품과 비슷해요 부분과 최근 본 상품 등 대신에 카테고리 인기 상품 보여 주는 것으로 대체
2. EC2 서버 구축
3. dev/prod 도메인 구축
4. 프로젝트 RDS 연동
5. ERD 설계(50%)

## 230305 개발일지
1. 명세서 1주차 범위 구현
  + 홈화면은 최근 본 상품과 비슷해요 부분과 최근 본 상품 등 대신에 카테고리 인기 상품 보여 주는 것으로 대체
2. ERD 설계(100%)
3. 배너 이미지 리스트 API
4. 본인인증 API


## 230306 개발일지
* 서버 잘못건드려서 EC2, SSL 재설정(prod 재설정 중 80포트에만 SSL적용이 돼어 proxy_pass를 통해 재설정)
![9001-prod](https://user-images.githubusercontent.com/84388081/222973065-ce022b1d-c8da-4edc-bcd1-9e6021701023.png)
![test/log/](https://user-images.githubusercontent.com/84388081/222973151-a6be3cfe-bb3d-4362-aeb8-f62bdef9a185.png)
* api리스트업 10% -> 오늘 중으로 완료 예정
* 더미데이터 아이템 넣는 중 -> 오늘 중으로 완료 예정
* 클라이언트 개발자분들한테 이미지 압축파일 받음
* ERD user_id -> store_id로 일부 수정
* 1주차 피드백
  + API 명세서 피드백
    - 본인인증 로그인을 회원가입으로 바꾸고 로그인 API 필요(문자전송)
    - 회원 앱이기 때문에 헤더에 jwt를 다 넣도록 하자.
    - 인덱스 2번과 3번 같은 경우에 uri에 home을 넣지 않아도 됨
    - /app/categories/sub/:categoryId -> 이거는 /app/categories/:categoryId/subs 이런식으로 해야 REST API 다운 URI이고, sub 등 uri는 복수형을 주로 사용하자.
    - /app/products/d/:productId -> /app/products/:productId/status 이렇게 해야 한눈에 알아볼 수 있고 REST API로 볼 수 있다.(아래도 마찬가지)
    - /app/products/m/:productId -> /app/products/:productId
    - /app/products/similar/:productId -> /app/products/:productId/similar
    - /app/users/d -> /app/users/status
    - /app/products/search= -> /app/products?search= 쿼리스트링이기 때문에 ?로 바꾸자.
    - Result Code가 적다.(정규화한 에러코드나 데이터베이스 암호화 이런것들도 추가해야함)
* 피드백 반영 후 ERD 재설계 완료, REST API설계 완료(추가 구현 필요)


