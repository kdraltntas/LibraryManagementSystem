# High-Performance Library Management System (Custom DSA) 📚

**Bu proje, standart Java kütüphaneleri (`java.util.*`) kullanılmadan, tamamen sıfırdan geliştirilmiş özel veri yapıları (Custom Data Structures) üzerine inşa edilmiş bir kütüphane yönetim sistemidir.**

Projenin temel amacı; bir yazılımın arka planında çalışan **bellek yönetimi, algoritmik karmaşıklık (Big O Notation) ve veri organizasyonu** prensiplerini derinlemesine uygulamaktır.

---

## 🚀 Teknik Mimari ve Veri Yapıları (Architecture)

Sistemde her bir fonksiyonel gereksinim için en optimize veri yapısı seçilmiş ve implemente edilmiştir:

| Özellik (Feature) | Kullanılan Veri Yapısı (Data Structure) | Neden Seçildi? (Engineering Decision) |
| :--- | :--- | :--- |
| **Kitap Arama** | **AVL Tree (Self-Balancing BST)** | Arama işleminde `O(log n)` garantisi sağlamak ve ağacın dengesizleşmesini (linked list'e dönüşmesini) engellemek için. |
| **Üye/Kitap ID Erişimi** | **Custom Hash Map** | `O(1)` erişim süresi için. (Collision handling için *Chaining* yöntemi kullanıldı). |
| **Popüler Kitaplar** | **Max Heap (Priority Queue)** | En çok okunan kitapları `O(1)` sürede zirvede tutmak ve sıralama maliyetinden kaçınmak için. |
| **Bekleme Listesi** | **Queue (FIFO)** | Kitap sırası bekleyen üyeler için "İlk Gelen İlk Alır" (First-Come-First-Served) mantığını uygulamak için. |
| **Geri Al (Undo)** | **Stack (LIFO)** | Son yapılan işlemi hafızada tutup geri alabilmek için. |
| **Ödünç Listesi** | **Linked List** | Dinamik ekleme/çıkarma işlemlerinin sık yapıldığı üye kitap listeleri için Array yerine tercih edildi. |

---

## 🛠 Algoritmik Detaylar (Implementation Details)

### 1. AVL Tree & Rotations
Sıralı veri girişinde performans kaybını önlemek için **AVL Ağacı** kullanılmıştır. Denge faktörü (Balance Factor) bozulduğunda sistem otomatik olarak rotasyon yapar:
* **Right Rotation & Left Rotation:** Ağaç yüksekliğini dengede tutarak arama hızını optimize eder.

### 2. Custom Hashing Strategy
Java'nın hazır `HashMap` sınıfı yerine, Generic yapıda kendi Hash tablom geliştirilmiştir.
* **Unique Salting:** Hash çakışmalarını ve dağılımı test etmek amacıyla öğrenci numarası (`240315011L`) salt değeri olarak entegre edilmiştir.

### 3. Memory Management
* `Node<T>` sınıfı generic yapıdadır ve tüm dinamik veri yapılarının temel taşıdır.
* Pointer manipülasyonları manuel olarak yönetilmiştir (Örn: Linked List `next` referansları).

---

## 💻 Kurulum ve Çalıştırma (Installation)

Projeyi çalıştırmak için JDK kurulu olması yeterlidir. Herhangi bir harici kütüphane (Maven/Gradle) gerektirmez.

```bash
# Repoyu klonlayın
git clone [https://github.com/kullaniciadin/Custom-Library-System.git](https://github.com/kullaniciadin/Custom-Library-System.git)

# Dizin değiştirin
cd Custom-Library-System

# Derleyin (Tüm java dosyaları aynı dizindeyse)
javac *.java

# Çalıştırın
java LibrarySystem
```
---

## 📊 Örnek Senaryo ve Çıktılar

### Konsol Menüsü:
```bash
=== LIBRARY SYSTEM PROJECT ===
Student ID Integration: 240315011

--- MENU ---
1. Add a Book
2. Register a Member
3. Borrow a Book
4. Return a Book
5. Search Book (Title - AVL Tree)
6. See Most Popular Books (Max Heap)
7. Undo Last Action (Stack)
0. Exit
```

### Popülarite Analizi (Heap Çıktısı):

```bash
--- MOST POPULAR BOOKS ---
1. Dune (Total Borrows: 5)
2. 1984 (Total Borrows: 3)
3. Nutuk (Total Borrows: 2)
```

# 👨‍💻 Geliştirici Notu
Bu proje, bir bilgisayar mühendisinin "siyah kutu" (black-box) kütüphaneler kullanmak yerine, sistemin çekirdeğini nasıl inşa edebileceğini göstermektedir.

Geliştirici: Recep Kadir Altıntaş - Computer Engineering Student