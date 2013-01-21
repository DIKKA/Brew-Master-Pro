CREATE TABLE barrel (id INTEGER PRIMARY KEY AUTOINCREMENT,serial_no TEXT,volume INTEGER,weight INTEGER,type_id INTEGER,manufacturer TEXT,build_year INTEGER,FOREIGN KEY (type_id) REFERENCES barrel_type (id));
CREATE TABLE barrel_type (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT);
CREATE TABLE event (id INTEGER PRIMARY KEY AUTOINCREMENT,event_type_id INTEGER,name TEXT,description TEXT,start INTEGER,end INTEGER);
CREATE TABLE event_barrel (event_id INTEGER,barrel_id INTEGER,FOREIGN KEY (event_id) REFERENCES event (id),FOREIGN KEY (barrel_id) REFERENCES barrel (id),PRIMARY KEY (event_id, barrel_id));
CREATE TABLE ingredient (id INTEGER PRIMARY KEY AUTOINCREMENT,journal_id INTEGER,name TEXT,scale_unit_id INTEGER,quantity INTEGER,remarks TEXT,FOREIGN KEY (journal_id) REFERENCES journal (id),FOREIGN KEY (scale_unit_id) REFERENCES scale_unit (id));
CREATE TABLE journal (id INTEGER PRIMARY KEY AUTOINCREMENT, event_id INTEGER, description TEXT, attendees TEXT, expected_color TEXT,expected_bitterness TEXT,expected_misc TEXT,received_color TEXT,received_bitterness TEXT,received_misc TEXT, FOREIGN KEY (event_id) REFERENCES event (id));
CREATE TABLE journal_event (journal_id INTEGER,event_id INTEGER,FOREIGN KEY (journal_id) REFERENCES journal (id),FOREIGN KEY (event_id) REFERENCES event (id),PRIMARY KEY (journal_id, event_id));
CREATE TABLE scale_unit (id INTEGER PRIMARY KEY AUTOINCREMENT,short_name TEXT UNIQUE, name TEXT);
CREATE TABLE task (id INTEGER PRIMARY KEY AUTOINCREMENT,journal_id INTEGER,name TEXT, start INTEGER,end INTEGER,degree INTEGER, remarks TEXT, FOREIGN KEY (journal_id) REFERENCES journal (id));
