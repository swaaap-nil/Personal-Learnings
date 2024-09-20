
import java.util.Arrays;
import java.util.Scanner;

class Monster implements Comparable<Monster>{
    int power;
    int bonus;

    Monster(int power, int bonus) {
        this.power = power;
        this.bonus = bonus;
    }
    @Override
    public int compareTo(Monster o) {
        return this.power - o.power;
    }
}

public class RPGQuest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // Number of monsters
        int e = scanner.nextInt(); // Initial experience

        Monster[] monsters = new Monster[n];
        for (int i = 0; i < n; i++) {
            int power = scanner.nextInt();
            int bonus = scanner.nextInt();
            monsters[i] = new Monster(power, bonus);
        }

        // Sort monsters by their power
        Arrays.sort(monsters);

        int count = 0;

        // Try to defeat monsters in sorted order
        for (Monster monster : monsters) {
            if (e >= monster.power) { // If we have enough experience to defeat the monster
                e += monster.bonus; // Gain bonus experience
                count++; // Increase the count of defeated monsters
            }
        }

        System.out.println(count); // Output the maximum number of monsters defeated
        scanner.close();
    }
}
