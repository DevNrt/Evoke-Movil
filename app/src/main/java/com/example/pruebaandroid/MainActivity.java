package com.example.pruebaandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
    }

    public void login(View view) {
        EditText email = findViewById(R.id.editText);
        EditText password = findViewById(R.id.editText2);
        final Intent login = new Intent(this, Main2Activity.class);
        final JsonObject json = new JsonObject();
        json.addProperty("email", email.getText().toString());
        json.addProperty("password", password.getText().toString());
        //json.addProperty("email", "dev-camilo@gmail.com");
        //json.addProperty("password", "123456");
        String url = "https://addmin-system.herokuapp.com/login";
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                String token = obj.getString("token");
                                Log.i("TOKEN", token);
                                JSONArray data = obj.getJSONArray("data");
                                JSONObject info = data.getJSONObject(0);
                                login.putExtra("token",token);
                                login.putExtra("_id",info.getString("_id"));
                                login.putExtra("name",info.getString("name"));
                                login.putExtra("lastName",info.getString("lastName"));
                                login.putExtra("email",info.getString("email"));
                                login.putExtra("page", "inicio");
                                startActivity(login);
                            } else {
                                String msg = obj.getString("message");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() {
                return json.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(strRequest);
    }

    public void apiGet(View view) {
        String url = "https://addmin-system.herokuapp.com/listar-msg";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");
                    JSONObject info = data.getJSONObject(0);
                    Toast.makeText(MainActivity.this, info.getString("mensaje"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Error", "FALLO AL PARCEAR EL JSON");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", "FALLO EN LA PETICIÓN");
                Log.i("Error", error.getMessage());
            }
        });
        queue.add(request);

    }
    public void registro(View view){
        Intent registro=new Intent(this, register.class);
        startActivity(registro);
    }
}
