package com.jeeps.laboratorioutpl.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.jeeps.laboratorioutpl.R;
import com.jeeps.laboratorioutpl.model.user.User;
import com.jeeps.laboratorioutpl.model.user.UserDB;
import com.jeeps.laboratorioutpl.model.user.UserEdit;
import com.jeeps.laboratorioutpl.model.user.UserPutResult;
import com.jeeps.laboratorioutpl.service.RetrofitClient;
import com.jeeps.laboratorioutpl.service.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private UserService userService;
    private User user;
    private String userToken;
    private String userId;

    @BindView(R.id.firstNameField)
    EditText firstNameField;
    @BindView(R.id.secondNameField) EditText secondNameField;
    @BindView(R.id.firstSurnameField) EditText firstSurnameField;
    @BindView(R.id.secondSurnameField) EditText secondSurnameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        // Get user ID
        Intent intent = getIntent();
        userId = intent.getStringExtra(MainActivity.USER_ID_EXTRA);
        userToken = intent.getStringExtra(MainActivity.USER_TOKEN_EXTRA);

        // Initialize services
        userService = RetrofitClient.getRetrofitInstance().create(UserService.class);

        // Get user
        Call<UserDB> call = userService.getUser(userToken, userId);
        call.enqueue(new Callback<UserDB>() {
            @Override
            public void onResponse(Call<UserDB> call, Response<UserDB> response) {
                // Proceed if credentials are valid
                if (response.body() != null) {
                    user = response.body().getUsuarioDB();
                    runOnUiThread(() -> populateFields());
                } else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserDB> call, Throwable t) {
                finish();
            }
        });
    }

    private void populateFields() {
        firstNameField.setText(user.getFirstName());
        secondNameField.setText(user.getSecondName());
        firstSurnameField.setText(user.getFirstSurname());
        secondSurnameField.setText(user.getSecondSurname());
    }

    @OnClick(R.id.editProfileButton)
    protected void editProfile(View v) {
        // Change user based on parameters
        UserEdit userEdit = new UserEdit();
        userEdit.setFirstName(firstNameField.getText().toString());
        userEdit.setSecondName(secondNameField.getText().toString());
        userEdit.setFirstSurname(firstSurnameField.getText().toString());
        userEdit.setSecondSurname(secondSurnameField.getText().toString());

        // Send data to REST service
        Call<UserPutResult> call = userService.putUser(userToken, userId, userEdit);
        call.enqueue(new Callback<UserPutResult>() {
            @Override
            public void onResponse(Call<UserPutResult> call, Response<UserPutResult> response) {
                Snackbar.make(v, "Datos actualizados correctamente", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserPutResult> call, Throwable t) {
                Snackbar.make(v, "Hubo un error al actualizar tus datos", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
