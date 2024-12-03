# TODO

## Gegeben

- Simple books (These are normal books like novels etc.)✅
- Specialized books (These are books which are specialized to a particular subject.)
- Music CD's (Theses a normal Music CD's)✅
- Movies (These are normal movies without any age restriction.)✅
- Movies for adults (Theses are movies with special content (violent or erotic) which has an age restriction.)✅
- Customers (We divide between different type of customers.)✅
- Adult customers (These are regular customers which are allowed to loan all kinds of medias.)
- Children/youth customers (These are customers between 10 and 18 years, which are not allowed to loan movies for adults.)
- Students (Students are allowed to lean specialized books form a longer period (6 weeks).)✅
- customer max. loan time (4 weeks)✅
- Customers should be able to loan medias✅
- Customers should be able to return medias✅
- Customers should be able to reserve medias.✅
- Customers should be able to cancel reserved medias.
- Customers should be possible to ask for the availability of a specific media
- Customers should be possible to ask for the physical position of a specific media
- If a customer has exceed the max. loan time (4 weeks), the customer has to pay a penalty fee for every additional day

## Feedback

- Feedback von Activity Diagramm
- Feedback von Context Diagram übernehmen
- Context Diagram aufräumen

## UI

- on LoanSuccess html --> Add start_date and end_date instead of loan_date✅
- on LoanSuccess html --> rename header to reserve/loan success depending on method run✅
- Return Media Page --> completed transactions are not removed from view
- add returnMediaSuccess html page with details (return date✅, etc.) and fee calculations❌

## Database

- START_DATE --> incorrect assignment of start_date (currently uses transaction_date when reserving media for the future)✅
- END_DATE --> end date currently incorrectly calculated as START_DATE + 14 days✅
- LAST_POSSIBLE_RETURN_DATE --> set depending on customer type (student/ regular loan durations)✅
- TRANSACTIONS_MEDIA --> remove table (everything is in TRANSACTIONS table)

## Backend

- Wartelisten Logik fuer ReserveMedia
- Fix Autowires (@Autowire (Dependency Injection))
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
