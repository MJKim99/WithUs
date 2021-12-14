# WithUs
## 크라우드 펀딩 웹 사이트

펀딩이란 판매자가 제품 기획을 하고, 기획된 제품에 소비자가 펀딩을 하여 목표한 펀딩 금액 달성 시 모인 펀딩 금액으로 제품을 기획하고 출시하는 것이다.

[프로젝트 발표 영상](https://www.youtube.com/watch?v=nrJnLfMdHFo&list=PLedGoSru794850VQuzA4qUV1j_cN71WTe)

<br>

### 프로젝트 상세 설명
메인 페이지(사용자 페이지), 판매자 페이지, 관리자 페이지 세 가지의 페이지로 구성되어 있다.

**관리자 페이지**에서는 사용자 정보와 펀딩 프로젝트, 공지사항 및 이벤트 글을 관리한다. 이때 데이터 수가 비교적 많은 사용자 관리 부분에서는 REST API를 활용하여 직관적으로 사용자 정보를 알기 쉽게 하였다.

**판매자 페이지**에서는 자신이 생성한 펀딩 프로젝트를 관리할 수 있다. 또한 자신이 생성한 펀딩 프로젝트에 남겨진 문의 글을 확인하고, 답변을 달 수 있다.
관리자 페이지와 판매자 페이지에서는 Chart.js를 활용하여 전년대비 매출 비교, 카테고리 점유율, 펀딩된 금액 등을 총 망라하여 직관적으로 쉽게 볼 수 있게 하였다.

**메인 페이지**에서는 사용자들이 펀딩 예정 혹은 펀딩 진행 중인 펀딩 프로젝트 목록들을 열람할 수 있고, 펀딩 진행 중인 프로젝트에 펀딩을 할 수 있다. 이때 결제 API를 활용하여 간편하게 카카오페이로 결제할 수 있다. 좋아요 기능을 통해 자신이 관심 있는 프로젝트를 체크할 수 있고, 문의 사항이 있는 경우 문의 글을 남길 수 있다. 그리고 관리자가 작성한 공지사항 및 이벤트 글을 열람할 수 있다.

사용자 로그인은 웹 사이트 자체 로그인과 Google, Naver, Kakao 계정을 이용하여 로그인이 가능하다. 사용자 마이 페이지에서는 펀딩한 프로젝트와 프로젝트 관련하여 문의한 글을 열람할 수 있다. 문의 글에 답변이 달리는 경우 이 또한 열람 가능하다. 마이 페이지에서 판매자 등록이 가능하고, 회원 정보 수정 및 회원 탈퇴를 할 수 있다.


<br>

## Development Environment & Tools
- Backend : Java 11, Apache Tomcat 9.0, Mysql 8, Ajax, JSP, JSON, Spring 4, Spring MVC, Spring Boot, Spring Security, JDBC, Mybatis, JPA
- Frontend : HTML5, CSS3, JavaScript, JQuery, JSTL, Bootstrap, Tailwind CSS
- Flow Chart : Draw.io, Kakao oven
- Editors : Eclipse 2021-06, Visual Studio Code
- management : GitHub

<br>

## API · Library
- Google Login API
- Kakao Login API
- Naver Login API
- 스마트 택배 API
- iamport API
- 도로명주소 API
- Chart.js
- CKEditor4
- Lombok

<br>

## Implement
- User
  - Login
  - funding
  - Like
- Seller
- Admin

<br>

## Database (ERD)

![image](https://user-images.githubusercontent.com/81893393/137744824-1cbbc560-d4ec-4982-993d-fe5b9880ad62.png)

<br>

# Information architecture
- User
![image](https://user-images.githubusercontent.com/81893393/137752604-9a477a27-c30c-4202-9b1e-8701deb9f96d.png)

<br>

- Seller
![image](https://user-images.githubusercontent.com/81893393/137752613-4a81da5e-aabe-4ce3-b60e-491385c4a469.png)

<br>

- Admin
![image](https://user-images.githubusercontent.com/81893393/137752630-2a7b8393-f740-49ec-802b-e90094cb6fde.png)


