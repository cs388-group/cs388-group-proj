package com.example.legisly.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.legisly.databinding.ItemBillBinding
import com.example.legisly.models.Bill

class BillAdapter(
    private var bills: List<Bill> = emptyList()
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    class BillViewHolder(
        val binding: ItemBillBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillViewHolder {

        val binding = ItemBillBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BillViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BillViewHolder,
        position: Int
    ) {
        val bill = bills[position]

        holder.binding.apply {
            billNumber.text = bill.number
            billTitle.text = bill.title
            billLastAction.text =
                if (bill.lastAction.isBlank()) {
                    "Latest action: Not available"
                } else {
                    "Latest action: ${bill.lastAction}"
                }

            billActionDate.text =
                if (bill.lastActionDate.isBlank()) {
                    "Date unavailable"
                } else {
                    "Date: ${bill.lastActionDate}"
                }
        }
    }

    override fun getItemCount(): Int = bills.size

    fun updateBills(newBills: List<Bill>) {
        bills = newBills
        notifyDataSetChanged()
    }
}
