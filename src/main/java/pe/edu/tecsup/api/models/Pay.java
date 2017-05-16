package pe.edu.tecsup.api.models;

/**
 * Created by ebenites on 16/04/2017.
 */

public class Pay {

    private String concept;
    private String date;
    private String amount;
    private String voucher;

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    @Override
    public String toString() {
        return "Pay{" +
                "concept='" + concept + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", voucher='" + voucher + '\'' +
                '}';
    }
}
