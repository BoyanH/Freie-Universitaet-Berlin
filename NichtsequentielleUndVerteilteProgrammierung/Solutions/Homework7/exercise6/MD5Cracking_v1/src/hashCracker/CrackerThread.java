package hashCracker;

import fx.ResultPool;

public class CrackerThread extends Thread {

    String[] wordList;
    String[] hashList;
    ResultPool resPool;
    int startIndex;
    int endIndex;
    boolean stopped;

    public CrackerThread(String[] wordList, String[] hashList, ResultPool resPool, int i) {
        this.wordList = wordList;
        this.hashList = hashList;
        this.resPool = resPool;
        this.startIndex = (int) Math.floor(i*wordList.length/4);
        this.endIndex = (int) Math.ceil(startIndex + wordList.length/4);
        this.stopped = false;

        // spaghetti part here, just wanted to try this prototype solution
        // could be fixed at some point

        if (i == 3) {
            this.startIndex -= (int) Math.ceil(wordList.length / 8);
        } else if (i != 0) {
            this.startIndex -= (int) Math.ceil(wordList.length / 8);
            this.endIndex -= (int) Math.ceil(wordList.length / 8);
        } else {
            this.endIndex -= (int) Math.ceil(wordList.length / 8);
        }

        if (this.startIndex < 0) {
            this.startIndex = 0;
        }

        if (this.endIndex > this.wordList.length) {
            this.endIndex = this.wordList.length;
        }
    }

    public void stopCalc() {
        this.stopped = true;
    }

    @Override
    public void run() {

        int count = 0;
        int gausNumber = this.endIndex - 1 - startIndex;
        String[] pairs = new String[gausNumber*(gausNumber+1)];

        for (int i = this.startIndex; i < this.endIndex; i++) {
            for (int j = i; j < wordList.length; j++) {

                String[] variants = {
                        wordList[i] + wordList[j],
                        wordList[j] + wordList[i],
                        wordList[i],
                        wordList[j]
                };

                for (String hash : hashList) {
                    for (String variant : variants) {
                        if (MD5(variant).equals(hash)) {
                            resPool.pushPassword(variant);
                        }
                    }

                    if (this.stopped) {
                        return;
                    }
                }
            }
        }

    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
