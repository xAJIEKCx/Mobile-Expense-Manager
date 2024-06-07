package ajitsingh.com.expensemanager.activity;

import static android.content.Context.MODE_PRIVATE;
import static ajitsingh.com.expensemanager.activity.BalanceActivity.balance;
import static ajitsingh.com.expensemanager.utils.DateUtil.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ajitsingh.com.expensemanager.R;
import ajitsingh.com.expensemanager.database.ExpenseDatabaseHelper;
import ajitsingh.com.expensemanager.presenter.ExpensePresenter;
import ajitsingh.com.expensemanager.utils.DateUtil;
import ajitsingh.com.expensemanager.view.ExpenseView;

public class ExpenseFragment extends Fragment implements ExpenseView, View.OnClickListener {

    public SharedPreferences sPref;

    public void save(Double b) {
        sPref = this.getActivity().getSharedPreferences("INC", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, String.valueOf(b));
        ed.apply();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_expense, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ExpenseDatabaseHelper expenseDatabaseHelper = new ExpenseDatabaseHelper(this.getActivity());
        ExpensePresenter expensePresenter = new ExpensePresenter(expenseDatabaseHelper, this);
        expensePresenter.setExpenseTypes();
        expenseDatabaseHelper.close();

        sPref = this.getActivity().getSharedPreferences("INC", MODE_PRIVATE);
        balance = Double.parseDouble(sPref.getString(SAVED_TEXT, "0.00"));


   /* final String savedText;

    if(balance!=null){
      savedText = String.valueOf(balance);
    }else{
      savedText = "0.00";
    }
*/

        final TextView balanceTx = getActivity().findViewById(R.id.balance_txt);
        final EditText balanceEd = getActivity().findViewById(R.id.et_balance_e);
        balanceTx.setText(balance.toString() + getString(R.string.rupee_sym));


        Button addExpenseButton = (Button) getActivity().findViewById(R.id.add_expense);
        Button addButton = (Button) getActivity().findViewById(R.id.add_income);
        addExpenseButton.setOnClickListener(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balance += Double.parseDouble(balanceEd.getText().toString());
                save(balance);
                balanceTx.setText(balance.toString() + getString(R.string.rupee_sym));
            }
        });

   /* addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //EditText balanceEd = getActivity().findViewById(R.id.et_balance_e);
        balance+=Double.parseDouble(balanceEd.getText().toString());
        Toast.makeText("dfsf", balanceEd.getText().toString(), Toast.LENGTH_LONG).show();

      }
    });*/
    }


    @Override
    public String getAmount() {
        TextView view = (TextView) getActivity().findViewById(R.id.amount);
        return view.getText().toString();

    }

    @Override
    public String getType() {
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.expense_type);
        return (String) spinner.getSelectedItem();
    }

    @Override
    public void renderExpenseTypes(List<String> expenseTypes) {
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.expense_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, expenseTypes);
        spinner.setAdapter(adapter);
    }

    @Override
    public void displayError() {
        TextView view = (TextView) getActivity().findViewById(R.id.amount);
        view.setError(getActivity().getString(R.string.amount_empty_error));
    }

    @Override
    public void onClick(View view) {
        TextView balanceTx = getActivity().findViewById(R.id.balance_txt);
        EditText balanceEd = getActivity().findViewById(R.id.amount);
        balance -= Double.parseDouble(balanceEd.getText().toString());
        save(balance);
        balanceTx.setText(balance.toString() + getString(R.string.rupee_sym));


        ExpenseDatabaseHelper expenseDatabaseHelper = new ExpenseDatabaseHelper(this.getActivity());
        ExpensePresenter expensePresenter = new ExpensePresenter(expenseDatabaseHelper, this);
        if (expensePresenter.addExpense()) {
            MainActivity activity = (MainActivity) getActivity();
            Toast.makeText(activity, R.string.expense_add_successfully, Toast.LENGTH_LONG).show();
            activity.onExpenseAdded();
        }
        expenseDatabaseHelper.close();
    }


}
