package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.WebsiteContent;
import bt.edu.gcit.usermicroservice.service.WebsiteContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.util.StringUtils;
import com.thoughtworks.xstream.io.path.Path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/website-content")
public class WebsiteContentRestController {

    private final WebsiteContentService websiteContentService;

    @Autowired
    public WebsiteContentRestController(WebsiteContentService websiteContentService) {
        this.websiteContentService = websiteContentService;
    }

@PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> uploadMultipleContent(
        @RequestPart(value = "heroBanner", required = false) MultipartFile heroBannerFile,
        @RequestPart(value = "footerBanner", required = false) MultipartFile footerBannerFile,
        @RequestPart(value = "contactUs", required = false) String contactUs,
        @RequestPart(value = "aboutUs", required = false) String aboutUs
) throws IOException {
    
    WebsiteContent content = new WebsiteContent();
    if (contactUs != null) content.setContactUs(contactUs);
    if (aboutUs != null) content.setAboutUs(aboutUs);
    
    WebsiteContent saved = websiteContentService.saveContent(content, heroBannerFile, footerBannerFile);
    return ResponseEntity.ok(saved);
}



   @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateWebsiteContent(
            @PathVariable Long id,
            @RequestPart(value = "contactUs", required = false) String contactUs,
            @RequestPart(value = "aboutUs", required = false) String aboutUs,
            @RequestPart(value = "heroBanner", required = false) MultipartFile heroBannerFile,
            @RequestPart(value = "footerBanner", required = false) MultipartFile footerBannerFile
    ) {
        try {
            WebsiteContent content = websiteContentService.getContentById(id);

            if (contactUs != null) {
                content.setContactUs(contactUs);
            }
            if (aboutUs != null) {
                content.setAboutUs(aboutUs);
            }
            if (heroBannerFile != null && !heroBannerFile.isEmpty()) {
                String heroBannerPath = websiteContentService.saveImage(heroBannerFile);
                content.setHeroBanner(heroBannerPath);
            }
            if (footerBannerFile != null && !footerBannerFile.isEmpty()) {
                String footerBannerPath = websiteContentService.saveImage(footerBannerFile);
                content.setFooterBanner(footerBannerPath);
            }

            WebsiteContent updated = websiteContentService.saveContent(content, footerBannerFile, heroBannerFile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
        }
    }
 

    @GetMapping
    public ResponseEntity<List<WebsiteContent>> getAllContents() {
        return ResponseEntity.ok(websiteContentService.getAllContents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebsiteContent> getContentById(@PathVariable Long id) {
        return ResponseEntity.ok(websiteContentService.getContentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        websiteContentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id,
                                            @RequestParam("image") MultipartFile imageFile) throws IOException {
        websiteContentService.uploadImage(id, imageFile);
        return ResponseEntity.ok().build();
    }

   
}
