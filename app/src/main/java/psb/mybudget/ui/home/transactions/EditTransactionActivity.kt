package psb.mybudget.ui.home.transactions

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.MyTransaction
import psb.mybudget.models.TransactionType
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.models.sql.DBConverters
import psb.mybudget.ui.home.budgets.EditBudgetActivityArgs
import psb.mybudget.ui.recyclers.MyRecycler
import psb.mybudget.ui.recyclers.adapters.NameAdapter
import psb.mybudget.ui.recyclers.createGridRecycler
import psb.mybudget.utils.INTENT_TRANSACTION_ID
import kotlin.math.abs

class EditTransactionActivity : AppCompatActivity() {

    private lateinit var textName: EditText
    private lateinit var textAmount: EditText
    private lateinit var calendar: CalendarView
    private lateinit var spinner: Spinner
    private lateinit var recycler: MyRecycler<NameAdapter, NameAdapter.Data>

    private lateinit var transaction: MyTransaction
    private var cancel: Boolean = false
    private var delete: Boolean = false
    private val appDatabase = AppDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        textName = findViewById(R.id.aet_editText_transactionName)
        textAmount = findViewById(R.id.aet_editNumber_transactionAmount)
        calendar = findViewById(R.id.aet_calendar_transactionDate)
        spinner = findViewById(R.id.aet_spinner_transactionStatus)

        val liveData = MutableLiveData<List<NameAdapter.Data>>()
        liveData.observe(this) { budgets ->
            recycler = createGridRecycler(
                budgets.toTypedArray(), NameAdapter::class.java,
                R.id.aet_recycler_transactionBudgets, R.layout.recycler_name, window.decorView, 4
            )
        }

        findViewById<Button>(R.id.aet_button_cancel).setOnClickListener {
            cancel = !cancel
            finish()
        }
        findViewById<Button>(R.id.aet_button_delete).setOnClickListener {
            delete = !delete
            finish()
        }

        val args: EditTransactionActivityArgs by navArgs()
        CoroutineScope(SupervisorJob()).launch {
            val id = args.StringTransactionId
            transaction = if(id == null) MyTransaction() else appDatabase.TransactionTable().getById(id)

            textName.setText(transaction.name)
            textAmount.setText(transaction.amount.toString())
            calendar.date = DBConverters().dateToTimestamp(transaction.date)!!
            ArrayAdapter.createFromResource(
                applicationContext,
                R.array.transaction_status_value,
                android.R.layout.simple_spinner_item
            )
                .also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    spinner.adapter = adapter
                    spinner.setSelection(transaction.transactionType.ordinal)
                }
            appDatabase.BudgetTable().getAllAndMatchByTransactionIdIn(transaction.ID, liveData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!cancel) {
            if(delete) {
                AppDatabase.getInstance().TransactionTable().remove(transaction)
            }
            else{
                if(textName.text.toString() != "" && textAmount.text.toString().toDouble() != 0.0) {
                    transaction.name = textName.text.toString()
                    transaction.amount = abs(textAmount.text.toString().toDouble())
                    transaction.date = DBConverters().fromTimestamp(calendar.date)!!
                    transaction.transactionType = TransactionType.fromInt(spinner.selectedItemPosition)

                    if(transaction.transactionType == TransactionType.PAID || transaction.transactionType == TransactionType.WITHHELD)
                        transaction.amount = -transaction.amount

                    appDatabase.TransactionTable().insertOrUpdate(transaction)
                }
                else{
                    Toast.makeText(this, "You must fill name and amount fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}