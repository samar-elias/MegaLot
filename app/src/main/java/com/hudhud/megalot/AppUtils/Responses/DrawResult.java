package com.hudhud.megalot.AppUtils.Responses;

public class DrawResult {
    private String winningAmount, totalCombinations, winningCombinations;

    public DrawResult(String winningAmount, String totalCombinations, String winningCombinations) {
        this.winningAmount = winningAmount;
        this.totalCombinations = totalCombinations;
        this.winningCombinations = winningCombinations;
    }

    public String getWinningAmount() {
        return winningAmount;
    }

    public String getTotalCombinations() {
        return totalCombinations;
    }

    public String getWinningCombinations() {
        return winningCombinations;
    }
}
