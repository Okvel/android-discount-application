package by.bsuir.levko.salesby.task

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.net.URL

class DownloadImageAsyncTask(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg params: String?): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            val inputStream = URL(params[0]!!).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        imageView.setImageBitmap(result)
    }
}