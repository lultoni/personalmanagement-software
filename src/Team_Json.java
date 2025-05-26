package src;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Team {
    @JsonProperty("team-id") // JSON-Feldname weicht vom Java-Feldnamen ab
    private String teamId;
    private List<Role> roles;

    // Leerer Konstruktor für Jackson notwendig
    public Team() {
    }

    public Team(String teamId, List<Role> roles) {
        this.teamId = teamId;
        this.roles = roles;
    }

    // Getter und Setter
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId='" + teamId + '\'' +
                ", roles=" + roles +
                '}';
    }
}
