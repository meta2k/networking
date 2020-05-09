package contacts;

import static contacts.Constants.CONTACT_DISTANCE;
import static contacts.Constants.N_ENTITIES;
import static contacts.Constants.SIZE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainThreadPool {

	public static void main(String[] args) {

		final int subAreas = Runtime.getRuntime().availableProcessors();

		System.out.println("\n========== Sequential ========================");
		System.out.format("SIZE: %,d%n", SIZE);
		System.out.format("N_ENTITIES: %,d%n", N_ENTITIES);
		System.out.format("DISTANCE: %,d%n", CONTACT_DISTANCE);
		System.out.println("-------------------------------------------------");

		// creates an area and populates the area with entities
		Area area = new Area(0, 0, SIZE, SIZE);
		MainSeq.populate(area, N_ENTITIES);

		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(subAreas);

		Area[] splitted = area.split(subAreas);
		List<Future<Set<Contact>>> contactFutures = new ArrayList<>(splitted.length);
		ExecutorService threadpool = Executors.newFixedThreadPool(splitted.length);
		for (int i = 0; i < splitted.length; i++) {
			ThreadPoolTask task = new ThreadPoolTask(splitted[i], cdl);
			Future<Set<Contact>> contacts = threadpool.submit(task);
			contactFutures.add(contacts);
		}
		try {
			cdl.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		
		threadpool.shutdown();
		while (!threadpool.isShutdown()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		Set<Contact> allContacts = contactFutures.stream().flatMap(f -> {
			try {
				return f.get().stream();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return Stream.empty();
		}).collect(Collectors.toSet());

		System.out.format("\nElapsed time [ms] %,d", (System.currentTimeMillis() - start));

		System.out.println("\n-------------------------------------------------");
		System.out.println("Contacts: " + allContacts.size());
		System.out.println("\n-------------------------------------------------");
		for (Contact c : allContacts) {
			System.out.println(c);
		}
		System.out.println("\n-------------------------------------------------");
	}
}
