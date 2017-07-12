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
    private Double totalmonths;
    private Double elapsedmonths;
    private Double remainingmonths;
    private Double payedfee;
    private Double elapsedfee;
    private Double remainingfee;

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

    public Double getTotalmonths() {
        return totalmonths;
    }

    public void setTotalmonths(Double totalmonths) {
        this.totalmonths = totalmonths;
    }

    public Double getElapsedmonths() {
        return elapsedmonths;
    }

    public void setElapsedmonths(Double elapsedmonths) {
        this.elapsedmonths = elapsedmonths;
    }

    public Double getRemainingmonths() {
        return remainingmonths;
    }

    public void setRemainingmonths(Double remainingmonths) {
        this.remainingmonths = remainingmonths;
    }

    public Double getPayedfee() {
        return payedfee;
    }

    public void setPayedfee(Double payedfee) {
        this.payedfee = payedfee;
    }

    public Double getElapsedfee() {
        return elapsedfee;
    }

    public void setElapsedfee(Double elapsedfee) {
        this.elapsedfee = elapsedfee;
    }

    public Double getRemainingfee() {
        return remainingfee;
    }

    public void setRemainingfee(Double remainingfee) {
        this.remainingfee = remainingfee;
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
                ", totalmonths=" + totalmonths +
                ", elapsedmonths=" + elapsedmonths +
                ", remainingmonths=" + remainingmonths +
                ", payedfee=" + payedfee +
                ", elapsedfee=" + elapsedfee +
                ", remainingfee=" + remainingfee +
                '}';
    }
}
