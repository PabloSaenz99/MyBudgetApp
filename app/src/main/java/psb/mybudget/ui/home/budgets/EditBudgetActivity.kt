package psb.mybudget.ui.home.budgets

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.navigation.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import psb.mybudget.R
import psb.mybudget.models.Budget
import psb.mybudget.models.sql.AppDatabase
import psb.mybudget.ui.MainActivity

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

        val colorNames = resources.getStringArray(R.array.budget_colors_name)

        radioGroup = findViewById(R.id.aeb_radioGroup_budgetColor)
        radioGroup.removeAllViewsInLayout()
        for(i in colorNames.indices){
            val rb = RadioButton(this)
            rb.text = colorNames[i] + "  "
            rb.background = MainActivity.getStrokeFromColorIntResolved(resources.getIntArray(R.array.budget_colors_int)[i])
            radioGroup.addView(rb)
        }
        radioGroup.check(-1)

        val instance = this
        radioGroup.setOnCheckedChangeListener { _, i ->
            val gd = GradientDrawable()
            gd.setColor(resources.getIntArray(R.array.budget_colors_int)[i - 1])
            instance.window.decorView.background = gd
        }

        CoroutineScope(SupervisorJob()).launch {
            val args: EditBudgetActivityArgs by navArgs()
            val id = args.StringBudgetId
            budget = if(id == null) Budget() else AppDatabase.getInstance().BudgetTable().getById(id)

            textName.setText(budget.name)
            textDescription.setText(budget.description)
            //TODO: check color
            //radioGroup.check( resources.getIntArray(R.array.budget_colors_int)[budget.color])
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
                if(radioGroup.checkedRadioButtonId > 0 && radioGroup.checkedRadioButtonId < resources.getIntArray(R.array.budget_colors_int).size)
                    budget.color = resources.getIntArray(R.array.budget_colors_int)[radioGroup.checkedRadioButtonId - 1]

                AppDatabase.getInstance().BudgetTable().insertOrUpdate(budget)
            }
        }
    }
}