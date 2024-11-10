import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final AtomicInteger COUNTER_3 = new AtomicInteger();
    public static final AtomicInteger COUNTER_4 = new AtomicInteger();
    public static final AtomicInteger COUNTER_5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String word : texts) {
                String reversedWord = new StringBuilder(word).reverse().toString();
                if (word.equals(reversedWord)) {
                    incrementCounter(word.length());
                }
            }
        });
        thread1.start();


        Thread thread2 = new Thread(() -> {
            for (String word : texts) {
                boolean nice = true;
                char first = word.charAt(0);
                for (int i = 1; i < word.length(); i++) {
                    if (first != word.charAt(i)) {
                        nice = false;
                        break;
                    }
                }
                if (nice) {
                    incrementCounter(word.length());
                }
            }
        });
        thread2.start();


        Thread thread3 = new Thread(() -> {
            for (String word : texts) {
                char[] array = Arrays.copyOf(word.toCharArray(), word.length());
                Arrays.sort(array);
                if (Arrays.equals(word.toCharArray(), array)) {
                    incrementCounter(word.length());
                }
            }
        });
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов с длиной 3: " + COUNTER_3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + COUNTER_4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + COUNTER_5 + " шт");
    }


    public static void incrementCounter(int wordLength) {
        if (wordLength == 3) {
            COUNTER_3.incrementAndGet();
        } else if (wordLength == 4) {
            COUNTER_4.incrementAndGet();
        } else if (wordLength == 5) {
            COUNTER_5.incrementAndGet();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
