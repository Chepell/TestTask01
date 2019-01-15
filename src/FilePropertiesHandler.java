import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Artem Voytenko
 * 14.01.2019
 * <p>
 * Класс обработчик операций работы с файлом параметров
 */
class FilePropertiesHandler {
	// имя файла по умолчанию
	private String file = "resources/data.properties";
	// мэп для хранения свойств полученных из файла
	private Properties properties = new Properties();
	// свой логгер
	private static Logger logger = Logger.getLogger(Company.class.getName());

	/**
	 * Конструктор при создании объекта производит загрузку настроек
	 * из файла в поле properties и автозакрытие потока
	 */
	//
	public FilePropertiesHandler() {
		try (InputStream stream = Files.newInputStream(Paths.get(file))) {
			properties.load(stream);
		} catch (IOException e) {
			logger.warning("Не могу прочитать файл " + file);
		}
	}

	/**
	 * Констуктор с явным указанием имени файла параметров
	 *
	 * @param fileName имя файла (без расширения)
	 */
	public FilePropertiesHandler(String fileName) {
		file = "resources/" + fileName + ".properties";
		try (InputStream stream = Files.newInputStream(Paths.get(file))) {
			properties.load(stream);
		} catch (IOException e) {
			logger.warning("Не могу прочитать файл " + file);
		}
	}

	/**
	 * @param paramName имя параметра в properties файле
	 * @return значение параметра в виде строки
	 */
	public String getParamByName(String paramName) {
		return properties.getProperty(paramName);
	}

	/**
	 * Метод производит сохранение настроек из поля properties в файл и закрытие потока
	 */
	public void savePropertiesToFile() {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
			properties.store(writer, null);
		} catch (IOException e) {
			System.out.println("Can't write properties file");
		}
	}

	/**
	 * Тестовый метод для провеки работы метода doRefresh() в главной программе
	 * Метод обновляет значение по имени после чего вызывает метод savePropertiesToFile()
	 * для сохранения изменений в файл
	 *
	 * @param nameValue новое значение параметра в properties файле
	 */
	public void setCompanyName(String nameValue) {
		properties.setProperty("com.mycompany.name", nameValue);
		savePropertiesToFile();
	}
}
