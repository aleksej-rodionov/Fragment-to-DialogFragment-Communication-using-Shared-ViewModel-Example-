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
import timber.log.Timber

class CategoryDialog : DialogFragment(), MyRecyclerViewAdapter.OnItemClickListener {

    companion object {
        const val TAG = "DatePickerDialog"
    }

    private val viewModel: EditTransactionViewModel by viewModels({ requireParentFragment() })

    private lateinit var chooseCatAdapter: MyRecyclerViewAdapter

    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    val categories = listOf(
        "Food",
        "Transport",
        "Healthcare",
        "Other",
        "Joy",
        "Credit",
        "Communication",
        "ZhKU",
        "Brother"
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Timber.d("LOGS onCreateDialog vizvan")
        _binding = FragmentRecyclerBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("Choose fucking category")
            .setView(binding.root)
            .setNegativeButton("Cancel", null)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("LOGS onCreateView vizvan")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("LOGS onViewCreated vizvan")

        val category = viewModel.category.value ?: "Food"
        chooseCatAdapter = MyRecyclerViewAdapter(this, category)

        binding.apply {
            recyclerView.apply {
                adapter = chooseCatAdapter
                layoutManager = LinearLayoutManager(requireContext())
                chooseCatAdapter.submitList(categories)
            }
        }

    }

    override fun onItemClick(text: String) {
        viewModel.category.value = text
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





