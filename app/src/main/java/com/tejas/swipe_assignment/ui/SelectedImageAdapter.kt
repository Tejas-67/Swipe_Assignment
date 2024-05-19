package com.tejas.swipe_assignment.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.tejas.swipe_assignment.R
import com.tejas.swipe_assignment.util.OnClickListener

class SelectedImageAdapter(
    private val listener: OnClickListener
): RecyclerView.Adapter<SelectedImageAdapter.SelectedImageViewHolder>() {
    private var data: ArrayList<Uri> = arrayListOf()

    class SelectedImageViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val imageView = view.findViewById<ShapeableImageView>(R.id.iv)
        val remoteBtn = view.findViewById<ShapeableImageView>(R.id.remove_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.selected_image_item, null)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        val item = data[position]
        holder.imageView.setImageURI(item)
        holder.remoteBtn.setOnClickListener {
            listener.onClick(position)
        }
    }
    fun updateList(newList: ArrayList<Uri>){
        data = newList
        notifyDataSetChanged()
    }
}