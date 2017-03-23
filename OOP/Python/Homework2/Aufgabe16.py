def isAValidMatrix(matrix, cols):

	for row in matrix:
		if len(row) != cols:
			return False

	return True	

def transpose(matrix):
	cols = len(matrix[0])
	valid = isAValidMatrix(matrix, cols)

	newRowIndex = 0
	newMatrix = []

	assert valid

	while(newRowIndex < cols):

		newRow = [row[newRowIndex] for row in matrix]
		newMatrix.append(newRow)
		newRowIndex += 1;

	return newMatrix



print(transpose([[1,2,5],[4,3,7]]))