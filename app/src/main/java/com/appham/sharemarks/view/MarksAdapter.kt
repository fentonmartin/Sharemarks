package com.appham.sharemarks.view

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.appham.sharemarks.R
import com.appham.sharemarks.model.MarkItem
import com.appham.sharemarks.presenter.MarksContract
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

/**
 * @author thomas
 */
class MarksAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val marks = mutableListOf<MarkItem>()

    private lateinit var context: Context

    private val screenWidthPx by lazy { Resources.getSystem().displayMetrics.widthPixels }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.mark_item, parent, false)
        return MarkHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is MarkHolder) {
            val item = marks[position]
            holder.txtTitle.text = position.toString() + " - " + item.title
            holder.txtContent.text = item.content
            holder.txtReferrer.text = item.referrer + " | " + item.domain
            if (item.imageUrl != null) {
                Picasso.with(context).load(item.imageUrl)
                        .resize(screenWidthPx / 3,
                                screenWidthPx / 4)
                        .placeholder(R.mipmap.ic_launcher)
                        .onlyScaleDown()
                        .centerInside()
                        .transform(RoundedCornersTransformation(10, 10))
                        .into(holder.imgMark)
                holder.imgMark.visibility = View.VISIBLE
            } else {
                holder.imgMark.visibility = View.GONE
            }

            // add click listener to open browser
            holder.itemView.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
                //TODO: open in a webview instead
            }

            holder.itemView.setOnLongClickListener {
                (context as MarksContract.View).showShareChooser(item)
                true
            }

            holder.itemView.tag = item
        }

        //TODO: maybe need different layouts for big and small images
    }

    override fun getItemCount(): Int {
        return marks.size
    }

    fun addItem(markItem: MarkItem) {
        marks.add(0, markItem)
    }

    fun removeItem(item: MarkItem) {
        marks.remove(item)
    }

    internal inner class MarkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardItem: CardView = itemView.findViewById(R.id.cardMarkItem)
        val layItem: RelativeLayout = itemView.findViewById(R.id.layMarkItem)
        val imgMark: ImageView = itemView.findViewById(R.id.imgMark)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtContent: TextView = itemView.findViewById(R.id.txtContent)

        val txtReferrer: TextView = itemView.findViewById(R.id.txtReferrer)

    }
}