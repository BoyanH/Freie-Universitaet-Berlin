fi = int(input('fi: '))
e = int(input('e: '))
d = 0

for k in range(fi):
    n = (k * fi + 1) / e

    if int(n) == n:
        print('d = {}'.format(int(n)))
        break
