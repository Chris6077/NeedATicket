package me.projectx.needaticket.pojo;

public class AmountWrapper {
    private Float amount;

    public AmountWrapper(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AmountWrapper{" +
                "amount=" + amount +
                '}';
    }
}
