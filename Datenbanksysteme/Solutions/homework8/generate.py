from scipy.spatial import distance

def iter(a):
    string = '4. \\\\ \n$\sqrt{{({0}-12.8)^2 + ({1}-42.6)^2}} \\approx {2}$ \\\\ \n$\sqrt{{({0}-78.4)^2 + ({1}-43.6)^2}} \\approx {3}$ \\\\'
    print(string.format(a[0], a[1], distance.euclidean(a, [12.8, 42.6]), distance.euclidean(a, [78.4,43.6])))
    

for i in [[22,57], [79,46], [61,52], [14,20], [22,14], [75,17], [81,40], [2,91], [96,31], [4,31]]:
	iter(i)