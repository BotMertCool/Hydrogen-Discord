package net.frozenorb.hydrogen.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Request;
import org.json.JSONObject;
import org.json.JSONTokener;

@Getter
@AllArgsConstructor
public class RequestResponse {

    private boolean successful;
    private String errorMessage;
    private String response;
    private Request.Builder requestBuilder;

    public boolean wasSuccessful() {
        return this.successful;
    }

    public JSONObject asJSONObject() {
        return new JSONObject(new JSONTokener(this.response));
    }

    public Request rebuildRequest() {
        return this.requestBuilder.build();
    }

    public boolean couldNotConnect() {
        return this.getErrorMessage() != null && this.getErrorMessage().toLowerCase().contains("could not connect to api");
    }

}
