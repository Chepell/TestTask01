/**
 * Artem Voytenko
 * 14.01.2019
 *
 * Класс для хранения адреса в виде объекта
 */
public class Address {
	private String street;
	private Integer home;

	public Address(String street, Integer home) {
		this.street = street;
		this.home = home;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getHome() {
		return home;
	}

	public void setHome(Integer home) {
		this.home = home;
	}
}
