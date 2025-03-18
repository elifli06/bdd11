# DataTables Editor Test Projesi

Bu proje, DataTables Editor web sayfasında test otomasyonu gerçekleştiren bir Selenium WebDriver projesidir.

## Gereksinimler

- Java JDK 19 veya üzeri
- Maven 3.8.x veya üzeri
- Chrome tarayıcı (en son sürüm)

## Kurulum

1. Projeyi bilgisayarınıza klonlayın:
```bash
git clone <proje-url>
```

2. Proje dizinine gidin:
```bash
cd proje-dizini
```

3. Maven bağımlılıklarını yükleyin:
```bash
mvn clean install
```

## Testleri Çalıştırma

Tüm testleri çalıştırmak için:
```bash
mvn clean test
```

## Proje Yapısı

- `src/test/java/stepdefinitions/`: Test adımlarının tanımlandığı sınıflar
- `src/test/java/runners/`: Cucumber test runner sınıfı
- `src/test/resources/features/`: Cucumber feature dosyaları
- `target/cucumber-reports/`: Test raporları

## Kullanılan Teknolojiler

- Selenium WebDriver 4.16.1
- Cucumber 7.14.0
- JUnit 5.10.1
- WebDriverManager 5.6.3
- Maven
