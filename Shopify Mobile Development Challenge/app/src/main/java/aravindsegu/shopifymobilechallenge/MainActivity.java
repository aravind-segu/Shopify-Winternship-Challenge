package aravindsegu.shopifymobilechallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView price;
    TextView bags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        price = (TextView) findViewById(R.id.price);
        bags = (TextView) findViewById(R.id.bags);
        //textView = (TextView)textView.findViewById(R.id.totalAmount);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray= response.getJSONArray("orders");
                            double total = 0;
                            int numOfBagsSold = 0;
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject order = jsonArray.getJSONObject(i);
                                String email = order.getString("email");
                                if (email.equals("napoleon.batz@gmail.com")){
                                    double totalPrice = order.getDouble("total_price");
                                    total += totalPrice;
                                }else {
                                    continue;
                                }
                                System.out.println(email);
                                String id = order.getString("id");
                                System.out.println(id);
                            }
                            System.out.println(total);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject order = jsonArray.getJSONObject(i);
                                JSONArray itemArray = order.getJSONArray("line_items");
                                for (int j = 0; j < itemArray.length(); j++){
                                    JSONObject item = itemArray.getJSONObject(j);
                                    System.out.println(item.getString("title"));
                                    if (item.getString("title").equals("Awesome Bronze Bag")){
                                        int quantity = item.getInt("quantity");
                                        numOfBagsSold+= quantity;
                                    }
                                }
                            }
                            price.setText(String.valueOf(total));
                            bags.setText(String.valueOf(numOfBagsSold));

                            System.out.println(total);
                            System.out.println(numOfBagsSold);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
