/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package isp.lab9.exercise1.services;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Uses Lombok to get rid of boilerplate code.
 *
 * @author mihai.hulea
 * @author radu.miron
 */
public class UserPortfolio {
    private BigDecimal cash;
    private Map<String, Integer> shares; // a map of number of shares by stock symbol

    public UserPortfolio(){
    }

    public UserPortfolio(BigDecimal cash, Map<String, Integer> shares) {
        this.cash = cash;
        this.shares = shares;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public Map<String, Integer> getShares() {
        return shares;
    }

    public void setShares(Map<String, Integer> shares) {
        this.shares = shares;
    }

    @Override
    public String toString() {
        return "UserPortfolio{" +
                "cash=" + cash +
                ", shares=" + shares +
                '}';
    }
}
