package khomenko.pharmacy.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@ToString(of = {"id", "pharmId", "drugId", "useUntilDate", "amount"})
@EqualsAndHashCode(of = {"id"})
public class    Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer pharmId;

    private Integer drugId;

    @Temporal(TemporalType.DATE)
    private Date useUntilDate;

    private Integer amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPharmId() {
        return pharmId;
    }

    public void setPharmId(Integer pharm_id) {
        this.pharmId = pharm_id;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drug_id) {
        this.drugId = drug_id;
    }

    public Date getUseUntilDate() {
        return useUntilDate;
    }

    public void setUseUntilDate(Date useUntilDate) {
        this.useUntilDate = useUntilDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
