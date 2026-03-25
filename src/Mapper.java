
import static java.awt.SystemColor.text;

//transforma o texto/poema em notas
public class Mapper {

        public static String TextToMusic(String text) {
            String music = "";


            for(int i = 0; i < text.length(); i++ ) {
                char c = text.charAt(i);

                if(c == 'A'){
                    music += "Lá";
                }
                else if(c == 'B'){
                    music += "Si";

                }
                else if (c == 'C'){
                    music += "Dó";

                }
                else if (c == 'D'){
                    music += "Ré";

                }
                else if (c == 'E'){
                    music += "Mi";

                }
                else if (c == 'F'){
                    music += "Fá";

                }
                else if (c == 'G'){
                    music += "Sol";

                }
                else if (c == 'H'){
                    music += "Si Bemol";

                }

            }

            return music;

        }
}

