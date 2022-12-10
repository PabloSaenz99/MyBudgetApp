package psb.mybudget.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.utils.removeFragment

class EditBudgetActivity : AppCompatActivity() {

    private lateinit var budget: Budget

    private lateinit var textName: EditText
    private lateinit var textDescription: EditText

    private var cancel: Boolean = false
    private var delete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_budget)

        textName = findViewById(R.id.aeb_editText_budgetName)
        textDescription = findViewById(R.id.aeb_editText_budgetDescription)

        findViewById<Button>(R.id.aeb_button_cancel).setOnClickListener {
            cancel = !cancel
            finish()
        }
        findViewById<Button>(R.id.aeb_button_delete).setOnClickListener {
            delete = !delete
            finish()
        }

        CoroutineScope(SupervisorJob()).launch {
            val id = intent.getStringExtra("ID")
            budget = if(id == null) Budget() else AppDatabase.getInstance().BudgetTable().getById(id)

            textName.setText(budget.name)
            textDescription.setText(budget.description)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!cancel) {
            if(delete) {
                AppDatabase.getInstance().BudgetTable().remove(budget)
            }
            else if (textName.text.toString() != "") {
                budget.name = textName.text.toString()
                budget.description = textDescription.text.toString()

                AppDatabase.getInstance().BudgetTable().insertOrUpdate(budget)
            }
        }
    }
}