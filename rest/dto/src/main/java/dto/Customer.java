package dto;

import java.util.Arrays;
import java.util.List;

public class Customer {

	static final List<String> names = Arrays.asList("Mona", "Peter", "Anna", "Gustav", "Vendella", "Rolf");

	String name;

	String email;

	int age;

	public Customer(String email, int age, String name) {
		this.email = email;
		this.name = name;
		this.age = age;
	}
	
	public Customer() {
	}
	
	public Customer(String email) {
		this.name = names.get(Utils.randInt(0, names.size() - 1));
		this.email = email;
		this.age = Utils.randInt(18, 65);
	}

	public static List<String> getNames() {
		return names;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
