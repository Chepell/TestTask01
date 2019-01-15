import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

/**
 * Artem Voytenko
 * 14.01.2019
 * <p>
 * реализую класс в виде Singleton
 */

public class Company {
	// поля класса с аннотациями
	@Property(propertyName = "com.mycompany.name")
	private String companyName;

	@Property(propertyName = "com.mycompany.owner", defaultValue = "It's my company!")
	private String companyOwner;

	@Property(propertyName = "com.mycompany.years.old", defaultValue = "2")
	private Integer companyAge;

	@Property(propertyName = "com.mycompany.address", defaultValue = "{\"street\": \"Lenina\", \"home\": 11}")
	private Address companyAddress;

	// поле для хранения единственного объекта класса
	private static Company instance;

	// логгер класса
	private static Logger logger = Logger.getLogger(Company.class.getName());

	// скрываю конструктор, создание объекта только через статичный метод
	private Company() {}

	/**
	 * Метод ленивой инициализации объекта, делаю его потокобезопасным
	 * раз предполагается что может быть обращение к методу doRefresh()
	 * в нескольких потоках, тогда и создание объекта пусть будет потокобезопасным
	 *
	 * @return объект класса
	 */
	public static synchronized Company getInstance() {
		// если объект еще не создавался,
		if (instance == null) {
			synchronized (Company.class) {
				// то создать объект
				instance = new Company();
			}
		}
		// и вызваю на объекте метод установки значени полей из файла properties
		instance.doRefresh();
		return instance;
	}

	/**
	 * метод находит все поля в текущем объекте помеченные аннотацией @Property,
	 * и заполняет эти поля соответствующим значением лежащим в файле data.properties
	 */
	public synchronized void doRefresh() {
		// создается объект обработчика файла properties
		FilePropertiesHandler propertiesHandler = new FilePropertiesHandler();
		// через рефлексию получаю массив всех полей текущего объекта
		Field[] fields = this.getClass().getDeclaredFields();
		// обхожу циклом все поля
		for (Field field : fields) {
			// делаю приватное поле доступными
			if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
			// получаю у текущего поля аннотацию @Property
			Property annotation = field.getAnnotation(Property.class);
			// если аннотация у поля есть то
			if (annotation != null) {
				// получаю имя поля propertyName аннотации @Property в виде строки
				String key = annotation.propertyName();
				// получаю из файла параметров value по имени поля аннотации
				String value = propertiesHandler.getParamByName(key);
				// если такой пары key:value не нашлось в файле properties или value
				// оказалось пустым, тогда беру value из поля defaultValue аннотации
				if (value == null) value = annotation.defaultValue();
				// помещаю value как значение в поле объекта только если value не пустое
				// иначе поле не инициализируется вручную никаким значением
				if (!value.isEmpty()) {
					try {
						// получаю тип поля в виде стоки
						String fieldType = field.getType().getSimpleName();
						// и исходя из типа трансформирую строку value в
						// нужный тип данных и помещаю в текущее поле
						switch (fieldType) {
							case "Integer":
								field.set(this, getIntFromValue(value, annotation));
								break;
							case "Address":
								field.set(this, deserializeAddressObject(value, annotation));
								break;
							default:
								// по умоланию, для строковых полей
								field.set(this, value);
						}
					} catch (IllegalAccessException e) {
						logger.warning("Проблема с обновлением значений в полях объекта!");
					}
				}
			}
		}
	}

	/**
	 * Метод парсинга строки в число с обработкой исключений
	 *
	 * @param value      принимает значение в виде строки
	 * @param annotation принимает объект аннотации
	 * @return Integer, а если парсинг не удался то null
	 */
	private Integer getIntFromValue(String value, Property annotation) {
		Integer result = null;
		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			logger.warning("Проблема с получением возраста компании из файла!");

			logger.info("Пробую взять дифолтное значение возраста из аннотации.");
			try {
				result = Integer.parseInt(annotation.defaultValue());
			} catch (Exception e1) {
				logger.warning("Взять дифолтное значение возраста из аннотации так же не удалось!");
			}
		}
		return result;
	}

	/**
	 * Метод десериализации объекта из строки
	 *
	 * @param stringForDeserialize сериализованный объект в виде строки
	 * @param annotation           принимает объект аннотации
	 * @return десериализованный объект
	 */
	private Address deserializeAddressObject(String stringForDeserialize, Property annotation) {
		ObjectMapper mapper = new ObjectMapper();
		Address address = null;
		try {
			address = mapper.readValue(stringForDeserialize, Address.class);
		} catch (IOException e) {
			logger.warning("Проблема с десериализацией адреса из файла!");

			logger.info("Пробую взять дифолтное значение адресав из аннотации.");
			try {
				address = mapper.readValue(annotation.defaultValue(), Address.class);
			} catch (Exception e1) {
				logger.warning("Десериализовать адрес из дифолтного значения аннотации так же не удалось!");
			}
		}
		return address;
	}

	@Override
	public String toString() {
		return "Company{" +
				"companyName='" + companyName + '\'' +
				", companyOwner='" + companyOwner + '\'' +
				", companyAge=" + companyAge +
				", companyAddress=" + companyAddress +
				'}';
	}
}
