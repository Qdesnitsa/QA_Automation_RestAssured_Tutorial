package api.automation.pojo.complex.collection;

import api.automation.pojo.complex.folder.FolderResponse;
import api.automation.pojo.complex.info.Info;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionResponse extends CollectionBase {
    private List<FolderResponse> item;

    public CollectionResponse() {
    }

    public CollectionResponse(Info info, List<FolderResponse> item) {
        super(info);
        this.item = item;
    }

    public List<FolderResponse> getItem() {
        return item;
    }

    public void setItem(List<FolderResponse> item) {
        this.item = item;
    }
}
