package org.example;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        BTree bTree = new BTree();


        Random random = new Random();
        int[] array = new int[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100000);
        }

        long totalInsertionTime = 0;
        int insertionCount = 0;


        long startTime, endTime;
        for (int i = 0; i < array.length; i++) {
            startTime = System.nanoTime();
            bTree.insert(array[i]);
            endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            totalInsertionTime += elapsedTime;
            insertionCount++;
        }


        double averageInsertionTime = (double) totalInsertionTime / insertionCount;
        System.out.println("Среднее время добавления: " + averageInsertionTime + " наносекунд");

        long totalSearchTime = 0;
        int searchCount = 0;

        // Поиск случайных чисел в дереве
        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(array.length);
            startTime = System.nanoTime();
            boolean found = bTree.search(array[index]);
            endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            totalSearchTime += elapsedTime;
            searchCount++;
        }


        double averageSearchTime = (double) totalSearchTime / searchCount;
        System.out.println("Среднее время поиска: " + averageSearchTime + " наносекунд");


        long totalDeletionTime = 0;
        int deletionCount = 0;

        for (int i = 0; i < 1000; i++) {
            int index = random.nextInt(array.length);
            startTime = System.nanoTime();
            bTree.delete(array[index]);
            endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            totalDeletionTime += elapsedTime;
            deletionCount++;
        }


        double averageDeletionTime = (double) totalDeletionTime / deletionCount;
        System.out.println("Среднее время удаления: " + averageDeletionTime + " наносекунд");
    }
}