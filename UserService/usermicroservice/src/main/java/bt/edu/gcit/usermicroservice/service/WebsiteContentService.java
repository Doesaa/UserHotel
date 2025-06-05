package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.WebsiteContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface WebsiteContentService {
    WebsiteContent saveContent(WebsiteContent content, MultipartFile heroBannerFile, MultipartFile footerBannerFile) throws IOException;
    List<WebsiteContent> getAllContents();
    WebsiteContent getContentById(Long id);
    void deleteContent(Long id);
    void uploadImage(Long id, MultipartFile imageFile) throws IOException;
    WebsiteContent updateWebsiteContent(Long id, MultipartFile heroBannerFile, MultipartFile footerBannerFile, String contactUs, String aboutUs) throws IOException;

    String saveImage(MultipartFile imageFile) throws IOException;

}
