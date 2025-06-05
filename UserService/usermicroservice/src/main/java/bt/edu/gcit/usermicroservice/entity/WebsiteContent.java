package bt.edu.gcit.usermicroservice.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "website_contents")
public class WebsiteContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hero_banner")
    private String heroBanner; // stores filename or path

    @Column(name = "footer_banner")
    private String footerBanner; // stores filename or path

    @Column(name = "contact_us")
    private String contactUs; // Changed to String for flexibility

    @Column(name = "about_us", columnDefinition = "TEXT")
    private String aboutUs;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHeroBanner() { return heroBanner; }
    public void setHeroBanner(String heroBanner) { this.heroBanner = heroBanner; }

    public String getFooterBanner() { return footerBanner; }
    public void setFooterBanner(String footerBanner) { this.footerBanner = footerBanner; }

    public String getContactUs() { return contactUs; }
    public void setContactUs(String contactUs) { this.contactUs = contactUs; }

    public String getAboutUs() { return aboutUs; }
    public void setAboutUs(String aboutUs) { this.aboutUs = aboutUs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "WebsiteContent{" +
                "id=" + id +
                ", heroBanner='" + heroBanner + '\'' +
                ", footerBanner='" + footerBanner + '\'' +
                ", contactUs='" + contactUs + '\'' +
                ", aboutUs='" + aboutUs + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
