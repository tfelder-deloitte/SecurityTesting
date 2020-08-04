
import { MyCustomComponentWebComponent } from "../CustomComponent";
import { IComponentDefinition } from "../../models/IComponentDefinition";
import { IExtensibilityLibrary } from "../../models/IExtensibilityLibrary";
import { ISuggestionProviderDefinition } from "../../models/ISuggestionProviderDefinition";
import { CustomSuggestionProvider } from "../CustomSuggestionProvider";
import { IQueryModifierDefinition } from "../../models/IQueryModifierDefinition";
import { CustomQueryModifier } from "../CustomQueryModifier";

export class MyCompanyLibraryLibrary implements IExtensibilityLibrary {

  public getCustomWebComponents(): IComponentDefinition<any>[] {
    return [
      {
        componentName: 'my-custom-component',
        componentClass: MyCustomComponentWebComponent
      }
    ];
  }

  public getCustomSuggestionProviders(): ISuggestionProviderDefinition<any>[] {
    return [
      {
        providerName: CustomSuggestionProvider.ProviderName,
        providerDisplayName: CustomSuggestionProvider.ProviderDisplayName,
        providerDescription: CustomSuggestionProvider.ProviderDescription,
        providerClass: CustomSuggestionProvider
      },
    ];
  }

  public getCustomQueryModifiers(): IQueryModifierDefinition<any>[] {
    return [
      {
        displayName: CustomQueryModifier.DisplayName,
        description: CustomQueryModifier.Description,
        class: CustomQueryModifier
      },
      {
        displayName: 'Test 2',
        description: 'Test 2',
        class: CustomQueryModifier
      },
      {
        displayName: 'Test 3',
        description: 'Test 3',
        class: CustomQueryModifier
      }
    ];
  }
}
