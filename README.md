# Auction-App
* [General info](#general-info)
* [Technologies](#technologies)
* [Properties](#properties)
* [Setup](#setup)
* [TODO](#todo)
* [Web Schema](#web-schema)
* [Authors](#authors)

## General info
Serwis Aukcyjny zapewniający kupowanie, sprzedaż oraz licytację produktów. Każdy może kupuić produkt lecz tylko zalogowanie użytkownicy mogą brać udział w licytacjach oraz dodawać nowe produkty.
Dodatkowo konto zawiera weryfikację po przez wysyłanie emial z aktywacją konta, zmianę danych czy też hasło oraz zakup konta premium lub przywrócennie bezpłatnego konta.

## Technologies
Projetk Jest realizowany za pomocą:
### Back-End
* Java
* Spring-Boot
* Spring-Security
* Spring-Data
* Spring-Web
* Hibernate
* Thymeleaf
#### Testy
* JUnit
* Mockito
### Baza Danych
* MySql
### Front-End
* JS
* HTML
* CSS
* Bootstrap

## Properties
* Logowanie użytkownika i potwierdzenie jego konta mailem
* Hasło użytkownika jest szyfrowane
* Użytkownik jest zapisywany w bazie danych z podziałem konta na ADMIN/USER oraz ACTIV/INACTIV/BLOCKED
* Pliki cookies są obsługiwane
* Podstrony są zablokowane dla niezalogowanych ze statusem 403
* Stworzenie Użytkownika, dodanie produktu/aukcji jest syganlizowane statusem 201
* Nie znaleźienie Produktu/Aukcji jest sygnalizowane 404
* Próba edycji lub usunięcia są sygnalizowane 409
* Puste listy produktów/aukcji zwracają 204


## Setup

Uruchomienie Projektu jest możliwe: 
Po wystartowaniu serwera z Back-End (należy tylko ściągnąć kod i uruchomić)
A następnie Kliknąć ikonę aplikacji Front-End.

## TODO
* Przetestowanie pozostałych klas,
* Dodanie śledzenia tokenu (JWT, OAUTH2)
* Dodanie Koszyku zamówień 
* Obserwacja produktów/aukcji
* Ocena produktów/aukji

## Web Schema
wypełnia Jakub Moszyński

## Authors
* Kamil Bąk - Back-End
* Jakub Moszyński - Front-End

