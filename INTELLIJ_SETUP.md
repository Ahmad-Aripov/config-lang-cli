# Инструкция по запуску в IntelliJ IDEA

## Способ 1: Запуск через конфигурацию Run/Debug (РЕКОМЕНДУЕТСЯ)

### Шаг 1: Первый запуск
1. Откройте файл `src/main/java/org/example/Main.java` в редакторе
2. Найдите метод `main(String[] args)` (строка 16)
3. Нажмите правой кнопкой мыши на зелёном треугольнике слева от `public static void main`
4. Выберите **"Run 'Main.main()'"** или просто нажмите **Ctrl+Shift+F10**

**Примечание:** При первом запуске программа завершится с ошибкой, потому что не переданы аргументы. Это нормально!

### Шаг 2: Настроить аргументы командной строки
1. Вверху экрана (рядом с кнопками Run/Debug) найдите выпадающий список конфигураций
2. Нажмите на него и выберите **"Edit Configurations..."** (или нажмите **Alt+Shift+F10**, затем **0**)
3. В открывшемся окне найдите поле **"Program arguments"**
4. Введите один из вариантов:
   ```
   -i test.conf
   ```
   или
   ```
   -i src/main/resources/examples/network.conf
   ```
   или
   ```
   -i src/main/resources/examples/game.conf
   ```
5. Нажмите **OK**

### Шаг 3: Запустить программу
- Нажмите кнопку **Run** (зелёный треугольник) или **Shift+F10**
- Результат (XML) появится в консоли внизу

### Альтернативный способ настройки (через меню)
1. Меню **Run** → **Edit Configurations...**
2. Если конфигурации нет, нажмите **"+"** → **Application**
3. В поле **"Name"** введите: `Main`
4. В поле **"Main class"** введите: `org.example.Main`
5. В поле **"Program arguments"** введите: `-i test.conf`
6. Нажмите **OK**

## Способ 2: Запуск через терминал IntelliJ IDEA

1. Откройте встроенный терминал в IDEA:
   - **View** → **Tool Windows** → **Terminal**
   - Или нажмите **Alt+F12**
2. Убедитесь, что вы в корне проекта (должен быть файл `pom.xml`)
3. Выполните команду:
   ```bash
   mvn compile
   ```
4. Затем запустите программу через Java:
   ```bash
   java -cp "target/classes;target/dependency/*" org.example.Main -i test.conf
   ```
   
   **Для Windows PowerShell:**
   ```powershell
   java -cp "target/classes;target/dependency/*" org.example.Main -i test.conf
   ```

## Способ 3: Запуск тестов

1. Откройте класс `ConfigLangTest.java`
2. Нажмите правой кнопкой на имени класса или на конкретном тесте
3. Выберите **"Run 'ConfigLangTest'"** или **"Run 'название_теста'"**
4. Результаты появятся в окне Run внизу

Или через Maven:
```bash
mvn test
```

## Примеры запуска с разными файлами

### Пример 1: Сетевая конфигурация
**Program arguments:**
```
-i src/main/resources/examples/network.conf
```

### Пример 2: Игровая конфигурация
**Program arguments:**
```
-i src/main/resources/examples/game.conf
```

### Пример 3: Простой тест
**Program arguments:**
```
-i test.conf
```

## Проверка работы программы

После запуска в консоли должен появиться XML-вывод, например:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
  <const name="x" value="10"/>
  <constExpr result="11">
    <add>
      <constRef name="x"/>
      <number>1</number>
    </add>
  </constExpr>
  <dict>
    <entry name="key">
      <number>15</number>
    </entry>
  </dict>
</config>
```

## Обработка ошибок

Если в файле есть синтаксическая ошибка, программа выведет:
```
Syntax error: line X:Y <описание ошибки>
```

## Сборка JAR-файла

Для создания исполняемого JAR-файла:
```bash
mvn clean package
```

Затем запуск:
```bash
java -jar target/config-lang-cli-1.0-SNAPSHOT.jar -i test.conf
```

