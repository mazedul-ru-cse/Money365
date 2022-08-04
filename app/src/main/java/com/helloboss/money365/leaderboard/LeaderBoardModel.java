package com.helloboss.money365.leaderboard;

public class LeaderBoardModel {

    String rank;
    String name;
    String amount;

    public LeaderBoardModel() {
    }

    public LeaderBoardModel(String rank, String name, String amount) {
        this.rank = rank;
        this.name = name;
        this.amount = amount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
