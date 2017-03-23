public class VerketteteSchlange extends VerketteteListe{
	
	//damit wir Listen und neue Knoten am Ende anh�nge k�nnen, m�ssen wir uns immer
	//die Ende der Liste merken
	Knoten ende;
	
	
	//wir brauchen nur das Ende uns zu merken, das andere wird schon von der 
	//Konstructor von VerketteListe f�r uns gemacht
	
	public VerketteteSchlange() {
		
		super();
		this.ende = null;
	}
	
	public VerketteteSchlange(int i) {
		
		super(i);
		
		//am Anfang sind Ende uns Anfang gleich
		this.ende = this.eingang;
	}
	
	//diese Methode brauchen wir in dem Fall, dass 
	//anhaengen mit Argument nicht von Typ VerketteteSchlange benutzt wird
	static Knoten findEnd(VerketteteListe list) {
		
		//beginn von Anfang
		Knoten currentElement = list.eingang;
		
		//nimm immer das n�chste Element, bis es keiner mehr gibt
		while(currentElement.next != null) {
			
			currentElement = currentElement.next;
		}
		
		return currentElement;
	}
	
	public void anhaengen(int i) {
		
		Knoten newElement = new Knoten(i);
		
		if(this.ende != null) {
			this.ende.next = newElement;
			this.ende = newElement;
		}
		else {
			
			this.eingang = newElement;
			this.ende = newElement;
		}
	}
	
	
	//Laufzeit n f�r n = L�nge von L2 bei Typ VerketteteListe
	public void anhaengen(VerketteteListe L2) {
		
		if(L2.eingang == null) {
			
			//anh�nge nichts
			return;
		}
		
		//leider m�ssen wir erstmal die Ende der Liste hier finden, da
		//L2 keine Schlange ist
		
		//findEnd ist eine statische Methode, da es Objektunabh�ngig ist
		//diese Methode darf L2 nicht ver�ndern und einfach Attribut ende anh�ngen!
		//deswegen wird das Ende zur�ckgegeben
		
		Knoten endeVonList2 = findEnd(L2); // VerketteteSchlange.findEnd(L2);
		
		if(this.ende != null) {
			this.ende.next = L2.eingang;
			this.ende = endeVonList2;
		}
		else {
			
			this.eingang = L2.eingang;
			this.ende = endeVonList2;
		}
	}
	
	//constante Laufzeit f�r Typ VerketteteSchlange
	public void anhaengen(VerketteteSchlange L2) {
		
		if(this.ende != null) {
			this.ende.next = L2.eingang;
		}
		else {

			this.eingang = L2.eingang;
		}

		if(L2.ende != null) {

			this.ende = L2.ende;
		}
		//otherwise keep it null or equal to the current end, whatever
	}
	
	//Damit ist anhaengen = O(1) und anhaengen = T(n)
	
	//bei einfuege m�ssen wir aufpassen, dass bei leere Liste immer 
	//eine Ende hergestellt wird
	@Override
	public void einfuege(int i) {
		
		super.einfuege(i);
		this.handleFirstAppend();
	}
	
	@Override
	public Knoten einfuege(int i, Knoten k) {
		
		Knoten neuesElement = super.einfuege(i, k);
		this.handleFirstAppend();
		
		return neuesElement;
	}
	
	private void handleFirstAppend() {
		
		if(this.ende == null) {
			this.ende = this.eingang;
		}
	}
	
	
	//wir m�ssen bei loesche aufpassen, wenn das letzte Element der Liste gegeben wird
	//und aus der Schlange raus muss
	
	
	//ich hatte Gl�ck und habe letztes mal loesche so definiert, dass es das
	//Element und sein Vorg�nger sucht und dann loescheGleicht aufruft
	//deswegen kann ich jetzt nur loescheGleich �berschreiben ^>^ :P
	
	@Override
	public void loescheGleich (Knoten previous, Knoten current) {
		
		if(current == this.eingang) {
			
			this.eingang = null;
			this.ende = null;
		}
	}
}