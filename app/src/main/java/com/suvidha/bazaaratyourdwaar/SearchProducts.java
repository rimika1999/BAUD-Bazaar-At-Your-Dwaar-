package com.suvidha.bazaaratyourdwaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class SearchProducts extends AppCompatActivity {

    EditText editText_search_v;
    Context context = this;
    RequestQueue queue;
    RecyclerView recyclerView_reuslt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        editText_search_v = findViewById(R.id.search_et_products);
        queue = Volley.newRequestQueue(context);

        recyclerView_reuslt = findViewById(R.id.search_rv_results);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_reuslt.setLayoutManager(linearLayoutManager);

        editText_search_v.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        editText_search_v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty())
                    recyclerView_reuslt.setAdapter(new RVAdapter_Result(context,Integer.parseInt(charSequence.toString())));
                else
                    recyclerView_reuslt.setAdapter(new RVAdapter_Result(context,0));
                //sendRequest(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void sendRequest(String search)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://bazaaratyourdwaar.000webhostapp.com/beauty.php?s=" + search, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("tag","" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);

    }

}