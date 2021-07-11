package space.rodionov.dialogfragmentdriller

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import space.rodionov.dialogfragmentdriller.databinding.FragmentEditTransacionBinding

private const val TAG = "MainFragment LOGS"

class EditTransactionFragment : Fragment(R.layout.fragment_edit_transacion) {

    private val viewModel: EditTransactionViewModel by viewModels()
    private lateinit var binding: FragmentEditTransacionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditTransacionBinding.bind(view)

        binding.apply {
            viewModel.dateFormatted.observe(viewLifecycleOwner) {
                tvDate.setText(it)
            }
            etTransactionSum.setText(viewModel.sum.toString())
            viewModel.category.observe(viewLifecycleOwner) {
                tvCategory.setText(it)
            }
            etTransactionComment.setText(viewModel.comment)
            viewModel.debtReduced.observe(viewLifecycleOwner) {
                Log.d(TAG, "onViewCreated: debtLiveDataValue = $it")
                tvCutDebt.text = if (!it.toString().isBlank()) "Cut down debt: $it" else "Is it debt repayment?"
            }


            //=========LISTENERS====================================
            ivDate.setOnClickListener {
                DatePickerDIalog().show(childFragmentManager, DatePickerDIalog.TAG)
            }
            etTransactionSum.addTextChangedListener {
                if (!it.toString().isBlank()) viewModel.sum = it.toString().toFloat() else 0f
            }
            layoutChooseCategory.setOnClickListener {
                CategoryDialog().show(childFragmentManager, CategoryDialog.TAG)
            }
            etTransactionComment.addTextChangedListener {
                viewModel.comment = it.toString()
            }
            layoutChooseDebt.setOnClickListener {
                DebtDialog().show(childFragmentManager, DebtDialog.TAG)
            }
        }
    }


}






