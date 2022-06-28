package com.hudhud.megalot.AppUtils.Responses;

public class BigWinner {
    private String username, winningAmount, totalCombinations, winningCombinations;

    public BigWinner() {
        this.username = "";
        this.winningAmount = "";
        this.totalCombinations = "";
        this.winningCombinations = "";
    }

    public BigWinner(String username, String winningAmount, String totalCombinations, String winningCombinations) {
        this.username = username;
        this.winningAmount = winningAmount;
        this.totalCombinations = totalCombinations;
        this.winningCombinations = winningCombinations;
    }

    public String getUsername() {
        return username;
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
