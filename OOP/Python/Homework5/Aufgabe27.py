# a) Objekt (Exemplar) einer Klasse - Das wird auch Instanzierung genannt und bedeutet die Herstellung eines Exemplars von einer Klasse. 
#		Die Klasse ist wie ein Muster, der die Struktur und Funktionalitäten dieser Struktur definiert. Das Objet ist eine konkretes
#		Exemplar dieser Klasse, die spezifische Eigenschaften nach dem gegebenen von der Klasse Muster hat. Die Klasse definiert zum Beispiel ein
#		String Name, und das Objekt kann dafür eine konkrete Name haben (Max Mustermann)		

class Person: #a class, something like a template for objects

	def __init__(self, name):
		self.name = name

ivan = Person("Ivan") #an instance, which is an object created using the class Person, which now has some values assigned to its properties

#while the class remains the same, we can create many different instances from it

peter = Person("Peter") #another instance, here with property name = Peter and not Ivan

#they are not the same
print(ivan == peter)

#but they are instances of the same object
print(type(ivan) == type(peter))


#b)Vererbung - das ist die Möglichkeit eine Klasse zu definieren, die auf eine andere basiert ist, aber weitere Eigenschaften und Funktionalitäten definiert

class Student(Person): 						#here is where the inheritance happens, Student inherits from Person
	def __init__(self, name, university):
		super().__init__(name) #here we call the constructor of our parent class to assign name to our name property
		self.university = university		#and assign our university property, which is special for Student

me = Student("Anonymous", "FU Berlin")
print("My name is: ", me.name)				#student has name, although we have not defined it in class Student 

#c) Uberschreiben von Attributen und Methoden - wenn eine Klasse von eine andere erbt, nimmt die automatisch alle Properties und Funktionen.
# 		Deswegen müssen wir manchmal diese überschrieben, also neue Funktionalitäten speziell für die Kinderklasse definieren. Das erklären wir
# 		anhand von dem berühmtesten Beispiel. In den meisten Programmiersprachen gibt es eine Funktion für jede Klasse, die irgendwie diese zu String
#		konvertiert, damit es in der Konsole geschrieben werden kann. Das überschreiben wir in unsere Klasse, damit wir ein sinnvolles Text in der
# 		Konsole bekommen. Dazu überschreiben wir auch die Property Name, was nicht so sinnvoll ist, aber Überschreibung von Attributen müssen wir auch
#		irgendwie zeigen :D

class Bachelor(Student): #note, the classes get more specific the lower we go in the inheritance chain
	
	#override name attribute
	def __init__(self, name, university):
		super(Bachelor, self).__init__(name, university)
		self.name += "(^.^)"								#here we used the inherited constructor and then overwrote the name property
	
	#overriding the initial toString method
	def __str__(self):
		stringifiedJonny = self.name + ", a bachelor student from " + self.university

		return stringifiedJonny

jonny = Bachelor("Jonny", "FU Berlin")

print(me)		#the default function __str__ printing just a hash-code of the object
print(jonny)	#instance of Bachelor class, where __str__ was overwritten to describe the student

#d)Aufruf einer Methode - Methoden sind nichts weiteres als Beschreibung von Operationen. Wenn man die Aufruft, werden die Operationen, die
#		drin definiert sind ausgeführt. Bei der objektorientierten Programmierung ist es auch wichtig, auf welches Exemplar man die Methode Aufruft
# 		wir erklären das Anhand von eine weitere Klasse Master, wo wir die Methode stellDichVor implementieren

class Master(Student):

	def introduce(self):
		print("Hi, I am", self.name, "and I am a master student at", self.university, ". Nice to meet you!")

stamat = Master("Stamat", "FMI")
george = Master("George", "MIT")

stamat.introduce() #we call the method on both instances of Master, yet the console log is a bit different, as we are printing
george.introduce() # object-specific data. Therefore it is important which instance the method is called on


#e) Unterklasse und Oberklasse - das erklären wir anhand von den Definitionen oben. Die Oberklasse ist die Klasse, die allgemeiner ist. Die Unterklasse
#		ist diese, die von Oberklasse erbt und konkreter wird. Bei uns ist Student eine Oberklasse und Master eine Unterklasse. Deswegen sind me, das Exemplar
#		von Student und stamat, das Exemplar von Master, beide Exemplaren von Student, aber nur stamat ist ein Exemplar von Master, da Master eine konkretere
#		Unterklasse ist. Unter steht für unter in der Vererbungshierarchie.

print("isinstance(me, Student): ", isinstance(me, Student))
print("isinstance(stamat, Student): ", isinstance(stamat, Student))

print("isinstance(me, Master): ", isinstance(me, Master))
print("isinstance(stamat, Master): ", isinstance(stamat, Master))

#Diese Hausaufgabe wurde geschrieben vor der Übung, danach wurde überall Instanz mit Exemplar vertauscht