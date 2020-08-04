define([], function() {
  return {
    SearchVerticalsGroupName: "Configuration des verticaux de recherche",
    PlaceHolderEditLabel: "Éditer",
    PlaceHolderConfigureBtnLabel: "Configurer",
    PlaceHolderIconText: "Web Part de verticaux de recherche",
    PlaceHolderDescription: "Ce composant permet de recherche à travers des périmètre de recherche prédéfinis",
    SameTabOpenBehavior: "Utiliser l'onglet courant",
    NewTabOpenBehavior: "Ouvrir dans un nouvel onglet",
    PageOpenBehaviorLabel: "Mode d'ouverture de la page",
    PropertyPane: {
      Verticals: {
        PropertyLabel: "Verticaux de recherche",
        PanelHeader: "Configurer les verticaux de recherche",
        PanelDescription: "Ajouter un vertical de recherche pour permettre aux utilisateurs de recherche selon un périmètre précis.",
        ButtonLabel: "Configurer",
        FieldValidationErrorMessage: "Ce champ est requis",
        Fields: {
          TabName: "Nom de l'onglet",
          QueryTemplate: "Modèle de requête",
          ResultSource: "Identifiant de la source de résultats",
          IconName: "Nom de l'icône Office UI Fabric",
          IsLink: "Est un lien",
          LinkUrl: "URL du lien",
          OpenBehavior: "Mode d'ouverture"
        }
      },
      ShowCounts: {
        PropertyLabel: "Afficher le nombre de résultats"
      },
      DefaultVerticalQuerystringParam: {
        PropertyLabel: "Défaut paramètre de querystring verticale"
      },
      SearchResultsDataSource: {
        PropertyLabel: "Se connecter à un Web Part de résultats de recherche"
      }
    }
  }
});