package net.frozenorb.hydrogen.connection;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.frozenorb.hydrogen.Hydrogen;
import net.frozenorb.hydrogen.util.HttpUtils;
import okhttp3.MediaType;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;

public class RequestHandler {

    @Getter private static long lastAPIError = 0L;
    @Getter private static long lastAPIRequest = 0L;
    @Getter private static boolean apiDown = false;
    @Getter private static long lastLatency;
    private static long averageLatency;
    private static int averageLatencyTicks = 0;

    public static double getAverageLatency() {
        return averageLatencyTicks == 0 ? -1.0D : Math.round((double) averageLatency / (double) averageLatencyTicks);
    }

    public static RequestResponse get(String endpoint) {
        return get(endpoint, null);
    }

    public static Builder createBuilder(String endpoint, Map<String, Object> queryParameters) {
        Builder builder = new Builder();
        builder.url(Hydrogen.getInstance().getSettings().getApiHost() + endpoint + (queryParameters != null ? HttpUtils.generateQueryString(queryParameters) : ""));
        HttpUtils.authorize(builder);
        lastAPIRequest = System.currentTimeMillis();

        return builder;
    }

    public static RequestResponse get(String endpoint, Map<String, Object> queryParameters) {
        Builder builder = createBuilder(endpoint, queryParameters);
        builder.get();
        
        try {
            long e = System.currentTimeMillis();
            Response response = Hydrogen.getInstance().getOkHttpClient().newCall(builder.build()).execute();
            if (response.code() >= 500) {
                apiDown = true;
                lastAPIError = System.currentTimeMillis();
                return new RequestResponse(false, "Could not connect to API", null, builder);
            } else {
                apiDown = false;
                lastLatency = System.currentTimeMillis() - e;
                averageLatency += System.currentTimeMillis() - e;
                ++averageLatencyTicks;
                String body = response.body().string();

                try {
                    JSONObject object = new JSONObject(new JSONTokener(body));
                    if (object.has("success") && !object.getBoolean("success"))
                        return new RequestResponse(false, object.getString("message"), body, builder);
                } catch (JSONException ignored) {
                }

                return new RequestResponse(true, null, body, builder);
            }
        } catch (UnknownHostException | ConnectException e) {
            apiDown = true;
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Could not connect to API", null, builder);
        } catch (Exception e) {
            e.printStackTrace();
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Failed to get response from API", null, builder);
        }
    }

    public static RequestResponse post(String endpoint, Map<String, Object> bodyParams) {
        Builder builder = new Builder();
        if (bodyParams != null) {
            for (Entry<String, Object> e : bodyParams.entrySet()) {
                if (e.getValue() instanceof Double && !Double.isFinite((Double) e.getValue()))
                    e.setValue(0.0D);
            }
        }

        String bodyJson = Hydrogen.PLAIN_GSON.toJson(bodyParams == null ? ImmutableMap.of() : bodyParams);
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyJson));
        builder.url(Hydrogen.getInstance().getSettings().getApiHost() + endpoint);

        HttpUtils.authorize(builder);
        lastAPIRequest = System.currentTimeMillis();

        try {
            long e = System.currentTimeMillis();
            Response response = Hydrogen.getInstance().getOkHttpClient().newCall(builder.build()).execute();
            if (response.code() >= 500) {
                apiDown = true;
                lastAPIError = System.currentTimeMillis();
                return new RequestResponse(false, "Could not connect to API", null, builder);
            } else {
                apiDown = false;
                lastLatency = System.currentTimeMillis() - e;
                averageLatency += System.currentTimeMillis() - e;
                ++averageLatencyTicks;
                String body = response.body().string();

                try {
                    JSONObject object = new JSONObject(new JSONTokener(body));
                    if (object.has("success") && !object.getBoolean("success"))
                        return new RequestResponse(false, object.getString("message"), body, builder);
                } catch (JSONException ignored) {
                }

                return new RequestResponse(true, null, body, builder);
            }
        } catch (UnknownHostException | ConnectException e) {
            apiDown = true;
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Could not connect to API", null, builder);
        } catch (Exception e) {
            e.printStackTrace();
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Failed to get response from API", null, builder);
        }
    }

    public static RequestResponse delete(String endpoint, Map<String, Object> bodyParams) {
        Builder builder = new Builder();
        String bodyJson = Hydrogen.PLAIN_GSON.toJson(bodyParams == null ? ImmutableMap.of() : bodyParams);

        builder.delete(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyJson));
        builder.url(Hydrogen.getInstance().getSettings().getApiHost() + endpoint);

        HttpUtils.authorize(builder);
        lastAPIRequest = System.currentTimeMillis();

        try {
            long e = System.currentTimeMillis();
            Response response = Hydrogen.getInstance().getOkHttpClient().newCall(builder.build()).execute();
            if (response.code() >= 500) {
                apiDown = true;
                lastAPIError = System.currentTimeMillis();
                return new RequestResponse(false, "Could not connect to API", null, builder);
            } else {
                apiDown = false;
                lastLatency = System.currentTimeMillis() - e;
                averageLatency += System.currentTimeMillis() - e;
                ++averageLatencyTicks;
                String body = response.body().string();

                try {
                    JSONObject object = new JSONObject(new JSONTokener(body));
                    if (object.has("success") && !object.getBoolean("success")) {
                        return new RequestResponse(false, object.getString("message"), body, builder);
                    }
                } catch (JSONException ignored) {
                }

                return new RequestResponse(true, null, body, builder);
            }
        } catch (UnknownHostException | ConnectException e) {
            apiDown = true;
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Could not connect to API", null, builder);
        } catch (Exception e) {
            e.printStackTrace();
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Failed to get response from API", null, builder);
        }
    }

    public static RequestResponse send(Builder builder) {
        lastAPIRequest = System.currentTimeMillis();

        try {
            long e = System.currentTimeMillis();
            Response response = Hydrogen.getInstance().getOkHttpClient().newCall(builder.build()).execute();
            if (response.code() >= 500) {
                apiDown = true;
                lastAPIError = System.currentTimeMillis();
                return new RequestResponse(false, "Could not connect to API", null, builder);
            } else {
                apiDown = false;
                lastLatency = System.currentTimeMillis() - e;
                averageLatency += System.currentTimeMillis() - e;
                ++averageLatencyTicks;
                String body = response.body().string();

                try {
                    JSONObject object = new JSONObject(new JSONTokener(body));
                    if (object.has("success") && !object.getBoolean("success"))
                        return new RequestResponse(false, object.getString("message"), body, builder);
                } catch (JSONException ignored) {
                }

                return new RequestResponse(true, null, body, builder);
            }
        } catch (UnknownHostException | ConnectException e) {
            apiDown = true;
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Could not connect to API", null, builder);
        } catch (Exception e) {
            e.printStackTrace();
            lastAPIError = System.currentTimeMillis();
            return new RequestResponse(false, "Failed to get response from API", null, builder);
        }
    }
    


}
