package Parse;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Parse_Class {
    public static List<List<String>> Find_Class(String path) throws IOException {

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ontModel.read(new FileInputStream(path), "");

        List<String> list_class = new ArrayList<>();  //0
        List<String> list_id = new ArrayList<>();  //1
        List<String> list_idAndSon = new ArrayList<>();  //2
        List<String> list_fatherAndSon = new ArrayList<>();  //3
        List<String> list_label = new ArrayList<>();  //4
        List<String> list_son = new ArrayList<>();  //5
        List<String> list_father = new ArrayList<>();  //6
        List<List<String>> list = new ArrayList<>();

        String str_id = "";

        for (ExtendedIterator<OntClass> i = ontModel.listClasses(); i.hasNext(); ) {
            OntClass c = (OntClass) i.next();

            if (!c.isAnon()) {

                str_id = c.getLocalName();

                String str_label = c.getLabel(null);

                String str_comment;
                try {
                    str_comment = c.getComment(null);
                } catch (Exception e) {
                    str_comment = null;
                }

                String son = Link_ClassAndSon(c);
                String father = Link_ClassAndFather(c);
                String disjoint = Parse_Other.Link_DisjointWith(c);

                list_class.add((str_id + "---" + str_label + "---" + str_comment + "---" + son + "---" + father).toLowerCase());
                list_id.add((str_id).toLowerCase());
                list_idAndSon.add((str_id + son).toLowerCase());
                list_fatherAndSon.add((father + son).toLowerCase());
                if (str_label == null)
                    list_label.add(str_label);
                else list_label.add(str_label.replace("_", " ").toLowerCase());
                list_son.add(son.toLowerCase());
                list_father.add(father.toLowerCase());

            }
        }
        list.add(list_class);
        list.add(list_id);
        list.add(list_idAndSon);
        list.add(list_fatherAndSon);
        list.add(list_label);
        list.add(list_son);
        list.add(list_father);

        return list;
    }

    public static String Link_ClassAndSon(OntClass oc) {

        if (oc.hasSubClass()) {
            String all_subclass = "";
            for (ExtendedIterator<OntClass> it = oc.listSubClasses(); it.hasNext(); ) {
                OntClass subclass = (OntClass) it.next();
                String s = subclass.toString().substring(subclass.toString().indexOf("#") + 1);
                all_subclass += ' ' + s;
            }
            return all_subclass;
        } else {
            return "";
        }
    }

    public static String Link_ClassAndFather(OntClass oc) {
        if (oc.hasSuperClass()) {

            String all_supclass = "";
            for (ExtendedIterator<OntClass> it = oc.listSuperClasses(); it.hasNext(); ) {
                OntClass supclass = (OntClass) it.next();
                if (supclass.toString().contains("#")) {
                    String s = supclass.toString().substring(supclass.toString().indexOf("#") + 1);
                    all_supclass += ' ' + s;
                }
            }
            return all_supclass;
        } else {
            return "";
        }
    }

}
