import numpy as np

class Classifier:
    def score(self, X, y):
        predictions = self.predict(X)
        return np.mean(predictions == y)
    
    def confusion_matrix(self, X, y):
        size = 10 # hardcoded here
        predicted = self.predict(X)
        
        results = np.zeros((size, size), dtype=np.int32)

        for pi, yi in zip(predicted, y):
            assert(int(pi) == int(yi))
            results[int(pi)][int(yi)] += 1

        return results
