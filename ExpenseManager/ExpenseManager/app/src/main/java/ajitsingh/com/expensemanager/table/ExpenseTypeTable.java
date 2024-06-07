package ajitsingh.com.expensemanager.table;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ajitsingh.com.expensemanager.model.ExpenseType;

public class ExpenseTypeTable implements BaseColumns {
  public static final String TABLE_NAME = "expense_types";
  public static final String TYPE = "type";

  public static final String CREATE_TABLE_QUERY = "create table " + TABLE_NAME + " ("+ _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ TYPE +" TEXT)";
  public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

  public static List<ExpenseType> seedData(){
    ArrayList<ExpenseType> expenseTypes = new ArrayList<>();
    expenseTypes.add(new ExpenseType("Еда"));
    expenseTypes.add(new ExpenseType("Путешествовие"));
    expenseTypes.add(new ExpenseType("Здоровье"));
    expenseTypes.add(new ExpenseType("Покупки"));
    expenseTypes.add(new ExpenseType("Аренда"));
    expenseTypes.add(new ExpenseType("Перевод денег"));
    expenseTypes.add(new ExpenseType("Другое"));

    return expenseTypes;
  }
}
