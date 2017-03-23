def ausgleich(s1,s2):
    """Gleiche zwei Listen aus, sodass sie die gleiche Summe haben.

    s1 und s2 sind Listen mit positiven ganzen Zahlen.."""
    assert all(x>0 and isinstance(x,int) for x in s1)
    assert all(x>0 and isinstance(x,int) for x in s2)

    while sum(s1)!=sum(s2):
        if sum(s1)>sum(s2):
            a = s1.pop()
            s2.append(a)
        else:
            a = s2.pop()
            s1.append(a)
    return s1,s2

s1=[] #s1=[2,4,2]
s2=[] #s2=[3,2,1]

t1,t2 =  ausgleich(s1,s2)
print (sum(t1),t1)
print (sum(t2),t2)


"""
1. Bestimmung der Invariante:
    I   =   {type(s1) = list && type(s2) = list && for every i in {0,1...(len(s1)-1)} type(s1[i]) = Int && s1[i]>0 
            && for every j in {0,1...(len(s2)-1)} type(s2[j]) = Int && s2[j] > 0}

    --- Mit Wörter: s1 und s2 sind listen von Integers, deren Werte größer als 0 sind.

2.1 B   =   sum(s1) != sum(s2)

2.2 Wir erhalten die Schleifeninvariante I && B

    {I && B}
        if sum(s1)>sum(s2):
                a = s1.pop()
                s2.append(a)
            else:
                a = s2.pop()
                s1.append(a)
        return s1,s2
    {I}

3.1 Nach der while-Regeln zerteilen wir den obigen Teil in zwei Teilen für den True und für den False Fall:
    
    Falls True:     {I && B && sum(s1) > sum(s2)} a = s1.pop(); s2.append(a) {I}

    Falls False:    {I && B && sum(s1) <= sum(s2)} a = s2.pop(); s1.append(a) {I}


3.2 Beweis der True Fall
"""



"""
Relevanter Programmteil
---------------------------
while sum(s1)!=sum(s2):
        if sum(s1)>sum(s2):
            a = s1.pop()
            s2.append(a)
        else:
            a = s2.pop()
            s1.append(a)
    return s1,s2



Vor- und Nachbedingung
----------------------

{P = \/x e s1, \/ y e s2: x,y e N; sum(s1) >= 0, sum(s2) >= 0} // s1 und s2 sind Listen von Zahlen, deren Sum ist größer oder gleich 0
while sum(s1)!=sum(s2):
        if sum(s1)>sum(s2):
            a = s1.pop()
            s2.append(a)
        else:
            a = s2.pop()
            s1.append(a)
    return s1,s2
{Q = sum(s1) == sum(s2), s1 und s2 wieder Listen}



Verifikation der Schleife
---------------------------

{P}

while sum(s1)!=sum(s2):
    {P) /\ sum(s1) != sum(s2)}
        if sum(s1)>sum(s2):
            a = s1.pop()
            s2.append(a)
        else:
            a = s2.pop()
            s1.append(a)
    {P}
    return s1,s2


{Q}




"""

