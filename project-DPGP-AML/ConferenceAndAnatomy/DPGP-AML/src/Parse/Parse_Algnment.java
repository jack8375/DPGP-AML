package Parse;

import fr.inrialpes.exmo.align.parser.XMLParser;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parse_Algnment {
    public static List<String> parseRefalignFile(String refalignFilePath) throws Exception {
        Alignment alignment = new XMLParser().parse(new FileInputStream(new File(refalignFilePath)));
        Iterator<Cell> it = alignment.iterator();
        List<String> listOfAllAlignment = new ArrayList<>();
        while (it.hasNext()) {
            Object o = it.next();
            String strMerge = (tearWay(((Cell) o).getObject1().toString()) + "---" + tearWay(((Cell) o).getObject2().toString())).toLowerCase();
            listOfAllAlignment.add(strMerge);
        }
        return listOfAllAlignment;
    }


    public static String tearWay(String s) {
        String sEnd = s.substring(s.indexOf("#") + 1);
        return sEnd;
    }
}
