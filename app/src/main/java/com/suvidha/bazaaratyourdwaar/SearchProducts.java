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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                    sendRequest(charSequence.toString());
                else
                {
                    products.clear();
                    recyclerView_reuslt.setAdapter(new RVAdapter_Result(context,products));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    List<Product> products = new ArrayList<>();
    private void sendRequest(String search)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://bazaaratyourdwaar.000webhostapp.com/beauty.php?s=" + search, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                products.clear();
                for(int pos=0;pos<response.length();pos++)
                {
                    try {
                        JSONArray jsonObject = response.getJSONArray(pos);
                        Product product = new Product();
                        product.setProductId(jsonObject.get(0).toString());
                        product.setProductName(jsonObject.get(1).toString());
                        product.setProductCategory(jsonObject.get(2).toString());
                        product.setProductSubCategory(jsonObject.get(3).toString());
                        product.setProductIcon(jsonObject.get(4).toString());
                        products.add(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView_reuslt.setAdapter(new RVAdapter_Result(context,products));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);

    }
    public class Product
    {
        private String productId,productName,productCategory,productSubCategory,productIcon;

        public Product()
        {

        }

        public Product(String productId, String productName, String productCategory, String productSubCategory, String productIcon) {
            this.productId = productId;
            this.productName = productName;
            this.productCategory = productCategory;
            this.productSubCategory = productSubCategory;
            this.productIcon = productIcon;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductCategory() {
            return productCategory;
        }

        public void setProductCategory(String productCategory) {
            this.productCategory = productCategory;
        }

        public String getProductSubCategory() {
            return productSubCategory;
        }

        public void setProductSubCategory(String productSubCategory) {
            this.productSubCategory = productSubCategory;
        }

        public String getProductIcon() {
            return productIcon;
        }

        public void setProductIcon(String productIcon) {
            this.productIcon = productIcon;
        }
    }

}