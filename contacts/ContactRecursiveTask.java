package contacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class ContactRecursiveTask extends RecursiveTask<Set<Contact>> {

	private final Area area;
	private final int maxEntityCount;
	private final int splitCount;

	public ContactRecursiveTask(Area area, int maxEntityCount, int splitCount) {
		this.area = area;
		this.maxEntityCount = maxEntityCount;
		this.splitCount = splitCount;
	}

	@Override
	protected Set<Contact> compute() {
		Set<Contact> contacts = new HashSet<Contact>();
		if (area.getEntities().size() < maxEntityCount) {
			for (Entity e1 : area.getEntities()) {
				for (Entity e2 : area.getEntities()) {
					if (!e1.equals(e2)) {
						if (e1.distance(e2) < Constants.CONTACT_DISTANCE) {
							Contact c = Contact.of(e1, e2);
							contacts.add(c);
						}
					}
				}
			}
			return contacts;
		}
		Area[] areas = area.split(2);
		List<ContactRecursiveTask> tasks = new ArrayList<>(areas.length);
		for (Area area : areas) {
			ContactRecursiveTask task1 = new ContactRecursiveTask(area, maxEntityCount, splitCount);
			task1.fork();
			tasks.add(task1);
		}

		for (ContactRecursiveTask task : tasks) {
			contacts.addAll(task.join());
		}
		return contacts;
	}

}
