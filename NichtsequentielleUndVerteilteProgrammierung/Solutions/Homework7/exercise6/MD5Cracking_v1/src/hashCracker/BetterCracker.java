package hashCracker;


import fx.ResultPool;

public class BetterCracker implements HashCracker {

    ResultPool resPool;
    CrackerThread[] crackerThreads;

    @Override
    public void start(String[] wordList, String[] hashList, ResultPool resPool) {

        this.crackerThreads = new CrackerThread[4];

        for (int i = 0; i < 4; i++) {
            this.crackerThreads[i] = new CrackerThread(wordList, hashList, resPool, i);
            this.crackerThreads[i].start();
        }

        for (int t = 0; t < this.crackerThreads.length; t++) {
            try {
                this.crackerThreads[t].join();
                this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {

        for (CrackerThread thread : this.crackerThreads) {
            thread.stopCalc();
        }
    }
}
