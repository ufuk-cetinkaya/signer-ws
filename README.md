# signer-ws
e-Fatura ve e-Arşiv raporlarını imzalayan bir elektronik imza servisi

UBL (E-Fatura) ve E-Arşiv Raporu XML belgelerini dijital olarak imzalamak için geliştirilmiş, Java 1.8 tabanlı bir SOAP Web Servisidir.

Bu uygulama, imzalama süreçlerinde Türkiye'deki standart olan KamuSM MA3 kütüphanesini kullanır.

⚙️ Fonksiyonel Özellikler
Servis, stateless (durumsuz) bir yapıda çalışır ve iki ana metod sunar:

SignDocuments: İmzalanmamış UBL byte dizisini alır, KamuSM kütüphanesini kullanarak mali mühür/e-imza ile imzalar ve imzalı byte dizisini geri döner.

SignReports: E-Arşiv raporu formatındaki XML'ler için özelleşmiş imzalama metodudur.

🛠 Teknik Gereksinimler & Bağımlılıklar
Bu proje, lisanslama ve dosya boyutu kısıtlamaları nedeniyle tüm bağımlılıkları repo içerisinde barındırmaz.

Runtime: Java JRE/JDK 1.8

Kütüphane: KamuSM MA3 (Milli Açık Anahtar Altyapısı)

Önemli Not: Repoyu klonladıktan sonra KamuSM web sitesinden temin edeceğiniz .jar dosyalarını projenin lib/ dizinine manuel olarak eklemeniz gerekmektedir.

MA3 e-İmza Kütüphanesi:
https://yazilim.kamusm.gov.tr/sites/all/modules/pubdlcnt/pubdlcnt.php?fid=123

⚠️ Bilinen Kısıtlamalar ve Güvenlik Uyarıları (Disclaimers)
Uygulamanın mimari kararları ve zayıf yönleri aşağıda belirtilmiştir:

HSM Desteği: Mevcut sürümde HSM (Hardware Security Module) entegrasyonu bulunmamaktadır.

Sertifika Yönetimi: İmzalama işlemi yerel dosya sistemindeki bir .pfx dosyası üzerinden gerçekleştirilir.

Güvenlik Riskleri: Canlı ortamlarda imza sertifikası anahtarlarının dosya sisteminde tutulması güvenlik standartlarına aykırıdır. Bu uygulama mevcut haliyle test/geliştirme ortamları için uygundur. Production kullanımı için HSM entegrasyonu geliştirilmelidir.

Teknoloji Yığını: SOAP protokolü ve Java 1.8 seçimi, eski kurumsal sistemlerle uyumluluk (legacy compatibility) amacı taşımaktadır.

🚀 Kurulum
Projeyi klonlayın.

lib/ klasörünü oluşturun ve KamuSM JAR dosyalarını buraya kopyalayın.

config.properties (veya ilgili konfigürasyon) dosyasından .pfx yolunu ve şifresini tanımlayın.

Maven veya doğrudan JAR paketleme ile derleyip dağıtın.
