package contacts;

import static contacts.Constants.CONTACT_DISTANCE;
import static contacts.Constants.N_ENTITIES;
import static contacts.Constants.SIZE;
import static contacts.MainSeq.populate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class MainForkJoin {

	public static void main(String[] args) {

		System.out.println("\n========== Sequential ========================");
		System.out.format("SIZE: %,d%n", SIZE);
		System.out.format("N_ENTITIES: %,d%n", N_ENTITIES);
		System.out.format("DISTANCE: %,d%n", CONTACT_DISTANCE);
		System.out.println("-------------------------------------------------");

		// creates an area and populates the area with entities
		Area area = new Area(0, 0, SIZE, SIZE);
		populate(area, N_ENTITIES);

		long start = System.currentTimeMillis();

		// ====================================================================
		Set<Contact> contacts = new HashSet<Contact>();
		ContactRecursiveTask task = new ContactRecursiveTask(area, 100, 5);
		ForkJoinPool pool = new ForkJoinPool();
		contacts = pool.invoke(task);
		
		// ====================================================================

		System.out.format("\nElapsed time [ms] %,d", (System.currentTimeMillis() - start));

		System.out.println("\n-------------------------------------------------");
		System.out.println("Contacts: " + contacts.size());
		System.out.println("\n-------------------------------------------------");
		for (Contact c : contacts) {
			System.out.println(c);
		}
		System.out.println("\n-------------------------------------------------");
	}

}
