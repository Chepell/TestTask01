import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Artem Voytenko
 * 14.01.2019
 * <p>
 * Класс для хранения адреса в виде объекта
 */

@JsonAutoDetect()
public class Address {
	private String street;
	private Integer home;

//	public Address() {
//	}
//
//	public Address(String street, Integer home) {
//		this.street = street;
//		this.home = home;
//	}

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

	@Override
	public String toString() {
		return "Address{" +
				"street='" + street + '\'' +
				", home=" + home +
				'}';
	}
}
