package Parse;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Parse_ObjectProperity {
    public static List<List<String>> Find_ObjectProperity(String path) throws IOException {

        OntModel ontModel_op = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM.OWL_MEM);
        ontModel_op.read(new FileInputStream(path), "");

        List<String> list_op = new ArrayList<>();
        List<String> list_id = new ArrayList<>();
        List<String> list_idAndSon = new ArrayList<>();
        List<String> list_domainAndRange = new ArrayList<>();
        List<List<String>> list = new ArrayList<>();

        Iterator<?> it = ontModel_op.listObjectProperties();

        while (it.hasNext()) {
            OntProperty op = (OntProperty) it.next();
            if (!op.isAnon()) {

                String str_id = op.getLocalName();
                String str_label = op.getLabel(null);
                String str_comment = op.getComment(null);

                String son = Link_ObjectProperity_Son(op);
                String father = Link_ObjectProperity_Father(op);

                String str_domain = Link_ObjectProperity_Domain(op);

                String str_range = Link_ObjectProperity_Range(op);


                list_op.add((str_id + "---" + str_label + "---" + str_comment + "---" + son + "---" + father
                        + "---" + str_domain + "---" + str_range).toLowerCase());  //0
                list_id.add((str_id).toLowerCase());  //1
                list_idAndSon.add((str_id + son).toLowerCase());  //2
                list_domainAndRange.add((str_domain + "---" + str_range).toLowerCase());  //3
            }
        }
        list.add(list_op);
        list.add(list_id);
        list.add(list_idAndSon);
        list.add(list_domainAndRange);
        return list;
    }


    public static String Link_ObjectProperity_Son(OntProperty OP) {

        String all_subclass = "";

        for(ExtendedIterator<? extends  OntProperty> it = OP.listSubProperties(); it.hasNext();) {
            OntProperty op_subclass = (OntProperty) it.next();
            String s = op_subclass.toString().substring(op_subclass.toString().indexOf("#") + 1);
            all_subclass += ' ' + s;
        }
        if (all_subclass.equals("") || all_subclass.equals(null)){
            return "";
        }else {
            return all_subclass;
        }
    }

    public static String Link_ObjectProperity_Father(OntProperty OP) {

        String all_supclass = "";

        for(ExtendedIterator<? extends  OntProperty> it = OP.listSuperProperties(); it.hasNext();) {
            OntProperty op_supclass = (OntProperty) it.next();
            String s = op_supclass.toString().substring(op_supclass.toString().indexOf("#") + 1);
            all_supclass += s;
        }

        if (all_supclass.equals("") || all_supclass.equals(null)){
            return "";
        }else {
            return all_supclass;
        }
    }

    public static String Link_ObjectProperity_Domain(OntProperty OP) {

        String all_Domain = "";

        for(ExtendedIterator<? extends OntResource> it = OP.listDomain(); it.hasNext();) {
            OntResource op_domain = (OntResource) it.next();
            if (op_domain.toString().contains("#")){
                String s = op_domain.toString().substring( op_domain.toString().indexOf("#") + 1);
                all_Domain += s;
            }
        }

        if (all_Domain.equals("") || all_Domain.equals(null)){
            return null;
        }else {
            return all_Domain;
        }
    }

    public static String Link_ObjectProperity_DomainUnionOf(OntProperty OP) {

        String all_Domain = "";

        for(ExtendedIterator<? extends OntResource> it = OP.listDomain(); it.hasNext();) {
            OntResource op_domain = (OntResource) it.next();
            String s = op_domain.toString().substring( op_domain.toString().indexOf("#") + 1);
            all_Domain += s;
        }

        if (all_Domain.equals("") || all_Domain.equals(null)){
            return null;
        }else {
            return all_Domain;
        }
    }

    public static String Link_ObjectProperity_Range(OntProperty OP) {

        String all_range = "";

        for(ExtendedIterator<? extends OntResource> it = OP.listRange(); it.hasNext();) {

            OntResource op_range = (OntResource) it.next();
            if (op_range.toString().contains("#")){
                String s = op_range.toString().substring( op_range.toString().indexOf("#") + 1);
                all_range += s;
            }
        }
        if (all_range.equals("") || all_range.equals(null)){
            return null;
        }else {
            return all_range;
        }
    }

}
