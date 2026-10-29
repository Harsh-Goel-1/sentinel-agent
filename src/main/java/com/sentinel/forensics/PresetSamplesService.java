package com.sentinel.forensics;

import com.sentinel.dto.SampleMetadata;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresetSamplesService {

    private final ResourceLoader resourceLoader;
    private final Map<String, SampleMetadata> samples = new ConcurrentHashMap<>();

    public PresetSamplesService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        initSamples();
    }

    private void initSamples() {
        samples.put("verified", new SampleMetadata(
                "verified",
                "Press Conference — AP Wire",
                "press_conference_ap.jpg",
                "/assets/verified_media.webp",
                "VERIFIED",
                "Signed original press photo with authentic Associated Press C2PA Content Credentials and matching EXIF."
        ));

        samples.put("modified", new SampleMetadata(
                "modified",
                "Press Conference — Edited",
                "press_conference_edited.jpg",
                "/assets/modified_media.svg",
                "MODIFIED",
                "Modified copy with broken C2PA hash verification, Adobe Photoshop editing metadata, and ELA splice anomalies."
        ));

        samples.put("unknown", new SampleMetadata(
                "unknown",
                "Press Conference — Unknown Source",
                "viral_clip_unknown.jpg",
                "/assets/unknown_media.svg",
                "UNKNOWN",
                "Viral social media repost with stripped EXIF and absent C2PA manifest. Absence of proof is not proof of deepfake."
        ));
    }

    public List<SampleMetadata> listSamples() {
        return new ArrayList<>(samples.values());
    }

    public SampleMetadata getSample(String id) {
        return samples.get(id);
    }

    public byte[] getSampleBytes(String id) {
        SampleMetadata meta = samples.get(id);
        if (meta == null) return new byte[0];

        try {
            Resource resource = resourceLoader.getResource("classpath:static" + meta.getPreviewImage());
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return is.readAllBytes();
                }
            }
        } catch (Exception ignored) {
        }
        return ("SAMPLE_MEDIA_PAYLOAD_FOR_" + id).getBytes();
    }
}

