import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Student> students;
    private List<Equipment> inventory;
    private List<Reservation> reservations;
    private DiscountPolicy discountPolicy;
    private int reservationCounter = 1;

    public ReservationService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.students = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.reservations = new ArrayList<>();
        initData();
    }

    private void initData() {
        students.add(new Student("S001", "Anna Kowalska", "12c", 120));
        students.add(new Student("S002", "Marek Nowak", "12c", 40));
        students.add(new Student("S003", "Julia Zielinska", "13a", 0));

        inventory.add(new LaptopSet("E001", "Lenovo ThinkPad Lab", 80, 32, true));
        inventory.add(new LaptopSet("E002", "Dell XPS Demo", 100, 16, false));
        inventory.add(new CameraKit("E003", "Sony Content Kit", 90, 3, true));
        inventory.add(new CameraKit("E004", "Canon Interview Kit", 70, 1, true));
    }

    public void displayStudents() {
        System.out.println("--- Students List ---");
        for (Student s : students) System.out.println(s.toString());
    }

    public void displayEquipment() {
        System.out.println("--- Equipment List ---");
        for (Equipment e : inventory) System.out.println(e.getDisplayText());
    }

    public void createReservation(String studentId, String equipmentId, int days) {
        Student student = students.stream().filter(s -> s.getId().equals(studentId)).findFirst().orElse(null);
        if (student == null) {
            System.out.println("Error: Student not found.");
            return;
        }

        Equipment eq = inventory.stream().filter(e -> e.getId().equals(equipmentId)).findFirst().orElse(null);
        if (eq == null) {
            System.out.println("Error: Equipment not found.");
            return;
        }

        if (!eq.isAvailable()) {
            System.out.println("Error: Equipment " + equipmentId + " is not available.");
            return;
        }

        if (days < 1 || days > 14) {
            System.out.println("Error: Number of days must be between 1 and 14.");
            return;
        }

        String resId = "R00" + reservationCounter++;
        Reservation res = new Reservation(resId, student, eq, days);
        eq.setAvailable(false);
        reservations.add(res);

        System.out.println("Reservation " + resId + " created successfully.");
        System.out.println("Equipment: " + eq.getName());
        System.out.printf("Cost: %.2f PLN\n", res.calculateTotalCost(discountPolicy));
        System.out.println("Status: " + res.getStatus());
    }

    public void returnEquipment(String reservationId) {
        Reservation res = reservations.stream()
                .filter(r -> r.getId().equals(reservationId) && r.getStatus() == ReservationStatus.ACTIVE)
                .findFirst().orElse(null);

        if (res == null) {
            System.out.println("Error: Active reservation not found.");
            return;
        }

        res.setStatus(ReservationStatus.RETURNED);
        res.getEquipment().setAvailable(true);

        double totalCost = res.calculateTotalCost(discountPolicy);
        int earnedPoints = (int) (totalCost / 10);
        res.getStudent().addLoyaltyPoints(earnedPoints);

        System.out.printf("Equipment returned. The student received %d loyalty points.\n", earnedPoints);
    }

    public void displayActiveReservations() {
        System.out.println("--- Active Reservations ---");
        reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .forEach(r -> System.out.println(r.getDisplayText()));
    }

    public void printReport() {
        System.out.println("--- Report ---");
        double totalRevenue = 0;
        int completedCount = 0;

        System.out.println("Completed Reservations:");
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.RETURNED) {
                System.out.println(r.getDisplayText());
                totalRevenue += r.calculateTotalCost(discountPolicy);
                completedCount++;
            }
        }
        System.out.println("Total completed reservations: " + completedCount);
        System.out.printf("Total revenue: %.2f PLN\n", totalRevenue);

        Student topStudent = students.stream()
                .max((s1, s2) -> Integer.compare(s1.getLoyaltyPoints(), s2.getLoyaltyPoints()))
                .orElse(null);

        if (topStudent != null) {
            System.out.println("Student with the highest loyalty points: " + topStudent.getFullName() + " (" + topStudent.getLoyaltyPoints() + " points)");
        }
    }
}