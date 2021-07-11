package space.rodionov.dialogfragmentdriller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import space.rodionov.dialogfragmentdriller.databinding.ItemRecyclerBinding

class MyRecyclerViewAdapter(
    private val listener: OnItemClickListener,
    private val curCatName: String
) : ListAdapter<String, MyRecyclerViewAdapter.MyViewHolder>(MyRecyclerItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem)
    }

    inner class MyViewHolder(private val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val category = getItem(position)
                        listener.onItemClick(category)
                    }
                }
                radioButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val category = getItem(position)
                        listener.onItemClick(category)
                    }
                }
            }
        }

        fun bind(category: String) {
            binding.apply {
                tvItemname.text = category
                radioButton.isChecked = category == curCatName
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(text: String)
    }

    class MyRecyclerItemComparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
//            oldItem == newItem
    }
}







