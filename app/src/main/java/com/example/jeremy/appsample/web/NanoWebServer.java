package com.example.jeremy.appsample.web;

import android.util.Log;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class NanoWebServer extends NanoHTTPD {
    private static final String TAG = "WebInterfaceService";

    //public NanoWebServer() throws IOException {
    public NanoWebServer() {
        super(8080);
        Log.d(TAG, "\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }

        return new Response(Response.Status.OK, NanoHTTPD.MIME_HTML, msg + "</body></html>\n"); // using version 2.1.1
        //return Response.newFixedLengthResponse(msg + "</body></html>\n");
	}
}
