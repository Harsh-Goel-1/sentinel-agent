package com.sentinel.controller;

import com.sentinel.agent.SentinelAgentService;
import com.sentinel.dto.AgentStatusResponse;
import com.sentinel.dto.InvestigationRequest;
import com.sentinel.dto.SampleMetadata;
import com.sentinel.forensics.PresetSamplesService;
import com.sentinel.model.InvestigationReport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InvestigationController {

    private final SentinelAgentService agentService;
    private final PresetSamplesService presetSamplesService;

    public InvestigationController(SentinelAgentService agentService, PresetSamplesService presetSamplesService) {
        this.agentService = agentService;
        this.presetSamplesService = presetSamplesService;
    }

    @GetMapping("/agent/status")
    public ResponseEntity<AgentStatusResponse> getAgentStatus(
            @RequestHeader(value = "X-OpenAI-Key", required = false) String apiKeyHeader) {
        return ResponseEntity.ok(agentService.getStatus(apiKeyHeader));
    }

    @GetMapping("/samples")
    public ResponseEntity<List<SampleMetadata>> listSamples() {
        return ResponseEntity.ok(presetSamplesService.listSamples());
    }

    @PostMapping("/investigate/preset/{sampleId}")
    public ResponseEntity<InvestigationReport> investigatePreset(
            @PathVariable("sampleId") String sampleId,
            @RequestHeader(value = "X-OpenAI-Key", required = false) String apiKeyHeader) {
        InvestigationReport report = agentService.investigatePreset(sampleId, apiKeyHeader);
        return ResponseEntity.ok(report);
    }

    @PostMapping(value = "/investigate/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvestigationReport> investigateUpload(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-OpenAI-Key", required = false) String apiKeyHeader) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        InvestigationReport report = agentService.investigateFile(
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType(),
                apiKeyHeader
        );
        return ResponseEntity.ok(report);
    }

    @PostMapping("/investigate/custom")
    public ResponseEntity<InvestigationReport> investigateCustom(
            @RequestBody InvestigationRequest request,
            @RequestHeader(value = "X-OpenAI-Key", required = false) String apiKeyHeader) {
        if (request.getSampleId() != null && !request.getSampleId().isBlank()) {
            return ResponseEntity.ok(agentService.investigatePreset(request.getSampleId(), apiKeyHeader));
        }
        String filename = request.getCustomFilename() != null ? request.getCustomFilename() : "custom_media.mp4";
        byte[] dummyBytes = ("SENTINEL_CUSTOM_INVESTIGATION_PAYLOAD_" + filename).getBytes();
        InvestigationReport report = agentService.investigateFile(
                dummyBytes,
                filename,
                request.getFileType() != null ? request.getFileType() : "video/mp4",
                apiKeyHeader
        );
        return ResponseEntity.ok(report);
    }
}

