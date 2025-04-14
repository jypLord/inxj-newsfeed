# INXJ: NEWSFEED
![Image](https://github.com/user-attachments/assets/57143a17-a221-4e0e-943d-977e60a68efd)
<br><br>

## 📝 소개
> **📰 SNS 기반 뉴스피드 프로젝트**  
> *페이스북, 인스타그램*과 같은 형태의 SNS 플랫폼
<br>

### ✅ 주요 기능
- **사용자, 게시글, 댓글의 CRUD** 기능을 지원합니다.
- 게시글과 댓글에 **좋아요(❤️)** 를 남길 수 있습니다. 
- 사용자는 다른 사용자에게 **친구 요청을 보내고, 상대방이 수락하면**하여 **친구 목록**에 추가할 수 있으며,  
  언제든지 친구 목록에서 **삭제**할 수 있습니다.

<br><br>

## 목차
- [🙋‍♀️ INXJ](#-inxj)
- [📚개발 규칙](#개발-규칙)
- [⚙ 기술 스택](#-기술-스택)
- [📂 프로젝트 구조](#-프로젝트-구조)
- [📊 ERD](#-erd)
- [🛠 와이어 프레임](#-와이어-프레임)
- [🚀 기능](#-기능)
- [🗂️ APIs](#️-apis)
- [⚠️ 트러블 슈팅](#-트러블-슈팅)

<br><br>

## 🙋‍♀️ INXJ

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/0ceb06cb-3857-4c8f-85a8-cdb096b236b2" width="100"></td>
    <td><img src="https://github.com/user-attachments/assets/c735f29f-5b4d-426a-9848-5b9a7ea99383" width="100"></td>
    <td><img src="https://github.com/user-attachments/assets/e36d8960-d138-48af-8640-8d4969ad6a6c" width="100"></td>
    <td><img src="https://github.com/user-attachments/assets/5fb947bd-d58f-488b-b0c8-7f46e945457c" width="100"></td>
    <td><img src="https://github.com/user-attachments/assets/8e6252d5-8895-4f5c-a75a-75bcd537e70f" width="100"></td>
  </tr>
  <tr>
    <td><strong>팀장</strong><br>박준우<br><a href="https://github.com/jypLord" target="_blank">@jypLord</a></td>
    <td><strong>팀원</strong><br>김채진<br><a href="https://github.com/kmchaejin" target="_blank">@kmchaejin</a></td>
    <td><strong>팀원</strong><br>윤예진<br><a href="https://github.com/YejinY00n" target="_blank">@YejinY00n</a></td>
    <td><strong>팀원</strong><br>임경수<br><a href="https://github.com/rabitis99" target="_blank">@rabitis99</a></td>
    <td><strong>팀원</strong><br>임지윤<br><a href="https://github.com/jiyun-im-dev" target="_blank">@jiyun-im-dev</a></td>
  </tr>
</table>

## 👥 역할 분담

### 🧑‍💻 박준우
- 유저 도메인 (CRUD, 로그인)
- 비밀번호 인코딩

### 🧑‍💻 김채진
- 친구 도메인

### 🧑‍💻 윤예진
- 게시글 도메인
- 카테고리 도메인

### 🧑‍💻 임경수
- 댓글 도메인
- ExceptionHandler 구현
- 이메일 인증 기능 구현

### 🧑‍💻 임지윤
- 로그인 필터
- 좋아요 도메인

<br><br>

## 📚 **개발 규칙**
### ⏱️ 고민은 30분까지
- 해결되지 않는 부분은 **30분까지만 고민**하고, **바로 팀원에게 질문**하기

---

### 📝 **Commit 컨벤션**
- **한글**: `영어`
- **본문**: `한글`
- 예시) 기능: 댓글 조회 기능 추가

---

### 🔀 **PR(Pull Request) 규칙**
- **모든 PR은 최소 2명 이상**의 **리뷰 승인** 필요
- 리뷰어 지정

---

### 🌿 **브랜치 전략**
- `main` : ✅ **최종 배포** 브랜치
- `develop` : 🔧 **개발 통합** 브랜치
- `feat/#issue-number` : 🧩 **기능 개발용** 브랜치  
(ex. `feat/#12`)

> 📌 **Git issue 연동**  
> 커밋 메시지 또는 PR 설명에 이슈 번호 포함하기 (`#12` 등)

<br><br>

## ⚙ 기술 스택

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">

<br><br>

## 📂 프로젝트 구조
```bas      
├─📁 newsfeed
            └─ domain
                   ├─💬comment
                   │  ├─controller, dto, entity, repository, service
                   ├─🔧common
                   │  ├─annotation, config, dto, entity, enums, filter, util
                   ├─⚠️exception 
                   ├─⚠️exceptionHandler
                   ├─🤝friend
                   │  ├─controller, dto, entity, repository, service
                   ├─❤️like
                   │  ├─controller, entity, repository, service
                   ├─✏️post
                   │  ├─controller, converter, dto, entity, initializer, repository, service
                   └─👤user
                      ├─controller, entity, repository, service
                      ├─dto
                         ├─request
                         └─response
```
<br><br>

## 📊 ERD
![Image](https://github.com/user-attachments/assets/fca98190-482a-4731-a400-619472e58220)
<br><br>

## 🛠 와이어 프레임
![figma](https://velog.velcdn.com/images/jiyun_im/post/a680b3fe-80f2-41f6-8ff3-74a354749477/image.png)

#### [🔗MockUp](https://www.figma.com/proto/yrCjbgRATpaDL1ohmQ94vn/INXJ-15%EC%A1%B0----%EB%89%B4%EC%8A%A4%ED%94%BC%EB%93%9C?node-id=13-2&t=FoeWFYWwnoYPTZeL-1)

<br><br>

## 🚀 기능 소개

- **회원 가입/로그인**
  - 이메일을 통한 인증
- **게시글 작성**
  - 카테고리 지정
  - 공개 범위 설정
- **댓글 작성**
- **게시글/댓글 좋아요**
- **친구 추가**

### 👤 회원
- 회원가입 및 로그인
- 회원  정보 조회
- 회원 정보 수정
- 회원 탈퇴

### 🧑‍🤝‍🧑 친구
- 친구 요청 보내기 / 수락 / 거절
- 친구 목록 확인

### 📝 게시글
- 새 게시글 작성
- 게시글 목록 조회
  - 조건 검색: 카테고리 별, 전체/친구 공개
- 게시글 수정 / 삭제

### 💬 댓글
- 댓글 작성 / 수정 / 삭제
- 댓글 목록 확인

### ❤️ 좋아요
- 게시글에 좋아요 추가 및 취소
- 댓글에 좋아요 추가 및 취소

<br><br>

## 🗂️ APIs
[🔗API Docs](https://quill-hourglass-d62.notion.site/ebd/1d3c428f990a80d7bf15da2afc66d306)

<br><br>

## ⚠️ 트러블 슈팅
### Github merge 충돌
**📌 상황**  
develop 브랜치가 아닌 main 브랜치에 PR이 merge 됨.
<br>

**🔧 해결**  
1️⃣ 롤백
- 브랜치 롤백 고려하였으나, 원격 브랜치 충돌이 없었고 프로젝트 초기 설정이었으므로 그대로 진행
2️⃣✅ 대신 main 브랜치를 **develop 브랜치로 merge**하여 **최신 상태**로 만듦
<br><br>

### 양방향 체크
**📌 상황**  
- 친구 요청에서 user1, user2가 서로 receiver, requester 가 될 수 있는 연관관계
➡️ 양방향 조회 필요
<br>

**🔧 해결**  
✅ JPQL 로 직접 쿼리를 작성하는 것이 효율적
<br>

**💡 JPQL 이란?**
**엔티티**를 기준으로 **객체지향적**으로 쿼리를 작성하게 하는 도구
<br>

**🛠️ 구현 방식**
- `@Query` 어노테이션을 통해 직접 쿼리를 작성
- 테이블 명, 컬럼명이 아닌 엔티티 명과 필드 명 사용 가능
- 실제 테이블 명과 컬럼명 사용하고 싶을 시 `@Query(nativeQuery=true)`로 설정
<br><br>

### 일대다(1:N) 연관관계 최적화
**📌 상황**  
게시물에 달린 댓글(Comment)에 대해 권한 검사를 하는 로직
```java
if (!userId.equals(comment.getUser().getId())&&!userId.equals(comment.getPost().getUser().getId())) {
    throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ID);
}
```
이때 `comment.getUser()`나 `comment.getPost().getUser()`가 **Lazy Loading** 상태라면 각각 호출될 때 마다 추가 쿼리가 실행됨
<br>

즉 하나의 댓글을 불러오더라도
<br>

`comment.getUser()` → 추가 쿼리 1회
`comment.getPost().getUser()` → 추가 쿼리 1회
➡️ **총 3개의 쿼리 발생** (Comment, User, PostUser)
<br>

**🔧 해결**  
✅ **fetch join**을 통해 관련 엔티티들을 한 번에 조회하도록 Repository 수정
<br>

**📈 결과**
- DB에 불필요한 쿼리 요청이 줄어들어 성능 향상
- N+1 문제 예방
- 엔티티 접근 시 LazyLoading으로 인한 예외 가능성 제거 (LazyInitializationException 방지)
- 전체 로직의 응답속도 향상
<br><br>

### Category 테이블 관리 전략
**📌 상황**  
- `Enum Category`를 **룩업 테이블**로 분리하여 DB에서 관리.
- 정적이고 자주 참조되는 값을 DB 테이블로 만들어 유연한 관리와 연관관계 설정을 용이하게 함
<br>

**❗ 문제**
- 상수처럼 사용될 값이므로 **초기값**을 DB에 어떻게 넣을지 결정이 필요.
<br>

**🔧 해결**
✅ 어플리케이션 실행 시점에 @PostConstruct를 사용하여 자동 초기화.

- 코드와 DB 동기화 쉬움
- 클라이언트 요청 시 **enum 기반 검증** 가능
- 코드 내에서도 **직관적 사용** 가능
<br>

**🛠️ 구현 방식**
- `@Component` + `@PostConstruct` 조합으로 **CategoryInitializer** 생성
- 애플리케이션 시작 시 enum 값 기준으로 DB에 값 삽입
<br>

**✅ 결론**
- enum보다 확장성/유연성 향상
- 코드와 DB의 불일치 방지
- 새로운 값 필요 시 enum에 추가만 하면 자동 반영됨
<br><br>
