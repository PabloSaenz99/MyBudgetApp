package psb.mybudget.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.utils.INTENT_BUDGET_ID

class EditBudgetActivity : AppCompatActivity() {

    private lateinit var budget: Budget

    private lateinit var textName: EditText
    private lateinit var textDescription: EditText
    private lateinit var radioGroup: RadioGroup

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
            val id = intent.getStringExtra(INTENT_BUDGET_ID)
            budget = if(id == null) Budget() else AppDatabase.getInstance().BudgetTable().getById(id)

            textName.setText(budget.name)
            textDescription.setText(budget.description)
        }

        val colorNames = resources.getStringArray(R.array.budget_colors_name)
        val colorInts = resources.getIntArray(R.array.budget_colors_int)

        radioGroup = findViewById(R.id.aeb_radioGroup_budgetColor)
        radioGroup.removeAllViewsInLayout()
        for(i in colorNames.indices){
            val rb = RadioButton(this)
            rb.text = colorNames[i]
            rb.setBackgroundColor(colorInts[i])
            radioGroup.addView(rb)
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
                budget.color = resources.getIntArray(R.array.budget_colors_int)[radioGroup.checkedRadioButtonId - 1]

                AppDatabase.getInstance().BudgetTable().insertOrUpdate(budget)
            }
        }
    }
}