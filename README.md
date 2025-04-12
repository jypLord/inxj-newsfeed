# INXJ: NEWSFEED
이미지

## 📝 소개
> **📰 SNS 기반 뉴스피드 프로젝트**  
> *페이스북, 인스타그램*과 같은 형태의 SNS 플랫폼
<br>

### ✅ 주요 기능
- **사용자, 게시글, 댓글의 CRUD** 기능을 지원합니다.
- 게시글과 댓글에 **좋아요(❤️)** 를 남길 수 있습니다. 
- 사용자는 다른 사용자를 **팔로우**하여 친구 목록에 추가할 수 있으며,  
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
- [⚠️ 트러블 슈팅](#-트러블-슈팅

<br><br>

## 🙋‍♀️ INXJ
팀원 팀소개
역할분담
<br><br>

## 📚 **개발 규칙**
### ⏱️ 고민은 30분까지
- 해결되지 않는 부분은 **30분까지만 고민**하고, **바로 팀원에게 질문**하기

---

### 📝 **Commit 컨벤션**
- **헤더**: `영어`
- **본문**: `한글`
- 예시) Feat: 댓글 조회 기능 추가

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
```bash
├─main
 └─java
     └─ inxj
         └─ 📁 newsfeed
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

#### [🔗ProtoType](https://www.figma.com/proto/yrCjbgRATpaDL1ohmQ94vn/INXJ-15%EC%A1%B0----%EB%89%B4%EC%8A%A4%ED%94%BC%EB%93%9C?node-id=13-2&t=FoeWFYWwnoYPTZeL-1)

<br><br>

## 🚀 기능 소개

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

<br><br>
