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

public class Parse_DataProperity {
    public static List<List<String>> Find_DataProperity(String path) throws IOException {

        OntModel ontModel_dp = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM.OWL_MEM);
        ontModel_dp.read(new FileInputStream(path), "");

        List<String> list_dp = new ArrayList<>();
        List<String> list_id = new ArrayList<>();
        List<String> list_idAndSon = new ArrayList<>();
        List<String> list_domainAndRange = new ArrayList<>();
        List<List<String>> list = new ArrayList<>();

        Iterator<?> it = ontModel_dp.listDatatypeProperties();

        while (it.hasNext()) {

            OntProperty dp = (OntProperty) it.next();
            if (!dp.isAnon()) {

                String str_id = dp.getLocalName();
                String str_label = dp.getLabel(null);

                String str_comment;
                try {
                    str_comment = dp.getComment(null);
                } catch (Exception e) {
                    str_comment = null;
                }

                String son = Link_DataProperity_Son(dp);
                String father = Link_DataProperity_Father(dp);

                String str_domain = Link_DataProperity_Domain(dp);
                String str_range = Link_DataProperity_Range(dp);

                list_dp.add((str_id + "---" + str_label + "---" + str_comment + "---" + son + "---" + father +
                        "---" + str_domain + "---" + str_range).toLowerCase());
                list_id.add((str_id).toLowerCase());
                list_idAndSon.add((str_id + son).toLowerCase());
                list_domainAndRange.add((str_domain + "---" + str_range).toLowerCase());
            }
        }
        list.add(list_dp);
        list.add(list_id);
        list.add(list_idAndSon);
        list.add(list_domainAndRange);
        return list;
    }

    public static String Link_DataProperity_Son(OntProperty DP) {

        String all_subclass = "";

        for(ExtendedIterator<? extends  OntProperty> it = DP.listSubProperties(); it.hasNext();) {
            OntProperty dp_subclass = (OntProperty) it.next();
            String s = dp_subclass.toString().substring(dp_subclass.toString().indexOf("#") + 1);
            all_subclass += ' ' + s;
        }

        if (all_subclass.equals("") || all_subclass.equals(null)){
            return "";
        }else {
            return all_subclass;
        }

    }

    public static String Link_DataProperity_Father(OntProperty DP) {

        String all_supclass = "";

        for(ExtendedIterator<? extends  OntProperty> it = DP.listSuperProperties(); it.hasNext();) {
            OntProperty dp_supclass = (OntProperty) it.next();
            String s = dp_supclass.toString().substring(dp_supclass.toString().indexOf("#") + 1);
            all_supclass += s;
        }
        if (all_supclass.equals("") || all_supclass.equals(null)){
            return "";
        }else {
            return all_supclass;
        }

    }

    public static String Link_DataProperity_Domain(OntProperty DP) {

        String all_Domain = "";

        for(ExtendedIterator<? extends OntResource> it = DP.listDomain(); it.hasNext();) {
            OntResource dp_domain = (OntResource) it.next();
            String s = dp_domain.toString().substring( dp_domain.toString().indexOf("#") + 1);
            all_Domain += s;
        }
        if (all_Domain.equals("") || all_Domain.equals(null)){
            return null;
        }else {
            return all_Domain;
        }
    }

    public static String Link_DataProperity_Range(OntProperty DP) {

        String all_range = "";

        for(ExtendedIterator<? extends OntResource> it = DP.listRange(); it.hasNext();) {
            OntResource dp_range = (OntResource) it.next();
            String s = dp_range.toString().substring( dp_range.toString().indexOf("#") + 1);
            all_range += s;
        }
        if (all_range.equals("") || all_range.equals(null)){
            return null;
        }else {
            return all_range;
        }
    }
}
