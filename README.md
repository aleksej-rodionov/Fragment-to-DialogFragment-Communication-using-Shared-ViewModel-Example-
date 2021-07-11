# Fragment-to-DialogFragment-Communication-using-Shared-ViewModel-Example-
The example of using Shared VewModel for DialogFragments to MainFragment Communication


```class EditTransactionViewModel() : ViewModel()``` is a shared viewModel 3 DialogFragments and 1 main Fragment:

- EditTransactionFragment is the main Fragment;
- DialogFragments: 
1) DatePickerDialogFragment (with datePicker);
2) ChooseCategoryDialogFragment (with RecyclerView inside. You can populate it by LiveData<List<Object>>, in which case ```setHasFixedSize()``` must be ```false``` (or just not set, for default state is false too) otherwise you'll not see your items)
3) ChooseDebtDialogFragment (it's logic is the same is in the previous dialog so I will not copypaste it's code below).

Shared ViewModel:

```
class EditTransactionViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    //=====================DATE=========================================
    var dateFormatted = state.getLiveData("date", sdf.format(System.currentTimeMillis()))

    //=====================SUM==========================================
    var sum = state.get<Float>("sum") ?: 0f
        set(value) {
            field = value
            state.set("sum", value)
        }

    //=====================CATEGORY=====================================
    var category = state.getLiveData("category", "Food")

    //=====================COMMENT======================================
    var comment = state.get<String>("comment") ?: ""
        set(value) {
            field = value
            state.set("comment", value)
        }

    //=====================DEBTS========================================
    var debtReduced = state.getLiveData("debtReduced", "")

}
```
  
MainFragment:
  
```
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
```

DatePickerDialogFragment:

```
class DatePickerDIalog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val TAG = "DatePickerDialog"
    }

    private val viewModel: EditTransactionViewModel by viewModels({ requireParentFragment() })
    val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateFormatted = viewModel.dateFormatted.value ?: sdf.format(System.currentTimeMillis()) // to viewModel
        val dateParsed = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(dateFormatted) // to viewModel
        val calendar = Calendar.getInstance() // to viewModel
        calendar.time = dateParsed
        val todayYear = calendar.get(Calendar.YEAR)
        val todayMonth = calendar.get(Calendar.MONTH)
        val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, todayYear, todayMonth, todayDay)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val newCalendar = Calendar.getInstance()
        newCalendar.set(Calendar.YEAR, year);
        newCalendar.set(Calendar.MONTH, month);
        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        val newDateFormatted = sdf.format(newCalendar.timeInMillis)
        viewModel.dateFormatted.value = newDateFormatted
    }
}
```

ChooseCategoryDialogFragment:

```
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
```
