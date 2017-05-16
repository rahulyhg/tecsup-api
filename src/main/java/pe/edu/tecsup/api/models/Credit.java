package pe.edu.tecsup.api.models;

import java.util.List;

/**
 * Created by ebenites on 16/04/2017.
 */

public class Credit {

    private String assigned;
    private String ajusted;
    private String paid;
    private String balance;
    private String startdate;
    private String enddate;
    private String extrainfo;
    private List<Debt> debts;
    private List<Pay> pays;

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getAjusted() {
        return ajusted;
    }

    public void setAjusted(String ajusted) {
        this.ajusted = ajusted;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getExtrainfo() {
        return extrainfo;
    }

    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public List<Pay> getPays() {
        return pays;
    }

    public void setPays(List<Pay> pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "assigned='" + assigned + '\'' +
                ", ajusted='" + ajusted + '\'' +
                ", paid='" + paid + '\'' +
                ", balance='" + balance + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", extrainfo='" + extrainfo + '\'' +
                ", debts=" + debts +
                ", pays=" + pays +
                '}';
    }
}
