package com.example.alan.chatterbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editText = findViewById(R.id.login_edittext);
        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String inputName = editText.getText().toString();
                for (ClientHandler c : ServerActivity.ar) {
                    if (c.getName().equals(inputName) && c.getLoginStatus()) {
                        Toast.makeText(getApplicationContext(), "Name already in use", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent enterServer = new Intent(LoginActivity.this, ServerActivity.class);
                startActivity(enterServer);
            }
        });
    }
}
