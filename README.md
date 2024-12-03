# TODO

## Feedback

- Feedback von Activity Diagramm
- Feedback von Context Diagram übernehmen
- Context Diagram aufräumen

## UI

- on LoanSuccess html --> Add start_date and end_date instead of loan_date✅
- on LoanSuccess html --> rename header to reserve/loan success depending on method run✅
- Return Media Page --> completed transactions are not removed from view
- add returnMediaSuccess html page with details (return date, etc.) and fee calculations✅

## Database

- START_DATE --> incorrect assignment of start_date (currently uses transaction_date when reserving media for the future)✅
- END_DATE --> end date currently incorrectly calculated as START_DATE + 14 days✅
- LAST_POSSIBLE_RETURN_DATE --> set depending on customer type (student/ regular loan durations)✅
- TRANSACTIONS_MEDIA --> remove table (everything is in TRANSACTIONS table)

## Backend

- Wartelisten Logik fuer ReserveMedia
- Fix Autowires (@Autowire (Dependency Injection))
- If date(today) > ExpirationDate ---> Status = overdue + invoice anpassen✅
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
