package services;

import java.util.HashMap;
import java.util.Map;

//transforma o texto/poema em notas



public class Mapper {

    private static final Map<Character, String> map = new HashMap<>();

    static{
        map.put('A', "LÁ");
        map.put('B', "Si");
        map.put('C', "Dó");
        map.put('D', "Ré");
        map.put('E', "Mi");
        map.put('F', "Fá");
        map.put('G', "Sol");
        map.put('H', "Si Bemol");

    }

    public static  String textToMusic(String text){
        StringBuilder music = new StringBuilder();

        for(int i = 0; i< text.length(); i++){
            char c = Character.toUpperCase(text.charAt(i));
            // verifica se devo usar upper,
            // pois letras minusculas nao sao notas e sim silencio ou pause
            if(map.containsKey(c)){
                music.append(map.get(c)).append(" ");
            }
        }

        return music.toString();

    }


}
