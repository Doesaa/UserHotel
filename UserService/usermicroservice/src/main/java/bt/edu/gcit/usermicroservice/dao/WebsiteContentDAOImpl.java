package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.WebsiteContent;
import bt.edu.gcit.usermicroservice.exception.ContentNotFoundException;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class WebsiteContentDAOImpl implements WebsiteContentDAO {
    private EntityManager entityManager;
    
    @Autowired
    public WebsiteContentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public WebsiteContent save(WebsiteContent content) {
        return entityManager.merge(content);
    }

    @Override
    public List<WebsiteContent> getAllContents() {
        TypedQuery<WebsiteContent> query = entityManager.createQuery("FROM WebsiteContent", WebsiteContent.class);
        return query.getResultList();
    }

    @Override
    public WebsiteContent findByID(Long id) {
        WebsiteContent content = entityManager.find(WebsiteContent.class, id);
        if (content == null) {
            throw new ContentNotFoundException("Content not found with id " + id);
        }
        return content;
    }

  

    @Override
    public void deleteById(Long id) {
        WebsiteContent content = findByID(id);
        entityManager.remove(content);
    }

   
}