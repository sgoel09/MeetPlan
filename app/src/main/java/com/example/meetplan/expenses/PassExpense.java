package com.example.meetplan.expenses;

import com.example.meetplan.expenses.models.Expense;

/** Interface that allows the create expense dialog fragment to pass back the
 * newly created expense to the expense fragment, in order to update the adapter. */
public interface PassExpense {

    /** Passes in a new expense upon creation. */
    void passNewExpense(Expense expense);
}
