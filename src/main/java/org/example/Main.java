package org.example;

import java.util.Arrays;
import java.util.Random;

/**
 * Здесь собраны сортировки для изучения и повторения
 */

public class Main {
    public static void main(String[] args) {
        int[] ints = generateArray();

        //QuickSort
        long timeForQuickSort_Start = System.nanoTime();
        int[] forQuickSort = generateArray();
        QuickSort.quickSort(forQuickSort, 0, forQuickSort.length - 1);
        System.out.println(Arrays.toString(Arrays.stream(forQuickSort).limit(50).toArray()));
        long timeForQuickSort_End = System.nanoTime();
        System.out.println("lead time of QuickSort: " + (timeForQuickSort_End - timeForQuickSort_Start) / 1_000_000 + " ms");

        System.out.println("=========================================================================================");

        //MergeSort
        long timeForMergeSort_Start = System.nanoTime();
        int[] forMergeSort = generateArray();
        MergeSort.mergeSort(forMergeSort);
        System.out.println(Arrays.toString(Arrays.stream(forMergeSort).limit(50).toArray()));
        long timeForMergeSort_End = System.nanoTime();
        System.out.println("lead time of MergeSort: " + (timeForMergeSort_End - timeForMergeSort_Start) / 1_000_000 + " ms");
    }

    //Метод для генерации рандомного массива
    private static int[] generateArray() {
        Random random = new Random();
        int[] ints = new int[100_000];
        for (int i = 0; i < 100_000; i++) {
            ints[i] = random.nextInt(100_000);
        }
        return ints;
    }

}

/**
 * Затрачиваемое время O(n log2 n). Сортировка считается достаточно быстрой.
 *
 * @1. Разбиваем исходный массив на массивы размером в 1 элемент
 * @2. Объединение массивов размером в 1 элемент в массивы по 2 элемента
 * @3. Объединение массивов размером в 2 элемента в массивы по 4 элемента
 * @4. Объединение массивов размером в 4 элемента в массивы по 8 элемента
 * @5. ...
 * @6. Так продолжается пока не дойдёт до конца. В конце идёт объединение массивов размером N в
 * результирующий массив. Конец сортировки
 */
class MergeSort {
    public static int[] mergeSort(int[] arr) {

        //Для для того чтобы менять местами
        int[] tmp;

        //Источник откуда сливаем массивы друг с другом
        int[] currentSrc = arr;

        //Массив приёмник, куда мы заливаем результирующие массивы после слияния
        int[] currentDest = new int[arr.length];

        //хранится текущий размер сливаемых массивов
        int size = 1;

        while (size < arr.length) {
            //В этом цикле каждый раз берётся по два массива размером size и происходит их слияние в массив размером size * 2
            for (int i = 0; i < arr.length; i += 2 * size) {
                merge(currentSrc, i, currentSrc, i + size, currentDest, i, size);
            }
            //здесь меняются местами приёмник и источник
            tmp = currentSrc;
            currentSrc = currentDest;
            currentDest = tmp;

            //На каждом шаге увеличивается на 2  || 1 -> 2 -> 4 -> 8 -> 16 -> 32...
            size = size * 2;
        }
        return currentSrc;
    }

    /**
     * @param src1      первый массив
     * @param src1Start с какого элемента начинать из массива src1
     * @param src2      второй массив
     * @param src2Start с какого элемента начинать из массива src2
     * @param dest      массив-приёмник
     * @param destStart с какого элемента начинать запись в массив dest
     * @param size      указывает какого размера подмассив хотите сливать
     */
    public static void merge(int[] src1, int src1Start, int[] src2, int src2Start, int[] dest, int destStart, int size) {
        //Присваиваем индексы
        int index1 = src1Start;
        int index2 = src2Start;

        //Определяем концы двух подмассивов
        int src1End = Math.min(src1Start + size, src1.length);
        int src2End = Math.min(src2Start + size, src2.length);

        //Находим общие кол-во элементов двух массивов для необходимого кол-ва циклов(суммарное кол-во элементов двух массивов)
        int iterationCount = src1End - src1Start + src2End - src2Start;

        //Цикл начинается с destStart и в условиях определяем кол-во циклов (destStart + iterationCount)
        for (int i = destStart; i < destStart + iterationCount; i++) {
            //src1[index1] < src2[index2] - опредеяет что если элемент в первом подмассиве(src1) меньше чем во втором(src2),
            //То записать в результирующий массив dest
            //index1 < src1End && index2 >= src2End - предназначенны, когда в первом или во втором массиве заканчиваются элементы
            if (index1 < src1End && (index2 >= src2End || src1[index1] < src2[index2])) {
                dest[i] = src1[index1];
                index1++;
            } else { // Иначе, записать в результирующий массив dest элемент второго подмассива
                dest[i] = src2[index2];
                index2++;
            }
        }
    }
}


/**
 * Затрачиваемое время O(n log2 n). Сортировка считается достаточно быстрой.
 * <br>Быстрая сортировки в Java (Quick sort)
 * Описание алгоритма сортировки:
 *
 * @1.Выбираем опорный элемент из массива. Обычно это средний элемент.
 * @2.Делим массив на 2 подмассива. Элементы, которые меньше опорного, и элементы, которые больше опорного.
 * @3.Рекурсивно применяем сортировку к обоим подмассивам.
 */
class QuickSort {
    //рекурсивный метод, который будет делить массив на два подмассива
    public static void quickSort(int[] arr, int from, int to) {
        if (arr.length == 0 || arr == null)
            return;

        if (from < to) {
            int divideIndex = partition(arr, from, to);
            quickSort(arr, from, divideIndex - 1);
            quickSort(arr, divideIndex, to);
        }
    }

    //Сортирует массив вызовом swap()}
    private static int partition(int[] arr, int from, int to) {
        int rightIndex = to;
        int leftIndex = from;

        int pivot = arr[from + (to - from) / 2];
        while (leftIndex <= rightIndex) {
            while (arr[leftIndex] < pivot) {
                leftIndex++;
            }
            while (arr[rightIndex] > pivot) {
                rightIndex--;
            }
            if (leftIndex <= rightIndex) {
                swap(arr, rightIndex, leftIndex);
                leftIndex++;
                rightIndex--;
            }
        }
        return leftIndex;
    }

    //Меняет местами два элемента массива
    private static void swap(int[] arr, int rightIndex, int leftIndex) {
        int temp = arr[rightIndex];
        arr[rightIndex] = arr[leftIndex];
        arr[leftIndex] = temp;
    }
}