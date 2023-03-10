package api.automation.pojo.complex.collection_root;

import api.automation.pojo.complex.collection.CollectionResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionRootResponse extends CollectionRootBase {
    private CollectionResponse collection;

    public CollectionRootResponse() {
    }

    public CollectionRootResponse(CollectionResponse collection) {
        this.collection = collection;
    }

    public CollectionResponse getCollection() {
        return collection;
    }

    public void setCollection(CollectionResponse collection) {
        this.collection = collection;
    }
}
