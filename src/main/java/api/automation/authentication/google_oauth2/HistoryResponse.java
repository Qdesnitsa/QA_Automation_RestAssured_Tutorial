package api.automation.authentication.google_oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryResponse {
    private List<History> history;

    public HistoryResponse() {
    }

    public HistoryResponse(List<History> history) {
        this.history = history;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }
}
