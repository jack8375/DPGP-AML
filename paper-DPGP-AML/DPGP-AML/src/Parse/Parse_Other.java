package Parse;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.UnionClass;
import org.apache.jena.util.iterator.ExtendedIterator;

public class Parse_Other {

    public static String Link_Properity_InverseOf(OntProperty DP) {

        String deal_InverseOf = "";

        for (ExtendedIterator<? extends OntProperty> it = DP.listInverseOf(); it.hasNext(); ) {
            OntProperty op_inverseOf = (OntProperty) it.next();
            String s = op_inverseOf.toString().substring(op_inverseOf.toString().indexOf("#") + 1) + " ";
            deal_InverseOf += s;

        }
        if (deal_InverseOf.equals("") || deal_InverseOf.equals(null)) {
            return "";
        } else {
            return (deal_InverseOf).replace(":", "");
        }
    }


    public static String Link_DisjointWith(OntClass oc) {

        String deal_disjointWith = "";
        for (ExtendedIterator<OntClass> it = oc.listDisjointWith(); it.hasNext(); ) {
            OntClass disjointWith = (OntClass) it.next();
            String s = disjointWith.toString().substring(disjointWith.toString().indexOf("#") + 1) + " ";
            deal_disjointWith += s;

        }
        if (deal_disjointWith.equals("") || deal_disjointWith.equals(null)) {
            return "";
        } else {
            return (deal_disjointWith).replace(":", "");
        }
    }


    public static String Link_EquivalentClasses(OntClass oc) {

        String deal_equivalentClasses = "";
        for (ExtendedIterator<OntClass> it = oc.listEquivalentClasses(); it.hasNext(); ) {
            OntClass equivalentClasses = (OntClass) it.next();

            if (equivalentClasses.isUnionClass()) {
                UnionClass equivalentClasses_Union = (UnionClass) equivalentClasses.asUnionClass();
                String s = equivalentClasses_Union.toString().substring(equivalentClasses_Union.toString().indexOf("#") + 1) + " ";
                deal_equivalentClasses += s;

            }
        }
        if (deal_equivalentClasses.equals("") || deal_equivalentClasses.equals(null)) {
            return "";
        } else {
            return (deal_equivalentClasses).replace(":", "");
        }
    }
}
