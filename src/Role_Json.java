package src;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
    private String id;
    private String name;
    @JsonProperty("requiredQualifications")
    private List<String> requiredQualifications;

    // Constructors
    public Role() {
    }

    public Role(String id, String name, List<String> requiredQualifications) {
        this.id = id;
        this.name = name;
        this.requiredQualifications = requiredQualifications;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRequiredQualifications() {
        return requiredQualifications;
    }

    public void setRequiredQualifications(List<String> requiredQualifications) {
        this.requiredQualifications = requiredQualifications;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", requiredQualifications=" + requiredQualifications +
                '}';
    }
}
