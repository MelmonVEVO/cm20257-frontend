package com.cm20257.frontend;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHandler {
    // this is supposed to be a singleton class to handle HTTP requests
    // but if it isn't being used, it's safe to delete
    private static RequestHandler INSTANCE;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestHandler(Context ctx) {
        requestQueue = Volley.newRequestQueue(ctx);
        context = ctx;


    }

    public static void startRequestHandler(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new RequestHandler(ctx);
        }
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
