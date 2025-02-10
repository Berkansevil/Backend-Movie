# Movie Project

Movie Project, film verilerini yöneten ve kullanıcıların filmler hakkında yorum yapmasına olanak tanıyan bir uygulamadır. Proje, JWT (JSON Web Token) ile güvenli kimlik doğrulaması sağlar, kullanıcı rollerini yönetir ve film incelemeleri eklenebilir. Ayrıca, hataların düzgün bir şekilde işlenmesi için özel exception'lar kullanılmıştır.

## Özellikler

- **Kimlik Doğrulama (JWT)**: Uygulama, kullanıcıların güvenli bir şekilde giriş yapabilmesi için JWT tabanlı kimlik doğrulaması kullanır.
- **Kullanıcı Rolleri**: Kullanıcılar farklı rollere sahip olabilir. Şu anki rolleri:
  - **User**: Film incelemelerini görüntüleme ve ekleme yetkisi.
- **Film İncelemeleri**: Kullanıcılar, izledikleri filmler hakkında yorumlar bırakabilir ve bu yorumları görüntüleyebilir.
- **Exception Handling**: Uygulama, oluşabilecek hataları düzgün bir şekilde yakalar ve anlamlı hata mesajları ile kullanıcıya geri döner.
- **Spring Boot**: Backend geliştirme için Spring Boot kullanılmıştır.
- **PostgreSQL**: Veritabanı yönetimi için PostgreSQL kullanılmıştır.
- **Spring Security**: JWT tabanlı güvenlik ve kullanıcı kimlik doğrulaması.
- **Maven/Gradle**: Bağımlılık yönetimi ve proje derleme.
- **Lombok**: Boilerplate kodları azaltmak için kullanıldı.

## Teknolojiler

- **Java**: Backend geliştirme için ana dil olarak kullanıldı.
- **Spring Boot**: RESTful API geliştirmek için kullanıldı.
- **JWT (JSON Web Token)**: Kullanıcı kimlik doğrulaması için.
- **PostgreSQL**: Veritabanı yönetimi.
- **Spring Security**: JWT tabanlı güvenlik ve kullanıcı kimlik doğrulaması.
- **Maven/Gradle**: Bağımlılık yönetimi ve proje derleme.
- **Lombok**: Boilerplate kodları azaltmak için kullanıldı.

## Proje Kurulumu

### Gereksinimler
- JDK 17 veya üzeri
- Maven veya Gradle
- PostgreSQL

### Adımlar

1. **Projeyi Klonla**
   ```bash
   git clone https://github.com/yourusername/movie-project.git
   cd movie-project
