package am.a_t.dailyapp.presentation.adapter

import am.a_t.dailyapp.R
import am.a_t.dailyapp.databinding.DialogDeleteBinding
import am.a_t.dailyapp.databinding.ItemTodoBinding
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.presentation.ui.mainFragment.MainViewModel
import am.a_t.dailyapp.domain.utils.ListColor
import android.app.AlertDialog
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ListTodoAdapter(
    private val context: Context,
    private var inflater: LayoutInflater,
    private var container: ViewGroup?,
    private val viewModel: MainViewModel,
    private val click: (Boolean, Boolean, ListTodo?) -> Unit,
) :
    ListAdapter<ListTodo, ListTodoAdapter.MyViewHolder>(DiffUtilItemCallBackList()) {

    private lateinit var myDialog: DialogDeleteBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTodoBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(list: ListTodo) {

            with(binding) {

                tvTitle.isSelected = true
                tvDate.text = list.listDate

                if (!list.listIsChecked) {
                    tvTitle.text = list.listTitle
                } else {
                    val spannableString = SpannableString(list.listTitle)
                    val strikethroughSpan = StrikethroughSpan()
                    spannableString.setSpan(
                        strikethroughSpan,
                        0,
                        list.listTitle.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvTitle.text = spannableString
                }

                when (list.listColor) {
                    ListColor.RED -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_red)
                    }
                    ListColor.BLUE -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_blue)
                    }
                    ListColor.ORANGE -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_orange)
                    }
                    ListColor.PURPLE -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_purple)
                    }
                    else -> {
                        viewTodo.setBackgroundResource(R.drawable.btn_red)
                    }
                }

                btnIsChecked.isChecked = list.listIsChecked

                btnDelete.setOnClickListener {
                    removeDialog(inflater, container, list)
                }

                btnIsChecked.setOnClickListener {
                    viewModel.updateList(list.copy(listIsChecked = btnIsChecked.isChecked))
                }

                btnList.setOnClickListener {
                    click(false, false, list)
                }


                btnEdit.setOnClickListener {
                    click(true, false, list)
                }

            }
        }
    }

    private fun removeDialog(inflater: LayoutInflater, container: ViewGroup?, list: ListTodo) {

        myDialog = DialogDeleteBinding.inflate(inflater, container, false)

        with(myDialog) {

            alertDialog = AlertDialog.Builder(context)
                .setView(root)
                .show()

            when (list.listColor) {
                ListColor.RED -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_red)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_red)
                }
                ListColor.BLUE -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_blue)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_blue)
                }
                ListColor.ORANGE -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_orange)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_orange)
                }
                ListColor.PURPLE -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_purple)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_purple)
                }
                else -> {
                    btnYesTodo.setBackgroundResource(R.drawable.btn_red)
                    btnNoTodo.setBackgroundResource(R.drawable.btn_red)
                }
            }

            btnYesTodo.setOnClickListener {
                viewModel.removeList(list)
                click(false, true, null)
                alertDialog.dismiss()
            }

            btnNoTodo.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

class DiffUtilItemCallBackList : DiffUtil.ItemCallback<ListTodo>() {

    override fun areItemsTheSame(oldItem: ListTodo, newItem: ListTodo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ListTodo, newItem: ListTodo): Boolean =
        oldItem == newItem

}