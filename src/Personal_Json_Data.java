package src;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersonalManagementData {
    private List<Team> teams;
    private final ObjectMapper objectMapper;

    public PersonalManagementData() {
        this.objectMapper = new ObjectMapper();
        this.teams = new ArrayList<>(); // Initialisiere mit einer leeren Liste
    }

    /**
     * Liest JSON-Daten aus einer Datei und speichert sie als Liste von Team-Objekten.
     * @param filePath Der Pfad zur JSON-Datei.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    public void loadData(String filePath) throws IOException {
        // Die JSON-Datei ist ein Array von Team-Objekten auf der obersten Ebene.
        // TypeReference wird benötigt, um Jackson mitzuteilen, dass es ein List<Team> deserialisieren soll.
        this.teams = objectMapper.readValue(new File(filePath), new TypeReference<List<Team>>() {});
    }

    /**
     * Gibt alle Team-Objekte zurück.
     * @return Eine nicht-modifizierbare Liste von Team-Objekten.
     */
    public List<Team> getTeams() {
        return new ArrayList<>(teams); // Rückgabe einer Kopie, um direkte Modifikationen zu verhindern
    }

    /**
     * Sucht ein Team anhand seiner ID.
     * @param teamId Die ID des zu suchenden Teams.
     * @return Ein Optional, das das Team-Objekt enthält, falls gefunden, ansonsten leer.
     */
    public Optional<Team> getTeamById(String teamId) {
        return teams.stream()
                .filter(team -> team.getTeamId().equals(teamId))
                .findFirst();
    }

    /**
     * Sucht eine Rolle anhand ihrer ID in allen Teams.
     * @param roleId Die ID der zu suchenden Rolle.
     * @return Ein Optional, das das Role-Objekt enthält, falls gefunden, ansonsten leer.
     */
    public Optional<Role> getRoleById(String roleId) {
        return teams.stream()
                .flatMap(team -> team.getRoles().stream()) // Flacht alle Rollen aus allen Teams ab
                .filter(role -> role.getId().equals(roleId))
                .findFirst();
    }

    /**
     * Ruft alle Rollen für eine bestimmte Team-ID ab.
     * @param teamId Die ID des Teams.
     * @return Eine Liste von Rollen, die zu diesem Team gehören. Eine leere Liste, wenn das Team nicht gefunden wird.
     */
    public List<Role> getRolesByTeamId(String teamId) {
        return getTeamById(teamId)
                .map(Team::getRoles)
                .orElse(new ArrayList<>()); // Wenn Team nicht gefunden, leere Liste zurückgeben
    }

    /**
     * Ruft die erforderlichen Qualifikationen für eine bestimmte Rollen-ID ab.
     * @param roleId Die ID der Rolle.
     * @return Eine Liste von Qualifikations-Strings. Eine leere Liste, wenn die Rolle nicht gefunden wird.
     */
    public List<String> getRequiredQualificationsByRoleId(String roleId) {
        return getRoleById(roleId)
                .map(Role::getRequiredQualifications)
                .orElse(new ArrayList<>()); // Wenn Rolle nicht gefunden, leere Liste zurückgeben
    }

    /**
     * Fügt eine neue Qualifikation zu einer Rolle hinzu.
     * @param roleId Die ID der Rolle, zu der die Qualifikation hinzugefügt werden soll.
     * @param qualification Die hinzuzufügende Qualifikation.
     * @return true, wenn die Qualifikation erfolgreich hinzugefügt wurde, false sonst (z.B. Rolle nicht gefunden oder Qualifikation bereits vorhanden).
     */
    public boolean addQualificationToRole(String roleId, String qualification) {
        Optional<Role> roleOpt = getRoleById(roleId);
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            // Stellen Sie sicher, dass die Liste der Qualifikationen nicht null ist
            if (role.getRequiredQualifications() == null) {
                role.setRequiredQualifications(new ArrayList<>());
            }
            List<String> qualifications = role.getRequiredQualifications();
            if (!qualifications.contains(qualification)) { // Fügt nur hinzu, wenn nicht bereits vorhanden
                qualifications.add(qualification);
                return true;
            }
        }
        return false;
    }

    /**
     * Entfernt eine Qualifikation von einer Rolle.
     * @param roleId Die ID der Rolle, von der die Qualifikation entfernt werden soll.
     * @param qualification Die zu entfernende Qualifikation.
     * @return true, wenn die Qualifikation erfolgreich entfernt wurde, false sonst (z.B. Rolle oder Qualifikation nicht gefunden).
     */
    public boolean removeQualificationFromRole(String roleId, String qualification) {
        Optional<Role> roleOpt = getRoleById(roleId);
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            if (role.getRequiredQualifications() != null) {
                return role.getRequiredQualifications().remove(qualification);
            }
        }
        return false;
    }

    /**
     * Ändert den Namen einer Rolle.
     * @param roleId Die ID der Rolle, deren Name geändert werden soll.
     * @param newName Der neue Name der Rolle.
     * @return true, wenn der Name erfolgreich geändert wurde, false sonst (z.B. Rolle nicht gefunden).
     */
    public boolean updateRoleName(String roleId, String newName) {
        Optional<Role> roleOpt = getRoleById(roleId);
        if (roleOpt.isPresent()) {
            roleOpt.get().setName(newName);
            return true;
        }
        return false;
    }

    /**
     * Speichert die aktuellen Daten zurück in eine JSON-Datei.
     * Hinweis: Dies überschreibt die ursprüngliche Datei.
     * @param filePath Der Pfad, unter dem die Daten gespeichert werden sollen.
     * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt.
     */
    public void saveData(String filePath) throws IOException {
        // Pretty Printer macht die JSON-Ausgabe besser lesbar
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), teams);
    }


    public static void main(String[] args) {
        PersonalManagementData dataManager = new PersonalManagementData();
        // Stellen Sie sicher, dass dies der korrekte Pfad zu Ihrer JSON-Datei ist
        // Wenn die Datei direkt im Projekt-Stammverzeichnis liegt, ist der Name ausreichend.
        // Ansonsten der volle Pfad: "C:/Users/doria/IdeaProjects/personalmanagement-software/roles-qualifications.txt"
        String jsonFilePath = "roles-qualifications.txt";

        try {
            dataManager.loadData(jsonFilePath);
            System.out.println("Daten erfolgreich aus " + jsonFilePath + " geladen.");

            // --- Beispiel: Daten auslesen ---

            System.out.println("\n--- Alle Team-IDs ---");
            dataManager.getTeams().forEach(team -> System.out.println(team.getTeamId()));

            String targetTeamId = "team-legal-compliance";
            System.out.println("\n--- Rollen für Team: " + targetTeamId + " ---");
            List<Role> legalComplianceRoles = dataManager.getRolesByTeamId(targetTeamId);
            if (!legalComplianceRoles.isEmpty()) {
                legalComplianceRoles.forEach(role -> System.out.println("- " + role.getName() + " (ID: " + role.getId() + ")"));
            } else {
                System.out.println("Team '" + targetTeamId + "' nicht gefunden oder hat keine Rollen.");
            }

            String targetRoleId = "role-data-protection-lead";
            System.out.println("\n--- Details für Rolle: " + targetRoleId + " ---");
            Optional<Role> dpoRole = dataManager.getRoleById(targetRoleId);
            if (dpoRole.isPresent()) {
                System.out.println("Name: " + dpoRole.get().getName());
                System.out.println("Erforderliche Qualifikationen: " + dataManager.getRequiredQualificationsByRoleId(targetRoleId));
            } else {
                System.out.println("Rolle mit ID " + targetRoleId + " nicht gefunden.");
            }

            // --- Beispiel: Daten bearbeiten ---

            String roleToModify = "role-compliance-analyst";
            String newQualification = "qual-gdpr-basics";
            System.out.println("\n--- Qualifikation zu Rolle hinzufügen: " + roleToModify + " ---");
            if (dataManager.addQualificationToRole(roleToModify, newQualification)) {
                System.out.println("Qualifikation '" + newQualification + "' erfolgreich zu '" + roleToModify + "' hinzugefügt.");
            } else {
                System.out.println("Qualifikation konnte nicht hinzugefügt werden (Rolle nicht gefunden oder Qualifikation bereits vorhanden).");
            }
            System.out.println("Aktualisierte Qualifikationen für " + roleToModify + ": " + dataManager.getRequiredQualificationsByRoleId(roleToModify));

            String roleToUpdateName = "role-compliance-coordinator";
            String updatedName = "Compliance Process Coordinator";
            System.out.println("\n--- Rollennamen aktualisieren: " + roleToUpdateName + " ---");
            if (dataManager.updateRoleName(roleToUpdateName, updatedName)) {
                System.out.println("Name der Rolle '" + roleToUpdateName + "' erfolgreich auf '" + updatedName + "' aktualisiert.");
            } else {
                System.out.println("Name der Rolle konnte nicht aktualisiert werden (Rolle nicht gefunden).");
            }
            dataManager.getRoleById(roleToUpdateName).ifPresent(role -> System.out.println("Neuer Name: " + role.getName()));

            String roleToRemoveQual = "role-legal-compliance-specialist";
            String qualToRemove = "qual-3y-compliance-exp";
            System.out.println("\n--- Qualifikation von Rolle entfernen: " + roleToRemoveQual + " ---");
            System.out.println("Vorherige Qualifikationen: " + dataManager.getRequiredQualificationsByRoleId(roleToRemoveQual));
            if (dataManager.removeQualificationFromRole(roleToRemoveQual, qualToRemove)) {
                System.out.println("Qualifikation '" + qualToRemove + "' erfolgreich von '" + roleToRemoveQual + "' entfernt.");
            } else {
                System.out.println("Qualifikation konnte nicht entfernt werden (Rolle oder Qualifikation nicht gefunden).");
            }
            System.out.println("Aktualisierte Qualifikationen: " + dataManager.getRequiredQualificationsByRoleId(roleToRemoveQual));


            // --- Beispiel: Geänderte Daten speichern ---

            // Speichert die aktualisierten Daten in eine NEUE Datei, damit die Originaldatei erhalten bleibt.
            // Wenn Sie die Originaldatei überschreiben möchten, verwenden Sie denselben Pfad wie beim Laden.
            String outputFilePath = "roles-qualifications_updated.json";
            dataManager.saveData(outputFilePath);
            System.out.println("\nAktualisierte Daten erfolgreich nach " + outputFilePath + " gespeichert.");

        } catch (IOException e) {
            System.err.println("Fehler beim Lesen oder Schreiben der JSON-Datei: " + e.getMessage());
            e.printStackTrace(); // Dies hilft bei der Fehlersuche
        }
    }
}
