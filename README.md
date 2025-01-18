# TODO

## Feedback Danninger (Code)

- ✅Optimal wäre, wenn die Preise auf Ebene `MedienTyp` und `Genre` definiert würden; vermutlich kostet eine Blu-ray mehr als eine Zeitschrift.
- Section und Shelf wären DB-seitig besser als identifizierende Beziehung umgesetzt (zusammengesetzter PK --> `@EmbeddedId`). Sollte am Ende noch Zeit bleiben.
- ✅`MediaTransaction` unklar: Es verweist auf viele Medien, aber nur auf ein Exemplar!? Wird das Medium hier überhaupt benötigt, wenn ohnehin nur ein Exemplar ausgeliehen werden kann?
- Ich wäre davon ausgegangen, dass Sie unter `ReserveMediaTransaction` die Reservierungen haben – kann aber nicht sein, da hier ein Exemplar referenziert wird. Irgendetwas passt hier nicht zusammen.
- Prüfen Sie bei den Unit-Tests, ob auch die Assoziationen korrekt sind (z.B. ob Sie vom Exemplar zum Medium kommen).
- Lt. Komponentendiagramm werden die Exemplare vom `MediaManagement` verwaltet, im Klassendiagramm und in der Umsetzung gibt es jetzt aber auch einen `EditionService`. Ich würde davon ausgehen, dass ein sehr enger Zusammenhang zwischen Medium und Exemplar existiert und diese daher gemeinsam von einem Service verwaltet werden sollten.
- ✅Verantwortlichkeiten sind teilweise nicht korrekt, da Services direkt die Repositories anderer Services nutzen (sie müssten über die Service-Schnittstelle gehen). Beispiel: `LibraryService` nutzt `mediaRepository`, `editionRepository` und `mediaTransactionRepository`.
- ✅Das Thema Ausleihdauer haben wir im Labor besprochen.
- ✅Bei der Ausleihe scheint nicht berücksichtigt zu sein, dass das Medium eventuell von jemand anderem bereits reserviert sein könnte.
- ✅`FSK`-Prüfung fehlt.
- Mehrbenutzerbetrieb müsste noch adressiert werden.
- ✅`MediaTransactionService.returnMedia`: Edition sollte nicht erneut gespeichert werden müssen, da das ORM bei Objekten, die bereits gemanaged sind, automatisch Änderungen verfolgt (gilt auch für `Transaction`).
- ✅Kann es beim Return auch passieren, dass der entsprechende Betrag nicht vom Benutzerkonto abgezogen werden kann?
- `ReserveMediaTransaction.reserveMediaForCustomer`: Medium als `String` übergeben ist nicht optimal; es würde auch gar nicht darauf reagiert werden, falls kein Medium gefunden werden kann. Die Textausgabe bei `editions.isEmpty` scheint mir nicht korrekt.
- Reserviert wird am Ende doch ein Exemplar und kein Medium? Was, wenn das Medium bereits von jemand anderem reserviert ist? Wird aktuell nicht berücksichtigt. Ebenfalls fehlen Mehrbenutzerbetrieb und `FSK`-Prüfung.
- ✅Unit-Test Rückgabe: Hier wäre es wichtig zu testen, dass bei einer verspäteten Rückgabe entsprechend die `Invoice` erstellt wird.
- `UnitTest ReserveMediaTransactionServiceTest`: `reserveMediaForCustomer` schlägt fehl.
- Unit-Tests dürfen keine Benutzereingaben abfragen; sie müssen automatisch und ohne Benutzer laufen.
- UI-Tests der Rückgabe laufen aktuell noch auf Fehler; UI-Tests der anderen Use-Cases fehlen noch.

- ✅Immer noch unklar, warum `MediaTransaction` eine Liste von `Media` und eine Assoziation zu `Edition` aufweist. Es scheint, als würden Sie die Liste der Medien im Programm nicht nutzen.
- ✅`Shelf` hat `id` und `number`?
- Da aktuell `ReserveMediaTransaction` ebenfalls eine Edition referenziert, wäre es vermutlich einfacher gewesen, diese mit `MediaTransaction` zu verschmelzen.
- Abbildung der Preise nur auf Genre-Ebene ist nicht optimal (siehe Vorfeedback).
- `testUserCRUDOperations` testet zu viel. Würde in der Praxis dafür drei getrennte Unit-Tests erstellen. Dieser Unit-Test schlägt aktuell fehl – unklar, warum ein Benutzer, der schon in der DB ist, nochmals mit `addUser` hinzugefügt wird.
- Warum hat die Edition einen `mediaName`? Edition ist einem Medium zugeordnet, und das Medium hat bereits einen Namen.
- Services sind sehr kleinteilig und die Zuständigkeiten unklar: Hätte Editionen vom `MediaService` verwalten lassen; aktuell nutzen sowohl der `MediaService` als auch der `EditionService` das Repository der Editionen. Wer ist zuständig? (es kann nur einen geben). Außerdem beinhaltet der `LibraryService` Repositories, die eigentlich zu anderen Services gehören (das Problem zieht sich generell durch).
- Theoretisch könnten alle Medientypen in einem Repository verwaltet werden (muss aber nicht geändert werden).
- Wenn ein Objekt aus der DB geladen wurde und etwas verändert wird, muss die `repo.save`-Methode nicht mehr aufgerufen werden, da das ORM die Änderungen automatisch nachzieht. Wann die SQL-Statements tatsächlich ausgeführt werden, könnte unter anderem über `@Transactional` gesteuert werden (z.B. `createLoanRecord`).
- `MediaTransactionService.createLoanRecord` scheint nicht verwendet zu werden; toter Code erschwert die Lesbarkeit.
- `loanMedia`: `FSK`-Prüfung fehlt. Methode nicht für den Mehrbenutzerbetrieb abgesichert. Die Verfügbarkeit über einen Status auf der Edition abzubilden ist unzureichend. Es kann aktuell ein Exemplar ausgeliehen werden, obwohl es eigentlich für morgen reserviert ist. Eine Ausleihe sollte auch auf Basis einer Reservierung stattfinden können. Es wäre sinnvoll, in `loanMedia` ein optionales Argument wie eine "Reservierungsnummer" hinzuzufügen (falls das nicht schon woanders berücksichtigt wurde).
- ✅`returnMedia`: Mehrbenutzerbetrieb nicht adressiert (insbesondere Logik hinsichtlich Credit); Gebühren sind aktuell hardcoded mit € 1.
- `ReserveMedia` berücksichtigt nicht, ob das Medium in diesem Zeitraum gerade ausgeliehen ist (und `loanMedia` berücksichtigt nicht, ob es reserviert ist). Keine `FSK`-Prüfung bei der Reservierung. Mehrbenutzerbetrieb noch nicht adressiert.
- `MediaTransactionServiceTest` läuft auf Fehler. Service-Tests für Ausleihe und Reservierung fehlen.
- Infos, wie das UI getestet werden kann, fehlen (User und Passwort).
- Zwei Controller verwirrend: `TemplateController` hat `/loan`, und `MediaController` hat `/loanReserveMedia`.
- ✅Code-Duplizierung durch die Methoden `showLoanPage` und `searchMedia`. Da danach dieselbe Seite angezeigt wird, könnte die Logik in einer Methode zusammengeführt werden.
- Aktuell unterscheidet der Controller abhängig vom Datum, ob eine Reservierung oder Ausleihe erstellt wird. Besser wäre, diese Logik in den Services zu haben, da ansonsten jemand `createLoan` einfach mit einem zukünftigen Datum aufrufen könnte.
- `LoanMediaControllerTests` laufen nicht.
- `ReturnMediaControllerTests` laufen auf Fehler.
- Services überarbeiten. Sicherstellen, dass ein Repository nur in einem Service verwendet wird. Mehrbenutzerbetrieb adressieren und Logik für Ausleihe vs. Reservierung konsolidieren.

## Feedback

- Feedback von Activity Diagram (khanh)
- Feedback von Context Diagram übernehmen + aufraeumen (khanh)
- use case diagram kontrollieren obs passt & evtl fixen (tommy)
- sequence diagram (tommy)
- component diagram kontrollieren (mario)
- ✅class diagram fix (mario)

## Gegeben

- ✅Simple books (These are normal books like novels etc.)
- ❓Specialized books (These are books which are specialized to a particular subject.)
- ✅Music CD's (Theses a normal Music CD's)
- ✅Movies (These are normal movies without any age restriction.)
- ✅Movies for adults (Theses are movies with special content (violent or erotic) which has an age restriction.)
- ✅Customers (We divide between different type of customers.)
- ✅Adult customers (These are regular customers which are allowed to loan all kinds of medias.)
- ✅Children/youth customers (These are customers between 10 and 18 years, which are not allowed to loan movies for adults.)
- ✅Students (Students are allowed to lean specialized books form a longer period (6 weeks).)
- ✅customer max. loan time (4 weeks)
- ✅Customers should be able to loan medias
- ✅Customers should be able to return medias
- ✅Customers should be able to reserve medias.
- ✅Customers should be able to cancel reserved medias.
- Customers should be possible to ask for the availability of a specific media
- ✅Customers should be possible to ask for the physical position of a specific media
- If a customer has exceed the max. loan time (4 weeks), the customer has to pay a penalty fee for every additional day

## UI

- ✅on LoanSuccess html --> Add start_date and end_date instead of loan_date
- ✅on LoanSuccess html --> rename header to reserve/loan success depending on method run
- ✅Return Media Page --> completed transactions are not removed from view
- ✅add returnMediaSuccess html page with details with return date, fee calculation
- ✅add sections and shelf to successLoan page
- ✅add "must login first" error when accessing return media before logging in
- ✅returnMediaSuccess page not showing genre and MediaType

## Database

- ✅START_DATE --> incorrect assignment of start_date (currently uses transaction_date when reserving media for the future)
- ✅END_DATE --> end date currently incorrectly calculated as START_DATE + 14 days
- ✅LAST_POSSIBLE_RETURN_DATE --> set depending on customer type (student/ regular loan durations)
- ✅TRANSACTIONS_MEDIA --> remove table (everything is in TRANSACTIONS table)

## Backend

- Wartelisten Logik fuer ReserveMedia
- ✅Fix Autowires (@Autowire (Dependency Injection))
- If date(today) > ExpirationDate ---> Status = overdue + invoice anpassen
- Mahnungen/Erinnerungen jede Woche --> also nur wenn es länger als 7 Tage ausgeliehen wird
- Add synchronization to loanMedia and reserveMedia
- check loanLimits (loanAmount & max loanPeriod)

## Unit Tests

- Create at least one unit test for each selected use-case --> remove use cases from clsr
- Isolate the unit tests by mocking
- Create unit test for UI
- unit test for LAST_POSSIBLE_RETURN_DATE --> (student/ regular loan durations)
- FSK check in UI
- exceeding loanLimit
- loan media marked as unavailable test
- due date calculation test (late fee)
- synchronization test
- fix MehrbenutzerbetriebTest (reserveMediaTransactionRepository.save() issue)
