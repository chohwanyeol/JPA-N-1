---

# 🔁 JPA N+1 문제 실험 프로젝트

Spring Boot 기반으로 N+1 문제가 발생하는 상황을 직접 만들어 보고,  
이를 `fetch join`, DTO Projection 방식으로 해결하여  
**쿼리 수와 성능 차이를 비교해본 실험 프로젝트**입니다.

---

## ✅ 프로젝트 정보

- **프로젝트명**: JPA N+1 Test
- **기술스택**: Java 21, Spring Boot 3.4.4, Spring Web, Lombok, Spring Data JPA, H2 Database, Swagger
- **실험 목적**:
  - JPA의 지연 로딩 전략(Lazy Loading)이 N+1 문제를 어떻게 유발하는지 체감
  - `fetch join`, DTO Projection 방식으로 N+1 문제를 해결하는 전략을 실험
  - 단순 쿼리 수 비교를 넘어 JSON 직렬화 크기까지 포함한 전체 응답 성능 분석

---

## 📦 주요 의존성

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-devtools'
implementation 'org.projectlombok:lombok'
runtimeOnly 'com.h2database:h2'
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
```

---

## 🔧 실행 방법

```bash
# 프로젝트 실행
./gradlew bootRun
```

> 실행 후 Swagger 접속:  
> [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧪 실험 API

| Method | Endpoint              | 설명 |
|--------|------------------------|------|
| GET    | `/posts/bench/basic`  | 기본 방식 (N+1 문제 발생) |
| GET    | `/posts/bench/fetch`  | fetch join 적용 |
| GET    | `/posts/bench/dto`    | DTO Projection 방식 적용 |

---

## 🧬 실험 구성 요약

- `Post` ↔ `Comment`: 1:N 관계 설정
- `@PostConstruct`에서 게시글 10개, 각 댓글 5개(총 50개) 자동 삽입
- 지연 로딩(Lazy) 기반으로 N+1 문제 유도
- fetch join과 DTO 방식으로 해결
- JSON 직렬화 크기, 객체 메모리 사용량 등을 포함한 성능 비교 수행

---

## 📊 실험 결과 요약

- **기본 방식**: 쿼리 11개 발생 (게시글 1 + 댓글 10)
- **fetch join** / **DTO** 방식: 쿼리 1개로 줄어듦
- 실제 성능 테스트를 통해 **쿼리 수, JSON 응답 크기, 속도 등 정량적 비교** 수행

---

## 🧠 느낀 점: "JPA는 겉과 속이 다르다."

- 처음엔 쿼리가 1개만 실행되는 줄 알았는데, 실제로는 연관 엔티티 때문에 **N+1 문제가 발생**하고 있었다.
- `fetch join`과 DTO 방식의 적용을 통해 **눈에 띄는 쿼리 수 감소**와 **응답 속도 개선**을 확인했고, 단순히 기능 구현보다 **데이터 조회 전략이 성능에 얼마나 영향을 미치는지 체감**했다.
- 특히, `fetch join`을 사용한 뒤 쿼리가 1개로 줄어드는 것을 보며 속이 시원했다.
- 일반적으로 DTO 방식이 전송량이 더 적다고 알려져 있지만, 이번 실험에선 평평한 구조로 인한 **중복 필드** 때문에 오히려 `fetch join`보다 JSON 크기가 더 크게 나온 것 같다.
- 즉, **DTO가 항상 가볍다는 건 아니며**, 구조와 목적에 따라 오히려 비효율적일 수 있어 DTO를 알맞게 설계해야 할 필요성을 느꼈다.
- 앞으로 JPA를 사용할 때는 단순히 작동만 하는 코드를 짜기보다는, **내부에서 쿼리가 몇 번 날아가는지 항상 염두에 둘 것**이다.

---
