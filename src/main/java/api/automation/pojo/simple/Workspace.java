package api.automation.pojo.simple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(value = {"id", "someMap"}, allowSetters = true)
public class Workspace {
    @JsonIgnore
    //@JsonInclude(JsonInclude.Include.NON_EMPTY) //EMPTY includes null values
    private Map<String, String> someMap;
    @JsonIgnore
    //@JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int someField;
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    private String name;
    private String type;
    private String description;

    public Workspace() {
    }

    public Workspace(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    //@JsonIgnore
    public Map<String, String> getSomeMap() {
        return someMap;
    }

    //@JsonIgnore
    public void setSomeMap(Map<String, String> someMap) {
        this.someMap = someMap;
    }

    public int getSomeField() {
        return someField;
    }

    public void setSomeField(int someField) {
        this.someField = someField;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
