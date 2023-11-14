package com.omapp.plztransfer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omapp.plztransfer.databinding.ItemTfBinding
import com.omapp.plztransfer.db.TFEntity

class TFRecyclerViewAdapter(private val tfList: ArrayList<TFEntity>, private val listener: OnItemLongClickListener) :
    RecyclerView.Adapter<TFRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTfBinding) : RecyclerView.ViewHolder(binding.root) {
        val item_number = binding.itemNumber
        val item_title = binding.itemTitle
        val item_score = binding.itemScore
        val item_percent = binding.itemPercent

        val root = binding.root
    }

    //layout binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTfBinding =
            ItemTfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // 각 DB의 항목을 내용에 넣기
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tfData = tfList[position]
        val score_text = "점수: " + tfData.examScore.toString()
        val percent_text = "백분율: " + tfData.examPercent.toString()

        holder.item_title.text = tfData.examTitle
        holder.item_score.text = score_text
        holder.item_percent.text = percent_text

        //해당 항목을 길게 눌렀을 때 리스너 함수
        holder.root.setOnLongClickListener {
            listener.onLongClick(position)
            false
        }
    }


    override fun getItemCount(): Int {
        return tfList.size
    }
}