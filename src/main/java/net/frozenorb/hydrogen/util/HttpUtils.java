package net.frozenorb.hydrogen.util;

import net.frozenorb.hydrogen.Hydrogen;
import okhttp3.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class HttpUtils {

    public static void authorize(Request.Builder builder) {
        builder.header("MHQ-Authorization", Hydrogen.getInstance().getSettings().getApiKey());
    }

    public static String generateQueryString(Map<String, Object> parameters) {
        StringBuilder queryBuilder = new StringBuilder("?");

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (queryBuilder.length() > 1)
                queryBuilder.append("&");

            try {
                queryBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException ignored) {}
        }

        return queryBuilder.toString();
    }

}
