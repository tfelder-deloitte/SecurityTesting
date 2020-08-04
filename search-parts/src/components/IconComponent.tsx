import * as React from 'react';
import { Icon, IconType, mergeStyles, ImageFit, ITheme } from 'office-ui-fabric-react';
import { getFileTypeIconProps, FileTypeIconSize, FileIconType } from '@uifabric/file-type-icons';
import { isEmpty, trimStart } from '@microsoft/sp-lodash-subset';
import { BaseWebComponent } from './BaseWebComponent';
import * as ReactDOM from 'react-dom';
import { IReadonlyTheme } from '@microsoft/sp-component-base';

export interface IIconProps {
    fileExtension?: string;
    iconName?: string;
    size?: FileTypeIconSize;
    imageUrl?: string;

    /**
     * The current theme settings
     */
    themeVariant?: IReadonlyTheme;
}

export interface IIconState {
}

export class IconComponent extends React.Component<IIconProps, IIconState> {

    public render() {
        let iconSize: FileTypeIconSize = 32;
        if (this.props.size && this.props.size > 0) {
            iconSize = this.props.size;
        }

        const iconClass = mergeStyles({
            height: iconSize,
            width: iconSize,
            fontSize: iconSize + "px",
            marginRight: 15,
            overflow: 'visible'
        });

        let iconProps;
        if (!isEmpty(this.props.imageUrl)) {
            iconProps = { iconType: IconType.Image, imageProps: { src: this.props.imageUrl, imageFit: ImageFit.contain, width: iconSize + 'px', height: iconSize + 'px' } };
        } else if (!isEmpty(this.props.fileExtension)) {

            if (this.props.fileExtension == "IsListItem") {
                iconProps = getFileTypeIconProps({ type: FileIconType.listItem, size: iconSize, imageFileType: 'svg' });
            } else if (this.props.fileExtension == "IsContainer") {
                iconProps = getFileTypeIconProps({ type: FileIconType.folder, size: iconSize, imageFileType: 'svg' });
            } else {
                let fileExt = trimStart(this.props.fileExtension.trim(), '.');
                iconProps = getFileTypeIconProps({ extension: fileExt, size: iconSize });
            }
        } else if (!isEmpty(this.props.iconName)) {
            iconProps = { iconName: this.props.iconName };
        } else {
            iconProps = getFileTypeIconProps({ type: FileIconType.genericFile, size: iconSize, imageFileType: 'svg' });
        }
        return <Icon {...iconProps} theme={this.props.themeVariant as ITheme} className={iconClass} />;
    }
}

export class IconWebComponent extends BaseWebComponent {
   
    public constructor() {
        super(); 
    }
 
    public connectedCallback() {
 
       let props = this.resolveAttributes();
       const iconComponent = <IconComponent {...props}/>;
       ReactDOM.render(iconComponent, this);
    }    
 }