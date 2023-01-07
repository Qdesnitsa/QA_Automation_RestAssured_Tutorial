package api.automation.pojo.complex.request_root;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RequestRootBase {
    private String name;

    public RequestRootBase() {
    }

    public RequestRootBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
