package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.WebsiteContentDAO;
import bt.edu.gcit.usermicroservice.entity.WebsiteContent;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class WebsiteContentServiceImpl implements WebsiteContentService {

    private final WebsiteContentDAO websiteContentDAO;
    private final String uploadDir = "src/main/resources/static/images/website_content";

    @Autowired
    public WebsiteContentServiceImpl(WebsiteContentDAO websiteContentDAO) {
        this.websiteContentDAO = websiteContentDAO;
    }

    @Override
    @Transactional
    public WebsiteContent saveContent(WebsiteContent content, MultipartFile heroBannerFile, MultipartFile footerBannerFile) throws IOException {
        if (heroBannerFile != null && !heroBannerFile.isEmpty()) {
            String heroFilename = saveImageToFileSystem(heroBannerFile);
            content.setHeroBanner(heroFilename);
        }
        if (footerBannerFile != null && !footerBannerFile.isEmpty()) {
            String footerFilename = saveImageToFileSystem(footerBannerFile);
            content.setFooterBanner(footerFilename);
        }
        return websiteContentDAO.save(content);
    }

    @Override
    @Transactional
    public WebsiteContent updateWebsiteContent(Long id, MultipartFile heroBannerFile, MultipartFile footerBannerFile, String contactUs, String aboutUs) throws IOException {
        WebsiteContent existing = websiteContentDAO.findByID(id);
        if (existing == null) {
            throw new RuntimeException("Content not found with id: " + id);
        }

        if (contactUs != null) {
            existing.setContactUs(contactUs);
        }
        if (aboutUs != null) {
            existing.setAboutUs(aboutUs);
        }
        if (heroBannerFile != null && !heroBannerFile.isEmpty()) {
            String heroFilename = saveImageToFileSystem(heroBannerFile);
            existing.setHeroBanner(heroFilename);
        }
        if (footerBannerFile != null && !footerBannerFile.isEmpty()) {
            String footerFilename = saveImageToFileSystem(footerBannerFile);
            existing.setFooterBanner(footerFilename);
        }

        return websiteContentDAO.save(existing);
    }

    @Override
    public List<WebsiteContent> getAllContents() {
        return websiteContentDAO.getAllContents();
    }

    @Override
    public WebsiteContent getContentById(Long id) {
        Optional<WebsiteContent> optional = Optional.ofNullable(websiteContentDAO.findByID(id));
        return optional.orElseThrow(() -> new RuntimeException("Content not found"));
    }

    @Override
    @Transactional
    public void deleteContent(Long id) {
        websiteContentDAO.deleteById(id);
    }

    @Override
    public void uploadImage(Long id, MultipartFile imageFile) throws IOException {
        WebsiteContent content = getContentById(id);

        if (imageFile == null || imageFile.isEmpty()) {
            return;
        }
        if (imageFile.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB");
        }

        String filename = saveImageToFileSystem(imageFile);
        content.setHeroBanner(filename); // Or footerBanner depending on context
        websiteContentDAO.save(content);
    }

    private String saveImageToFileSystem(MultipartFile imageFile) throws IOException {
        String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String filename = baseName + "_" + System.currentTimeMillis() + "." + extension;

        Path uploadPathDir = Paths.get(uploadDir);
        if (!Files.exists(uploadPathDir)) {
            Files.createDirectories(uploadPathDir);
        }

        Path destination = uploadPathDir.resolve(filename);
        imageFile.transferTo(destination);

        return filename;
    }
    @Override
    public String saveImage(MultipartFile imageFile) throws IOException {
        return saveImageToFileSystem(imageFile);
    }

}
