import * as React from 'react';
import { IPersonaSharedProps, Persona } from 'office-ui-fabric-react/lib/Persona';
import { TemplateService } from "../services/TemplateService/TemplateService";
import { BaseWebComponent } from './BaseWebComponent';
import * as ReactDOM from 'react-dom';
import { IReadonlyTheme } from '@microsoft/sp-component-base';
import { ITheme } from '@uifabric/styling';

export interface IPersonaCardComponentProps {

    // Item context
    item?: string;

    // Fields configuration object
    fieldsConfiguration?: string;

    // Individual content properties (i.e web component attributes)
    imageUrl?: string;
    text?: string;
    secondaryText?: string;
    tertiaryText?: string;
    optionalText?: string;

    /**
     * The persona image size
     */
    personaSize?: string;

    /**
     * The current theme settings
     */
    themeVariant?: IReadonlyTheme;
}

export interface IPersonaCardComponentState {
}

export class PersonaCardComponent extends React.Component<IPersonaCardComponentProps, IPersonaCardComponentState> {

    public render() {

        let processedProps: IPersonaCardComponentProps = this.props;

        if (this.props.fieldsConfiguration && this.props.item) {
            processedProps = TemplateService.processFieldsConfiguration<IPersonaCardComponentProps>(this.props.fieldsConfiguration, this.props.item);
        }
        
        const persona: IPersonaSharedProps = {
            theme:this.props.themeVariant as ITheme,
            imageUrl: processedProps.imageUrl,
            text: processedProps.text,
            secondaryText: processedProps.secondaryText,
            tertiaryText: processedProps.tertiaryText,
            optionalText: processedProps.optionalText
        };

        return <Persona {...persona} size={parseInt(this.props.personaSize)} />;
    }
}

export class PersonaCardWebComponent extends BaseWebComponent {
   
    constructor() {
        super();
    }
 
    public connectedCallback() {
 
       let props = this.resolveAttributes();
       const personaItem = <PersonaCardComponent {...props}/>;
       ReactDOM.render(personaItem, this);
    }    
}