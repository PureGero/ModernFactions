package com.modernfactions.util;

public class UpsidedownTextUtil {

    public static String makeUpsideDown(String text) {
        // From https://stackoverflow.com/questions/24371977/how-to-flip-a-letter-upside-down

        String normal = "abcdefghijklmnopqrstuvwxyz_,;.?!/\\'";
        String split  = "ɐqɔpǝɟbɥıظʞןɯuodbɹsʇnʌʍxʎz‾'؛˙¿¡/\\,";
        //maj
        normal += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        split  += "∀qϽᗡƎℲƃHIſʞ˥WNOԀὉᴚS⊥∩ΛMXʎZ";
        //number
        normal += "0123456789";
        split  += "0ƖᄅƐㄣϛ9ㄥ86";

        String newstr = "";
        char letter;
        for (int i=0; i< text.length(); i++) {
            letter = text.charAt(i);

            int a = normal.indexOf(letter);
            newstr += (a != -1) ? split.charAt(a) : letter;
        }

        return new StringBuilder(newstr).reverse().toString();
    }
}
