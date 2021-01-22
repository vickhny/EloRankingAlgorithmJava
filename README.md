## The Assignment: Elo Ranking Algorithm

Implement a ranking program using the Elo ranking algorithm.

Given two files:

1. `names`, where each line has an ID number and a name for said ID `src/com/eloAlgo/example/names.txt`
2. `matches`, where each line contains the ID of the two players of a match and the first one is the winner of said match. `src/com/eloAlgo/example/matches.txt`
 

Implement a program that can read both files and then:

1. Score each player based on the games played
2. Generate a list of players sorted by score, their rank and their number of wins and losses.
3. Generate a report for each person, showing with whom they played and how they fared.
4. Generate a list of suggested next matches.

## Requirement
* Java


## How to Run

* Invocation format:

    ```
    java EloRanking.java <names_file> <matches_file> [<view_file>] [<sortBy>]
    ```

    where
    * `names_file` - text file with names
    * `matches_file` - text file with matches
    * `view_file` - optional, name of the player information you want to view, provided options are:
        * `view_players_scores`
        * `generate_player_report`
        * `suggested_next_matches`
    * `sortBy` - optional, options for the view to sort the output by the following param.
        * `ID`     - by ID (default)
        * `RATING` - by rating
        * `RANK`   - by rank
        * `WINS`   - by number of wins
        * `LOSSES` - by number of losses

* The algorithm uses default Elo rating value of **1000** as a starting (average) value
* The algorithm uses constant value of **30** for K-factor.

* Go inside the src directory using the following command:
    ```
    cd src/com/eloAlgo/example
    ```

* Generate the score each player based on the games played by running the application using the following command:
    ```
    java EloRanking.java names.txt matches.txt 
    ```
    ```
    Rank       ID    Name    Win    Lose    Score
    ---------------------------------------------------------------------------------------------
    26   |     0  |  Wesley  |  0  |  1  |  1485
    32   |     1  |  Melodie |  2  |  4  |  1472
    34   |     2  |  Solange |  0  |  2  |  1471
    38   |     3  |  Johanne |  1  |  5  |  1450
    .
    .
    ```

* Generate a list of players sorted by score, their ranking and their number of wins and losses by running the application using the following command (Default order is based on playerId) :
    ```
     java EloRanking.java names.txt matches.txt view_players_scores
    ```
    
    ```
     java EloRanking.java names.txt matches.txt view_players_scores [ID, RANK, SCORE, WINS, LOSES]
    ```
    ```
    `Rank`       ID    Name           Win    Lose    Score
    ---------------------------------------------------------------------------------------------
    1   |     36  |  Jacquelynn   |  5  |  1  |  1549
    2   |     39  |  Odette       |  4  |  1  |  1544
    3   |     26  |  Brice        |  3  |  0  |  1541
    4   |     23  |  Denyse       |  4  |  1  |  1540
    .
    .
    ```

* Generate a report for each person, showing with whom they played and how they fared by running the application using the following command (Default order is based on playerId) :
    ```
     java EloRanking.java names.txt matches.txt generate_player_report
    ```
    ```
    Rank     ID        Name     Win    Lose    Score          WinAgainstId                          LoseAgainstId
    --------------------------------------------------------------------------------------------------------------------------------------------------
    26   |    0  |  Wesley   |   0   |   1  |   1485     |     []                               |     [Micheline]
    32   |    1  |  Melodie  |   2   |   4  |   1472     |     [Solange, Marica]                |     [Dave, Antwan, Hunter, Windy]
    34   |    2  |  Solange  |   0   |   2  |   1471     |     []                               |     [Melodie, Denyse]
    38   |    3  |  Johanne  |   1   |   5  |   1450     |     [Janene]                         |     [Johnna, Ileana, Jaye, Hunter, Brice]
    39   |    4  |  Bunny    |   2   |   6  |   1443     |     [Jaye, Dortha]                   |     [Aleen, Ira, Ute, Dortha, Cassondra, Kami]
    7    |    5  |  Tai      |   4   |   1  |   1537     |     [Wm, Johnna, Brianna, Joella]    |     [Meda]
    .
    .
    ```

* Generate a list of suggested next matches by running the application using the following command :
    ```
     java EloRanking.java names.txt matches.txt suggested_next_matches
    ```
    ```
    Cassondra (38, 1529) vs. Wesley( 0, 1485)
    Odette (39, 1544) vs. Wesley( 0, 1485)
    Wesley (0, 1485) vs. Melodie( 1, 1472)
    Johanne (3, 1450) vs. Melodie( 1, 1472)
    .
    .
    ```
