package ajitsingh.com.expensemanager.presenter;

import ajitsingh.com.expensemanager.activity.CurrentMonthExpenseFragment;
import ajitsingh.com.expensemanager.activity.CurrentWeekExpenseFragment;
import ajitsingh.com.expensemanager.view.NavigationDrawerItemView;

public class NavigationDrawerPresenter {

  public static final String THIS_WEEK = "Эта неделя";
  public static final String THIS_MONTH = "Этот месяц";
  public static final String HOME = "На главную";
  private NavigationDrawerItemView view;

  public NavigationDrawerPresenter(NavigationDrawerItemView view) {
    this.view = view;
  }

  public void onItemSelected(String drawerItem) {
    switch (drawerItem){
      case THIS_WEEK:
        view.render(new CurrentWeekExpenseFragment());
        break;
      case THIS_MONTH:
        view.render(new CurrentMonthExpenseFragment());
        break;
      case HOME:
        view.goToHome();
        break;
    }
  }
}
