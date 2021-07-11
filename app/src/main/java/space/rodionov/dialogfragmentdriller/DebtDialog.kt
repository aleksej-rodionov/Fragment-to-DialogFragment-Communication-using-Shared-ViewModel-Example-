package space.rodionov.dialogfragmentdriller

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import space.rodionov.dialogfragmentdriller.databinding.FragmentRecyclerBinding

class DebtDialog : DialogFragment(), MyRecyclerViewAdapter.OnItemClickListener {

    companion object {
        const val TAG = "DebtDialog"
    }

    private val viewModel: EditTransactionViewModel by viewModels({ requireParentFragment() })
    private lateinit var debtAdapter: MyRecyclerViewAdapter
    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!
    val debts = listOf("Credit", "ZhKU", "Credit card", "Pete Vilochkinu")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentRecyclerBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireContext())
            .setTitle("Choose debt to reduce")
            .setView(binding.root)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val debt = viewModel.debtReduced.value ?: ""
        debtAdapter = MyRecyclerViewAdapter(this, debt)
        binding.apply {
            recyclerView.apply {
                adapter = debtAdapter
                layoutManager = LinearLayoutManager(requireContext())
                debtAdapter.submitList(debts)
            }
        }
    }

    override fun onItemClick(text: String) {
        viewModel.debtReduced.value = text
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




