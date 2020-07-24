package com.example.meetplan.expenses;

import com.example.meetplan.models.Expense;

public interface QueryResponder {

    void passNewExpense(Expense expense);
}
