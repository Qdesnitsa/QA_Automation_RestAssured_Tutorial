package api.automation.pojo.complex.collection_root;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CollectionRootBase {
    public CollectionRootBase() {
    }
}
