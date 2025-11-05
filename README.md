````markdown
# 📚 BookMarket (Spring Boot 기반 온라인 서점)

> **Spring Boot + JPA + Thymeleaf + Toss Payments API**  
> 도서 판매, 주문, 결제 기능을 통합한 웹 애플리케이션 프로젝트입니다.

---

## 🚀 프로젝트 개요

**BookMarket**은 사용자가 책을 조회하고 장바구니에 담아 주문 및 결제할 수 있는  
Spring Boot 기반 온라인 서점 웹 애플리케이션입니다.  
**Toss Payments API**를 활용하여 실제 결제 시나리오를 테스트할 수 있습니다.

---

## 🛠️ 주요 기술 스택

| 구분 | 사용 기술 |
|------|------------|
| 백엔드 | Spring Boot 3.x, Spring Data JPA, Hibernate |
| 프론트엔드 | Thymeleaf, HTML5, CSS3, JavaScript |
| 데이터베이스 | MySQL / H2 (테스트 환경) |
| 결제 시스템 | Toss Payments API (테스트 모드) |
| 빌드 도구 | Gradle |
| 배포 환경 | JAR 실행 (로컬 서버 기준) |

---

## 💳 Toss Payments 연동

본 프로젝트에서는 **Toss Payments 테스트 키**를 사용하여 결제 기능을 구현했습니다.  
결제 흐름은 다음과 같습니다:

1. 사용자가 **주문 확인 페이지(orderConfirmation.html)** 에서 결제 버튼 클릭  
2. JavaScript로 **Toss Payments 결제창 호출**  
3. 결제 성공 시 → `/payment/success` 컨트롤러 호출  
4. Spring 서버에서 `toss.secret-key`를 이용해 **결제 승인 요청** (`/v1/payments/confirm`)  
5. 결과를 **성공/실패 페이지**에 표시

### ✅ 예시 코드

**application.properties**
```properties
toss.secret-key=test_sk_************
toss.client-key=test_ck_************
````

**PaymentController.java**

```java
@Value("${toss.secret-key}")
private String tossSecretKey;

@Value("${toss.client-key}")
private String tossClientKey;

@PostMapping("/success")
public String confirmPayment(
        @RequestParam String paymentKey,
        @RequestParam String orderId,
        @RequestParam Long amount,
        Model model) {

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String auth = tossSecretKey + ":";
    String encodedAuth = Base64.getEncoder()
            .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    headers.set("Authorization", "Basic " + encodedAuth);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> payloadMap = Map.of(
        "paymentKey", paymentKey,
        "orderId", orderId,
        "amount", amount
    );

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(payloadMap, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(
            "https://api.tosspayments.com/v1/payments/confirm",
            request,
            String.class
    );

    model.addAttribute("paymentInfo", response.getBody());
    return "payment/success";
}
```

---

## ⚙️ 프로젝트 실행 방법

1. **Git Clone**

   ```bash
   git clone https://github.com/web9622/book_market.git
   cd book_market
   ```

2. **환경 설정**

   * `application.properties`에 DB 정보와 Toss API 키 설정
   * 민감정보(`secret-key`, `client-key`)는 `.gitignore`에 포함되어 GitHub에 업로드되지 않음

3. **빌드 및 실행**

   ```bash
   ./gradlew build
   java -jar build/libs/BookMarket-0.0.1-SNAPSHOT.jar
   ```

4. **접속**

   ```
   http://localhost:20000/BookMarket
   ```

---

## 📂 주요 디렉토리 구조

```
BookMarket/
 ├── src/main/java/com/springboot/
 │    ├── controller/         # 웹 컨트롤러
 │    ├── domain/             # 엔티티 클래스
 │    ├── repository/         # JPA Repository
 │    ├── service/            # 비즈니스 로직
 │    └── BookMarketApplication.java
 │
 ├── src/main/resources/
 │    ├── templates/          # Thymeleaf HTML 템플릿
 │    ├── static/             # 정적 리소스 (CSS/JS/Image)
 │    ├── application.properties
 │    └── logging/log4j2.xml
 │
 ├── build.gradle
 └── README.md
```

---

## 🧩 기타 설정

* `.gitignore`에 민감정보 및 불필요 파일 제외:

  ```
  /build/
  /out/
  /.idea/
  /.gradle/
  *.log
  application.properties
  ```

---

## 🧑‍💻 개발자 정보

* **개발자**: 윤혜정 (Hyejeong Yoon)
* **GitHub**: [web9622](https://github.com/web9622)
* **프로젝트명**: BookMarket
```
