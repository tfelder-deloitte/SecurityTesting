(window.webpackJsonp=window.webpackJsonp||[]).push([[319],{1921:function(e,t,n){"use strict";n.r(t),n.d(t,"Key",(function(){return y}));var r=n(2),o=n(332),a=n(317),i=n(31),l=n(365),c=n(806),s=n(312),u=n(375),p=n.n(u),d=n(889),m=n(890),f=n(344);function h(e){const{component:t}=e,[n,o]=r.useState(void 0),a=void 0!==n?n:t.key,l=a!==t.key,c=Object(m.a)(a),u=c===f.b.Valid?void 0:Object(i.translate)("onboarding.create_project.project_key.error",c);return r.createElement(p.a,{confirmButtonText:Object(i.translate)("update_verb"),confirmData:n,modalBody:r.createElement(r.Fragment,null,Object(i.translateWithParameters)("update_key.are_you_sure_to_change_key",t.name),r.createElement("div",{className:"spacer-top"},Object(i.translate)("update_key.old_key"),": ",r.createElement("strong",null,t.key)),r.createElement("div",{className:"spacer-top"},Object(i.translate)("update_key.new_key"),": ",r.createElement("strong",null,n))),modalHeader:Object(i.translate)("update_key.page"),onConfirm:e.onKeyChange},({onFormSubmit:e})=>r.createElement("form",{onSubmit:e},r.createElement(d.a,{error:u,label:Object(i.translate)("update_key.new_key"),onProjectKeyChange:e=>{o(e.currentTarget.value)},touched:l,placeholder:Object(i.translate)("update_key.new_key"),projectKey:a}),r.createElement("div",{className:"spacer-top"},r.createElement(s.SubmitButton,{disabled:!l||void 0!==u,id:"update-key-submit"},Object(i.translate)("update_verb")),r.createElement(s.Button,{className:"spacer-left",disabled:!l,id:"update-key-reset",onClick:()=>{o(void 0)},type:"reset"},Object(i.translate)("reset_verb")))))}class y extends r.PureComponent{constructor(){super(...arguments),this.handleChangeKey=e=>Object(l.b)({from:this.props.component.key,to:e}).then(()=>{c.a.remove(this.props.component.key),this.props.router.replace({pathname:"/project/key",query:{id:e}})})}render(){const{component:e}=this.props;return r.createElement("div",{className:"page page-limited",id:"project-key"},r.createElement(o.a,{defer:!1,title:Object(i.translate)("update_key.page")}),r.createElement("header",{className:"page-header"},r.createElement("h1",{className:"page-title"},Object(i.translate)("update_key.page")),r.createElement("div",{className:"page-description"},Object(i.translate)("update_key.page.description"))),r.createElement(h,{component:e,onKeyChange:this.handleChangeKey}))}}t.default=Object(a.withRouter)(y)},324:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(311),o=n(2),a=n(370),i=n(340);n(341);var l=n(316);t.default=function(e){var t=e.tagName,n=void 0===t?"div":t;return o.createElement(n,{className:r("help-tooltip",e.className)},o.createElement(l.default,{mouseLeaveDelay:.25,onShow:e.onShow,overlay:e.overlay,placement:e.placement},o.createElement("span",{className:"display-inline-flex-center"},e.children||o.createElement(i.ThemeConsumer,null,(function(e){return o.createElement(a.default,{fill:e.colors.gray71,size:12})})))))}},335:function(e,t,n){"use strict";var r,o=this&&this.__extends||(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)}),a=this&&this.__assign||function(){return(a=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var o in t=arguments[n])Object.prototype.hasOwnProperty.call(t,o)&&(e[o]=t[o]);return e}).apply(this,arguments)},i=this&&this.__rest||function(e,t){var n={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(n[r]=e[r]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var o=0;for(r=Object.getOwnPropertySymbols(e);o<r.length;o++)t.indexOf(r[o])<0&&Object.prototype.propertyIsEnumerable.call(e,r[o])&&(n[r[o]]=e[r[o]])}return n};Object.defineProperty(t,"__esModule",{value:!0});var l=n(2),c=n(330),s=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={submitting:!1},t.stopSubmitting=function(){t.mounted&&t.setState({submitting:!1})},t.handleCloseClick=function(e){e&&(e.preventDefault(),e.currentTarget.blur()),t.props.onClose()},t.handleFormSubmit=function(e){e.preventDefault(),t.submit()},t.handleSubmitClick=function(e){e&&(e.preventDefault(),e.currentTarget.blur()),t.submit()},t.submit=function(){var e=t.props.onSubmit();e&&(t.setState({submitting:!0}),e.then(t.stopSubmitting,t.stopSubmitting))},t}return o(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=this.props,t=e.children,n=e.header,r=e.onClose,o=(e.onSubmit,i(e,["children","header","onClose","onSubmit"]));return l.createElement(c.default,a({contentLabel:n,onRequestClose:r},o),t({onCloseClick:this.handleCloseClick,onFormSubmit:this.handleFormSubmit,onSubmitClick:this.handleSubmitClick,submitting:this.state.submitting}))},t}(l.Component);t.default=s},341:function(e,t,n){var r=n(313),o=n(342);"string"==typeof(o=o.__esModule?o.default:o)&&(o=[[e.i,o,""]]);var a={insert:"head",singleton:!1},i=(r(o,a),o.locals?o.locals:{});e.exports=i},342:function(e,t,n){(t=n(314)(!1)).push([e.i,".help-tooltip{display:inline-flex;align-items:center;vertical-align:middle}.help-toolip-link{display:block;width:12px;height:12px;border:none}",""]),e.exports=t},348:function(e,t,n){"use strict";var r,o=this&&this.__extends||(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)}),a=this&&this.__assign||function(){return(a=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var o in t=arguments[n])Object.prototype.hasOwnProperty.call(t,o)&&(e[o]=t[o]);return e}).apply(this,arguments)};Object.defineProperty(t,"__esModule",{value:!0});var i=n(2),l=n(31),c=n(318),s=n(312),u=n(428),p=n(335),d=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.handleSubmit=function(){var e=t.props.onConfirm(t.props.confirmData);return e?e.then(t.props.onClose,(function(){})):void t.props.onClose()},t.renderModalContent=function(e){var n=e.onCloseClick,r=e.onFormSubmit,o=e.submitting,a=t.props,p=a.children,d=a.confirmButtonText,m=a.confirmDisable,f=a.header,h=a.headerDescription,y=a.isDestructive,b=a.cancelButtonText,v=void 0===b?l.translate("cancel"):b;return i.createElement(u.default,null,i.createElement("form",{onSubmit:r},i.createElement("header",{className:"modal-head"},i.createElement("h2",null,f),h),i.createElement("div",{className:"modal-body"},p),i.createElement("footer",{className:"modal-foot"},i.createElement(c.default,{className:"spacer-right",loading:o}),i.createElement(s.SubmitButton,{autoFocus:!0,className:y?"button-red":void 0,disabled:o||m},d),i.createElement(s.ResetButtonLink,{disabled:o,onClick:n},v))))},t}return o(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=this.props,t={header:e.header,onClose:e.onClose,noBackdrop:e.noBackdrop,size:e.size};return i.createElement(p.default,a({onSubmit:this.handleSubmit},t),this.renderModalContent)},t}(i.PureComponent);t.default=d},375:function(e,t,n){"use strict";var r,o=this&&this.__extends||(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)}),a=this&&this.__assign||function(){return(a=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var o in t=arguments[n])Object.prototype.hasOwnProperty.call(t,o)&&(e[o]=t[o]);return e}).apply(this,arguments)},i=this&&this.__rest||function(e,t){var n={};for(var r in e)Object.prototype.hasOwnProperty.call(e,r)&&t.indexOf(r)<0&&(n[r]=e[r]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var o=0;for(r=Object.getOwnPropertySymbols(e);o<r.length;o++)t.indexOf(r[o])<0&&Object.prototype.propertyIsEnumerable.call(e,r[o])&&(n[r[o]]=e[r[o]])}return n};Object.defineProperty(t,"__esModule",{value:!0});var l=n(2),c=n(348),s=n(376),u=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.renderConfirmModal=function(e){var n=e.onClose,r=t.props,o=(r.children,r.modalBody),s=r.modalHeader,u=r.modalHeaderDescription,p=i(r,["children","modalBody","modalHeader","modalHeaderDescription"]);return l.createElement(c.default,a({header:s,headerDescription:u,onClose:n},p),o)},t}return o(t,e),t.prototype.render=function(){return l.createElement(s.default,{modal:this.renderConfirmModal},this.props.children)},t}(l.PureComponent);t.default=u},376:function(e,t,n){"use strict";var r,o=this&&this.__extends||(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)});Object.defineProperty(t,"__esModule",{value:!0});var a=n(2),i=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={modal:!1},t.handleButtonClick=function(){t.setState({modal:!0})},t.handleFormSubmit=function(e){e&&e.preventDefault(),t.setState({modal:!0})},t.handleCloseModal=function(){t.mounted&&t.setState({modal:!1})},t}return o(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){return a.createElement(a.Fragment,null,this.props.children({onClick:this.handleButtonClick,onFormSubmit:this.handleFormSubmit}),this.state.modal&&this.props.modal({onClose:this.handleCloseModal}))},t}(a.PureComponent);t.default=i},561:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(2),o=n(483),a=n(434),i=n(324);t.default=function(e){var t=e.isInvalid&&void 0!==e.error;return r.createElement("div",{className:e.className},r.createElement("label",{htmlFor:e.id},r.createElement("span",{className:"text-middle"},r.createElement("strong",null,e.label),e.required&&r.createElement("em",{className:"mandatory"},"*")),e.help&&r.createElement(i.default,{className:"spacer-left",overlay:e.help})),r.createElement("div",{className:"little-spacer-top spacer-bottom"},e.children,e.isInvalid&&r.createElement(o.default,{className:"spacer-left text-middle"}),t&&r.createElement("span",{className:"little-spacer-left text-danger text-middle"},e.error),e.isValid&&r.createElement(a.default,{className:"spacer-left text-middle"})),e.description&&r.createElement("div",{className:"note abs-width-400"},e.description))}},889:function(e,t,n){"use strict";n.d(t,"a",(function(){return s}));var r=n(311),o=n(2),a=n(561),i=n.n(a),l=n(31),c=n(417);function s(e){const{error:t,help:n,label:a,placeholder:s,projectKey:u,touched:p,validating:d}=e,m=p&&void 0!==t,f=p&&!d&&void 0===t;return o.createElement(i.a,{className:"form-field",description:Object(l.translate)("onboarding.create_project.project_key.description"),error:t,help:n,id:"project-key",isInvalid:m,isValid:f,label:a,required:void 0!==a},o.createElement("input",{autoFocus:!0,className:r("input-super-large",{"is-invalid":m,"is-valid":f}),id:"project-key",maxLength:c.b,minLength:1,onChange:e.onProjectKeyChange,placeholder:s,type:"text",value:u}))}},890:function(e,t,n){"use strict";n.d(t,"a",(function(){return a}));var r=n(344),o=n(417);function a(e){return 0===e.length?r.b.Empty:e.length>o.b?r.b.TooLong:/^[\w\-.:]*[a-z\-_.:]+[\w\-.:]*$/i.test(e)?r.b.Valid:/^[0-9]+$/.test(e)?r.b.OnlyDigits:r.b.InvalidChar}}}]);
//# sourceMappingURL=319.m.dc748a47.chunk.js.map