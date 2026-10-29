# Sentinel — AI Media Trust Agent (Spring Boot & OpenAI)

> **Can you trust what you see?**  
> Sentinel is an AI-powered media forensics and provenance trust agent built with **Spring Boot 3.3.5** and an **OpenAI agent backend (`gpt-4o`)**. Rather than issuing opaque binary real/fake judgments, Sentinel traces provenance chains, validates C2PA cryptographic signatures, parses hardware EXIF tags, executes Error Level Analysis (ELA), and reports evidence with calibrated uncertainty.

---

## 🌟 Key Capabilities

1. **Spring Boot 3.3.5 Architecture**:
   - Modern Spring MVC REST API and static web engine running on embedded Tomcat.
   - Real-time file upload handling (`MultipartFile` up to 50MB).
   - Java 21/25 native speed and sub-second startup (~580ms).

2. **OpenAI Agent Backend (`gpt-4o`)**:
   - Orchestrates the multi-stage forensic reasoning loop.
   - Analyzes findings across container formats, camera metadata, C2PA certificate authorities, and pixel-level forensic scans.
   - Synthesizes calibrated Bayesian uncertainty, executive summaries, and investigative narratives.
   - Flexible configuration: supports `OPENAI_API_KEY` via environment variable, `application.yml`, or directly from the in-browser settings modal via `X-OpenAI-Key`.
   - **Autonomous Local Agent Fallback**: If no OpenAI API key is supplied, Sentinel automatically switches to its built-in Autonomous Local Forensic Agent so all capabilities run out-of-the-box offline.

3. **5-Stage Investigative Pipeline**:
   - **Stage 1 — Inspect Media**: Decode container, codec, resolution, and compute bitstream SHA-256 hash.
   - **Stage 2 — Examine Metadata**: Uses `com.drewnoakes:metadata-extractor` to parse real hardware EXIF, GPS coordinates, capture dates, and detect editing software (Photoshop, Stable Diffusion, etc.).
   - **Stage 3 — Verify Signatures**: Validates C2PA / Content Credentials manifests, certificate authority trust chains (Associated Press, Adobe, etc.), and checks for bitstream hash tampering.
   - **Stage 4 — Forensic Analysis**: Performs Error Level Analysis (ELA), splice boundary detection, and generative GAN/diffusion pattern detection.
   - **Stage 5 — Report Evidence**: Produces structured JSON evidence with calibrated uncertainty score and categorical verdict (`VERIFIED`, `MODIFIED`, `UNKNOWN`).

4. **Live Interactive UI**:
   - Hero Terminal with interactive CLI runner: test live presets directly with live streaming logs.
   - Live benchmark comparison cards (Signed Original AP Wire, Modified Photoshop copy, Unknown viral social clip).
   - Drag & drop live media upload zone for testing any custom image (JPG, PNG, WebP).
   - Real-time OpenAI configuration modal with instant backend connection testing.

---

## 🚀 Quick Start

### Prerequisites
- Java 21 or Java 25 installed (`java -version`)
- Maven (or use the included `./mvnw` wrapper)

### 1. Run with Maven Wrapper
```bash
./mvnw spring-boot:run
```

Once started, open your browser at:
```
http://localhost:8080/
```

### 2. Configure OpenAI Agent Backend (Optional)
You can configure OpenAI in three ways:

- **Option A (.env File — Recommended for local testing)**:
  Create a `.env` file in the project root:
  ```env
  OPENAI_API_KEY=sk-proj-...
  OPENAI_MODEL=gpt-4o
  ```
  Both `./mvnw` and `SentinelApplication` automatically detect and load `.env`.

- **Option B (Environment Variable)**:
  ```bash
  export OPENAI_API_KEY="sk-proj-..."
  export OPENAI_MODEL="gpt-4o"
  ./mvnw spring-boot:run
  ```

- **Option C (In the Web UI)**:
  Click the **"OpenAI Backend"** button in the top navigation bar, enter your API key, and click **Save & Apply**. It will be saved securely in browser storage and passed in the `X-OpenAI-Key` header with all requests.

- **Option C (`src/main/resources/application.yml`)**:
  ```yaml
  sentinel:
    openai:
      api-key: "sk-proj-..."
      model: "gpt-4o"
  ```

*Note: Without an OpenAI key, Sentinel uses its Autonomous Local Forensic Agent, ensuring complete zero-config usability.*

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/agent/status` | Returns backend status (`OPENAI` or `LOCAL_AGENT`), model name, and configuration health |
| `GET` | `/api/samples` | Lists preset case studies (`verified`, `modified`, `unknown`) |
| `POST` | `/api/investigate/preset/{sampleId}` | Runs the agent investigation pipeline on a preset sample |
| `POST` | `/api/investigate/upload` | Multipart file upload (`file=@image.jpg`) for real-time investigation |
| `POST` | `/api/investigate/custom` | Runs investigation with custom metadata parameters |

### Example cURL Commands

#### Check Agent Backend Status
```bash
curl -s http://localhost:8080/api/agent/status | jq .
```

#### Investigate Preset Case
```bash
curl -s -X POST http://localhost:8080/api/investigate/preset/verified | jq .
```

#### Investigate Uploaded Media File
```bash
curl -s -X POST -F "file=@assets/verified_media.webp" http://localhost:8080/api/investigate/upload | jq .
```

---

## 🧪 Testing & Build

Run the test suite:
```bash
./mvnw test
```

Package production executable JAR:
```bash
./mvnw clean package
java -jar target/sentinel-agent-1.0.0.jar
```

---

## 🏛️ Project Structure

```
gdsc/
├── pom.xml                               # Maven project descriptor (Spring Boot 3.3.5)
├── mvnw                                  # Maven wrapper script
├── README.md                             # Documentation
├── src/
│   ├── main/
│   │   ├── java/com/sentinel/
│   │   │   ├── SentinelApplication.java  # Main application entry point
│   │   │   ├── agent/
│   │   │   │   └── SentinelAgentService.java   # 5-stage agent reasoning orchestrator
│   │   │   ├── config/
│   │   │   │   └── OpenAiConfig.java           # OpenAI settings properties
│   │   │   ├── controller/
│   │   │   │   └── InvestigationController.java # REST API endpoints
│   │   │   ├── dto/
│   │   │   │   ├── AgentStatusResponse.java
│   │   │   │   ├── InvestigationRequest.java
│   │   │   │   └── SampleMetadata.java
│   │   │   ├── forensics/
│   │   │   │   ├── C2PAVerifierService.java    # C2PA signature & manifest validation
│   │   │   │   ├── ForensicAnalyzerService.java # ELA & splice detection
│   │   │   │   ├── MediaInspector.java         # Bitstream & SHA-256 analysis
│   │   │   │   ├── MetadataExtractorService.java# Drew Noakes EXIF extractor
│   │   │   │   └── PresetSamplesService.java   # Benchmark sample library
│   │   │   ├── model/
│   │   │   │   ├── EvidenceStep.java
│   │   │   │   ├── InvestigationReport.java
│   │   │   │   ├── MediaInspectionResult.java
│   │   │   │   └── VerdictType.java            # VERIFIED, MODIFIED, UNKNOWN
│   │   │   └── openai/
│   │   │       └── OpenAiClient.java           # OpenAI chat completion client
│   │   └── resources/
│   │       ├── application.yml                 # Spring Boot application configuration
│   │       └── static/                         # Frontend served directly by Spring Boot
│   │           ├── index.html                  # Upgraded UI with upload & OpenAI controls
│   │           ├── index.css                   # Complete design system & responsive styling
│   │           ├── app.js                      # Live REST API client & terminal runner
│   │           └── assets/                     # Media samples & vector assets
│   └── test/
│       └── java/com/sentinel/
│           └── SentinelApplicationTests.java   # Context & integration tests
```

