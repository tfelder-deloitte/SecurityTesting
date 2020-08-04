import ResultsLayoutOption from '../../models/ResultsLayoutOption';
import { DynamicProperty } from '@microsoft/sp-component-base';
import { ISortFieldConfiguration } from '../../models/ISortFieldConfiguration';
import ISortableFieldConfiguration from '../../models/ISortableFieldConfiguration';
import { ISearchResultType } from '../../models/ISearchResultType';
import { ICustomTemplateFieldValue } from '../../services/ResultService/ResultService';
import { ISynonymFieldConfiguration} from '../../models/ISynonymFieldConfiguration';
import IQueryModifierConfiguration from '../../models/IQueryModifierConfiguration';
import { IQueryModifierDefinition } from '../../services/ExtensibilityService/IQueryModifierDefinition';
import { IPagingSettings } from '../../models/IPagingSettings';

export interface ISearchResultsWebPartProps {
    queryKeywords: DynamicProperty<string>;
    defaultSearchQuery: string;
    useDefaultSearchQuery: boolean;
    queryTemplate: string;
    resultSourceId: string;
    sortList: ISortFieldConfiguration[];
    enableQueryRules: boolean;
    includeOneDriveResults: boolean;
    selectedProperties: string;
    sortableFields: ISortableFieldConfiguration[];
    showResultsCount: boolean;
    showBlank: boolean;
    selectedLayout: ResultsLayoutOption;
    externalTemplateUrl: string;
    inlineTemplateText: string;
    webPartTitle: string;
    resultTypes: ISearchResultType[];
    rendererId: string;
    customTemplateFieldValues: ICustomTemplateFieldValue[];
    enableLocalization: boolean;
    useRefiners: boolean;
    useSearchVerticals: boolean;
    refinerDataSourceReference: string;
    searchVerticalDataSourceReference: string;
    paginationDataSourceReference: string;
    synonymList: ISynonymFieldConfiguration[];
    searchQueryLanguage: number;
    templateParameters: { [key:string]: any };
    queryModifiers: IQueryModifierConfiguration[];
    selectedQueryModifierDisplayName: string;
    refinementFilters: string;

    /**
     * The Web Part paging settings
     */
    paging: IPagingSettings;
}
