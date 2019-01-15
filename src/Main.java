import java.io.IOException;

/**
 * Artem Voytenko
 * 14.01.2019
 */

public class Main {
	public static void main(String[] args) throws IOException {

		Company company = Company.getInstance();
		System.out.println(company);

		FilePropertiesHandler propertiesHandler = new FilePropertiesHandler();

//		propertiesHandler.setCompanyName("BadConmany");
//
//		Company company2 = Company.getInstance();
//		System.out.println(company2);


	}
}
