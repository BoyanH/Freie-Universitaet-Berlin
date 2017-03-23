class VerketteteListe {
    
	Knoten eingang;
	
	//damit wir auch leeren Konstruktor erlauben
	public VerketteteListe() {}
	
	//manchmal ist es leichter, direkt den Wurzel zu übergeben
	public VerketteteListe(int i) {
		
		Knoten a = new Knoten(i);
		a.next = a;
		
		this.eingang = a;
	}
	
    public Knoten einfuegeAmEnde(int i) {
    	
    	//damit wir die Methode leichter ohne Parameterübergabe zu dem Konstruktor
    	//nutzen können, behandeln wir hier die Situation, in der es noch kein
    	//Wurzel gibt
    	
    	if(this.eingang == null) {
    		
    		//Herstellung des neuen Knotens
    		Knoten a = new Knoten(i);
    		
    		//setze das als Wurzel
    		this.eingang = a;
    		
    		//hängt von der Implementierung ab
    		//this.eingang.next = this.eingang;
    		
    		
    		//gib es zurück
    		return this.eingang;
    	}
    	
    	//sonst, beginne von Anfang an
    	Knoten last = this.eingang;
    	
    	
    	//und suche die Ende der Liste
    	while(last.next != null) {
    		
    		last = last.next;
    	}
    	
    	//definiere den neue Knoten
        Knoten a = new Knoten(i);
        
        //a.next = this.eingang;
        
        //setze es als letzten, verbinde zur Liste
        last.next = a;
        
        return a;
    }
    
	public void einfuege(int i) {
		
		Knoten a = new Knoten(i);
        a.next = eingang;
        eingang = a;
	}
    
	//es ist schon den Knoten zurückzugeben, damit wir später genau nach dieser
	//neue Knoten einfügen können
    public Knoten einfuege(int i, Knoten k) {
    	
    	//neuen Knoten herstellen
    	Knoten a = new Knoten(i);
    	//speichere im temp zu welchem Knoten k zeigt
    	//damit wir die Verbindung wiederherstellen können
    	Knoten temp = k.next;
    	
    	//k zeigt jetzt zu k
    	k.next = a;
    	
    	//und k zu das, was vorher k zeigte, so bleibt die Liste
    	//verkettet
    	a.next = temp;
    	
    	return a;
    }

    public void durchlaufe() {
        
    	//Laufe alle Knoten durch und drücke die aus
    	
    	Knoten a = eingang;
    	System.out.print("[");
        while (a != null) { //wenn wir eine Kreisliste definieren wollen muss hier
        					//this.eingang anstatt von null stehen
        	
        	String separator = a.next == null ? "" : ", ";
        	
            System.out.print(a.wert + separator);
            a = a.next;
        }
        System.out.println("]");
    }
    
    public boolean loesche() {
    
    	//falls eingang undefiniert ist, dann ist die Liste leer
    	if(this.eingang == null) {
    		
    		return false;
    	}
    	
    	//sonst nimm das 2 Element (Index 1) für Würzel
    	this.eingang = this.eingang.next;
    	return true;
    	
    }
    
    public boolean loesche (Knoten k) {
    	
    	//fange von Anfang an
    	Knoten crnt = this.eingang;
    	
    	//merke dich immer das vorige Element
    	Knoten prev = null; 
    	
    	//brich ab, wenn die Liste zu Ende ist (hoffentlich früher)
    	while(crnt != null) {
    		
    		//yey, gefunden
    		if(crnt == k) {
    			
    			//nutze unser Methode unten
    			this.loescheGleich(prev, crnt);
    			
    			//ja, habe es gefunden, return true
    			return true;
    		}
    		
    		prev = crnt;
    		crnt = crnt.next;
    	}
    	
    	//wenn aber brich am Ende der Liste statt gefunden hat, ohne das Element
    	//zu finden:
    	
    	return false;
    }
    
    //private funktion für mehr Verständniss
    public void loescheGleich (Knoten previous, Knoten current) {
    	
    	//gegeben sind den Knoten, den wir löschen und dieser davor
    	
    	//nächsten bekomme
    	Knoten next = current.next;
    	
    	//wen das gelöschte NICHT unser Wurzel ist
    	if(previous != null) {
    		
    		//verbinde die Liste ohne current
    		previous.next = next;
    	}
    	else {
    		
    		//kein vorigen Element, das war den Würzel, setze ein neuen Würzel
    		
    		this.eingang = next;
    	}
    	
    	
    }
       
}