package khomenko.pharmacy.domain;

import lombok.ToString;

@ToString(of = {"drugSearchName", "amount"})
public class OrderItem {

    private String drugSearchName;
    private Integer amount;

    public String getDrugSearchName() {
        return drugSearchName;
    }

    public void setDrugSearchName(String drugSearchName) {
        this.drugSearchName = drugSearchName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
