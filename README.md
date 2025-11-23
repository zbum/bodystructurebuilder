# BodyStructureBuilder

## What is BodyStructureBuilder?

BodyStructureBuilder is a Java library that builds BODYSTRUCTURE responses according to [RFC3501](https://tools.ietf.org/html/rfc3501#section-7.4.2) (IMAP protocol). It parses MIME messages and generates the corresponding IMAP BODYSTRUCTURE format that can be used in IMAP server implementations.

## Features

- **RFC3501 Compliant**: Generates BODYSTRUCTURE responses that conform to the IMAP protocol specification
- **MIME Support**: Handles various MIME types including:
  - Multipart messages (multipart/alternative, multipart/mixed, multipart/report, etc.)
  - Text messages
  - Message/rfc822 (embedded messages)
  - Other MIME media types
- **Extension Support**: Optional inclusion of BODYSTRUCTURE extensions (MD5, Content-Disposition, Content-Language, Content-Location)
- **Simple API**: Easy-to-use static utility method
- **Efficient**: Built with performance in mind using streaming and buffering

## Requirements

- Java 11 or higher
- Maven 3.x

## Dependencies

- Apache MIME4J (0.8.13) - MIME message parsing
- Jakarta Mail (1.6.8) - Mail API support
- SLF4J (1.7.29) - Logging facade
- Lombok (1.18.40) - Code generation

## Usage

### Maven Configuration

Add the following repository configuration to your `pom.xml`:

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

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>kr.co.manty.mail.imap</groupId>
    <artifactId>bodystructurebuilder</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Basic Usage

```java
import kr.co.manty.mail.imap.bodystructure.BodyStructureBuilder;
import java.io.InputStream;

// Read MIME message from input stream
InputStream mimeInputStream = ...;

// Build BODYSTRUCTURE with extensions
byte[] bodyStructure = BodyStructureBuilder.build(mimeInputStream, true);

// Build BODYSTRUCTURE without extensions
byte[] bodyStructure = BodyStructureBuilder.build(mimeInputStream, false);
```

### Example

```java
// Read an .eml file
InputStream mimeInputStream = getClass().getResourceAsStream("/eml/test1.eml");

// Generate BODYSTRUCTURE
byte[] bodyStructure = BodyStructureBuilder.build(mimeInputStream, true);

// Use the result (e.g., send as IMAP response)
String bodyStructureString = new String(bodyStructure);
```

## Testing

```bash
mvn test
```

The test suite includes examples for:
- Multipart alternative messages
- Multipart mixed messages
- Multipart report messages

## Project Structure

```
src/main/java/kr/co/manty/mail/imap/bodystructure/
├── BodyStructureBuilder.java          # Main entry point
├── BodyStructureEncoder.java          # Encodes structure to IMAP format
├── BodyStructureComposer.java         # Composes IMAP response
├── MimeDescriptorStructure.java       # Structure implementation
├── Structure.java                      # Structure interface
└── mime/                              # MIME parsing components
```

## Author

For more information about the author, visit: [https://www.manty.co.kr](https://www.manty.co.kr)

## License

[Add your license information here]
