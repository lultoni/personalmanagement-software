## Finalize these Changes Please:
- Alles was in den TODOs steht
- Zugriffsberechtigungen
  - Pro Feature kann man enablen ab welchem level es benutzt werden darf
  - levels (that still need to be implemented):
    - employee, view but no edits and such
    - manager, only has some functionality if emp to edit subordinate or something like that
    - admin, allowed everything

## Already Planned Ideas:


- Table Mitarbeiter Expanden
  - Team (bringt das neue rollen?)
  - Qualifikationen/Schulungen
  - (Urlaubstage Anspruch)
- Mitarbeiter im Programm...
  - Erstellen
  - Bearbeiten
  - Löschen
  - Manager Features, also auch hier admin rights und so
- Hierachiedarstellung
  - Baumstruktur, Zoom, Drag around
  - Jeder manager dort eine anzahl in wie viele unter ihm stehen, anklickbar zum öffnen
- Programm: Vorschlag von passenden Schulungen basierend auf den Vorhandenen
  - Pro Rolle gibt es Expectations/Requirements und Fortbildungsmöglichkeiten
  - Pro Mitarbeiter wird gematched was bereits vorhanden ist und was noch gemacht werden muss/kann
    - Hier ist ein Ranking ganz gut
    - Wichtigsten werden angezeigt (Compliance, Fälligkeit)
    - Am bestimmter unwichtigkeit werden nicht in die visual liste gepackt auch wenn im backend da
  - potentiell in welche teams man als nächstes gehen kann oder so
  - (basierend auf alter schulungen)

## Vielleicht Features wenn noch genug Zeit und Lust:

- Viele infos aus Emp Generation in eigene Datenbanken moven maybe
- Search Verbesserungen
  - General Search, any term in any employee field - "Volltextsuche innerhalb von Mitarbeiterdaten."
  - each specific search area has not a label besides it, but a greyed out text when no text is in the area
  - enter does a search
  - page view (top and bottom have change page options)
    - also a small "displaying x-y of z results
  - different emp icons based on role/department/gender or something
    - this can be based on
  - Employee_List_View_Single has max height
  - better filtering, not based on levenshtein or something, rather contains as well
    - Research which options are available right now
    - when given in a string, dynamically check if that is a name or if it is rather a part of it
      - (reg ex if latter or somerthing, idk)
  - sort options of search returned list
- better generation of in company structure
- Zeitmanagement / Urlaubsmanagement