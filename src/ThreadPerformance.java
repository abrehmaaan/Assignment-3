import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ThreadPerformance {
	// Constants
	private static final int NUM_THREADS = 5;
	private static final int NUM_VALUES = 1000;
	private static final int MIN_VALUE = 0;
	private static final int MAX_VALUE = 1000;
	private static final String FILENAME = "random_values.txt";

	public static void main(String[] args) {
		// Generate random values and write them to a file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(FILENAME)))) {
			Random random = new Random();
			for (int i = 0; i < NUM_VALUES; i++) {
				int value = (int)(random.nextDouble() * ((MAX_VALUE - MIN_VALUE) + 1)) + MIN_VALUE;
				bw.write(Integer.toString(value));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Read values from the file and store them in an array
		int[] values = new int[NUM_VALUES];
		try (Scanner scanner = new Scanner(new File(FILENAME))) {
			for (int i = 0; i < NUM_VALUES; i++) {
				values[i] = scanner.nextInt();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Find the minimum value using multithreading
		long startTime = System.currentTimeMillis();
		int minValueMultithreaded = findMinValueMultithreaded(values, NUM_THREADS);
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		System.out.println("Minimum value using multithreading: " + minValueMultithreaded);
		System.out.println("Elapsed time: " + elapsedTime + "ms");

		// Find the minimum value without using multithreading
		startTime = System.currentTimeMillis();
		int minValueSingleThreaded = findMinValueSingleThreaded(values);
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		System.out.println("Minimum value without using multithreading: " + minValueSingleThreaded);
		System.out.println("Elapsed time: " + elapsedTime + "ms");
	}

	// Finds the minimum value in the given array using multithreading
	private static int findMinValueMultithreaded(int[] values, int numThreads) {
		  int minValue = Integer.MAX_VALUE;
		  MinValueThread[] threads = new MinValueThread[numThreads];

		  // Divide the array into equal-sized chunks and find the minimum value in each chunk in a separate thread
		  int chunkSize = values.length / numThreads;
		  for (int i = 0; i < numThreads; i++) {
		    int startIndex = i * chunkSize;
		    int endIndex = startIndex + chunkSize;
		    if (i == numThreads - 1) {
		      endIndex = values.length; // The last thread may have a larger chunk if the array size is not divisible by the number of threads
		    }
		    threads[i] = new MinValueThread(values, startIndex, endIndex);
		    threads[i].start();  // Start the thread
		  }

		  // Wait for all threads to complete and find the overall minimum value
		  try {
		    for (MinValueThread thread : threads) {
		      thread.join();
		      minValue = Math.min(minValue, thread.getMinValue());
		    }
		  } catch (InterruptedException e) {
		    e.printStackTrace();
		  }
		  return minValue;
		}

	//Finds the minimum value in the given array without using multithreading
	private static int findMinValueSingleThreaded(int[] values) {
		int minValue = Integer.MAX_VALUE;
		for (int value : values) {
			minValue = Math.min(minValue, value);
		}
		return minValue;
	}
}
