package by.bsuir.levko.salesby.task

import android.os.AsyncTask
import by.bsuir.levko.salesby.data.MapMarkerItem
import com.google.android.gms.maps.model.LatLng
import org.jsoup.Jsoup
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MarkerDataParserAsyncTask : AsyncTask<Void, Void, List<MapMarkerItem>>() {

    override fun doInBackground(vararg params: Void): List<MapMarkerItem> {

        val markerItems = arrayListOf<MapMarkerItem>()

        val time = Calendar.getInstance().time
        val date = SimpleDateFormat("dd-MM-yyyy").format(time)
        val url = "https://gotoshop.by/minsk/$date/produkty/?show=map"

        try {
            val document = Jsoup.connect(url).get()
            val items = document.select("a.list-group-item")
            for (item in items) {
                markerItems.add(
                    MapMarkerItem(
                        item.attr("data-name"),
                        LatLng(item.attr("data-lat").toDouble(), item.attr("data-lng").toDouble()),
                        item.select("span.address").text()
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return markerItems
    }
}