package api.automation.pojo.complex.request_root;

import api.automation.pojo.complex.request.RequestResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRootResponse extends RequestRootBase {
    private RequestResponse request;

    public RequestRootResponse() {
    }

    public RequestRootResponse(String name, RequestResponse request) {
        super(name);
        this.request = request;
    }

    public RequestResponse getRequest() {
        return request;
    }

    public void setRequest(RequestResponse request) {
        this.request = request;
    }
}
