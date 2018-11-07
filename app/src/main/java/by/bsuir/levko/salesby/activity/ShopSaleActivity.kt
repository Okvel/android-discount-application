package by.bsuir.levko.salesby.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import by.bsuir.levko.salesby.R
import by.bsuir.levko.salesby.activity.constants.ShopConstants
import by.bsuir.levko.salesby.adapter.SaleCardAdapter
import by.bsuir.levko.salesby.data.ShopSaleItem
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ShopSaleActivity : AppCompatActivity() {

    private var saleItems = arrayListOf<ShopSaleItem>()

    private lateinit var toolbar: Toolbar
    private lateinit var shopName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_sale)

        getData()
        initToolbar()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = shopName
        toolbar.setNavigationIcon(R.drawable.arrow_left)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SaleCardAdapter(saleItems)
    }

    private fun getData() {
        shopName = intent.getStringExtra("shopName")

        val time = Calendar.getInstance().time
        val date = SimpleDateFormat("dd-MM-yyyy").format(time)
        val shopId = ShopConstants.getId(shopName.toLowerCase())
        val url = "https://gotoshop.by/apiv3/products/?limit=30&offset=0&date=$date&shop_id=$shopId&city_id=282"

        val queue = Volley.newRequestQueue(this.applicationContext)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val products = it["products"] as JSONArray
                for (i in 0 until products.length()) {
                    val product = (products.get(i) as JSONObject)
                    saleItems.add(
                        ShopSaleItem(
                            product.getString("name"),
                            product.getString("date"),
                            (product["imagefull"] as JSONObject).getString("src")
                        )
                    )
                }
                initRecyclerView()
            },
            Response.ErrorListener {
                Toast(this)
            })

        queue.add(request)
    }
}
