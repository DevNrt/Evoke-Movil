package com.example.pruebaandroid.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebaandroid.CambioContrasenaActivity;
import com.example.pruebaandroid.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentoPerfil extends Fragment {

    private String name, id, token;
    private RequestQueue queue;
    private EditText nombre, apellido, email, telefono, torre, apto;
    private Button boton_actualizar, boton_cambiar_contra;
    private String urlGetUser;

    public FragmentoPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name", "Empety");
            id = getArguments().getString("_id");
            token = getArguments().getString("token");
            urlGetUser = "https://addmin-system.herokuapp.com/user-id/?id=" + id;
            Log.i("URL", urlGetUser);
        } else {
            name = "Sin Argumento";
        }
        queue = Volley.newRequestQueue(getContext());
        apiUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmento_perfil, container, false);
        nombre = view.findViewById(R.id.userName);
        apellido = view.findViewById(R.id.userLastName);
        email = view.findViewById(R.id.userEmail);
        telefono = view.findViewById(R.id.userTelephone);
        torre = view.findViewById(R.id.userTower);
        apto = view.findViewById(R.id.userApto);
        boton_actualizar = (Button) view.findViewById(R.id.button_update);
        boton_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle("Confirmación Actualzación")
                        .setMessage("¿Esta seguro que desea actualizar su información?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                apiUpdateUser();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        boton_cambiar_contra = (Button) view.findViewById(R.id.button_change_pass);
        boton_cambiar_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        nombre.setText(name);
        return view;
    }

    public void apiUser() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlGetUser, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    nombre.setText(data.getString("name"));
                    apellido.setText(data.getString("lastName"));
                    email.setText(data.getString("email"));
                    telefono.setText(data.getString("telephone"));
                    torre.setText(data.getString("tower"));
                    apto.setText(data.getString("apto"));
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

    public void apiUpdateUser(){
        String url = "https://addmin-system.herokuapp.com/update-user";
        final JsonObject json = new JsonObject();
        json.addProperty("_id", id);
        json.addProperty("name", nombre.getText().toString());
        json.addProperty("lastName", apellido.getText().toString());
        json.addProperty("telephone", telefono.getText().toString());

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String  msg = obj.getString("message");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error al parsear el JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

    public void changePassword(){
        Intent intent = new Intent(this.getContext(), CambioContrasenaActivity.class);
        intent.putExtra("_id", id);
        intent.putExtra("token", token);
        intent.putExtra("name", nombre.getText().toString());
        intent.putExtra("lastName", apellido.getText().toString());
        intent.putExtra("email", email.getText().toString());
        startActivity(intent);
    }

}
