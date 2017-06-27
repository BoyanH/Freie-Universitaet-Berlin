package hashCracker;


import fx.ResultPool;

public class SimpleCracker implements HashCracker {

    ResultPool resPool;
    boolean stopped;

    @Override
    public void start(String[] wordList, String[] hashList, ResultPool resPool) {

        this.stopped = false;

        for (int i = 0; i < wordList.length; i++) {
            for (int j = i; j < wordList.length; j++) {
                String wordA = wordList[i];
                String wordB = wordList[j];
                String variantA = wordA + wordB;
                String variantB = wordB + wordA;

                for (int k = 0; k < hashList.length; k++) {
                    if (MD5(variantA).equals(hashList[k])) {
                        resPool.pushPassword(variantA);
                    } else if (MD5(variantB).equals(hashList[k])) {
                        resPool.pushPassword(variantB);
                    }

                    if (this.stopped) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        this.stopped = true;
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
