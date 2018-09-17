package com.example.alan.chatterbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

public class ServerActivity extends AppCompatActivity {

    static ArrayList<ClientHandler> ar = new ArrayList<>();
    static CountDownLatch x = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        try {
            startServer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServer() throws IOException {
        final ServerSocket ss = new ServerSocket(9563);
        Socket this_s = new Socket("localhost", 9563);
        BufferedReader this_in = new BufferedReader(new InputStreamReader(this_s.getInputStream()));
        PrintWriter this_out = new PrintWriter(this_s.getOutputStream(), true);

        ClientHandler c = new ClientHandler(this_s, this_in, this_out, x);
        Thread t = new Thread(c);
        ar.add(c);
        t.start();
    }
}

class ClientHandler implements Runnable {
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private Socket s;
    private boolean isLoggedIn = true;
    private CountDownLatch x;

    public ClientHandler (Socket s, BufferedReader in, PrintWriter out) {
        this.s = s;
        this.in = in;
        this.out = out;
    }

    public ClientHandler (Socket s, BufferedReader in, PrintWriter out, CountDownLatch x) {
        this.s = s;
        this.in = in;
        this.out = out;
        this.x = x;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        out.println("/: Hello. Enter your name: ");
        try {
            String inputName = in.readLine();
            this.setName(inputName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        out.println("/: Great! Your name is " + this.name);

        if (x != null) {
            x.countDown();
        }

        String received;
        while (true) {
            try {
                received = in.readLine();
                if (received == null) {
                    this.isLoggedIn = false;
                    break;
                }
                else if (received.equals("#logout")) {
                    this.isLoggedIn = false;
                    this.s.close();
                    break;
                }
                else if (received.equals("#active")) {
                    int i = 1;
                    for (ClientHandler mc : ServerActivity.ar) {
                        if (mc.name == null) {
                            mc.isLoggedIn = false;
                        }
                        if (mc.isLoggedIn) {
                            out.println(i + ": " + mc.name);
                            i++;
                        }
                    }
                }
                else if (received.startsWith("/:")) {
                    out.println(received);
                }
                else {
                    StringTokenizer st = new StringTokenizer(received, "@");
                    String toSend = st.nextToken();
                    String recipient = st.nextToken();

                    for (ClientHandler mc : ServerActivity.ar) {
                        if (mc.name == null) {
                            mc.isLoggedIn = false;
                            continue;
                        }
                        if (mc.name.equals(recipient) && isLoggedIn) {
                            mc.out.println(this.name + ": " + toSend);
                            break;
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            this.in.close();
            this.out.close();
            this.s.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
