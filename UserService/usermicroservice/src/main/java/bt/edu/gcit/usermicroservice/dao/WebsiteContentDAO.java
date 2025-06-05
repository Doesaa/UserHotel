package bt.edu.gcit.usermicroservice.dao;

import java.util.List;
import bt.edu.gcit.usermicroservice.entity.WebsiteContent;

public interface WebsiteContentDAO {
    WebsiteContent save(WebsiteContent content);
    List<WebsiteContent> getAllContents();
    WebsiteContent findByID(Long id);
    void deleteById(Long id);
   
}