package com.example.pruebaandroid.fragmentos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebaandroid.R;
import com.example.pruebaandroid.recycle.view.RecycleViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentoMisiones extends Fragment {

    private RequestQueue queue;
    private String url, token;

    public FragmentoMisiones() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewPqrs;
    private RecycleViewAdapter pqrsAdapter;
    private ArrayList<ItemPqrs> itemPqrs = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = "https://addmin-system.herokuapp.com/list-pqrs-id-origin/?id=" + getArguments().getString("_id");
            token = getArguments().getString("token");
        }
        queue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento_misiones, container, false);

        // Vinculamos las dos instancias del secycler view

        return view;
    }

    public void apiPQRS() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");
                    itemPqrs.clear(); // Limpia la lista para evitar que se duplique cuando se cargue el fragment
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject info = data.getJSONObject(i);
                        itemPqrs.add(new ItemPqrs(info.getString("title"), info.getString("description")));
                    }
                    pqrsAdapter = new RecycleViewAdapter(itemPqrs);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        queue.add(request);
    }

}
