(window.webpackJsonp=window.webpackJsonp||[]).push([[300],{1912:function(e,t,r){"use strict";r.r(t),r.d(t,"default",(function(){return w}));var n=r(2),a=r(332),l=r(31),o=r(365),c=r(347),i=r.n(c),s=r(317),u=r(324),p=r.n(u),f=r(316),d=r.n(f),m=r(364),h=r.n(m),v=r(354),y=r.n(v),b=r(378),O=r.n(b),g=r(876),j=r(589),_=r(322);function E({project:e}){const{links:t}=e,r=Object(j.c)(t.map((e,t)=>{const{href:r,name:n,type:a}=e;return{id:"link-".concat(t),name:n,type:a,url:r}})),{lastAnalysisDate:a}=e;return n.createElement("div",{className:"account-project-card clearfix"},n.createElement("aside",{className:"account-project-side"},void 0!==a?n.createElement("div",{className:"account-project-analysis"},n.createElement(h.a,{date:a},e=>n.createElement(d.a,{overlay:n.createElement(y.a,{date:a})},n.createElement("span",null,Object(l.translateWithParameters)("my_account.projects.analyzed_x",e))))):n.createElement("div",{className:"account-project-analysis"},Object(l.translate)("my_account.projects.never_analyzed")),void 0!==e.qualityGate&&n.createElement("div",{className:"account-project-quality-gate"},"WARN"===e.qualityGate&&n.createElement(p.a,{className:"little-spacer-right",overlay:Object(l.translate)("quality_gates.conditions.warning.tooltip")}),n.createElement(O.a,{"aria-label":Object(l.translate)("quality_gates.status"),level:e.qualityGate}))),n.createElement("h3",{className:"account-project-name"},n.createElement(s.Link,{to:Object(_.u)(e.key)},e.name)),r.length>0&&n.createElement("div",{className:"account-project-links"},n.createElement("ul",{className:"list-inline"},r.map(e=>n.createElement(g.a,{iconOnly:!0,key:e.id,link:e})))),n.createElement("div",{className:"account-project-key"},e.key),!!e.description&&n.createElement("div",{className:"account-project-description"},e.description))}function x(e){const{projects:t}=e;return n.createElement("div",{id:"account-projects"},0===t.length?n.createElement("div",{className:"js-no-results"},Object(l.translate)("my_account.projects.no_results")):n.createElement("p",null,Object(l.translate)("my_account.projects.description")),t.length>0&&n.createElement("ul",{className:"account-projects-list"},t.map(e=>n.createElement("li",{key:e.key},n.createElement(E,{project:e})))),t.length>0&&n.createElement(i.a,{count:t.length,loadMore:e.loadMore,ready:!e.loading,total:e.total||0}))}class w extends n.PureComponent{constructor(){super(...arguments),this.mounted=!1,this.state={loading:!0,page:1},this.loadMore=()=>{this.loadProjects(this.state.page+1)}}componentDidMount(){this.mounted=!0,this.loadProjects()}componentWillUnmount(){this.mounted=!1}loadProjects(e=this.state.page){this.setState({loading:!0});const t={p:e,ps:100};return Object(o.s)(t).then(({paging:t,projects:r})=>{this.setState(n=>({projects:e>1?[...n.projects||[],...r]:r,loading:!1,page:t.pageIndex,total:t.total}))})}render(){const e=n.createElement(a.a,{title:Object(l.translate)("my_account.projects")});return null==this.state.projects?n.createElement("div",{className:"text-center"},e,n.createElement("i",{className:"spinner spacer"})):n.createElement("div",{className:"account-body account-container"},e,n.createElement(x,{loadMore:this.loadMore,loading:this.state.loading,projects:this.state.projects,total:this.state.total}))}}},324:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r(311),a=r(2),l=r(370),o=r(340);r(341);var c=r(316);t.default=function(e){var t=e.tagName,r=void 0===t?"div":t;return a.createElement(r,{className:n("help-tooltip",e.className)},a.createElement(c.default,{mouseLeaveDelay:.25,onShow:e.onShow,overlay:e.overlay,placement:e.placement},a.createElement("span",{className:"display-inline-flex-center"},e.children||a.createElement(o.ThemeConsumer,null,(function(e){return a.createElement(l.default,{fill:e.colors.gray71,size:12})})))))}},341:function(e,t,r){var n=r(313),a=r(342);"string"==typeof(a=a.__esModule?a.default:a)&&(a=[[e.i,a,""]]);var l={insert:"head",singleton:!1},o=(n(a,l),a.locals?a.locals:{});e.exports=o},342:function(e,t,r){(t=r(314)(!1)).push([e.i,".help-tooltip{display:inline-flex;align-items:center;vertical-align:middle}.help-toolip-link{display:block;width:12px;height:12px;border:none}",""]),e.exports=t},347:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r(311),a=r(2),l=r(31),o=r(320),c=r(318),i=r(312);t.default=function(e){var t,r=e.className,s=e.count,u=e.loading,p=e.needReload,f=e.total,d=e.ready,m=void 0===d||d,h=f&&f>s;return p&&e.reload?t=a.createElement(i.Button,{className:"spacer-left","data-test":"reload",disabled:u,onClick:e.reload},l.translate("reload")):h&&e.loadMore&&(t=a.createElement(i.Button,{className:"spacer-left",disabled:u,"data-test":"show-more",onClick:e.loadMore},l.translate("show_more"))),a.createElement("footer",{className:n("spacer-top note text-center",{"new-loading":!m},r)},l.translateWithParameters("x_of_y_shown",o.formatMeasure(s,"INT",null),o.formatMeasure(f,"INT",null)),t,u&&a.createElement(c.default,{className:"text-bottom spacer-left position-absolute"}))}},354:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)};Object.defineProperty(t,"__esModule",{value:!0});var a=r(2),l=r(319),o=r(26);t.formatterOption={year:"numeric",month:"long",day:"numeric",hour:"numeric",minute:"numeric"},t.default=function(e){var r=e.children,c=e.date;return a.createElement(l.FormattedDate,n({value:o.parseDate(c)},t.formatterOption),r)}},364:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r(2),a=r(319),l=r(26);t.default=function(e){var t=e.children,r=e.date;return n.createElement(a.FormattedRelative,{value:l.parseDate(r)},t)}},378:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r(311),a=r(2),l=r(320);r(410),t.default=function(e){var t=l.formatMeasure(e.level,"LEVEL"),r=n(e.className,"level","level-"+e.level,{"level-small":e.small,"level-muted":e.muted});return a.createElement("span",{"aria-label":e["aria-label"],"aria-labelledby":e["aria-labelledby"],className:r},t)}},410:function(e,t,r){var n=r(313),a=r(411);"string"==typeof(a=a.__esModule?a.default:a)&&(a=[[e.i,a,""]]);var l={insert:"head",singleton:!1},o=(n(a,l),a.locals?a.locals:{});e.exports=o},411:function(e,t,r){(t=r(314)(!1)).push([e.i,".level{display:inline-block;min-width:80px;height:24px;line-height:24px;border-radius:24px;box-sizing:border-box;color:#fff;letter-spacing:.02em;font-size:13px;font-weight:400;text-align:center;text-shadow:0 0 1px rgba(0,0,0,.35)}.level,.level-small{width:auto;padding-left:9px;padding-right:9px}.level-small{min-width:64px;margin-top:-1px;margin-bottom:-1px;height:20px;line-height:20px;font-size:12px}.level-muted{background-color:#bdbdbd!important}a>.level{margin-bottom:-1px;border-bottom:1px solid;transition:all .2s ease}a>.level:hover{opacity:.8}.level-OK{background-color:#0a0}.level-WARN{background-color:#ed7d20}.level-ERROR{background-color:#d4333f}.level-NONE{background-color:#b4b4b4}",""]),e.exports=t},492:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var a=0;for(n=Object.getOwnPropertySymbols(e);a<n.length;a++)t.indexOf(n[a])<0&&Object.prototype.propertyIsEnumerable.call(e,n[a])&&(r[n[a]]=e[n[a]])}return r};Object.defineProperty(t,"__esModule",{value:!0});var l=r(2),o=r(315);t.default=function(e){var t=e.fill,r=void 0===t?"currentColor":t,c=a(e,["fill"]);return l.createElement(o.default,n({},c),l.createElement("path",{d:"M12 9.25v2.5A2.25 2.25 0 0 1 9.75 14h-6.5A2.25 2.25 0 0 1 1 11.75v-6.5A2.25 2.25 0 0 1 3.25 3h5.5c.14 0 .25.11.25.25v.5c0 .14-.11.25-.25.25h-5.5C2.562 4 2 4.563 2 5.25v6.5c0 .688.563 1.25 1.25 1.25h6.5c.688 0 1.25-.563 1.25-1.25v-2.5c0-.14.11-.25.25-.25h.5c.14 0 .25.11.25.25zm3-6.75v4c0 .273-.227.5-.5.5a.497.497 0 0 1-.352-.148l-1.375-1.375L7.68 10.57a.27.27 0 0 1-.18.078.27.27 0 0 1-.18-.078l-.89-.89a.27.27 0 0 1-.078-.18.27.27 0 0 1 .078-.18l5.093-5.093-1.375-1.375A.497.497 0 0 1 10 2.5c0-.273.227-.5.5-.5h4c.273 0 .5.227.5.5z",style:{fill:r}}))}},493:function(e,t,r){var n=r(400)((function(e,t,r){e[r?0:1].push(t)}),(function(){return[[],[]]}));e.exports=n},589:function(e,t,r){"use strict";r.d(t,"b",(function(){return s})),r.d(t,"c",(function(){return u})),r.d(t,"a",(function(){return p}));var n=r(329),a=r.n(n),l=r(493),o=r.n(l),c=r(31);const i=["homepage","ci","issue","scm","scm_dev"];function s(e){return i.includes(e.type)}function u(e){const[t,r]=o()(e,s);return[...a()(t,e=>i.indexOf(e.type)),...a()(r,e=>e.name&&e.name.toLowerCase())]}function p(e){return s(e)?Object(c.translate)("project_links",e.type):e.name}},611:function(e,t,r){(function(e){!function(e){"use strict";e.exports.is_uri=r,e.exports.is_http_uri=n,e.exports.is_https_uri=a,e.exports.is_web_uri=l,e.exports.isUri=r,e.exports.isHttpUri=n,e.exports.isHttpsUri=a,e.exports.isWebUri=l;var t=function(e){return e.match(/(?:([^:\/?#]+):)?(?:\/\/([^\/?#]*))?([^?#]*)(?:\?([^#]*))?(?:#(.*))?/)};function r(e){if(e&&!/[^a-z0-9\:\/\?\#\[\]\@\!\$\&\'\(\)\*\+\,\;\=\.\-\_\~\%]/i.test(e)&&!/%[^0-9a-f]/i.test(e)&&!/%[0-9a-f](:?[^0-9a-f]|$)/i.test(e)){var r,n,a,l,o,c="",i="";if(c=(r=t(e))[1],n=r[2],a=r[3],l=r[4],o=r[5],c&&c.length&&a.length>=0){if(n&&n.length){if(0!==a.length&&!/^\//.test(a))return}else if(/^\/\//.test(a))return;if(/^[a-z][a-z0-9\+\-\.]*$/.test(c.toLowerCase()))return i+=c+":",n&&n.length&&(i+="//"+n),i+=a,l&&l.length&&(i+="?"+l),o&&o.length&&(i+="#"+o),i}}}function n(e,n){if(r(e)){var a,l,o,c,i="",s="",u="",p="";if(i=(a=t(e))[1],s=a[2],l=a[3],o=a[4],c=a[5],i){if(n){if("https"!=i.toLowerCase())return}else if("http"!=i.toLowerCase())return;if(s)return/:(\d+)$/.test(s)&&(u=s.match(/:(\d+)$/)[0],s=s.replace(/:\d+$/,"")),p+=i+":",p+="//"+s,u&&(p+=u),p+=l,o&&o.length&&(p+="?"+o),c&&c.length&&(p+="#"+c),p}}}function a(e){return n(e,!0)}function l(e){return n(e)||a(e)}}(e)}).call(this,r(34)(e))},704:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var a=0;for(n=Object.getOwnPropertySymbols(e);a<n.length;a++)t.indexOf(n[a])<0&&Object.prototype.propertyIsEnumerable.call(e,n[a])&&(r[n[a]]=e[n[a]])}return r};Object.defineProperty(t,"__esModule",{value:!0});var l=r(2),o=r(705),c=r(706),i=r(492),s=r(707),u=r(708);t.default=function(e){var t=e.type,r=a(e,["type"]);switch(t){case"issue":return l.createElement(o.default,n({},r));case"homepage":return l.createElement(s.default,n({},r));case"ci":return l.createElement(c.default,n({},r));case"scm":return l.createElement(u.default,n({},r));default:return l.createElement(i.default,n({},r))}}},705:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var a=0;for(n=Object.getOwnPropertySymbols(e);a<n.length;a++)t.indexOf(n[a])<0&&Object.prototype.propertyIsEnumerable.call(e,n[a])&&(r[n[a]]=e[n[a]])}return r};Object.defineProperty(t,"__esModule",{value:!0});var l=r(2),o=r(315);t.default=function(e){var t=e.fill,r=void 0===t?"currentColor":t,c=a(e,["fill"]);return l.createElement(o.default,n({},c),l.createElement("path",{d:"M13.5 9.5c1.003.033 1.466 1.952 0 2h-2.618L9.685 9.107 8 14.162 6.096 8.45l-.832 3.05-2.829-.002c-.984-.097-1.369-1.951.065-1.998h1.236l2.168-7.95L8 7.838l1.315-3.945L12.118 9.5H13.5z",style:{fill:r}}))}},706:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var a=0;for(n=Object.getOwnPropertySymbols(e);a<n.length;a++)t.indexOf(n[a])<0&&Object.prototype.propertyIsEnumerable.call(e,n[a])&&(r[n[a]]=e[n[a]])}return r};Object.defineProperty(t,"__esModule",{value:!0});var l=r(2),o=r(315);t.default=function(e){var t=e.fill,r=void 0===t?"currentColor":t,c=a(e,["fill"]);return l.createElement(o.default,n({},c),l.createElement("path",{d:"M13.805 9.25c0 .016 0 .04-.008.055C13.133 12.07 10.852 14 7.969 14c-1.524 0-3-.602-4.11-1.656l-1.007 1.008a.497.497 0 0 1-.352.148.504.504 0 0 1-.5-.5V9.5c0-.273.227-.5.5-.5H6c.273 0 .5.227.5.5a.497.497 0 0 1-.148.352l-1.07 1.07a3.988 3.988 0 0 0 6.125-.828c.187-.305.28-.602.413-.914.04-.11.117-.18.235-.18h1.5c.14 0 .25.117.25.25zM14 3v3.5c0 .273-.227.5-.5.5H10a.504.504 0 0 1-.5-.5c0-.133.055-.258.148-.352l1.079-1.078A4.019 4.019 0 0 0 8 4c-1.39 0-2.68.719-3.406 1.906-.188.305-.282.602-.414.914-.04.11-.117.18-.235.18H2.391a.252.252 0 0 1-.25-.25v-.055C2.812 3.922 5.117 2 8 2c1.531 0 3.023.61 4.133 1.656l1.015-1.008A.497.497 0 0 1 13.5 2.5c.273 0 .5.227.5.5z",style:{fill:r}}))}},707:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var a=0;for(n=Object.getOwnPropertySymbols(e);a<n.length;a++)t.indexOf(n[a])<0&&Object.prototype.propertyIsEnumerable.call(e,n[a])&&(r[n[a]]=e[n[a]])}return r};Object.defineProperty(t,"__esModule",{value:!0});var l=r(2),o=r(315);t.default=function(e){var t=e.fill,r=void 0===t?"currentColor":t,c=a(e,["fill"]);return l.createElement(o.default,n({},c),l.createElement("path",{d:"M13.002 8.848v4.168a.56.56 0 0 1-.556.555H9.11v-3.334H6.89v3.334H3.554a.56.56 0 0 1-.556-.555V8.848c0-.018.01-.035.01-.052L8 4.68l4.993 4.116c.009.017.009.034.009.052zm1.936-.6l-.538.643a.289.289 0 0 1-.183.096h-.026a.273.273 0 0 1-.182-.061L8 3.916l-6.009 5.01a.297.297 0 0 1-.208.06.289.289 0 0 1-.183-.095l-.538-.642a.285.285 0 0 1 .035-.391L7.34 2.656a1.07 1.07 0 0 1 1.32 0l2.119 1.772V2.735c0-.157.121-.278.278-.278h1.667c.156 0 .278.121.278.278v3.542l1.901 1.58c.113.096.13.279.035.392z",style:{fill:r}}))}},708:function(e,t,r){"use strict";var n=this&&this.__assign||function(){return(n=Object.assign||function(e){for(var t,r=1,n=arguments.length;r<n;r++)for(var a in t=arguments[r])Object.prototype.hasOwnProperty.call(t,a)&&(e[a]=t[a]);return e}).apply(this,arguments)},a=this&&this.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(null!=e&&"function"==typeof Object.getOwnPropertySymbols){var a=0;for(n=Object.getOwnPropertySymbols(e);a<n.length;a++)t.indexOf(n[a])<0&&Object.prototype.propertyIsEnumerable.call(e,n[a])&&(r[n[a]]=e[n[a]])}return r};Object.defineProperty(t,"__esModule",{value:!0});var l=r(2),o=r(315);t.default=function(e){var t=e.fill,r=void 0===t?"currentColor":t,c=a(e,["fill"]);return l.createElement(o.default,n({},c),l.createElement("path",{d:"M12.557 4.545c.241.247.443.743.443 1.098v7.714c0 .355-.28.643-.625.643h-8.75A.634.634 0 0 1 3 13.357V2.643C3 2.288 3.28 2 3.625 2h5.833c.345 0 .827.208 1.068.455l2.031 2.09zM9.667 2.91v2.518h2.448a.86.86 0 0 0-.144-.275L9.934 3.058a.823.823 0 0 0-.267-.147zm2.5 10.232V6.286H9.458a.634.634 0 0 1-.625-.643V2.857h-5v10.286h8.334z",style:{fill:r}}))}},709:function(e,t,r){"use strict";var n=r(611);t.a=function(e){return/^(\/|scm:)/.test(e)||!!Object(n.isWebUri)(e)}},876:function(e,t,r){"use strict";r.d(t,"a",(function(){return s}));var n=r(2),a=r(312),l=r(704),o=r.n(l),c=r(589),i=r(709);class s extends n.PureComponent{constructor(){super(...arguments),this.state={expanded:!1},this.handleClick=e=>{e.preventDefault(),this.setState(({expanded:e})=>({expanded:!e}))},this.handleCollapse=()=>{this.setState({expanded:!1})},this.handleSelect=e=>{e.currentTarget.select()}}render(){const{iconOnly:e,link:t}=this.props,r=Object(c.a)(t);return n.createElement("li",null,n.createElement("a",{className:"link-with-icon",href:t.url,onClick:Object(i.a)(t.url)?void 0:this.handleClick,rel:"nofollow noreferrer noopener",target:"_blank",title:r},n.createElement(o.a,{className:"little-spacer-right",type:t.type}),!e&&r),this.state.expanded&&n.createElement("div",{className:"little-spacer-top display-flex-center"},n.createElement("input",{className:"overview-key width-80",onClick:this.handleSelect,readOnly:!0,type:"text",value:t.url}),n.createElement(a.ClearButton,{className:"little-spacer-left",onClick:this.handleCollapse})))}}}}]);
//# sourceMappingURL=300.m.8d6ae5b7.chunk.js.map