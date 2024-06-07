package ajitsingh.com.expensemanager.activity;

import static ajitsingh.com.expensemanager.utils.DateUtil.SAVED_TEXT;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import ajitsingh.com.expensemanager.R;

public class BalanceActivity extends FragmentActivity {
    public SharedPreferences sPref;

    public static Double balance = 0.;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sPref = getSharedPreferences("INC", MODE_PRIVATE);
        balance = Double.parseDouble(sPref.getString(SAVED_TEXT, "0.00"));
        setContentView(R.layout.balance_acivity);
    }

    public void addBalance(View view) {
        save();
        Toast.makeText(this, sPref.getString(SAVED_TEXT, ""), Toast.LENGTH_SHORT).show();
    }

    public void save() {
        sPref = getSharedPreferences("INC", MODE_PRIVATE);
        EditText balanceEd = findViewById(R.id.et_balance);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, balanceEd.getText().toString());
        ed.apply();

        balance = Double.parseDouble(sPref.getString(SAVED_TEXT, ""));
    }

}
