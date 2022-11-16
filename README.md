[![Java CI with Maven](https://github.com/kristiania-pgr209-2022/pg209exam-Fargekritt/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr209-2022/pg209exam-Fargekritt/actions/workflows/maven.yml)
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


## Funksjonelle Krav
1.
   * [x] Når man kommer til applikasjonen kan man velge hvilken bruker man skal representere fra et sett med brukere som derehar opprettet på forhånd i databasen
      * [x] For full uttelling skal man kunne endre informasjon om en eksisterende bruker
      * [x] For full uttelling må brukeren ha felter utover navn og emailadresse
2.
   * [x] Når man har valgt en bruker skal man kunne se alle meldingstråder der denne brukeren er sender eller mottaker (krav til E)
     * [x] For full uttelling skal oversikten over meldingertråder inneholde navnet på alle mottakere for meldinger itråden
     * [ ] For full uttelling bør dere forhåndspopulere med noen meldingstråder
3.
   * [x] Når man har valgt en bruker kan man opprette en ny meldingstråd med mottaker. Meldingen skal kunne inneholde tittelog meldingstekst (krav til E)
     * [x] For full uttelling må det være mulig å opprette en meldingstråd til med flere mottakere
     * [x] For full uttelling må det meldingen inneholde flere felter enn tittel og meldingstekst
4.
   * [x] Når en bruker velger en meldingstråd skal de se alle meldinger i tråden
     * [x] For full uttelling må backend gjøre en join mellom melding- og brukertabellen for å vise avsenders navn
5.
   * [x] Når en bruker velger en meldingstråd skal det være mulig å svare på meldingstråden
     * [x] For full uttelling må svaret inneholde flere felter enn meldingstekst
     * [ ] For ekstra poeng: Registrer når en bruker har lest en melding og vis dette tidspunktet til andre brukere som kan semeldingen

## Plan
---- Konvertering og grunnmur på plass ----
* [x] Klone kode fra arbeidskrav som startingPoint
* [x] Modifisere koden slik at den bruker User
* [x] Lage startingPoint for DB i flyway migration fil
  * [ ] (EXTRA) lage test for å teste @manytoone relasjoner mellom Message og User
* [x] Lage UserEndPoint 
 * [x] lage tester for UserEndPoint.
  * [x] trenger vi mer en GET test? Ja, vi gjorde det 
 * [x] lage ny UserEndPoint klasse 
 * [x] Lage @GET og POST
 * [x] @Inject userDao 
  * [x] Kan vi bruke samme config på flere endPoints? Binde flere classer etter hverandre i ChatRoomConfig? Ja, det kunne vi! (*￣3￣)╭
* [ ] (EXTRA) abstrakt dao test ??
* [x] Slette alle referanser til Item i koden.
* [x] Endre ServeFrontPage test og react til å passe nytt prosject. (BASIC).
* [x] lage DAO og test for Message
* [x] lage DAO og test for Thread 
* [x] lage DAO og test for ThreadMembers

---- Business Logic ----

* [ ] Lage Abstrakt DAO test
* [ ] Extra :
  * Email på bruker
  * Tittel på melding i tråd 
  * vise tråd tittel på tråd