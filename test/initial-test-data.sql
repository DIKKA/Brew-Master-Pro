INSERT INTO barrel VALUES(1, 'ax-z14-v', 400, 200, 4, 'Fassbinder AG', 1934);
INSERT INTO barrel VALUES(2, '4231-zz', 10, 15, 2, 'Fässler & Co', 2012);
INSERT INTO barrel VALUES(3, '9932-zy', 70, 30, 1, 'Fassdaubi', 1347);

INSERT INTO barrel_type VALUES(1, 'Plastikfass');
INSERT INTO barrel_type VALUES(2, 'Holzfass (Eiche)');
INSERT INTO barrel_type VALUES(3, 'Super-Druckfass');
INSERT INTO barrel_type VALUES(4, 'Gummifass');

INSERT INTO event VALUES(1, 3, 'GV', 'Bitte Autoschlüssel abgeben!', 1356103810000, 1356130800000);
INSERT INTO event VALUES(55, 4, 'Degustationstag', 'Da müssen wir durch',  1357999200000, 1358024400000);

INSERT INTO journal_event VALUES(1, 1);
INSERT INTO journal_event VALUES(2, 2);
INSERT INTO journal_event VALUES(3, 1);
INSERT INTO journal_event VALUES(4, 2);

INSERT INTO event_barrel VALUES(1, 2);
INSERT INTO event_barrel VALUES(3, 1);

INSERT INTO ingredient VALUES(1, 1, 'Hopfen', 1, 120, 'Guter Stoff');
INSERT INTO ingredient VALUES(2, 2, 'Malz', 1, 300, NULL);
INSERT INTO ingredient VALUES(3, 3, 'Wasser', 2, 200, NULL);

INSERT INTO journal VALUES(1, 1,'Unser erster Brautag','Thomas, Chrigi, Dave, Beni, Adi','Gelb',NULL,NULL,NULL,NULL,NULL);
INSERT INTO journal VALUES(2, 2, 'Ein weiterer Brautag','Thomas, Adi, Chrigi, Beni, Dave','weiss','Grapefruit','Mindestens 9% Alkohol erwartet','Grün','Pfütze','40% Alkohol. Irgendwas lief schief');
INSERT INTO journal VALUES(3, 1, 'Brautag 1 vom Startrek-Bier', 'Worff, Picard, Data', 'sternanisbräunlichgelbgrün', 'Syntherol', 'Sollte modern schmecken', 'eher gräulich', 'Typischer Syntherol-Geruch => Erfolg!', '0% Alkohol, wie erwartet, tauglich für die Sternenflotte');
INSERT INTO journal VALUES(4, 2, 'Brautag 2 Vollmondbier', 'Hubertus, Wolfgang, Fuchsia', 'braun', 'voll, abgerundet', NULL, 'blass', 'Es haut einem die Reisszähne aus der Fresse', '99% Alkohol, go to hell and jump back');
INSERT INTO journal VALUES(5, NULL, 'Probiertag 1 Kloakenbräu', 'Lady Gala, Albert E.', 'gelblich', 'trinkbar','urks!','undefinierbar', 'Was zum Geier war das für eine Flüssigkeit?!', 'absoluter Horror');

INSERT INTO scale_unit VALUES(1,'kg','Kilogramm');
INSERT INTO scale_unit VALUES(2,'l','Liter');
INSERT INTO scale_unit VALUES(3,'mg','Milligramm');
INSERT INTO scale_unit VALUES(4,'ml','Milliliter');

INSERT INTO task VALUES(NULL, 1,'Wasser aufkochen',1353961291000,1353962500000,0,'Darauf achten, H2O zu verwenden!');
INSERT INTO task VALUES(NULL, 2, 'Phaser aufladen',1353735000000,1353781800000,90,'Das Bier soll das „spezielle Etwas“ erhalten');

