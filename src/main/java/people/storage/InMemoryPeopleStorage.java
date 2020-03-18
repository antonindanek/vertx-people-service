package people.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import people.model.Person;

public class InMemoryPeopleStorage implements PeopleStorage {

	// <id, person>
	private Map<Integer, Person> storage = new HashMap<>();

	private Logger logger = LoggerFactory.getLogger(InMemoryPeopleStorage.class);
	
	@Override
	public Person getPerson(int id) {

		logger.info("Reading a person with id " + id);
		
		Person person = storage.get(id);

		if (person == null) {
			throw new StorageException("User " + id + " not found");
		}
		
		return person;
	}

	@Override
	public List<Person> getAll() {
		
		logger.info("Reading all persons");

		return new ArrayList<Person>(storage.values());
	}

	@Override
	public void save(Person person) {

		logger.info("Saving person " + person);
		
		if (!storage.containsKey(person.getId())) {
			storage.put(person.getId(), person);
		} else {
			throw new StorageException("User " + person.getId() + " already exists");
		}
	}

}
