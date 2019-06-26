package com.jeeps.laboratorioutpl.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jeeps.laboratorioutpl.R;
import com.jeeps.laboratorioutpl.adapters.AccessAdapter;
import com.jeeps.laboratorioutpl.dialogs.InvalidAccessDialog;
import com.jeeps.laboratorioutpl.model.access.AccessResult;
import com.jeeps.laboratorioutpl.model.access.RegisterAccess;
import com.jeeps.laboratorioutpl.model.access.RegisterResult;
import com.jeeps.laboratorioutpl.model.user.UserDB;
import com.jeeps.laboratorioutpl.service.AccessService;
import com.jeeps.laboratorioutpl.service.RetrofitClient;
import com.jeeps.laboratorioutpl.service.UserService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String USER_ID_EXTRA = "USER_ID";
    public static final String USER_TOKEN_EXTRA = "USER_TOKEN";

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.nameText) TextView nameTextView;
    @BindView(R.id.acccessRegistryRecyclerView) RecyclerView accessRecyclerView;
    @BindView(R.id.logginProgressBar) ProgressBar loginProgressBar;

    private UserService userService;
    private AccessService accessService;
    private String loggedUserName;
    private String userId;
    private String userToken;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        fab.setOnClickListener(view -> startQrScanner());

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Read shared preferences
        userId = sharedPreferences.getString(getString(R.string.logged_user_id), "");
        userToken = sharedPreferences.getString(getString(R.string.logged_user_token), "");
        loggedUserName = sharedPreferences.getString(getString(R.string.logged_user_name), "");

        // Initialize services
        userService = RetrofitClient.getRetrofitInstance().create(UserService.class);
        accessService = RetrofitClient.getRetrofitInstance().create(AccessService.class);

        // Get intent from QR code scan
        Intent intent = getIntent();
        String qrCodeResult = intent.getStringExtra(QrScannerActivity.QR_CODE_RESULT);

        // Check if we are receiving a QR code from the scanner
        if (qrCodeResult == null)
            login();
        else
            registerAccess(qrCodeResult);
    }

    private void login() {
        loginProgressBar.setVisibility(View.VISIBLE);
        Call<UserDB> call = userService.getUser(userToken, userId);
        call.enqueue(new Callback<UserDB>() {
            @Override
            public void onResponse(Call<UserDB> call, Response<UserDB> response) {
                // Proceed if credentials are valid
                if (response.body() != null) {
                    Snackbar.make(fab, getString(R.string.welcome_message), Snackbar.LENGTH_SHORT).show();
                    // Save logged name
                    String firstName = response.body().getUsuarioDB().getFirstName();
                    editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.logged_user_name), firstName);
                    editor.apply();
                    nameTextView.setText(String.format("%s, %s",
                            getString(R.string.welcome_message), firstName));
                    populateAccessList();
                } else {
                    // Prompt for login
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<UserDB> call, Throwable t) {
                startLoginActivity();
            }
        });
    }
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        // Clear activity stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void populateAccessList() {
        // Get access
        Call<AccessResult> call = accessService.getAccessList(userToken, userId);
        call.enqueue(new Callback<AccessResult>() {
            @Override
            public void onResponse(Call<AccessResult> call, Response<AccessResult> response) {
                // Set the adapter
                runOnUiThread(() -> {
                    // Handle bad requests
                    if (response.body() != null) {
                        // Handle empty array
                        if (response.body().getPermisoDB() != null) {
                            AccessAdapter adapter = new AccessAdapter(response.body().getPermisoDB(),
                                    MainActivity.this.getApplicationContext());
                            accessRecyclerView.setAdapter(adapter);
                            // Improve performance if layout size will not change
                            accessRecyclerView.setHasFixedSize(true);
                            // layout manager
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                            accessRecyclerView.setLayoutManager(layoutManager);
                            accessRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(MainActivity.this.getApplicationContext()));
                            loginProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<AccessResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void registerAccess(String sala) {
        RegisterAccess registerAccess = new RegisterAccess();
        registerAccess.setSala(sala);
        registerAccess.setUser(userId);
        Call<RegisterResult> call = accessService.postAccess(userToken, registerAccess);
        call.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if (response.body() == null) {
                    InvalidAccessDialog invalidAccessDialog = new InvalidAccessDialog();
                    invalidAccessDialog.show(getSupportFragmentManager(), "AcceptDialog");
                } else
                    Snackbar.make(fab, "Registrado correctamente", Snackbar.LENGTH_SHORT).show();
                populateAccessList();
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void startQrScanner() {
        Intent intent = new Intent(this, QrScannerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nameTextView.setText(String.format("%s, %s",
                getString(R.string.welcome_message), loggedUserName));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            // Delete saved preferences
            editor = sharedPreferences.edit();
            editor.remove(getString(R.string.logged_user_id));
            editor.remove(getString(R.string.logged_user_token));
            editor.apply();
            // Start login activity
            startLoginActivity();
            return true;
        } else if (id == R.id.action_profile){
            // Pass the user id to the edit profile page
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra(USER_ID_EXTRA, userId);
            intent.putExtra(USER_TOKEN_EXTRA, userToken);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
