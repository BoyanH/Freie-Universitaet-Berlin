## Mustererkennung WS17/18, R. Rojas
### Übungsblatt 1

### Beschreibung
Ziffererkennung mit kNN-Classifier. Dabei wurde eine simple lineare kNN Klassifizierung ohne kd-Baum benutzt.
Die Eingabedaten (Grayscale Pixel von Bilder) wurden als Vektoren in n-Dimensionalen Raum (n=256) betrachtet, ohne weitere Verarbeitung von dem Merkmalen.

### Fehlerrate
Das ist die Ausgabe des Programs und damit auch die Fehlerrate

```
Error rate for k=1 is 5.630293971101146%
Error rate for k=2 is 5.879422022919781%
Error rate for k=3 is 5.5306427503736915%
```

###  Konfusionsmatrix (Plots)
![Konfusionsmatrix für k=1](https://raw.githubusercontent.com/BoyanH/Freie-Universitaet-Berlin/master/MachineLearning/Homework1/Plots/confusion_matrix_for_k_1.png "K = 1")
![Konfusionsmatrix für k=1](https://raw.githubusercontent.com/BoyanH/Freie-Universitaet-Berlin/master/MachineLearning/Homework1/Plots/confusion_matrix_for_k_2.png "K = 2")
![Konfusionsmatrix für k=1](https://raw.githubusercontent.com/BoyanH/Freie-Universitaet-Berlin/master/MachineLearning/Homework1/Plots/confusion_matrix_for_k_3.png "K = 3")
