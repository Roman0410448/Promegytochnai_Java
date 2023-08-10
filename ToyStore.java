import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Программа для розыгрыша игрушек в магазине.
 * Позволяет добавлять новые игрушки и задавать их вес (частоту выпадения).
 * Выполняет розыгрыш игрушек, генерируя случайные значения в соответствии с весом.
 * Результат розыгрыша записывается в файл.
 */
public class ToyStore {

    private class Toy {
        String id;
        String name;
        int frequency;

        public Toy(String id, String name, int frequency) {
            this.id = id;
            this.name = name;
            this.frequency = frequency;
        }
    }

    private PriorityQueue<Toy> toyQueue;
    private HashMap<String, Integer> toyWeights;
    private int totalWeight;

    public ToyStore() {
        toyQueue = new PriorityQueue<>((t1, t2) -> t2.frequency - t1.frequency);
        toyWeights = new HashMap<>();
        totalWeight = 0;
    }

    public void addToy(String id, String name, int weight) {
        Toy toy = new Toy(id, name, weight);
        toyQueue.add(toy);
        toyWeights.put(id, weight);
        totalWeight += weight;
    }

    public void performGiveaway(String filename, int numToys) {
        try {
            FileWriter writer = new FileWriter(filename);
            Random random = new Random();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numToys; i++) {
                int randomNumber = random.nextInt(totalWeight) + 1;
                String toyId = getToyIdByWeight(randomNumber);
                String toyName = getToyNameById(toyId);
                writer.write(toyId + " " + toyName);
                writer.write(System.lineSeparator());

                result.append(toyId).append(" ").append(toyName).append("\n");
            }
            writer.close();
            System.out.println("Результаты розыгрыша сохранены в файл " + filename);
            System.out.println("Результаты розыгрыша:");
            System.out.println(result.toString());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении результатов в файл");
            e.printStackTrace();
        }
    }

    private String getToyIdByWeight(int randomNumber) {
        int currentWeight = 0;
        for (Map.Entry<String, Integer> entry : toyWeights.entrySet()) {
            currentWeight += entry.getValue();
            if (randomNumber <= currentWeight) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getToyNameById(String toyId) {
        for (Toy toy : toyQueue) {
            if (toy.id.equals(toyId)) {
                return toy.name;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();
        toyStore.addToy("1", "Конструктор", 2);
        toyStore.addToy("2", "Робот", 2);
        toyStore.addToy("3", "Кукла", 6);

        toyStore.performGiveaway("results.txt", 10);
    }
}