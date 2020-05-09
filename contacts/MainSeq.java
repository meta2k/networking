package contacts;

import static contacts.Constants.CONTACT_DISTANCE;
import static contacts.Constants.N_ENTITIES;
import static contacts.Constants.RAND;
import static contacts.Constants.SIZE;

import java.util.HashSet;
import java.util.Set; 

/**
 * Main class for a program which computes the contacts for the entities within an area. 
 * This is a sequential program which tests all entities pairwise, if they are within 
 * {@link Constants.DISTANCE} distance. 
 * Testing entities pairwise has quadratic run-time complexity. 
 */
public class MainSeq {

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
		
		//====================================================================
		Set<Contact> contacts = new HashSet<Contact>(); 
		for (Entity e1: area.getEntities()) {
			for (Entity e2: area.getEntities()) {
				if (!e1.equals(e2)) {
					if (e1.distance(e2) < CONTACT_DISTANCE) {
						Contact c = Contact.of(e1, e2);
						contacts.add(c); 
					}
				}
			}
		}		
		//====================================================================
		
		System.out.format("\nElapsed time [ms] %,d", (System.currentTimeMillis() - start)); 

		System.out.println("\n-------------------------------------------------"); 
		System.out.println("Contacts: " + contacts.size()); 
		System.out.println("\n-------------------------------------------------"); 
		for (Contact c : contacts) {
			System.out.println(c); 
		}
		System.out.println("\n-------------------------------------------------"); 
	}
	
	/**
	 * Randomly populates an area with n entities. 
	 * @param area
	 */
	public static void populate(Area area, int n) {
		for (int i = 0; i < n; i++) {
			Entity e = Entity.at(RAND.nextInt(SIZE), RAND.nextInt(SIZE)); 
			area.add(e);
		}
		
	}

}
