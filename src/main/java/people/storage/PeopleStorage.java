package people.storage;

import java.util.List;

import people.model.Person;

public interface PeopleStorage {

	Person getPerson(int id);
	List<Person> getAll();
	void save(Person person);
}
