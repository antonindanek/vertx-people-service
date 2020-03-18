package people.model;

public class Person {
	
	private int id;
	private String name;
	
	// empty constructor needed for automatic mapping from JSON
	public Person() {
	}
	
	public Person(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Person) {
			return ((Person) obj).getId() == this.getId();
		}
		
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return "Person[" + id + ", " + name + "]";
	}
	
}
