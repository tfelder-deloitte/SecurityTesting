(window.webpackJsonp=window.webpackJsonp||[]).push([[336,23,362],{1844:function(e,t,n){"use strict";n.r(t);var r=n(2),a=n(321),s=n(323),o=n(420),i=n(525);t.default=Object(a.connect)(e=>({adminPages:Object(s.getAppState)(e).adminPages}))((function(e){const{extensionKey:t,pluginKey:n}=e.params,a=(e.adminPages||[]).find(e=>e.key==="".concat(n,"/").concat(t));return a?r.createElement(i.a,{extension:a}):r.createElement(o.default,{withContainer:!1})}))},402:function(e,t,n){"use strict";n.r(t),n.d(t,"default",(function(){return c}));var r=n(2),a=n(484),s=n.n(a),o=n(336),i=n(508);function c({children:e}){return r.createElement("div",{className:"global-container"},r.createElement("div",{className:"page-wrapper",id:"container"},r.createElement(s.a,{className:"navbar-global",height:o.rawSizes.globalNavHeightRaw}),e),r.createElement(i.a,null))}},414:function(e,t,n){"use strict";n.d(t,"b",(function(){return o})),n.d(t,"a",(function(){return i}));var r=n(338),a=n(17);let s=!1;function o(e,t="body"){return new Promise(n=>{const a=document.createElement("script");a.src="".concat(Object(r.getBaseUrl)()).concat(e),a.onload=n,document.getElementsByTagName(t)[0].appendChild(a)})}async function i(e){const t=Object(a.a)(e);if(t)return Promise.resolve(t);if(!s){(0,(await Promise.all([n.e(0),n.e(1),n.e(2),n.e(15),n.e(285)]).then(n.bind(null,550))).default)(),s=!0}await o("/static/".concat(e,".js"));const r=Object(a.a)(e);return r||Promise.reject()}},420:function(e,t,n){"use strict";n.r(t),n.d(t,"default",(function(){return c}));var r=n(2),a=n(332),s=n(317),o=n(31),i=n(402);function c({withContainer:e=!0}){const t=e?i.default:r.Fragment;return r.createElement(t,null,r.createElement(a.a,{defaultTitle:Object(o.translate)("404_not_found"),defer:!1}),r.createElement("div",{className:"page-wrapper-simple",id:"bd"},r.createElement("div",{className:"page-simple",id:"nonav"},r.createElement("h2",{className:"big-spacer-bottom"},Object(o.translate)("page_not_found")),r.createElement("p",{className:"spacer-bottom"},Object(o.translate)("address_mistyped_or_page_moved")),r.createElement("p",null,r.createElement(s.Link,{to:"/"},Object(o.translate)("go_back_to_homepage"))))))}},525:function(e,t,n){"use strict";var r=n(2),a=n(332),s=n(319),o=n(321),i=n(31),c=n(359),l=n(414),p=n(18),u=n(6),m=n(387),d=n(323),b=n(336),h=n(386);function f(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function O(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}class g extends r.PureComponent{constructor(){super(...arguments),this.state={},this.handleStart=e=>{const t=e(function(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?f(Object(n),!0).forEach((function(t){O(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):f(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}({store:Object(h.default)(),el:this.container,currentUser:this.props.currentUser,intl:this.props.intl,location:this.props.location,router:this.props.router,theme:b,baseUrl:Object(u.getBaseUrl)(),l10nBundle:Object(p.a)()},this.props.options));t&&(r.isValidElement(t)?this.setState({extensionElement:t}):"function"==typeof t&&(this.stop=t))},this.handleFailure=()=>{this.props.onFail(Object(i.translate)("page_extension_failed"))}}componentDidMount(){this.startExtension()}componentDidUpdate(e){e.extension!==this.props.extension?(this.stopExtension(),this.startExtension()):e.location!==this.props.location&&this.startExtension()}componentWillUnmount(){this.stopExtension()}startExtension(){Object(l.a)(this.props.extension.key).then(this.handleStart,this.handleFailure)}stopExtension(){this.stop?(this.stop(),this.stop=void 0):this.setState({extensionElement:void 0})}render(){return r.createElement("div",null,r.createElement(a.a,{title:this.props.extension.name}),this.state.extensionElement?this.state.extensionElement:r.createElement("div",{ref:e=>this.container=e}))}}const j={onFail:m.a};t.a=Object(s.injectIntl)(Object(c.a)(Object(o.connect)(e=>({currentUser:Object(d.getCurrentUser)(e)}),j)(g)))}}]);
//# sourceMappingURL=336.m.b029b1f1.chunk.js.map