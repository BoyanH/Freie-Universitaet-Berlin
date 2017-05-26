import psycopg2
import uuid

class Student:
	
	def __init__(self, fname, lname):
		self.firstName = fname
		self.lastName = lname
		self.id = uuid.uuid4().int & (1<<16)-1 # turn into a 16 bit integer with mask

	def addToDB(self, connection):
		c = connection.cursor()

		c.execute("SELECT * FROM Student")
		students = c.fetchall()
		print("Students in Student table: ")
		for student in students:
			print(student)

		insertStudentSql = "INSERT INTO Student (vorname, nachname, matrikelnummer) VALUES ('{0}', '{1}', '{2}')"
		sqlCommand = insertStudentSql.format(self.firstName, self.lastName, self.id)
		c.execute(sqlCommand)
		connection.commit()

dbUser = "testuser"
dbName = "dbs"
host = "localhost"
dbUserPassword = "testpass"

connectionStringTemplate = "dbname='{0}' user='{1}' host='{2}' password='{3}'"
connectionString = connectionStringTemplate.format(dbName, dbUser, host, dbUserPassword)

try:
	connection = psycopg2.connect(connectionString)
	print("Connected to DB!")
	randomStudent = Student("Will", "Smith")
	randomStudent.addToDB(connection)
	print("Successfully addded Student to DB!")
	   
	connection.close()   
except Exception as er:
	print("Failed connecting to DB!")
	print(er)
