import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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

	@Property(propertyName = "com.mycompany.years.old")
	private Integer companyAge;

	// обработка поля пока не реализована
	//	private Address companyAddress;

	// поле для хранения единственного объекта класса
	private static Company instance;
	// дополнительный функционал к классу подключаю через композицию
	// объекта класса обработчика файлов properties в поле текущего класса
	private FilePropertiesHandler propertiesHandler;

	// замена констуктора по умолчанию
	private Company() {
		doRefresh();
	}

	// метод ленивой инициализации объекта
	public static Company getInstance() {
		if (instance == null) instance = new Company();
		return instance;
	}

	// метод находит все поля в текущем объекте, помеченные аннотацией @Property,
	// и заполняю эти поля соответствующим значением лежащим в файле data.properties
	public synchronized void doRefresh() {
		// создается объект обработчика файла properties
		propertiesHandler = new FilePropertiesHandler();
		// через рефлексию получаю массив всех полей текущего объекта
		Field[] fields = this.getClass().getDeclaredFields();
		// обхожу циклом все элементы массива
		for (Field field : fields) {
			// делаю приватные поля доступными
			if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
			// получаю у текущего поля аннотацию @Property
			Property annotation = field.getAnnotation(Property.class);
			// если аннотация у поля есть то
			if (annotation != null) {
				// получаю имя поля propertyName аннотации @Property в виде строки
				String key = annotation.propertyName();
				// получаю из файла параметров value по имени поля аннотации
				String value = propertiesHandler.getStringParam(key);
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
								int intValue = Integer.parseInt(value);
								field.set(this, intValue);
								break;
							case "Address":
								// пока нет реализации
								break;
							default:
								// по умоланию, для строковых полей
								field.set(this, value);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Company{" +
				"companyName='" + companyName + '\'' +
				", companyOwner='" + companyOwner + '\'' +
				", companyAge=" + companyAge +
				'}';
	}
}
