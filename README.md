# 1. 프로젝트 기획서
---
## 프로젝트 개요
- **프로젝트명 : 네플릭스마일😊**
- **진행 기간**: 2025.06.12 ~ 2025.06.16
---
# 📌 팀원 구성
| 이나영 | 유지은 | 박준서 | 박창준 | 정승원 |
|--------|--------|--------|--------|--------| 
| ![이나영](https://avatars.githubusercontent.com/u/106491547?v=4) | ![유지은](https://avatars.githubusercontent.com/u/106491548?v=4) | ![박준서](https://avatars.githubusercontent.com/u/106491549?v=4) | ![박창준](https://avatars.githubusercontent.com/u/123456789?v=4) | ![정승원](https://avatars.githubusercontent.com/u/106491551?v=4) |
| [GitHub](https://github.com/NYoungLEE) | [GitHub](https://github.com/yde222) | [GitHub](https://github.com/Berry-mas) | [GitHub](https://github.com/changjunpark13) | [GitHub](https://github.com/dkrio) |
---

## 프로젝트
- 본 프로젝트는 마이크로서비스 아키텍처(MSA) 기반의 영화 리뷰 및 추천 플랫폼이다. 사용자는 영화 정보를 검색·탐색하고, 리뷰와 평점을 남기며, 개인화된 추천 서비스를 경험할 수 있다. 관리자는 영화, 리뷰 등 각종 컨텐츠를 효율적으로 관리할 수 있다.
## 목표 및 범위
- 이 프로젝트의 목표는 스프링 부트 구조를 이해하고, MSA를 통해 확장성과 유지보수성이 뛰어난 
서비스를 구현하는 것이 목표이다. 또한 프론트엔드와의 연동에 익숙해져서 서비스 전반의 흐름을
이해하는 것이다.

## 타겟 사용자
- 사용자(영화 탐색 및 리뷰·추천 이용자)
- 관리자(영화·리뷰·회원 등 데이터 관리)

## 주요 기능 목록
- 회원가입/로그인 (이메일, 소셜 로그인)
- 영화 목록·상세 정보 제공
- 리뷰/평점 작성 및 조회
- 영화 검색/필터링 (제목, 장르, 감독, 배우 등)
- 맞춤형 영화 추천(별점, 선호장르, 인기작 등)
- 관리자 페이지(영화/회원/리뷰 관리, 데이터 통계 등)

## 마이크로서비스(MSA) 구조
- ```config-service``` : 공통 설정 관리
- ```discovery-service``` : 서비스 등록/탐색 (Eureka)
- ```gateway-service``` : API 진입점
- ```user-service``` : 회원가입/로그인
- ```movie-service``` : 영화 목록/상세 정보
- ```review-service``` : 리뷰 작성/조회
- ```search-service``` : 영화 검색/필터
- ```recommend-service``` : 맞춤형 추천 기능





