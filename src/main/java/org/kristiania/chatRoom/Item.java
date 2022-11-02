package org.kristiania.chatRoom;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    private String name;
    @Column(name="art_number")
    private String artNumber;
    private String category;
    private String description;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArtNumber(String artNumber) {
        this.artNumber = artNumber;
    }

    public String getArtNumber() {
        return artNumber;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
