define([], function() {
  return {
    SearchVerticalsGroupName: "Search verticals settings",
    PlaceHolderEditLabel: "Edit",
    PlaceHolderConfigureBtnLabel: "Configure",
    PlaceHolderIconText: "Search Verticals Web Part",
    PlaceHolderDescription: "This component allows you to search within scopes (i.e verticals)",
    SameTabOpenBehavior: "Use the current tab",
    NewTabOpenBehavior: "Open in a new tab",
    PageOpenBehaviorLabel: "Opening behavior",
    PropertyPane: {
      Verticals: {
        PropertyLabel: "Search verticals",
        PanelHeader: "Configure search verticals",
        PanelDescription: "Add a new vertical to allow users to search in a predefined scope.",
        ButtonLabel: "Configure",
        FieldValidationErrorMessage: "This field is required",
        Fields: {
          TabName: "Tab name",
          QueryTemplate: "Query Template",
          ResultSource: "Result Source Identifier",
          IconName: "Office UI Fabric icon name",
          IsLink: "Is hyperlink",
          LinkUrl: "Link URL",
          OpenBehavior: "Open behavior"
        }
      },
      ShowCounts: {
        PropertyLabel: "Show result counts"
      },
      DefaultVerticalQuerystringParam: {
        PropertyLabel: "Default vertical querystring parameter"
      },
      SearchResultsDataSource: {
        PropertyLabel: "Connect to a search results Web Part"
      }
    }
  }
});