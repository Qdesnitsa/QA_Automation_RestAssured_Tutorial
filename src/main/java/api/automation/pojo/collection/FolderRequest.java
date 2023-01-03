package api.automation.pojo.collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderRequest extends FolderBase {
    private List<RequestRootRequest> item;

    public FolderRequest() {
    }

    public FolderRequest(String name, List<RequestRootRequest> item) {
        super(name);
        this.item = item;
    }

    public List<RequestRootRequest> getItem() {
        return item;
    }

    public void setItem(List<RequestRootRequest> item) {
        this.item = item;
    }
}
