import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Artem Voytenko
 * 14.01.2019
 * <p>
 * Класс обработчик операций работы с файлом параметров
 */
class FilePropertiesHandler {
	private String file = "resources/data.properties";
	private Properties properties = new Properties();

	/**
	 * Конструктор при создании объекта производит загрузку настроек
	 * из файла в поле properties класса и автозакрытие потока
	 */
	//
	public FilePropertiesHandler() {
		try (InputStream stream = Files.newInputStream(Paths.get(file))) {
			properties.load(stream);
		} catch (IOException e) {
			System.out.println("Can't read properties file");
		}
	}

	/**
	 * Констуктор с явным указанием имени файла параметров
	 *
	 * @param fileName имя файла (без раширения)
	 */
	public FilePropertiesHandler(String fileName) {
		file = "resources/" + fileName + ".properties";
		try (InputStream stream = Files.newInputStream(Paths.get(file))) {
			properties.load(stream);
		} catch (IOException e) {
			System.out.println("Can't read properties file");
		}
	}

	/**
	 * Метод производит сохранение настроек из поля properties в файл и закрытие потока
	 */
	public void saveProperties() {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
			properties.store(writer, null);
		} catch (IOException e) {
			System.out.println("Can't write properties file");
		}
	}

	/**
	 * @param paramName имя параметра в properties файле
	 * @return значение параметра в виде строки
	 */
	public String getStringParam(String paramName) {
		return properties.getProperty(paramName);
	}


	/**
	 * Используется метод getStringParam(), результат парсится в Integer
	 *
	 * @param paramName имя параметра в properties файле
	 * @return значение параметра в виде Integer
	 */
	public Integer getIntegerParam(String paramName) {
		int result = 0;
		try {
			result = Integer.parseInt(getStringParam(paramName));
		} catch (NumberFormatException e) {
			System.out.println("Can't read int value from file");
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * Тестовый метод для провеки работы метода doRefresh() в главной программе
	 * Метод обновляет значение по имени после чего вызывает метод saveProperties()
	 * для сохранения изменений в файл
	 *
	 * @param nameValue новое значение параметра в properties файле
	 */
	public void setCompanyName(String nameValue) {
		properties.setProperty("com.mycompany.name", nameValue);
		saveProperties();
	}

}
