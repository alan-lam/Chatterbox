package com.example.alan.chatterbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ip = "";
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        ip = addr.getHostAddress();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        TextView ip_address_tv = findViewById(R.id.ip_address);
        ip_address_tv.setText(ip);

        Button start_server = findViewById(R.id.start_server);
        start_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                /*
                Intent ...
                startActivity(...);
                 */
            }
        });

        Button connect_server = findViewById(R.id.connect_server);
        connect_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                /*
                Intent ...
                startActivity(...);
                 */
            }
        });
    }
}
