package src;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Employee_Generator {

    private static final List<String> FIRST_NAMES = Arrays.asList(
            "Bob", "Alice", "Charlie", "David", "Eva", "Frank", "Grace", "Heidi", "Ivan", "Judy",
            "Klaus", "Laura", "Michael", "Nora", "Oscar", "Peter", "Renate", "Stefan", "Tina", "Ulrich",
            "Viktor", "Wendy", "Max", "Anna", "Paul", "Sophie", "Leon", "Mia", "Joshua", "Dorian",
            "Elias", "Fabian", "Nubar", "Tarik"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker", "Schulz", "Hoffmann",
            "Schäfer", "Koch", "Bauer", "Richter", "Klein", "Wolf", "Schröder", "Neumann", "Schwarz", "Zimmermann",
            "Braun", "Krüger", "Hartmann", "Lange", "Krause", "Lehmann", "Huber", "Maier", "Sperber", "Gläske"
    );

    private static final String COMPANY_DOMAIN = "btbc.com";

    private static final List<String> DEPARTMENTS = Arrays.asList(
            "Geschäftsführung", "Projektleitung", "Bauleitung", "Planung & Entwurf", "Einkauf & Logistik",
            "Personalwesen", "Finanzen", "Ausführung Hochbau", "Ausführung Tiefbau", "Ausführung Technik", "IT"
    );

    private static final Map<String, List<String>> ROLES_BY_DEPARTMENT = new HashMap<>();
    static {
        ROLES_BY_DEPARTMENT.put("Geschäftsführung", Arrays.asList("Geschäftsführer"));
        ROLES_BY_DEPARTMENT.put("Projektleitung", Arrays.asList("Abteilungsleiter Projektleitung", "Senior Projektleiter", "Projektleiter"));
        ROLES_BY_DEPARTMENT.put("Bauleitung", Arrays.asList("Abteilungsleiter Bauleitung", "Oberbauleiter", "Bauleiter", "Junior Bauleiter"));
        ROLES_BY_DEPARTMENT.put("Planung & Entwurf", Arrays.asList("Leiter Planung", "Architekt", "Bauingenieur (Statik)", "Bauzeichner", "Technischer Zeichner"));
        ROLES_BY_DEPARTMENT.put("Einkauf & Logistik", Arrays.asList("Leiter Einkauf", "Einkäufer", "Logistikmanager", "Lagermitarbeiter"));
        ROLES_BY_DEPARTMENT.put("Personalwesen", Arrays.asList("Personalleiter", "HR Manager", "Recruiter", "Personalreferent"));
        ROLES_BY_DEPARTMENT.put("Finanzen", Arrays.asList("Finanzleiter (CFO)", "Controller", "Buchhalter"));
        ROLES_BY_DEPARTMENT.put("Ausführung Hochbau", Arrays.asList("Polier (Hochbau)", "Maurer", "Betonbauer", "Zimmerer", "Dachdecker"));
        ROLES_BY_DEPARTMENT.put("Ausführung Tiefbau", Arrays.asList("Polier (Tiefbau)", "Straßenbauer", "Kanalbauer", "Baggerfahrer", "Baumaschinenführer"));
        ROLES_BY_DEPARTMENT.put("Ausführung Technik", Arrays.asList("Elektromeister", "Elektriker", "Anlagenmechaniker SHK", "Schweißer"));
        ROLES_BY_DEPARTMENT.put("IT", Arrays.asList("IT-Leiter", "Systemadministrator", "Softwareentwickler"));
        ROLES_BY_DEPARTMENT.put("Default", Arrays.asList("Fachkraft", "Sachbearbeiter", "Hilfsarbeiter", "Auszubildender"));

    }

    private static final Random random = new Random();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }

    private static String generateRandomEmail(String firstName, String lastName) {
        String emailPrefix = (firstName.substring(0, 1) + "." + lastName).toLowerCase()
                .replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss")
                .replaceAll("[^a-z0-9.]", "");
        return emailPrefix + "@" + COMPANY_DOMAIN;
    }

    private static String generateRandomDateString(LocalDate start, LocalDate end) {
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        long randomEpochDay = startEpochDay + random.nextInt((int) (endEpochDay - startEpochDay + 1));
        return LocalDate.ofEpochDay(randomEpochDay).format(DATE_FORMATTER);
    }

    private static String generateRandomBirthday() {
        LocalDate today = LocalDate.now();
        LocalDate earliestBirthDate = today.minusYears(65);
        LocalDate latestBirthDate = today.minusYears(18);
        return generateRandomDateString(earliestBirthDate, latestBirthDate);
    }

    private static String generateRandomStartDate(int longest_possible_tenure) {
        LocalDate today = LocalDate.now();
        LocalDate earliestStartDate = today.minusYears(longest_possible_tenure);
        return generateRandomDateString(earliestStartDate, today);
    }

    private static BigDecimal generateRandomSalary(String role) {
        double baseSalary;

        if (role.contains("Geschäftsführer") || role.contains("Leiter") || role.contains("CFO")) baseSalary = 80000 + random.nextDouble() * 60000; // 80k - 140k
        else if (role.contains("Senior") || role.contains("Oberbauleiter") || role.contains("Meister") || role.contains("Controller")) baseSalary = 65000 + random.nextDouble() * 35000; // 65k - 100k
        else if (role.contains("Projektleiter") || role.contains("Bauleiter") || role.contains("Ingenieur") || role.contains("Architekt") || role.contains("Manager")) baseSalary = 55000 + random.nextDouble() * 30000; // 55k - 85k
        else if (role.contains("Polier") || role.contains("Fachkraft") || role.contains("Referent") || role.contains("Entwickler") || role.contains("Admin")) baseSalary = 45000 + random.nextDouble() * 20000; // 45k - 65k
        else if (role.contains("Zeichner") || role.contains("Techniker") || role.contains("Sachbearbeiter") || role.contains("Maurer") || role.contains("Zimmerer") || role.contains("Elektriker") || role.contains("Installateur")) baseSalary = 38000 + random.nextDouble() * 17000; // 38k - 55k
        else if (role.contains("Auszubildender")) baseSalary = 12000 + random.nextDouble() * 8000; // 12k - 20k (Jahresgehalt)
        else baseSalary = 30000 + random.nextDouble() * 15000; // 30k - 45k (Rest/Hilfskräfte)

        return BigDecimal.valueOf(baseSalary).setScale(2, RoundingMode.HALF_UP);
    }

    public static List<Employee> generateEmployees(int totalEmployees, Map<String, Integer> departmentManagers, int ceoId) {
        List<Employee> employees = new ArrayList<>(totalEmployees);
        Set<Integer> generatedIds = new HashSet<>(); // Um sicherzustellen, dass IDs eindeutig sind

        // 1. Generiere die Manager aus der Map (falls vorhanden)
        Map<String, Integer> actualManagerIds = new HashMap<>(); // Speichert die tatsächlichen IDs der generierten Manager
        for (Map.Entry<String, List<String>> entry : ROLES_BY_DEPARTMENT.entrySet()) {
            String department = entry.getKey();
            if (departmentManagers.containsKey(department)) {
                // Finde eine passende Manager-Rolle für die Abteilung
                String managerRole = entry.getValue().stream()
                        .filter(r -> r.contains("Leiter") || r.contains("Geschäftsführer") || r.contains("Meister") || r.contains("Oberbauleiter"))
                        .findFirst()
                        .orElse(getRandomElement(entry.getValue())); // Fallback

                if (employees.size() < totalEmployees) {
                    int managerId = idCounter.getAndIncrement();
                    generatedIds.add(managerId);
                    actualManagerIds.put(department, managerId); // Speichere die ID des erstellten Managers

                    String firstName = getRandomElement(FIRST_NAMES);
                    String lastName = getRandomElement(LAST_NAMES);
                    String email = generateRandomEmail(firstName, lastName);
                    String birthday = generateRandomBirthday();
                    String startDate = generateRandomStartDate(10); // TODO maybe add a tenure based on age (birthday till today -16 years)
                    BigDecimal salary = generateRandomSalary(managerRole);

                    int reportsTo = (managerRole.contains("Geschäftsführer")) ? 0 : ceoId;

                    // (int id, String name, String birthday, String emailAddress,
                    //                    BigDecimal salary, String startDate, int managerId,
                    //                    String role, String department)

                    Employee manager = new Employee(managerId, firstName, lastName, birthday, email, salary, startDate, reportsTo, managerRole, department);
                    employees.add(manager);
                }
            }
        }

        // Sicherstellen, dass der CEO (falls noch nicht als Abteilungsleiter generiert) existiert
        if (ceoId != 0 && !generatedIds.contains(ceoId)) {
            boolean ceoExists = employees.stream().anyMatch(e -> e.getId() == ceoId);
            if (!ceoExists && employees.size() < totalEmployees) {
                int actualCeoId = (generatedIds.contains(ceoId)) ? idCounter.getAndIncrement() : ceoId; // Nur wenn ID schon vergeben
                if(!generatedIds.contains(actualCeoId)) { // Nur hinzufügen, wenn ID noch nicht existiert
                    idCounter.set(Math.max(idCounter.get(), actualCeoId + 1)); // Zähler anpassen
                    generatedIds.add(actualCeoId);

                    String firstName = "Bob"; // Chef heißt Bob ;)
                    String lastName = getRandomElement(LAST_NAMES);
                    String email = generateRandomEmail(firstName, lastName);
                    String birthday = generateRandomBirthday();
                    String startDate = generateRandomStartDate(); // Könnte auch ein fester früher Wert sein
                    String ceoRole = "Geschäftsführer";
                    String ceoDept = "Geschäftsführung";
                    BigDecimal salary = generateRandomSalary(ceoRole);

                    Employee ceo = new Employee(actualCeoId, firstName, lastName, birthday, email, salary, startDate, 0, ceoRole, ceoDept); // CEO hat keinen Manager
                    employees.add(ceo);
                    actualManagerIds.put(ceoDept, actualCeoId); // Füge CEO zur Manager-Map hinzu
                }
            }
        }


        // 2. Generiere die restlichen Mitarbeiter
        int remainingEmployees = totalEmployees - employees.size();
        for (int i = 0; i < remainingEmployees; i++) {
            int employeeId = idCounter.getAndIncrement();
            generatedIds.add(employeeId);

            String department = getRandomElement(DEPARTMENTS);
            // Wähle Rollen aus der entsprechenden Abteilung oder Default
            List<String> possibleRoles = ROLES_BY_DEPARTMENT.getOrDefault(department, ROLES_BY_DEPARTMENT.get("Default"));
            // Stelle sicher, dass nicht versehentlich ein weiterer Leiter generiert wird, wenn es schon einen gibt
            String role = getRandomElement(possibleRoles.stream()
                    .filter(r -> !(r.contains("Leiter") || r.contains("Geschäftsführer"))) // Filter Manager-Rollen raus
                    .toList());
            if (role == null) { // Falls nur Manager-Rollen übrig waren, nimm eine Default-Rolle
                role = getRandomElement(ROLES_BY_DEPARTMENT.get("Default"));
            }


            String firstName = getRandomElement(FIRST_NAMES);
            String lastName = getRandomElement(LAST_NAMES);
            String email = generateRandomEmail(firstName, lastName);
            String birthday = generateRandomBirthday();
            String startDate = generateRandomStartDate();
            BigDecimal salary = generateRandomSalary(role);

            // Finde den Manager für die Abteilung, falls einer generiert wurde, sonst 0
            int managerIdForEmployee = actualManagerIds.getOrDefault(department, 0);
            // Stelle sicher, dass sich niemand selbst managt (sollte nicht passieren, aber sicher ist sicher)
            if (managerIdForEmployee == employeeId) {
                managerIdForEmployee = 0;
            }

            Employee employee = new Employee(employeeId, firstName, lastName, birthday, email, salary, startDate, managerIdForEmployee, role, department);
            employees.add(employee);
        }

        return employees;
    }

    public static void main(String[] args) {
        final int NUM_EMPLOYEES_TO_GENERATE = 50;
        final boolean DELETE_EXISTING_EMPLOYEES = false;
        final boolean REPLACE_ON_DUPLICATE = true;

        final int CEO_ID = 1;
        Map<String, Integer> departmentManagerIds = new HashMap<>();
        departmentManagerIds.put("Projektleitung", 2);
        departmentManagerIds.put("Bauleitung", 3);
        departmentManagerIds.put("Planung & Entwurf", 4);
        departmentManagerIds.put("Finanzen", 5);
        departmentManagerIds.put("Personalwesen", 6);
        departmentManagerIds.put("IT", 7);

        System.out.println("Generiere " + NUM_EMPLOYEES_TO_GENERATE + " Mitarbeiter für 'Bob the Building Company'...");

        // 1. Mitarbeiter generieren
        List<Employee> generatedEmployees = generateEmployees(NUM_EMPLOYEES_TO_GENERATE, departmentManagerIds, CEO_ID);

        // 2. (Optional) Bestehende Mitarbeiter löschen
        if (DELETE_EXISTING_EMPLOYEES) {
            System.out.println("Lösche bestehende Mitarbeiter aus der Datenbank...");
            // Annahme: DB_API.deleteAllEmployees() existiert und funktioniert
            DB_API.deleteAllEmployees();
            System.out.println("Bestehende Mitarbeiter gelöscht.");
        }

        // 3. Generierte Mitarbeiter zur Datenbank hinzufügen
        System.out.println("Füge " + generatedEmployees.size() + " generierte Mitarbeiter zur Datenbank hinzu (replace on duplicate: " + REPLACE_ON_DUPLICATE + ")...");
        DB_API.addEmployees(generatedEmployees, REPLACE_ON_DUPLICATE);

        System.out.println("Mitarbeiter erfolgreich hinzugefügt!");
    }

}