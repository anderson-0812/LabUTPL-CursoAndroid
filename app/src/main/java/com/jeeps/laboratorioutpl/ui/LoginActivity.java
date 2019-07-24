package com.jeeps.laboratorioutpl.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeps.laboratorioutpl.R;
import com.jeeps.laboratorioutpl.model.Login;
import com.jeeps.laboratorioutpl.model.LoginCredentials;
import com.jeeps.laboratorioutpl.service.LoginService;
import com.jeeps.laboratorioutpl.service.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.usernameField)
    TextView usernameField;
    @BindView(R.id.passwordField) TextView passwordField;
    @BindView(R.id.badLoginText) TextView badLoginText;

    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize services
        loginService = RetrofitClient.getRetrofitInstance().create(LoginService.class);

        // Hide bad login text
        badLoginText.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.loginButton)
    protected void login(View v) {
        // Login to get user and token
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail(usernameField.getText().toString());
        credentials.setPassword(passwordField.getText().toString());
        Call<Login> call = loginService.login(credentials);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                // Store id and token to saved preferences to maintain user logged in
                Login login = response.body();
                if (login != null)
                    if (login.getUsuario() != null) {
                        storeLogin(login.getUsuario().getId(), login.getToken());
                        Toast mensajet = Toast.makeText(getApplicationContext()
                                ,"Te has logeado"
                                ,Toast.LENGTH_SHORT);
//                        mensajet.setGravity(Gravity.CENTER|Gravity.LEFT,0,0);
                        mensajet.show();
                        Log.d("CREATION","Logeado");
                    }
                    else
                        badLoginText.setVisibility(View.VISIBLE);
                else
                    badLoginText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void storeLogin(String id, String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.logged_user_id), id);
        editor.putString(getString(R.string.logged_user_token), token);
        editor.apply();

        // Start main activity clearing this one from the stack
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
