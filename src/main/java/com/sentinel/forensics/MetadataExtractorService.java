package com.sentinel.forensics;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetadataExtractorService {
    private static final Logger log = LoggerFactory.getLogger(MetadataExtractorService.class);

    public record ExtractedMetadata(
            boolean hasExif,
            String cameraMake,
            String cameraModel,
            String software,
            String dateTimeOriginal,
            String gpsCoordinates,
            boolean isModifiedByEditor,
            Map<String, String> rawTags
    ) {}

    public ExtractedMetadata extract(byte[] bytes) {
        Map<String, String> tags = new HashMap<>();
        String cameraMake = null;
        String cameraModel = null;
        String software = null;
        String dateTimeOriginal = null;
        String gpsCoordinates = null;
        boolean hasExif = false;
        boolean isModifiedByEditor = false;

        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    tags.put(directory.getName() + " : " + tag.getTagName(), tag.getDescription());
                }

                if (directory instanceof ExifIFD0Directory exifIFD0) {
                    hasExif = true;
                    if (exifIFD0.containsTag(ExifIFD0Directory.TAG_MAKE)) {
                        cameraMake = exifIFD0.getString(ExifIFD0Directory.TAG_MAKE);
                    }
                    if (exifIFD0.containsTag(ExifIFD0Directory.TAG_MODEL)) {
                        cameraModel = exifIFD0.getString(ExifIFD0Directory.TAG_MODEL);
                    }
                    if (exifIFD0.containsTag(ExifIFD0Directory.TAG_SOFTWARE)) {
                        software = exifIFD0.getString(ExifIFD0Directory.TAG_SOFTWARE);
                        if (software != null && (software.toLowerCase().contains("photoshop")
                                || software.toLowerCase().contains("gimp")
                                || software.toLowerCase().contains("canva")
                                || software.toLowerCase().contains("stable diffusion")
                                || software.toLowerCase().contains("midjourney"))) {
                            isModifiedByEditor = true;
                        }
                    }
                }

                if (directory instanceof ExifSubIFDDirectory subIFD) {
                    hasExif = true;
                    if (subIFD.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                        dateTimeOriginal = subIFD.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    }
                }

                if (directory instanceof GpsDirectory gpsDir) {
                    if (gpsDir.getGeoLocation() != null) {
                        gpsCoordinates = String.format("%.4f°N, %.4f°W",
                                gpsDir.getGeoLocation().getLatitude(),
                                gpsDir.getGeoLocation().getLongitude());
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Metadata extraction skipped or failed: {}", e.getMessage());
        }

        return new ExtractedMetadata(
                hasExif,
                cameraMake,
                cameraModel,
                software,
                dateTimeOriginal,
                gpsCoordinates,
                isModifiedByEditor,
                tags
        );
    }
}

