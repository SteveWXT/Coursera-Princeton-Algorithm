import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final int n;
    private Map<String, Integer> teams2id = new HashMap<>();
    private String[] id2teams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private Map<String, Set<String>> certificates = new HashMap<>();


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        id2teams = new String[n];
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        against = new int[n][n];

        for (int i = 0; i < n; i++) {
            String team = in.readString();
            teams2id.put(team, i);
            id2teams[i] = team;
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                against[i][j] = in.readInt();
            }
        }
    }
    // number of teams
    public int numberOfTeams() {
        return n;
    }
    // all teams
    public Iterable<String> teams() {
        return teams2id.keySet();
    }
    // number of wins for given team
    public int wins(String team) {
        if (!teams2id.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return wins[teams2id.get(team)];
    }
    // number of losses for given team
    public int losses(String team) {
        if (!teams2id.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return losses[teams2id.get(team)];
    }
    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams2id.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return remaining[teams2id.get(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams2id.containsKey(team1) || !teams2id.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        return against[teams2id.get(team1)][teams2id.get(team2)];
    }
    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams2id.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return isTrivialEliminated(team) || isNontrivialEliminated(team);
    }

    private boolean isTrivialEliminated(String team) {
        int teamIdx = teams2id.get(team);
        int maxWins = wins[teamIdx] + remaining[teamIdx];
        Set<String> cert = certificates.getOrDefault(team, new HashSet<>());

        for (int i = 0; i < n; i++) {
            if (i == teamIdx) {
                continue;
            }

            if (maxWins < wins[i]) {
                cert.add(id2teams[i]);
                certificates.put(team, cert);
                return true;
            }
        }

        return false;
    }

    private boolean isNontrivialEliminated(String team) {
        int teamIdx = teams2id.get(team);
        boolean flag = false;
        Set<String> cert = certificates.getOrDefault(team, new HashSet<>());

        FlowNetwork network = createNetwork(team);
        FordFulkerson ff = new FordFulkerson(network, 0, 1);

        for (int i = 0; i < n; i++) {
            if (i == teamIdx) {
                continue;
            }
            if (ff.inCut(i+2)) {
                flag = true;
                cert.add(id2teams[i]);
            }
        }

        certificates.put(team, cert);
        return flag;
    }

    private FlowNetwork createNetwork(String team) {
        int teamIdx = teams2id.get(team);
        // s = 0, t = 1, team vertex: [2, n + 2) , game vertex: [n + 2, totalV)
        int s = 0;
        int t = 1;
        int gameIdx = n + 2;
        // how many unique games between teams
        int games = n * (n - 1) / 2;
        // total vertices number
        int totalV = 2 + games + n;
        FlowNetwork network = new FlowNetwork(totalV);

        for (int i = 0; i < n; i++) {
            if (i == teamIdx) {
                continue;
            }
            network.addEdge(new FlowEdge(i+2, t, wins[teamIdx] + remaining[teamIdx] - wins[i]));

            for (int j = i + 1; j < n; j++) {
                if (j == teamIdx) {
                    continue;
                }
                network.addEdge(new FlowEdge(gameIdx, i+2, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(gameIdx, j+2, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(s, gameIdx, against[i][j]));
                gameIdx++;
            }
        }
        return network;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams2id.containsKey(team)) {
            throw new IllegalArgumentException();
        }else if (!isEliminated(team)) {
            return null;
        } else {
            return certificates.get(team);
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
