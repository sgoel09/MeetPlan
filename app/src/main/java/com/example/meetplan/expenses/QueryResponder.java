package com.example.meetplan.expenses;

import com.example.meetplan.expenses.models.Expense;

public interface QueryResponder {
    void passNewExpense(Expense expense);
}
