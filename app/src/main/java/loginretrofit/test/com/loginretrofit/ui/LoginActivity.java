package loginretrofit.test.com.loginretrofit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import loginretrofit.test.com.loginretrofit.R;
import loginretrofit.test.com.loginretrofit.api.RetrofitClient;
import loginretrofit.test.com.loginretrofit.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        login = findViewById(R.id.loginuser);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String email_new = email.getText().toString().trim();
        String password_new = password.getText().toString().trim();

        if(email_new.isEmpty()){
            email.setError("Email Required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email_new).matches()){
            email.setError("Enter valid email");
            email.requestFocus();
            return;
        }

        if(password_new.isEmpty()){
            password.setError("Password Required");
            password.requestFocus();
            return;
        }

        if(password.length() < 6){
            password.setError("Must be atleast 6 characters");
            password.requestFocus();
            return;
        }

        Call<LoginResponse> call = RetrofitClient.getInstance().getApi()
                .userLogin(email_new,password_new);
        
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(!loginResponse.isError()){
                    //save user
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),HomescreenActivity.class));
                    email.setText("");
                    password.setText("");
                }else{
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}
