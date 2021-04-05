package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.PatternProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Annotated Pet", nullable = true)
@PatternProperty(
        regex = "what.*ever",
        schema = @Schema(
                type = "integer",
                description = "prop schema 1",
                format = "int32",
                maximum = "10"
        )
)
@PatternProperty(
        regex = "it.*takes",
        schema = @Schema(
                implementation = Category.class,
                description = "prop schema 2"
        )
)
@SchemaProperty(
        name = "anotherCategory",
        schema = @Schema(
                implementation = Category.class,
                description = "prop schema 2"
        )
)
@SchemaProperty(
        name = "anotherInteger",
        schema = @Schema(
                type = "integer",
                description = "prop schema 1",
                format = "int32",
                maximum = "10"
        )
)
public class AnnotatedPet {
    private long id;
    private Category category;
    private String name;
    private List<String> photoUrls = new ArrayList<String>();
    private List<Tag> tags = new ArrayList<Tag>();
    private String status;

    @XmlElement(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement(name = "category")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElementWrapper(name = "photoUrls")
    @XmlElement(name = "photoUrl")
    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @XmlElement(name = "status")
    @Schema(description = "pet status in the store", allowableValues = {"available", "pending", "sold"})
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
