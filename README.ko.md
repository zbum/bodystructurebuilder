# BodyStructureBuilder

## BodyStructureBuilder란?

BodyStructureBuilder는 [RFC3501](https://tools.ietf.org/html/rfc3501#section-7.4.2) (IMAP 프로토콜)에 따라 BODYSTRUCTURE 응답을 생성하는 Java 라이브러리입니다. MIME 메시지를 파싱하여 IMAP 서버 구현에서 사용할 수 있는 IMAP BODYSTRUCTURE 형식으로 변환합니다.

## 주요 기능

- **RFC3501 준수**: IMAP 프로토콜 사양에 부합하는 BODYSTRUCTURE 응답 생성
- **MIME 지원**: 다양한 MIME 타입 처리:
  - 멀티파트 메시지 (multipart/alternative, multipart/mixed, multipart/report 등)
  - 텍스트 메시지
  - Message/rfc822 (임베디드 메시지)
  - 기타 MIME 미디어 타입
- **확장 기능 지원**: BODYSTRUCTURE 확장 정보 선택적 포함 (MD5, Content-Disposition, Content-Language, Content-Location)
- **간단한 API**: 사용하기 쉬운 정적 유틸리티 메서드
- **효율적**: 스트리밍과 버퍼링을 활용한 성능 최적화

## 요구사항

- Java 11 이상
- Maven 3.x

## 의존성

- Apache MIME4J (0.8.13) - MIME 메시지 파싱
- Jakarta Mail (1.6.8) - 메일 API 지원
- SLF4J (1.7.29) - 로깅 퍼사드
- Lombok (1.18.40) - 코드 생성

## 사용 방법

### Maven 설정

다른 프로젝트에서 이 라이브러리를 사용하려면 `pom.xml`에 다음 repository 설정을 추가하세요:

```xml
<repositories>
    <repository>
        <id>nexus-releases</id>
        <name>Nexus Release Repository</name>
        <url>https://nexus.manty.co.kr/repository/maven-releases/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>nexus-snapshots</id>
        <name>Nexus Snapshot Repository</name>
        <url>https://nexus.manty.co.kr/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

`pom.xml`에 다음 dependency를 추가하세요:

```xml
<dependency>
    <groupId>kr.co.manty.mail.imap</groupId>
    <artifactId>bodystructurebuilder</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 기본 사용법

```java
import kr.co.manty.mail.imap.bodystructure.BodyStructureBuilder;
import java.io.InputStream;

// 입력 스트림에서 MIME 메시지 읽기
InputStream mimeInputStream = ...;

// 확장 정보를 포함하여 BODYSTRUCTURE 생성
byte[] bodyStructure = BodyStructureBuilder.build(mimeInputStream, true);

// 확장 정보 없이 BODYSTRUCTURE 생성
byte[] bodyStructure = BodyStructureBuilder.build(mimeInputStream, false);
```

### 예제

```java
// .eml 파일 읽기
InputStream mimeInputStream = getClass().getResourceAsStream("/eml/test1.eml");

// BODYSTRUCTURE 생성
byte[] bodyStructure = BodyStructureBuilder.build(mimeInputStream, true);

// 결과 사용 (예: IMAP 응답으로 전송)
String bodyStructureString = new String(bodyStructure);
```

## 테스트

```bash
mvn test
```

테스트 스위트에는 다음 예제가 포함되어 있습니다:
- 멀티파트 alternative 메시지
- 멀티파트 mixed 메시지
- 멀티파트 report 메시지

## 프로젝트 구조

```
src/main/java/kr/co/manty/mail/imap/bodystructure/
├── BodyStructureBuilder.java          # 메인 진입점
├── BodyStructureEncoder.java          # 구조를 IMAP 형식으로 인코딩
├── BodyStructureComposer.java         # IMAP 응답 구성
├── MimeDescriptorStructure.java       # 구조 구현체
├── Structure.java                      # 구조 인터페이스
└── mime/                              # MIME 파싱 컴포넌트
```

## 작성자

작성자에 대한 자세한 정보는 다음을 참조하세요: [https://www.manty.co.kr](https://www.manty.co.kr)

## 라이선스

이 프로젝트는 Apache License 2.0에 따라 라이선스됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

Copyright 2024 Manty Co., Ltd.


