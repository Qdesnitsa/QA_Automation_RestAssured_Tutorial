package api.automation.pojo.complex.collection;

import api.automation.pojo.complex.info.Info;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CollectionBase {
    private Info info;

    public CollectionBase() {
    }

    public CollectionBase(Info info) {
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
