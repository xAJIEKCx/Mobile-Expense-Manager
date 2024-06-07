package ajitsingh.com.expensemanager.model;

public class Expense {
  private String type;
  private String date;
  private Double amount;
  private Integer id;

  public Expense(Double amount, String type, String date) {
    this.type = type;
    this.date = date;
    this.amount = amount;
  }

  public Expense(Integer id, Double amount, String type, String date) {
    this(amount, type, date);
    this.id = id;
  }

  public Double getAmount() {
    return amount;
  }

  public String getType() {
    return type;
  }

  public String getDate() {
    return date;
  }

  public Integer getId() {
    return id;
  }
}
