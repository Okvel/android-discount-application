package by.bsuir.levko.salesby.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import by.bsuir.levko.salesby.R
import by.bsuir.levko.salesby.data.ShopSaleItem
import by.bsuir.levko.salesby.task.DownloadImageAsyncTask

class SaleCardAdapter(private val dataset: List<ShopSaleItem>) : RecyclerView.Adapter<SaleCardAdapter.SaleCardViewHolder>() {

    class SaleCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val headerView: TextView = view.findViewById(R.id.header_text)
        val termView: TextView = view.findViewById(R.id.term_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sale_card, parent, false)
        return SaleCardViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: SaleCardViewHolder, position: Int) {
        val item = dataset[position]
        DownloadImageAsyncTask(holder.imageView).execute(item.picUrl)
        holder.headerView.textSize = 18f
        holder.headerView.text = item.header
        holder.termView.text = item.term
    }
}