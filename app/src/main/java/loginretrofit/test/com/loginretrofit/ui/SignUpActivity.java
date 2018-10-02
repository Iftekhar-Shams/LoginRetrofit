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
import loginretrofit.test.com.loginretrofit.models.DefaultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText email, name, password, school;
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        school = findViewById(R.id.school);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });
    }

    private void userSignUp() {
        String email_new = email.getText().toString().trim();
        String name_new = name.getText().toString().trim();
        String password_new = password.getText().toString().trim();
        String school_new = school.getText().toString().trim();

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

        if(name_new.isEmpty()){
            name.setError("Name Required");
            name.requestFocus();
            return;
        }

        if(school_new.isEmpty()){
            school.setError("School Required");
            school.requestFocus();
            return;
        }

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi()
                .createUser(email_new,name_new,password_new,school_new);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.code()==201){
                    DefaultResponse dr = response.body();
                    Toast.makeText(SignUpActivity.this, dr.getMessage(), Toast.LENGTH_SHORT).show();
                }else if(response.code()==422){
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });

//        Call<ResponseBody> call = RetrofitClient.getInstance().getApi()
//                .createUser(email_new,name_new,password_new,school_new);
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    String s = null;
//                    if(response.code() == 201){
//                        try {
//                            s = response.body().string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }else{
//                        try {
//                            s = response.errorBody().string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if(s!=null){
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Toast.makeText(SignUpActivity.this, "Error registering", Toast.LENGTH_SHORT).show();
//                }
//            });
    }
}
