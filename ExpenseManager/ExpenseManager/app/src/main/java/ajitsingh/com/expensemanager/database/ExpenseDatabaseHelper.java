package ajitsingh.com.expensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ajitsingh.com.expensemanager.model.Expense;
import ajitsingh.com.expensemanager.model.ExpenseType;
import ajitsingh.com.expensemanager.model.Income;
import ajitsingh.com.expensemanager.table.ExpenseTable;
import ajitsingh.com.expensemanager.table.ExpenseTypeTable;
import ajitsingh.com.expensemanager.table.IncomeTable;
import ajitsingh.com.expensemanager.utils.DateUtil;


import static ajitsingh.com.expensemanager.activity.BalanceActivity.balance;
import static ajitsingh.com.expensemanager.utils.DateUtil.getCurrentDate;
import static ajitsingh.com.expensemanager.utils.DateUtil.getCurrentWeeksDates;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {
  public static final String EXPENSE_DB = "expense";

  public ExpenseDatabaseHelper(Context context) {
    super(context, EXPENSE_DB, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(ExpenseTable.CREATE_TABLE_QUERY);
    sqLiteDatabase.execSQL(ExpenseTypeTable.CREATE_TABLE_QUERY);
    seedExpenseTypes(sqLiteDatabase);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

  }

  public List<String> getExpenseTypes() {
    ArrayList<String> expenseTypes = new ArrayList<>();

    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTypeTable.SELECT_ALL, null);

    if(isCursorPopulated(cursor)){
      do {
        String type = cursor.getString(cursor.getColumnIndex(ExpenseTypeTable.TYPE));
        expenseTypes.add(type);
      } while(cursor.moveToNext());
    }

    return expenseTypes;
  }

  public void deleteAll() {
    SQLiteDatabase database = this.getWritableDatabase();
    database.delete(ExpenseTypeTable.TABLE_NAME, "", new String[]{});
    database.delete(ExpenseTable.TABLE_NAME, "", new String[]{});
    database.close();
  }

  public void addExpense(Expense expense) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ExpenseTable.AMOUNT, expense.getAmount());
    values.put(ExpenseTable.TYPE, expense.getType());
    values.put(ExpenseTable.DATE, expense.getDate());
    //balance
    balance-=expense.getAmount();
    database.insert(ExpenseTable.TABLE_NAME, null, values);
  }

  public void addIncome(Income income) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(IncomeTable.AMOUNT, income.getAmount());
    values.put(IncomeTable.DATE, income.getDate());
    database.insert(IncomeTable.TABLE_NAME, null, values);
  }

  public List<Income> getIncome() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(IncomeTable.SELECT_ALL, null);
    return buildIncome(cursor);
  }

  public List<Expense> getTodaysExpenses() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.getExpensesForDate(getCurrentDate()), null);

    return buildExpenses(cursor);
  }

  public List<Expense> getCurrentWeeksExpenses() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.getConsolidatedExpensesForDates(getCurrentWeeksDates()), null);
    return buildExpenses(cursor);
  }

  public List<Expense> getExpensesGroupByCategory() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.SELECT_ALL_GROUP_BY_CATEGORY, null);
    return buildExpenses(cursor);
  }

  public List<Expense> getExpensesForCurrentMonthGroupByCategory() {
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(ExpenseTable.getExpenseForCurrentMonth(DateUtil.currentMonthOfYear()), null);
    return buildExpenses(cursor);
  }

  public void addExpenseType(ExpenseType type) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(ExpenseTable.TYPE, type.getType());

    database.insert(ExpenseTypeTable.TABLE_NAME, null, values);
  }

  public void truncate(String tableName) {
    SQLiteDatabase database = this.getWritableDatabase();
    database.execSQL("delete from " + tableName);
  }

  private List<Expense> buildExpenses(Cursor cursor) {
    List<Expense> expenses = new ArrayList<>();
    if(isCursorPopulated(cursor)){
      do {
        String type = cursor.getString(cursor.getColumnIndex(ExpenseTable.TYPE));
        String amount = cursor.getString(cursor.getColumnIndex(ExpenseTable.AMOUNT));
        String date = cursor.getString(cursor.getColumnIndex(ExpenseTable.DATE));
        String id = cursor.getString(cursor.getColumnIndex(ExpenseTable._ID));

        Expense expense = id == null ? new Expense(Double.parseDouble(amount), type, date) : new Expense(parseInt(id), Double.parseDouble(amount), type, date);
        expenses.add(expense);
      } while(cursor.moveToNext());
    }

    return expenses;
  }


  private List<Income> buildIncome(Cursor cursor) {
    List<Income> incomes = new ArrayList<>();
    if(isCursorPopulated(cursor)){
      do {
        String amount = cursor.getString(cursor.getColumnIndex(IncomeTable.AMOUNT));
        String date = cursor.getString(cursor.getColumnIndex(IncomeTable.DATE));
        String id = cursor.getString(cursor.getColumnIndex(IncomeTable._ID));

        Income income = id == null ? new Income(Double.parseDouble(amount), date) : new Income(parseInt(id), Double.parseDouble(amount), date);
        incomes.add(income);
      } while(cursor.moveToNext());
    }

    return incomes;
  }

  private boolean isCursorPopulated(Cursor cursor) {
    return cursor != null && cursor.moveToFirst();
  }

  private void seedExpenseTypes(SQLiteDatabase sqLiteDatabase) {
    List<ExpenseType> expenseTypes = ExpenseTypeTable.seedData();
    for (ExpenseType expenseType : expenseTypes) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(ExpenseTypeTable.TYPE, expenseType.getType());

      sqLiteDatabase.insert(ExpenseTypeTable.TABLE_NAME, null, contentValues);
    }
  }
}
