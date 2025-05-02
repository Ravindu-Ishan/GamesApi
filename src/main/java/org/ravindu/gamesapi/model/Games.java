package org.ravindu.gamesapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Games {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Publisher is required")
    @Column(nullable = false)
    private String publisher;

    @Size(max = 1000)
    private String description;

    @NotBlank(message = "Genre is required")
    @Column(nullable = false)
    private String genre;


    private String releaseDate;

    private String coverImageURL;

    @Column(nullable = false, updatable = false)
    private String dateCreated;

    private String dateModified;

    // Getters and setters (or use Lombok @Getter/@Setter)
    @PrePersist
    public void setDefaultReleaseDate() {
        if (this.releaseDate == null || this.releaseDate.isEmpty()) {
            this.releaseDate = "TBA"; // Default value if not provided
        }
    }
}
