package blog.openapi.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Widget {

	private Integer id;
	private String name;
	private String description;
	private Boolean isActive;

	public Widget() {

	}

	public Widget(Integer id, String name, String description, Boolean isActive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isActive = isActive;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	@Override
	public String toString() {
		return "Widget [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}