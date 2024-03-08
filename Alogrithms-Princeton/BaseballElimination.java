class BaseballElimination{
    public BaseballElimination(String filename){}                   // create a baseball division from given filename in format specified below
    public              int numberOfTeams(){}                        // number of teams
    public Iterable<String> teams(){}                                // all teams
    public              int wins(String team){}                      // number of wins for given team
    public              int losses(String team){}                    // number of losses for given team
    public              int remaining(String team){}                 // number of remaining games for given team
    
    
    public int against(String team1, String team2){
        if( team1==null || team2==null) 
        throw new IllegalArgumentException();
    }    // number of remaining games between team1 and team2
    
     // is given team eliminated?
    public boolean isEliminated(String team){
        if(team == null) throw new IllegalArgumentException("team string is null");
    }             
    
    
    public Iterable<String> certificateOfElimination(String team){
        if(team == null) throw new IllegalArgumentException("team string is null");
    }  // subset R of teams that eliminates given team; null if not eliminated
}
