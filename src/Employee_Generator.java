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
            "Elias", "Fabian", "Nubar", "Tarik", "Jenny", "Christian", "Mohammad", "Lee", "Joachim", "Johannes"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker", "Schulz", "Hoffmann",
            "Schäfer", "Koch", "Bauer", "Richter", "Klein", "Wolf", "Schröder", "Neumann", "Schwarz", "Zimmermann",
            "Braun", "Krüger", "Hartmann", "Lange", "Krause", "Lehmann", "Huber", "Maier", "Sperber", "Gläske",
            "Peter", "Seedorf", "Beckham", "Paulus", "Neuer"
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

    private static final AtomicInteger idCounter = new AtomicInteger(2);

    private static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }

    private static String generateRandomEmail(String firstName, String lastName, Set<String> existingEmails) {
        String basePrefix = (firstName + "." + lastName).toLowerCase()
                .replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss")
                .replaceAll("[^a-z0-9.]", "");
        String email = basePrefix + "@" + COMPANY_DOMAIN;

        if (!existingEmails.contains(email)) {
            return email;
        }

        int counter = 1;
        while (true) {
            email = basePrefix + counter + "@" + COMPANY_DOMAIN;
            if (!existingEmails.contains(email)) {
                return email;
            }
            counter++;
        }
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
        Set<Integer> generatedIds = new HashSet<>();
        Set<String> generatedEmails = new HashSet<>();
        Map<String, Integer> actualManagerIds = new HashMap<>();

        generatedIds.add(Integer.valueOf(ceoId));

        String ceo_firstName = "Bob";
        String ceo_lastName = getRandomElement(LAST_NAMES);
        String ceo_email = generateRandomEmail(ceo_firstName, ceo_lastName, generatedEmails);
        generatedEmails.add(ceo_email);
        String ceo_birthday = generateRandomBirthday();
        String ceo_startDate = generateRandomStartDate(10);
        String ceo_ceoRole = "Geschäftsführer (CEO)";
        String ceo_ceoDept = "Geschäftsführung";
        BigDecimal ceo_salary = generateRandomSalary(ceo_ceoRole);

        Employee ceo = new Employee(ceoId, (ceo_firstName + " " + ceo_lastName), ceo_birthday, ceo_email, ceo_salary, ceo_startDate, 0, ceo_ceoRole, ceo_ceoDept);
        employees.add(ceo);
        actualManagerIds.put(ceo_ceoDept, Integer.valueOf(ceoId));

        for (Map.Entry<String, List<String>> entry : ROLES_BY_DEPARTMENT.entrySet()) {
            String department = entry.getKey();
            if (departmentManagers.containsKey(department)) {
                String managerRole = entry.getValue().stream()
                        .filter(r -> r.contains("Leiter") || r.contains("Geschäftsführer") || r.contains("Meister") || r.contains("Oberbauleiter"))
                        .findFirst()
                        .orElse(getRandomElement(entry.getValue()));

                if (employees.size() < totalEmployees) {
                    int managerId = idCounter.getAndIncrement();
                    generatedIds.add(Integer.valueOf(managerId));
                    actualManagerIds.put(department, Integer.valueOf(managerId));

                    String firstName = getRandomElement(FIRST_NAMES);
                    String lastName = getRandomElement(LAST_NAMES);
                    String email = generateRandomEmail(firstName, lastName, generatedEmails);
                    generatedEmails.add(email);
                    String birthday = generateRandomBirthday();
                    String startDate = generateRandomStartDate(10);
                    BigDecimal salary = generateRandomSalary(managerRole);

                    int reportsTo = (managerRole.contains("Geschäftsführer")) ? 0 : ceoId;

                    Employee manager = new Employee(managerId, (firstName + " " + lastName), birthday, email, salary, startDate, reportsTo, managerRole, department);
                    employees.add(manager);
                }
            }
        }

        int remainingEmployees = totalEmployees - employees.size();
        for (int i = 0; i < remainingEmployees; i++) {
            int employeeId = idCounter.getAndIncrement();
            generatedIds.add(Integer.valueOf(employeeId));

            String department = getRandomElement(DEPARTMENTS);
            List<String> possibleRoles = ROLES_BY_DEPARTMENT.getOrDefault(department, ROLES_BY_DEPARTMENT.get("Default"));
            String role = getRandomElement(possibleRoles.stream()
                    .filter(r -> !(r.contains("Leiter") || r.contains("Geschäftsführer")))
                    .toList());
            if (role == null) role = getRandomElement(ROLES_BY_DEPARTMENT.get("Default"));

            String firstName = getRandomElement(FIRST_NAMES);
            String lastName = getRandomElement(LAST_NAMES);
            String email = generateRandomEmail(firstName, lastName, generatedEmails);
            generatedEmails.add(email);
            String birthday = generateRandomBirthday();
            String startDate = generateRandomStartDate(10);
            BigDecimal salary = generateRandomSalary(role);

            int managerIdForEmployee = actualManagerIds.getOrDefault(department, Integer.valueOf(0));
            if (managerIdForEmployee == employeeId) managerIdForEmployee = 0;

            Employee employee = new Employee(employeeId, (firstName + " " + lastName), birthday, email, salary, startDate, managerIdForEmployee, role, department);
            employees.add(employee);
        }

        return employees;
    }

    public static void main(String[] args) {
        final int NUM_EMPLOYEES_TO_GENERATE = Integer.parseInt(args[0]);
        final boolean DELETE_EXISTING_EMPLOYEES = Boolean.parseBoolean(args[1]);
        final boolean REPLACE_ON_DUPLICATE = Boolean.parseBoolean(args[2]);

        final int CEO_ID = 1;
        Map<String, Integer> departmentManagerIds = new HashMap<>();
        departmentManagerIds.put("Projektleitung", Integer.valueOf(2));
        departmentManagerIds.put("Bauleitung", Integer.valueOf(3));
        departmentManagerIds.put("Planung & Entwurf", Integer.valueOf(4));
        departmentManagerIds.put("Finanzen", Integer.valueOf(5));
        departmentManagerIds.put("Personalwesen", Integer.valueOf(6));
        departmentManagerIds.put("IT", Integer.valueOf(7));

        System.out.println(Main.debug_pre_string + "Generiere " + NUM_EMPLOYEES_TO_GENERATE + " Mitarbeiter für 'Bob the Building Company'...");

        List<Employee> generatedEmployees = generateEmployees(NUM_EMPLOYEES_TO_GENERATE, departmentManagerIds, CEO_ID);

        if (DELETE_EXISTING_EMPLOYEES) {
            System.out.println(Main.debug_pre_string + "Lösche bestehende Mitarbeiter aus der Datenbank...");
            DB_API.deleteAllEmployees();
            System.out.println(Main.debug_pre_string + "Bestehende Mitarbeiter gelöscht.");
        }

        System.out.println(Main.debug_pre_string + "Füge " + generatedEmployees.size() + " generierte Mitarbeiter zur Datenbank hinzu (replace on duplicate: " + REPLACE_ON_DUPLICATE + ")...");
        DB_API.addEmployees(generatedEmployees, REPLACE_ON_DUPLICATE);

        System.out.println(Main.debug_pre_string + "Mitarbeiter erfolgreich hinzugefügt!");
    }

    public static List<String> getDepartments() {
        return DEPARTMENTS;
    }

    public static Map<String, List<String>> getRolesByDepartment() {
        return ROLES_BY_DEPARTMENT;
    }
}