package people.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import people.model.Person;

public class InMemoryPeopleStorage implements PeopleStorage {

	// <id, person>
	Map<Integer, Person> storage = new HashMap<>();

	@Override
	public Person getPerson(int id) {

		Person person = storage.get(id);

		if (person == null) {
			throw new StorageException("User " + id + " not found");
		}

		return person;
	}

	@Override
	public List<Person> getAll() {

		return new ArrayList<Person>(storage.values());
	}

	@Override
	public void save(Person person) {

		if (!storage.containsKey(person.getId())) {
			storage.put(person.getId(), person);
		} else {
			throw new StorageException("User " + person.getId() + " already exists");
		}
	}

}
