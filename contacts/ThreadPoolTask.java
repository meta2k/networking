package contacts;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ThreadPoolTask implements Callable<Set<Contact>> {
	
	private final Area area;
	private final CountDownLatch cdl;
	
	public ThreadPoolTask(Area area, CountDownLatch cdl) {
		this.area = area;
		this.cdl = cdl;
	}

	@Override
	public Set<Contact> call() throws Exception {
		Set<Contact> contacts = new HashSet<Contact>(); 
		for (Entity e1: area.getEntities()) {
			for (Entity e2: area.getEntities()) {
				if (!e1.equals(e2)) {
					if (e1.distance(e2) < Constants.CONTACT_DISTANCE) {
						Contact c = Contact.of(e1, e2);
						contacts.add(c); 
					}
				}
			}
		}
		cdl.countDown();
		return contacts;
	}
	
	

}
