package api.automation.pojo.complex.request_root;

import api.automation.pojo.complex.request.RequestRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRootRequest extends RequestRootBase {
    private RequestRequest request;

    public RequestRootRequest() {
    }

    public RequestRootRequest(String name, RequestRequest request) {
        super(name);
        this.request = request;
    }

    public RequestRequest getRequest() {
        return request;
    }

    public void setRequest(RequestRequest request) {
        this.request = request;
    }
}
