package api.automation.authentication.google_oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
    private String emailAddress;
    private int messagesTotal;
    private int threadsTotal;
    private String historyId;

    public Profile() {
    }

    public Profile(String emailAddress, int messagesTotal, int threadsTotal, String historyId) {
        this.emailAddress = emailAddress;
        this.messagesTotal = messagesTotal;
        this.threadsTotal = threadsTotal;
        this.historyId = historyId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getMessagesTotal() {
        return messagesTotal;
    }

    public void setMessagesTotal(int messagesTotal) {
        this.messagesTotal = messagesTotal;
    }

    public int getThreadsTotal() {
        return threadsTotal;
    }

    public void setThreadsTotal(int threadsTotal) {
        this.threadsTotal = threadsTotal;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
