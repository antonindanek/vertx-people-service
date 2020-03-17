package people.storage;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import people.model.Person;

public class InMemoryPeopleStorageTest {
	
	private PeopleStorage storage;
	
	private Person p1 = new Person(1, "Antonin");
	private Person p2 = new Person(2, "Michal");
	private Person p3 = new Person(3, "Martin");
	
	@Before
	public void setUp() {
		storage = new InMemoryPeopleStorage();
	}
	
	@Test
	public void saveSuccess() {
		storage.save(p1);
		
		Assert.assertEquals(p1, storage.getPerson(p1.getId()));
	}
	
	@Test
	public void saveDuplicate() {
		storage.save(p1);
		
		try {
			storage.save(p1);
		} catch (RuntimeException e) {
			Assert.assertEquals("User 1 already exists", e.getMessage());
			return;
		}
		
		Assert.fail();
	}
	
	@Test
	public void getNonExistent() {
		
		try {
			storage.getPerson(p1.getId());
		} catch (RuntimeException e) {
			Assert.assertEquals("User 1 not found", e.getMessage());
			return;
		}
		
		Assert.fail();
	}
	
	@Test
	public void getAll() {
		
		storage.save(p1);
		storage.save(p2);
		storage.save(p3);
		
		List<Person> storageContent = storage.getAll();
		
		Assert.assertTrue(storageContent.contains(p1));
		Assert.assertTrue(storageContent.contains(p2));
		Assert.assertTrue(storageContent.contains(p3));
	}

}
