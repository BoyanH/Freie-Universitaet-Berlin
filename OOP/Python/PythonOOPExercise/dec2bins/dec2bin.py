def dec2bin(dec):

	binResult = "0" if dec == 0 else "" 

	while dec != 0:

		binResult = str(dec % 2) + binResult
		dec //= 2

	return binResult

def recDec2Bin(dec, binStr = ""):

	if dec == 0:
		return "0" if binStr == "" else binStr
	else:

		return recDec2Bin(dec//2, str(dec%2) + binStr)