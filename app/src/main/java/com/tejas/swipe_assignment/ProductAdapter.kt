package com.tejas.swipe_assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.tejas.swipe_assignment.datamodel.ProductItem

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var data: List<ProductItem> = listOf()

    class ProductViewHolder(val view: View): RecyclerView.ViewHolder(view){
        private val itemImage: ShapeableImageView = view.findViewById(R.id.image)
        private val priceTv: TextView = view.findViewById(R.id.price_tv)
        private val taxTv: TextView = view.findViewById(R.id.tax_tv)
        private val typeTv: TextView= view.findViewById(R.id.type_tv)
        private val nameTv: TextView = view.findViewById(R.id.name_tv)
        private val divider: View = view.findViewById(R.id.divider)

        private val context: Context = view.context
        fun bind(item: ProductItem, removeDivider: Boolean){
            Glide.with(context)
                .load(item.image)
                .error(ContextCompat.getDrawable(context, R.drawable.no_image_placeholder))
                .into(itemImage)

            priceTv.text = item.price.toCurrencyNotation()
            taxTv.text = item.tax.toTaxNotation()
            nameTv.text = item.product_name.trimAndCapitalizeFirstChar()
            typeTv.text = item.product_type

            if(removeDivider) divider.visibility = View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(
                parent.context
            )
                .inflate(R.layout.product_item, null)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = data[position]
        holder.bind(
            item = item,
            removeDivider = position==data.size-1
        )
    }

    fun setData(newData: ArrayList<ProductItem>){
        data = newData
        notifyDataSetChanged()
    }
}