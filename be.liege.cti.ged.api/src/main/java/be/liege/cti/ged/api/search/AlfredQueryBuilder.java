package be.liege.cti.ged.api.search;

import be.liege.cti.ged.api.NodeReference;

public interface AlfredQueryBuilder {

    /**
     * Recherche dans toutes les propriétés.
     *
     * @param search la chaîne de recherche. Exemple: "cadastre"
     * @return la requête.
     */
    AlfredQuery all(final String search);

    /**
     * Recherche par aspect.
     *
     * @param aspect l'aspect. Exemple: "sys:folder"
     * @return la requête.
     */
    AlfredQuery aspect(final String aspect);

    /**
     * Recherche par catégorie.
     *
     * @param category la catégorie. Exemple: "/cm:generalclassifiable/cm:Software_x0020_Document_x0020_Classification/member"
     * @return la requête.
     */
    AlfredQuery category(final String category);

    /**
     * Recherche par référence du noeud.
     *
     * @param nodeReference le noeud. Exemple: "workspace://SpacesStore/c4ebd508-b9e3-4c48-9e93-cdd774af8bbc"
     * @return
     */
    AlfredQuery nodeReference(final NodeReference nodeReference);

    /**
     * Recherche du noeud parent.
     *
     * @param child trouver le parent de cet enfant. Exemple: "workspace://SpacesStore/c4ebd508-b9e3-4c48-9e93-cdd774af8bbc"
     * @return la requête.
     */
    AlfredQuery parent(final NodeReference child);

    /**
     * Recherche par chemin.
     *
     * @param searchPath le chemin. Exemples: "/app:company_home/cm:Fred_x0020_Performance_x0020_Test//*",
     *                   "/app:company_home/cm:Fred_x0020_Performance_x0020_Test",
     *                   "/app:company_home/cm:Fred_x0020_Performance_x0020_Test/*"
     * @return la requête.
     */
    AlfredQuery path(final String searchPath);


    /**
     * Recherche sur les propriétés.
     *
     * @param name le nom de la propriété.
     * @param value la valeur de la propriété.
     * @param exact la valeur doit-elle être exacte.
     * @return la requête.
     */
    AlfredQuery property(final String name, final String value, final boolean exact);

    /**
     * Recherche sur les propriétés dont la valeur est comprise dans un écart.
     * @param name le nom de la propriété.
     * @param range l'écart dans lequel il faut rechercher les valeurs de la propriété.
     * @return la requête.
     */
    AlfredQuery property(final String name, final Range range);

    /**
     * Recherche par texte.
     *
     * @param text le texte. Exemple: "apple..banana", "ville de liège"
     * @return la requête.
     */
    AlfredQuery text(final String text);

    /**
     * Recherche par type. (Vérifier si exact est à <code>true</code> ou <code>false</code> par défaut.
     *
     * @param type le type de noeud. Exemple: "cm:folder"
     * @return la requête.
     */
    AlfredQuery type(final String type);

    /**
     * Recherche par type. Apparemment, si <em>exact</em> est <code>true</code>, il s'agit d'une recherche de termes.
     *
     * @param type le type de noeud. Exemple: "cm:folder"
     * @param exact <code>true</code> pour recevoir uniquement le type précisé.
     * @return la requête.
     */
    AlfredQuery type(final String type, final boolean exact);

}
