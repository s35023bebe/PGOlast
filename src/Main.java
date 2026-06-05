import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReservationService service = new ReservationService(new LoyaltyDiscountPolicy());
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- MediaLab Menu ---");
            System.out.println("1. Display students list");
            System.out.println("2. Display equipment list");
            System.out.println("3. Create reservation");
            System.out.println("4. Return equipment");
            System.out.println("5. Show active reservations");
            System.out.println("6. Show report");
            System.out.println("0. Exit");
            System.out.print("\nChoice: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    service.displayStudents();
                    break;
                case 2:
                    service.displayEquipment();
                    break;
                case 3:
                    System.out.print("Enter student ID: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Enter equipment ID: ");
                    String equipmentId = scanner.nextLine();
                    System.out.print("Enter number of days: ");
                    int days = scanner.nextInt();
                    service.createReservation(studentId, equipmentId, days);
                    break;
                case 4:
                    System.out.print("Enter reservation ID (e.g., R001): ");
                    String resId = scanner.nextLine();
                    service.returnEquipment(resId);
                    break;
                case 5:
                    service.displayActiveReservations();
                    break;
                case 6:
                    service.printReport();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
        scanner.close();
    }
}