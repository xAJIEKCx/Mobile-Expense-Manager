package ajitsingh.com.expensemanager.model;

public class Income {

    private String date;
    private Double amount;
    private Integer id;

    public Income(Double amount, String date) {
        this.date = date;
        this.amount = amount;
    }

    public Income(Integer id, Double amount, String date) {
        this(amount, date);
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
