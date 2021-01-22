package com.eloAlgo.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EloRanking {

    private static final int K_FACTOR = 30;
    private static final long DEFAULT_SCORE = 1500L;

    private Map<Long, Player> playerMap;

    public static void main(String[] args) {
        File names = null;
        File matches = null;
        String command = "";
        String sortBy = "";
        int length = args.length;
        EloRanking eloRanking = new EloRanking();
        eloRanking.playerMap = new HashMap<>();

        if (length < 2) {
            System.out.println("Invalid Argument!!");
            System.exit(1);
        }
        if (length >= 2) {
            names = new File(args[0]);
            matches = new File(args[1]);
        }
        if (length >= 3) {
            command = args[2];
        }
        if (length >= 4) {
            sortBy = args[3];
        }

        eloRanking.executeCommand(command, sortBy, names, matches);
    }

    //2.Generate a list of players sorted by score, their ranking (position in the list) and their number of wins and losses.
    //
    private void executeCommand(String command, String sortBy, File names, File matches) {

        switch (command.trim()) {
            case "":
                playerScored(names, matches, "");
                break;
            case "view_players_scores":
                playerScored(names, matches, sortBy);
                break;
            case "generate_player_report":
                playersDetailInfo(names, matches, sortBy);
                break;
            case "suggested_next_matches":
                getSuggestedMatches(names, matches, sortBy);
                break;
        }

    }

    //4.Generate a list of suggested next matches.
    public void getSuggestedMatches(File names, File matches, String sortBy) {
        try {
            savePlayerInfo(names);
            List<Player> playerList = updatePlayerData(matches, sortBy);

            playerList.forEach(player -> {
                List<Long> playedWith = new LinkedList<>();
                playedWith.addAll(player.getWonAgainst());
                playedWith.addAll(player.getLostAgainst());
                playerList.forEach(players -> {
                            if (players != player && !playedWith.contains(players.getPlayerId())) {
                                System.out.println(players.getName() + " (" + players.getPlayerId() + ", " + players.getScore() + ") vs. " + player.getName() + "( " + player.getPlayerId() + ", " + player.getScore() + ")");
                            }
                        }
                );
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Something went wrong - " + ex.getMessage());
        }

    }

    //3.Generate a report for each person, showing with whom they played and how they fared.
    private void playersDetailInfo(File names, File matches, String sortBy) {
        try {
            savePlayerInfo(names);
            System.out.println("Rank       ID   Name    Win    Lose    Score          WinAgainstId          LoseAgainstId");
            System.out.println("---------------------------------------------------------------------------------------------");
            updatePlayerData(matches, sortBy).stream().forEach(player -> System.out.println(player.rank + "   |    " + player.playerId + "  |  " + player.name + "  |   " + player.noOfWins + "   |   " + player.noOfLost + "  |   " + player.score + "     |     " + player.wonAgainst.stream().map(id -> playerMap.get(id).name).collect(Collectors.toList()) + "     |     " + player.lostAgainst.stream().map(id -> playerMap.get(id).name).collect(Collectors.toList())));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Something went wrong - " + ex.getMessage());
        }
    }

    //2.Generate a list of players sorted by score, their ranking (position in the list) and their number of wins and losses.
    private void playerScored(File names, File matches, String sortBy) {
        try {
            savePlayerInfo(names);
            System.out.println("Rank       ID    Name    Win    Lose    Score");
            System.out.println("---------------------------------------------------------------------------------------------");

            updatePlayerData(matches, sortBy).stream().forEach(player -> System.out.println(player.rank + "   |     " + player.playerId + "  |  " + player.name + " |  " + player.noOfWins + "  |  " + player.noOfLost + "  |  " + player.score));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Something went wrong - " + ex.getMessage());
        }
    }

    private void savePlayerInfo(File names) throws FileNotFoundException {

        InputStream nameStream = new FileInputStream(names);

        Scanner sc = new Scanner(nameStream);

        while (sc.hasNext()) {
            String player[] = sc.nextLine().trim().replaceAll("\\s+", " ").split(" ");
            if (player.length == 2) {
                try {
                    playerMap.put(Long.parseLong(player[0]), new Player(Long.parseLong(player[0]), player[1]));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println("Invalid input file - " + ex.getMessage());
                }

            }

        }
    }

    //1.Score each player based on the games played
    public List<Player> updatePlayerData(File matches, String sortBy) throws FileNotFoundException {

        InputStream nameStream = new FileInputStream(matches);
        Scanner sc = new Scanner(nameStream);

        while (sc.hasNext()) {
            String participants[] = sc.nextLine().trim().replaceAll("\\s+", " ").split(" ");
            if (participants.length == 2) {

                Player winner = playerMap.get(Long.parseLong(participants[0].trim()));
                Player lost = playerMap.get(Long.parseLong(participants[1].trim()));
                if (winner != null && lost != null) {
                    winner.playerWon(lost.getScore());
                    winner.wonAgainst(lost.getPlayerId());
                    lost.playerLost(winner.getScore());
                    lost.lostAgainst(winner.getPlayerId());
                }
            }

        }

        List<Player> playerList = playerMap.values().stream().sorted(Comparator.comparing(Player::getScore).reversed()).collect(Collectors.toCollection(LinkedList::new));
        playerList.forEach(player -> player.setRank(playerList.indexOf(player) + 1));
        return sortedList(playerList, sortBy);
    }

    private List<Player> sortedList(List<Player> playerList, String sortBy) {
        switch (sortBy.trim()) {
            case "ID":
                return playerList.stream().sorted(Comparator.comparing(Player::getPlayerId)).collect(Collectors.toList());
            case "WINS":
                return playerList.stream().sorted(Comparator.comparing(Player::getNoOfWins).reversed()).collect(Collectors.toList());
            case "LOSES":
                return playerList.stream().sorted(Comparator.comparing(Player::getNoOfLost).reversed()).collect(Collectors.toList());
            case "RANK":
                return playerList.stream().sorted(Comparator.comparing(Player::getRank)).collect(Collectors.toList());
            case "SCORE":
                return playerList.stream().sorted(Comparator.comparing(Player::getScore).reversed()).collect(Collectors.toList());
        }
        return playerList.stream().sorted(Comparator.comparing(Player::getPlayerId)).collect(Collectors.toList());
    }


    public class Player {

        private Long playerId;

        private String name;

        private int noOfWins;

        private int noOfLost;

        private long score;

        private int rank;

        private List<Long> wonAgainst;

        private List<Long> lostAgainst;

        public Player(Long playerId, String name) {
            this.playerId = playerId;
            this.name = name;
            this.score = EloRanking.DEFAULT_SCORE;
            this.wonAgainst = new LinkedList<>();
            this.lostAgainst = new LinkedList<>();
        }

        public Long getPlayerId() {
            return playerId;
        }

        public String getName() {
            return name;
        }

        public int getNoOfWins() {
            return noOfWins;
        }

        public int getNoOfLost() {
            return noOfLost;
        }

        public List<Long> getWonAgainst() {
            return wonAgainst;
        }

        public List<Long> getLostAgainst() {
            return lostAgainst;
        }

        public long getScore() {
            return score;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "playerId=" + playerId +
                    ", name='" + name + '\'' +
                    ", noOfWins=" + noOfWins +
                    ", noOfLost=" + noOfLost +
                    ", score=" + score +
                    ", rank=" + rank +
                    ", wonAgainst=" + wonAgainst +
                    ", lostAgainst=" + lostAgainst +
                    '}';
        }

        private float expectedPlayerScore(long opponent_score) {
            return 1.0f * 1.0f / (1 + 1.0f *
                    (float) (Math.pow(10, 1.0f *
                            (opponent_score - this.score) / 400)));
        }

        //1.Score each player based on the games played
        private void updateScore(long opponent_score, int score) {
            this.score += EloRanking.K_FACTOR * (score - expectedPlayerScore(opponent_score));
        }

        public void playerWon(long opponent_score) {
            this.noOfWins += 1;
            updateScore(opponent_score, 1);
        }

        public void playerLost(long opponent_score) {
            this.noOfLost += 1;
            updateScore(opponent_score, 0);
        }

        public void wonAgainst(long playerId) {
            wonAgainst.add(playerId);
        }

        public void lostAgainst(long playerId) {
            lostAgainst.add(playerId);
        }

    }

}
