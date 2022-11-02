# PG209 Backend programmering eksamen

## Sjekkliste for innleveringen

* [ ] Dere har lest eksamensteksten
* [ ] Koden er sjekket inn på github.com/pg209-2022 repository
* [ ] Dere har lastet opp en ZIP-fil lastet ned fra Github
* [ ] Dere har committed kode med begge prosjektdeltagernes GitHub-konto (alternativt: README beskriver hvordan dere har jobbet)

## README.md

* [ ] Inneholder link til Azure Websites deployment
* [ ] Inneholder en korrekt badge til GitHub Actions
* [ ] Beskriver hva dere har løst utover minimum
* [ ] Inneholder et diagram over databasemodellen

## Koden

* [ ] Oppfyller Java kodestandard med hensyn til indentering og navngiving
* [ ] Er deployet korrekt til Azure Websites
* [ ] Inneholder tester av HTTP og database-logikk
* [ ] Bruker Flyway DB for å sette opp databasen
* [ ] Skriver ut nyttige logmeldinger

## Basisfunksjonalitet

* [ ] Kan velge hvilken bruker vi skal opptre som
* [ ] Viser eksisterende meldinger til brukeren
* [ ] Lar brukeren opprette en ny melding
* [ ] Lar brukeren svare på meldinger
* [ ] For A: Kan endre navn og annen informasjon om bruker
* [ ] For A: Meldingslisten viser navnet på avsender og mottakere

## Kvalitet

* [ ] Datamodellen er *normalisert* - dvs at for eksempel navnet på en meldingsavsender ligger i brukertallen, ikke i meldingstabellen
* [ ] Når man henter informasjon fra flere tabellen brukes join, i stedet for 1-plus-N queries (et for hovedlisten og et per svar for tilleggsinformasjon)
* [ ] Det finnes test for alle JAX-RS endpoints og alle DAO-er


## Plan
---- Konvertering og grunnmur på plass ----
* [x] Klone kode fra arbeidskrav som startingPoint
* [x] Modifisere koden slik at den bruker User
* [x] Lage startingPoint for DB i flyway migration fil
  * [ ] (EXTRA) lage test for å teste @manytoone relasjoner mellom Message og User
* [ ] Lage UserEndPoint 
 * [ ] lage tester for UserEndPoint.
  * [ ] trenger vi mer en GET test? 
 * [ ] lage ny UserEndPoint klasse 
 * [ ] Lage @GET og POST
 * [ ] @Inject userDao 
  * [ ] Kan vi bruke samme config på flere endPoints ? Binde flere classer etter hverandre i ChatRoomConfig.
* [ ] (EXTRA) abstrakt dao test ??
* [ ] Slette alle referanser til Item i koden.
* [ ] Endre ServeFrontPage test og react til å passe nytt prosject. (BASIC).
* [ ] lage DAO og test for Message
 * [ ] 
* [ ] lage DAO og test for Thread
 * [ ]  
* [ ] lage DAO og test for ThreadMembers
 * [ ]
---- Business Logic ----