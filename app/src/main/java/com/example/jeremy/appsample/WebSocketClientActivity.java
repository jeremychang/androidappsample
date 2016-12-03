package com.example.jeremy.appsample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class WebSocketClientActivity extends Activity {
    private static final String TAG = WebSocketClientActivity.class.getSimpleName();
    private WebSocketClient mWebSocketClient;
    private static SSLSocketFactory sslFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websocketclient);

        connectWebSocket();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_websocketclient, container, false);
            return rootView;
        }
    }

    private void connectWebSocket() {
        URI uri;
        try {
            //uri = new URI("ws://echo.websocket.org");
            uri = new URI("wss://echo.websocket.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i(TAG, "Open: " + handshakedata.toString());
                mWebSocketClient.send("Opened " + uri.toString());
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.messages);
                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i(TAG, "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i(TAG, "Error " + e.getMessage());
            }
        };

        if (mWebSocketClient.getURI().getScheme().equals("wss")) {
            Log.i(TAG, "using wss scheme");
            SSLContext sslContext = null;
            try {
                //SSLSocket sslSocket = (SSLSocket) getSSLFactory().createSocket();
                //sslSocket.setEnabledProtocols(new String[] {"TLSv1"});
                //mWebSocketClient.setSocket(sslSocket);
                sslContext = SSLContext.getInstance( "TLS" );
                // will use java's default key and trust store which is
                // sufficient unless you deal with self-signed certificates
                //sslContext.init( null, null, null );
                sslContext = SSLContext.getDefault();
                mWebSocketClient.setWebSocketFactory( new DefaultSSLWebSocketClientFactory( sslContext ) );
            } catch (GeneralSecurityException e) {
                return;
            }
        }

        mWebSocketClient.connect();
    }

    public void sendMessage(View view) {
        EditText editText = (EditText)findViewById(R.id.message);
        mWebSocketClient.send(editText.getText().toString());
        editText.setText("");
    }
}
