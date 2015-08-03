package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.duplicated.Tag;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Pet")
public class Pet {
    private long id;
    private Category category;
    private CategoryWithApiModel categoryWithApiModel;
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

    @XmlElement(name = "categoryWithApiModel")
    public CategoryWithApiModel getCategoryWithApiModel() {
        return categoryWithApiModel;
    }

    public void setCategoryWithApiModel(CategoryWithApiModel category) {
        this.categoryWithApiModel = categoryWithApiModel;
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
    @ApiModelProperty(value = "pet status in the store", allowableValues = "available,pending,sold", access = "public")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
