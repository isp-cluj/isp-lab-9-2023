/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isp.lab9.exercise1.services;

import java.math.BigDecimal;

/**
 * Uses Lombok to get rid of boilerplate code.
 *
 * @author mihai.hulea
 * @author radu.miron
 */

public record StockItem(
        String symbol,
        BigDecimal price,
        BigDecimal change,
        String currency,
        String exchange,
        String name) {
    public StockItem() {
        this(null, new BigDecimal(0), new BigDecimal(0), null, null, null);
    }
}

