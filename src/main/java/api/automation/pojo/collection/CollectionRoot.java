package api.automation.pojo.collection;

public class CollectionRoot {
    private Collection collection;

    public CollectionRoot() {
    }

    public CollectionRoot(Collection coolection) {
        this.collection = coolection;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
