package com.example.pruebaandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CambioContrasenaActivity extends AppCompatActivity {

    private String id, token, nombre, apellido, correo;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra("_id");
        token = intent.getStringExtra("token");
        nombre = intent.getStringExtra("name");
        apellido = intent.getStringExtra("lastName");
        correo = intent.getStringExtra("email");
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_cambio_contrasena);

    }

    public void changePassword(View view){
        EditText actualPass = findViewById(R.id.actualPass);
        EditText newPass = findViewById(R.id.newPass);
        EditText repeatPass = findViewById(R.id.repeatPass);
        if (newPass.getText().toString().equals(repeatPass.getText().toString())){
            apiChangePassword();
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
        }
    }

    public void regresarPerfil(){
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("page", "perfil");
        intent.putExtra("_id", id);
        intent.putExtra("token", token);
        intent.putExtra("name", nombre);
        intent.putExtra("lastName", apellido);
        intent.putExtra("email", correo);
        startActivity(intent);

    }

    public void apiChangePassword(){
        EditText actualPass = findViewById(R.id.actualPass);
        EditText newPass = findViewById(R.id.newPass);
        String url = "https://addmin-system.herokuapp.com/change-password";
        final JsonObject json = new JsonObject();
        json.addProperty("_id", id);
        json.addProperty("password", actualPass.getText().toString());
        json.addProperty("newPassword", newPass.getText().toString());
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String  msg = obj.getString("message");
                            Toast.makeText(CambioContrasenaActivity.this, msg, Toast.LENGTH_LONG).show();
                            if(obj.getBoolean("status")){
                                regresarPerfil();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CambioContrasenaActivity.this, "Error al parsear el JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CambioContrasenaActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(strRequest);

    }
}
