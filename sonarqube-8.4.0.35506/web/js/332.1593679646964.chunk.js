(window.webpackJsonp=window.webpackJsonp||[]).push([[332],{1034:function(e,t,n){"use strict";n.d(t,"f",(function(){return a})),n.d(t,"d",(function(){return s})),n.d(t,"a",(function(){return c})),n.d(t,"e",(function(){return l})),n.d(t,"b",(function(){return i})),n.d(t,"g",(function(){return u})),n.d(t,"c",(function(){return p}));var r=n(49),o=n(677);function a(e){return Object(r.getJSON)("/api/user_groups/search",e).catch(o.a)}function s(e){return Object(r.getJSON)("/api/user_groups/users",e).catch(o.a)}function c(e){return Object(r.post)("/api/user_groups/add_user",e).catch(o.a)}function l(e){return Object(r.post)("/api/user_groups/remove_user",e).catch(o.a)}function i(e){return Object(r.postJSON)("/api/user_groups/create",e).then((function(e){return e.group}),o.a)}function u(e){return Object(r.post)("/api/user_groups/update",e).catch(o.a)}function p(e){return Object(r.post)("/api/user_groups/delete",e).catch(o.a)}},1450:function(e,t,n){"use strict";var r,o=n(13),a=n(661),s=n(667),c=n.n(s),l=n(176),i=n(966),u=n(668),p=n(723),m=n.n(p),d=n(666),h=n.n(d),f=n(705),g=n.n(f),E=n(1162),v=n.n(E),y=n(744),b=(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)}),_=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={loading:!1,showConfirmation:!1},t.handleClick=function(){t.state.showConfirmation?t.handleRevoke().then((function(){t.mounted&&t.setState({showConfirmation:!1})})):t.setState({showConfirmation:!0})},t.handleRevoke=function(){return t.setState({loading:!0}),Object(i.c)({login:t.props.login,name:t.props.token.name}).then((function(){return t.props.onRevokeToken(t.props.token)}),(function(){t.mounted&&t.setState({loading:!1})}))},t}return b(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=this.props,t=e.deleteConfirmation,n=e.token,r=this.state,s=r.loading,i=r.showConfirmation;return o.createElement("tr",null,o.createElement("td",null,o.createElement(h.a,{overlay:n.name},o.createElement("span",null,Object(y.limitComponentName)(n.name)))),o.createElement("td",{className:"nowrap"},o.createElement(v.a,{date:n.lastConnectionDate})),o.createElement("td",{className:"thin nowrap text-right"},o.createElement(g.a,{date:n.createdAt,long:!0})),o.createElement("td",{className:"thin nowrap text-right"},o.createElement(c.a,{loading:s},o.createElement("i",{className:"deferred-spinner-placeholder"})),"modal"===t?o.createElement(m.a,{confirmButtonText:Object(l.translate)("users.tokens.revoke_token"),isDestructive:!0,modalBody:o.createElement(u.FormattedMessage,{defaultMessage:Object(l.translate)("users.tokens.sure_X"),id:"users.tokens.sure_X",values:{token:o.createElement("strong",null,n.name)}}),modalHeader:Object(l.translate)("users.tokens.revoke_token"),onConfirm:this.handleRevoke},(function(e){var t=e.onClick;return o.createElement(a.Button,{className:"spacer-left button-red input-small",disabled:s,onClick:t,title:Object(l.translate)("users.tokens.revoke_token")},Object(l.translate)("users.tokens.revoke"))})):o.createElement(a.Button,{className:"button-red input-small spacer-left",disabled:s,onClick:this.handleClick},i?Object(l.translate)("users.tokens.sure"):Object(l.translate)("users.tokens.revoke"))))},t}(o.PureComponent),O=n(721),w=n(674);function C(e){var t=e.token;return o.createElement("div",{className:"panel panel-white big-spacer-top"},o.createElement(w.Alert,{variant:"warning"},Object(l.translateWithParameters)("users.tokens.new_token_created",t.name)),o.createElement(O.ClipboardButton,{copyValue:t.token}),o.createElement("code",{className:"big-spacer-left text-success"},t.token))}var j=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),k=function(){for(var e=0,t=0,n=arguments.length;t<n;t++)e+=arguments[t].length;var r=Array(e),o=0;for(t=0;t<n;t++)for(var a=arguments[t],s=0,c=a.length;s<c;s++,o++)r[o]=a[s];return r},N=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={generating:!1,loading:!0,newTokenName:"",tokens:[]},t.fetchTokens=function(){t.setState({loading:!0}),Object(i.b)(t.props.login).then((function(e){t.mounted&&t.setState({loading:!1,tokens:e})}),(function(){t.mounted&&t.setState({loading:!1})}))},t.updateTokensCount=function(){t.props.updateTokensCount&&t.props.updateTokensCount(t.props.login,t.state.tokens.length)},t.handleGenerateToken=function(e){e.preventDefault(),t.state.newTokenName.length>0&&(t.setState({generating:!0}),Object(i.a)({name:t.state.newTokenName,login:t.props.login}).then((function(e){t.mounted&&t.setState((function(t){var n=k(t.tokens,[{name:e.name,createdAt:e.createdAt}]);return{generating:!1,newToken:e,newTokenName:"",tokens:n}}),t.updateTokensCount)}),(function(){t.mounted&&t.setState({generating:!1})})))},t.handleRevokeToken=function(e){t.setState((function(t){return{tokens:t.tokens.filter((function(t){return t.name!==e.name}))}}),t.updateTokensCount)},t.handleNewTokenChange=function(e){return t.setState({newTokenName:e.currentTarget.value})},t}return j(t,e),t.prototype.componentDidMount=function(){this.mounted=!0,this.fetchTokens()},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.renderItems=function(){var e=this,t=this.state.tokens;return t.length<=0?o.createElement("tr",null,o.createElement("td",{className:"note",colSpan:3},Object(l.translate)("users.no_tokens"))):t.map((function(t){return o.createElement(_,{deleteConfirmation:e.props.deleteConfirmation,key:t.name,login:e.props.login,onRevokeToken:e.handleRevokeToken,token:t})}))},t.prototype.render=function(){var e=this.state,t=e.generating,n=e.loading,r=e.newToken,s=e.newTokenName,i=e.tokens,u=o.createElement("tr",null,o.createElement("td",null,o.createElement("i",{className:"spinner"})));return o.createElement(o.Fragment,null,o.createElement("h3",{className:"spacer-bottom"},Object(l.translate)("users.generate_tokens")),o.createElement("form",{autoComplete:"off",className:"display-flex-center",id:"generate-token-form",onSubmit:this.handleGenerateToken},o.createElement("input",{className:"input-large spacer-right",maxLength:100,onChange:this.handleNewTokenChange,placeholder:Object(l.translate)("users.enter_token_name"),required:!0,type:"text",value:s}),o.createElement(a.SubmitButton,{className:"js-generate-token",disabled:t||s.length<=0},Object(l.translate)("users.generate"))),r&&o.createElement(C,{token:r}),o.createElement("table",{className:"data zebra big-spacer-top"},o.createElement("thead",null,o.createElement("tr",null,o.createElement("th",null,Object(l.translate)("name")),o.createElement("th",null,Object(l.translate)("my_account.tokens_last_usage")),o.createElement("th",{className:"text-right"},Object(l.translate)("created")),o.createElement("th",null))),o.createElement("tbody",null,o.createElement(c.a,{customSpinner:u,loading:n&&i.length<=0},this.renderItems()))))},t}(o.PureComponent);t.a=N},2225:function(e,t,n){"use strict";n.r(t);var r,o=n(670),a=n(672),s=n(13),c=n(681),l=n(696),i=n.n(l),u=n(176),p=n(783),m=n(682),d=n(708),h=n(661),f=n(667),g=n.n(f),E=n(774),v=n.n(E),y=n(684),b=n.n(y),_=n(674),O=n(49),w=n(677),C=(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)}),j=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.handleChange=function(e){return t.props.onChange(t.props.idx,e.currentTarget.value)},t.handleRemove=function(){return t.props.onRemove(t.props.idx)},t}return C(t,e),t.prototype.render=function(){return s.createElement("div",{className:"js-scm-account display-flex-row spacer-bottom"},s.createElement("input",{maxLength:255,onChange:this.handleChange,type:"text",value:this.props.scmAccount}),s.createElement(h.DeleteButton,{onClick:this.handleRemove}))},t}(s.PureComponent),k=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),N=function(e){function t(t){var n=e.call(this,t)||this;n.mounted=!1,n.handleError=function(e){return n.mounted&&[400,500].includes(e.status)?Object(O.parseError)(e).then((function(e){return n.setState({error:e})}),w.a):Object(w.a)(e)},n.handleEmailChange=function(e){return n.setState({email:e.currentTarget.value})},n.handleLoginChange=function(e){return n.setState({login:e.currentTarget.value})},n.handleNameChange=function(e){return n.setState({name:e.currentTarget.value})},n.handlePasswordChange=function(e){return n.setState({password:e.currentTarget.value})},n.handleCreateUser=function(){return Object(p.b)({email:n.state.email||void 0,login:n.state.login,name:n.state.name,password:n.state.password,scmAccount:v()(n.state.scmAccounts)}).then((function(){n.props.onUpdateUsers(),n.props.onClose()}),n.handleError)},n.handleUpdateUser=function(){var e=n.props.user;return Object(p.j)({email:e.local?n.state.email:void 0,login:n.state.login,name:e.local?n.state.name:void 0,scmAccount:v()(n.state.scmAccounts)}).then((function(){n.props.onUpdateUsers(),n.props.onClose()}),n.handleError)},n.handleAddScmAccount=function(){n.setState((function(e){return{scmAccounts:e.scmAccounts.concat("")}}))},n.handleUpdateScmAccount=function(e,t){return n.setState((function(n){var r=n.scmAccounts.slice();return r[e]=t,{scmAccounts:r}}))},n.handleRemoveScmAccount=function(e){return n.setState((function(t){var n=t.scmAccounts;return{scmAccounts:n.slice(0,e).concat(n.slice(e+1))}}))};var r=t.user;return n.state=r?{email:r.email||"",login:r.login,name:r.name||"",password:"",scmAccounts:r.scmAccounts||[]}:{email:"",login:"",name:"",password:"",scmAccounts:[]},n}return k(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=this,t=this.props.user,n=this.state.error,r=t?Object(u.translate)("users.update_user"):Object(u.translate)("users.create_user");return s.createElement(b.a,{header:r,onClose:this.props.onClose,onSubmit:t?this.handleUpdateUser:this.handleCreateUser,size:"small"},(function(o){var a=o.onCloseClick,c=o.onFormSubmit,l=o.submitting;return s.createElement("form",{autoComplete:"off",id:"user-form",onSubmit:c},s.createElement("header",{className:"modal-head"},s.createElement("h2",null,r)),s.createElement("div",{className:"modal-body modal-container"},n&&s.createElement(_.Alert,{variant:"error"},n),!n&&t&&!t.local&&s.createElement(_.Alert,{variant:"warning"},Object(u.translate)("users.cannot_update_delegated_user")),!t&&s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"create-user-login"},Object(u.translate)("login"),s.createElement("em",{className:"mandatory"},"*")),s.createElement("input",{className:"hidden",name:"login-fake",type:"text"}),s.createElement("input",{autoFocus:!0,id:"create-user-login",maxLength:255,minLength:3,name:"login",onChange:e.handleLoginChange,required:!0,type:"text",value:e.state.login}),s.createElement("p",{className:"note"},Object(u.translateWithParameters)("users.minimum_x_characters",3))),s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"create-user-name"},Object(u.translate)("name"),s.createElement("em",{className:"mandatory"},"*")),s.createElement("input",{className:"hidden",name:"name-fake",type:"text"}),s.createElement("input",{autoFocus:!!t,disabled:t&&!t.local,id:"create-user-name",maxLength:200,name:"name",onChange:e.handleNameChange,required:!0,type:"text",value:e.state.name})),s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"create-user-email"},Object(u.translate)("users.email")),s.createElement("input",{className:"hidden",name:"email-fake",type:"email"}),s.createElement("input",{disabled:t&&!t.local,id:"create-user-email",maxLength:100,name:"email",onChange:e.handleEmailChange,type:"email",value:e.state.email})),!t&&s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"create-user-password"},Object(u.translate)("password"),s.createElement("em",{className:"mandatory"},"*")),s.createElement("input",{className:"hidden",name:"password-fake",type:"password"}),s.createElement("input",{id:"create-user-password",maxLength:50,name:"password",onChange:e.handlePasswordChange,required:!0,type:"password",value:e.state.password})),s.createElement("div",{className:"modal-field"},s.createElement("label",null,Object(u.translate)("my_profile.scm_accounts")),e.state.scmAccounts.map((function(t,n){return s.createElement(j,{idx:n,key:n,onChange:e.handleUpdateScmAccount,onRemove:e.handleRemoveScmAccount,scmAccount:t})})),s.createElement("div",{className:"spacer-bottom"},s.createElement(h.Button,{className:"js-scm-account-add",onClick:e.handleAddScmAccount},Object(u.translate)("add_verb"))),s.createElement("p",{className:"note"},Object(u.translate)("user.login_or_email_used_as_scm_account")))),s.createElement("footer",{className:"modal-foot"},l&&s.createElement("i",{className:"spinner spacer-right"}),s.createElement(h.SubmitButton,{disabled:l},t?Object(u.translate)("update_verb"):Object(u.translate)("create")),s.createElement(h.ResetButtonLink,{onClick:a},Object(u.translate)("cancel"))))}))},t}(s.PureComponent),P=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),S=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.state={openUserForm:!1},t.handleOpenUserForm=function(){t.setState({openUserForm:!0})},t.handleCloseUserForm=function(){t.setState({openUserForm:!1})},t}return P(t,e),t.prototype.render=function(){return s.createElement("header",{className:"page-header",id:"users-header"},s.createElement("h1",{className:"page-title"},Object(u.translate)("users.page")),s.createElement(g.a,{loading:this.props.loading}),s.createElement("div",{className:"page-actions"},s.createElement(h.Button,{id:"users-create",onClick:this.handleOpenUserForm},Object(u.translate)("users.create_user"))),s.createElement("p",{className:"page-description"},Object(u.translate)("users.page.description")),this.state.openUserForm&&s.createElement(N,{onClose:this.handleCloseUserForm,onUpdateUsers:this.props.onUpdateUsers}))},t}(s.PureComponent),U=n(699),T=n.n(U),A=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),F=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.handleSearch=function(e){t.props.updateQuery({search:e})},t}return A(t,e),t.prototype.render=function(){var e=this.props.query;return s.createElement("div",{className:"panel panel-vertical bordered-bottom spacer-bottom",id:"users-search"},s.createElement(T.a,{minLength:2,onChange:this.handleSearch,placeholder:Object(u.translate)("search.search_by_login_or_name"),value:e.search}))},t}(s.PureComponent),x=n(937),D=n.n(x),M=n(1162),R=n.n(M),q=n(726),L=n(668),B=n(678),z=n.n(B),W=n(1450);function I(e){return s.createElement(z.a,{contentLabel:Object(u.translate)("users.tokens"),onRequestClose:e.onClose},s.createElement("header",{className:"modal-head"},s.createElement("h2",null,s.createElement(L.FormattedMessage,{defaultMessage:Object(u.translate)("users.user_X_tokens"),id:"users.user_X_tokens",values:{user:s.createElement("em",null,e.user.name)}}))),s.createElement("div",{className:"modal-body modal-container"},s.createElement(W.a,{deleteConfirmation:"inline",login:e.user.login,updateTokensCount:e.updateTokensCount})),s.createElement("footer",{className:"modal-foot"},s.createElement(h.ResetButtonLink,{onClick:e.onClose},Object(u.translate)("Done"))))}var G=n(729),J=n.n(G),Q=n(686),X=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),H=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={submitting:!1},t.handleDeactivate=function(e){e.preventDefault(),t.setState({submitting:!0}),Object(p.c)({login:t.props.user.login}).then((function(){t.props.onUpdateUsers(),t.props.onClose()}),(function(){t.mounted&&t.setState({submitting:!1})}))},t}return X(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=this.props.user,t=this.state.submitting,n=Object(u.translate)("users.deactivate_user");return s.createElement(z.a,{contentLabel:n,onRequestClose:this.props.onClose},s.createElement("form",{autoComplete:"off",id:"deactivate-user-form",onSubmit:this.handleDeactivate},s.createElement("header",{className:"modal-head"},s.createElement("h2",null,n)),s.createElement("div",{className:"modal-body"},Object(u.translateWithParameters)("users.deactivate_user.confirmation",e.name,e.login)),s.createElement("footer",{className:"modal-foot"},t&&s.createElement("i",{className:"spinner spacer-right"}),s.createElement(h.SubmitButton,{className:"js-confirm button-red",disabled:t},Object(u.translate)("users.deactivate")),s.createElement(h.ResetButtonLink,{className:"js-modal-close",onClick:this.props.onClose},Object(u.translate)("cancel")))))},t}(s.PureComponent),V=n(769),K=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),Y=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={confirmPassword:"",newPassword:"",oldPassword:"",submitting:!1},t.handleError=function(e){return t.mounted&&400===e.status?Object(O.parseError)(e).then((function(e){return t.setState({error:e,submitting:!1})}),w.a):Object(w.a)(e)},t.handleConfirmPasswordChange=function(e){return t.setState({confirmPassword:e.currentTarget.value})},t.handleNewPasswordChange=function(e){return t.setState({newPassword:e.currentTarget.value})},t.handleOldPasswordChange=function(e){return t.setState({oldPassword:e.currentTarget.value})},t.handleChangePassword=function(e){e.preventDefault(),t.state.newPassword.length>0&&t.state.newPassword===t.state.confirmPassword&&(t.setState({submitting:!0}),Object(p.a)({login:t.props.user.login,password:t.state.newPassword,previousPassword:t.state.oldPassword}).then((function(){Object(V.a)(Object(u.translate)("my_profile.password.changed")),t.props.onClose()}),t.handleError))},t}return K(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=this.state,t=e.error,n=e.submitting,r=e.newPassword,o=e.confirmPassword,a=Object(u.translate)("my_profile.password.title");return s.createElement(z.a,{contentLabel:a,onRequestClose:this.props.onClose,size:"small"},s.createElement("form",{autoComplete:"off",id:"user-password-form",onSubmit:this.handleChangePassword},s.createElement("header",{className:"modal-head"},s.createElement("h2",null,a)),s.createElement("div",{className:"modal-body"},t&&s.createElement(_.Alert,{variant:"error"},t),this.props.isCurrentUser&&s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"old-user-password"},Object(u.translate)("my_profile.password.old"),s.createElement("em",{className:"mandatory"},"*")),s.createElement("input",{className:"hidden",name:"old-password-fake",type:"password"}),s.createElement("input",{id:"old-user-password",maxLength:50,name:"old-password",onChange:this.handleOldPasswordChange,required:!0,type:"password",value:this.state.oldPassword})),s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"user-password"},Object(u.translate)("my_profile.password.new"),s.createElement("em",{className:"mandatory"},"*")),s.createElement("input",{className:"hidden",name:"password-fake",type:"password"}),s.createElement("input",{id:"user-password",maxLength:50,name:"password",onChange:this.handleNewPasswordChange,required:!0,type:"password",value:this.state.newPassword})),s.createElement("div",{className:"modal-field"},s.createElement("label",{htmlFor:"confirm-user-password"},Object(u.translate)("my_profile.password.confirm"),s.createElement("em",{className:"mandatory"},"*")),s.createElement("input",{className:"hidden",name:"confirm-password-fake",type:"password"}),s.createElement("input",{id:"confirm-user-password",maxLength:50,name:"confirm-password",onChange:this.handleConfirmPasswordChange,required:!0,type:"password",value:this.state.confirmPassword}))),s.createElement("footer",{className:"modal-foot"},n&&s.createElement("i",{className:"spinner spacer-right"}),s.createElement(h.SubmitButton,{disabled:n||!r||r!==o},Object(u.translate)("change_verb")),s.createElement(h.ResetButtonLink,{onClick:this.props.onClose},Object(u.translate)("cancel")))))},t}(s.PureComponent),Z=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),$=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.state={},t.handleOpenDeactivateForm=function(){t.setState({openForm:"deactivate"})},t.handleOpenPasswordForm=function(){t.setState({openForm:"password"})},t.handleOpenUpdateForm=function(){t.setState({openForm:"update"})},t.handleCloseForm=function(){t.setState({openForm:void 0})},t.renderActions=function(){var e=t.props.user;return s.createElement(J.a,null,s.createElement(G.ActionsDropdownItem,{className:"js-user-update",onClick:t.handleOpenUpdateForm},Object(u.translate)("update_details")),e.local&&s.createElement(G.ActionsDropdownItem,{className:"js-user-change-password",onClick:t.handleOpenPasswordForm},Object(u.translate)("my_profile.password.title")),s.createElement(G.ActionsDropdownDivider,null),Object(Q.c)(e)&&s.createElement(G.ActionsDropdownItem,{className:"js-user-deactivate",destructive:!0,onClick:t.handleOpenDeactivateForm},Object(u.translate)("users.deactivate")))},t}return Z(t,e),t.prototype.render=function(){var e=this.state.openForm,t=this.props,n=t.isCurrentUser,r=t.onUpdateUsers,o=t.user;return s.createElement(s.Fragment,null,this.renderActions(),"deactivate"===e&&Object(Q.c)(o)&&s.createElement(H,{onClose:this.handleCloseForm,onUpdateUsers:r,user:o}),"password"===e&&s.createElement(Y,{isCurrentUser:n,onClose:this.handleCloseForm,user:o}),"update"===e&&s.createElement(N,{onClose:this.handleCloseForm,onUpdateUsers:r,user:o}))},t}(s.PureComponent),ee=n(719),te=n.n(ee),ne=n(850),re=n.n(ne),oe=n(828),ae=n.n(oe),se=n(1034),ce=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),le=function(){for(var e=0,t=0,n=arguments.length;t<n;t++)e+=arguments[t].length;var r=Array(e),o=0;for(t=0;t<n;t++)for(var a=arguments[t],s=0,c=a.length;s<c;s++,o++)r[o]=a[s];return r},ie=function(e){function t(t){var n=e.call(this,t)||this;return n.mounted=!1,n.fetchUsers=function(e){return Object(p.e)({login:n.props.user.login,organization:void 0,p:e.page,ps:e.pageSize,q:""!==e.query?e.query:void 0,selected:e.filter}).then((function(t){n.mounted&&n.setState((function(n){var r=null!=e.page&&e.page>1,o=r?le(n.groups,t.groups):t.groups,a=t.groups.filter((function(e){return e.selected})).map((function(e){return e.name})),s=r?le(n.selectedGroups,a):a;return{lastSearchParams:e,needToReload:!1,groups:o,groupsTotalCount:t.paging.total,selectedGroups:s}}))}))},n.handleSelect=function(e){return Object(se.a)({name:e,login:n.props.user.login}).then((function(){n.mounted&&n.setState((function(t){return{needToReload:!0,selectedGroups:le(t.selectedGroups,[e])}}))}))},n.handleUnselect=function(e){return Object(se.e)({name:e,login:n.props.user.login}).then((function(){n.mounted&&n.setState((function(t){return{needToReload:!0,selectedGroups:te()(t.selectedGroups,e)}}))}))},n.handleCloseClick=function(e){e.preventDefault(),n.handleClose()},n.handleClose=function(){n.props.onUpdateUsers(),n.props.onClose()},n.renderElement=function(e){var t=re()(n.state.groups,{name:e});return s.createElement("div",{className:"select-list-list-item"},void 0===t?e:s.createElement(s.Fragment,null,t.name,s.createElement("br",null),s.createElement("span",{className:"note"},t.description)))},n.state={needToReload:!1,groups:[],selectedGroups:[]},n}return ce(t,e),t.prototype.componentDidMount=function(){this.mounted=!0},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=Object(u.translate)("users.update_groups");return s.createElement(z.a,{contentLabel:e,onRequestClose:this.handleClose},s.createElement("div",{className:"modal-head"},s.createElement("h2",null,e)),s.createElement("div",{className:"modal-body modal-container"},s.createElement(ae.a,{elements:this.state.groups.map((function(e){return e.name})),elementsTotalCount:this.state.groupsTotalCount,needToReload:this.state.needToReload&&this.state.lastSearchParams&&this.state.lastSearchParams.filter!==oe.SelectListFilter.All,onSearch:this.fetchUsers,onSelect:this.handleSelect,onUnselect:this.handleUnselect,renderElement:this.renderElement,selectedElements:this.state.selectedGroups,withPaging:!0})),s.createElement("footer",{className:"modal-foot"},s.createElement("a",{className:"js-modal-close",href:"#",onClick:this.handleCloseClick},Object(u.translate)("Done"))))},t}(s.PureComponent),ue=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),pe=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.state={openForm:!1,showMore:!1},t.handleOpenForm=function(){return t.setState({openForm:!0})},t.handleCloseForm=function(){return t.setState({openForm:!1})},t.toggleShowMore=function(e){e.preventDefault(),t.setState((function(e){return{showMore:!e.showMore}}))},t}return ue(t,e),t.prototype.render=function(){var e=this.props.groups,t=e.length>3?2:3;return s.createElement("ul",null,e.slice(0,t).map((function(e){return s.createElement("li",{className:"little-spacer-bottom",key:e},e)})),e.length>3&&this.state.showMore&&e.slice(t).map((function(e){return s.createElement("li",{className:"little-spacer-bottom",key:e},e)})),s.createElement("li",{className:"little-spacer-bottom"},e.length>3&&!this.state.showMore&&s.createElement("a",{className:"js-user-more-groups spacer-right",href:"#",onClick:this.toggleShowMore},Object(u.translateWithParameters)("more_x",e.length-t)),s.createElement(h.ButtonIcon,{className:"js-user-groups button-small",onClick:this.handleOpenForm,tooltip:Object(u.translate)("users.update_groups")},s.createElement(D.a,null))),this.state.openForm&&s.createElement(ie,{onClose:this.handleCloseForm,onUpdateUsers:this.props.onUpdateUsers,user:this.props.user}))},t}(s.PureComponent),me=n(1016),de=n(687),he=n(685);function fe(e){var t=e.identityProvider,n=e.user;return s.createElement("td",{className:"text-middle"},s.createElement("div",null,s.createElement("strong",{className:"js-user-name"},n.name),s.createElement("span",{className:"js-user-login note little-spacer-left"},n.login)),n.email&&s.createElement("div",{className:"js-user-email little-spacer-top"},n.email),!n.local&&"sonarqube"!==n.externalProvider&&s.createElement(ge,{identityProvider:t,user:n}))}function ge(e){var t=e.identityProvider,n=e.user;return t?s.createElement("div",{className:"js-user-identity-provider little-spacer-top"},s.createElement("div",{className:"identity-provider",style:{backgroundColor:t.backgroundColor,color:Object(me.getTextColor)(t.backgroundColor,he.colors.secondFontColor)}},s.createElement("img",{alt:t.name,className:"little-spacer-right",height:"14",src:Object(de.getBaseUrl)()+t.iconPath,width:"14"}),n.externalIdentity)):s.createElement("div",{className:"js-user-identity-provider little-spacer-top"},s.createElement("span",null,n.externalProvider,": ",n.externalIdentity))}var Ee=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),ve=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.state={showMore:!1},t.toggleShowMore=function(e){e.preventDefault(),t.setState((function(e){return{showMore:!e.showMore}}))},t}return Ee(t,e),t.prototype.render=function(){var e=this.props.scmAccounts,t=e.length>3?2:3;return s.createElement("ul",{className:"js-scm-accounts"},e.slice(0,t).map((function(e,t){return s.createElement("li",{className:"little-spacer-bottom",key:t},e)})),e.length>3&&(this.state.showMore?e.slice(t).map((function(e,n){return s.createElement("li",{className:"little-spacer-bottom",key:n+t},e)})):s.createElement("li",{className:"little-spacer-bottom"},s.createElement("a",{className:"js-user-more-scm",href:"#",onClick:this.toggleShowMore},Object(u.translateWithParameters)("more_x",e.length-t)))))},t}(s.PureComponent),ye=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),be=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.state={openTokenForm:!1},t.handleOpenTokensForm=function(){return t.setState({openTokenForm:!0})},t.handleCloseTokensForm=function(){return t.setState({openTokenForm:!1})},t}return ye(t,e),t.prototype.render=function(){var e=this.props,t=e.identityProvider,n=e.onUpdateUsers,r=e.organizationsEnabled,o=e.user;return s.createElement("tr",null,s.createElement("td",{className:"thin nowrap text-middle"},s.createElement(q.a,{hash:o.avatar,name:o.name,size:36})),s.createElement(fe,{identityProvider:t,user:o}),s.createElement("td",{className:"thin nowrap text-middle"},s.createElement(ve,{scmAccounts:o.scmAccounts||[]})),s.createElement("td",{className:"thin nowrap text-middle"},s.createElement(R.a,{date:o.lastConnectionDate})),!r&&s.createElement("td",{className:"thin nowrap text-middle"},s.createElement(pe,{groups:o.groups||[],onUpdateUsers:n,user:o})),s.createElement("td",{className:"thin nowrap text-middle"},o.tokensCount,s.createElement(h.ButtonIcon,{className:"js-user-tokens spacer-left button-small",onClick:this.handleOpenTokensForm,tooltip:Object(u.translate)("users.update_tokens")},s.createElement(D.a,null))),s.createElement("td",{className:"thin nowrap text-right text-middle"},s.createElement($,{isCurrentUser:this.props.isCurrentUser,onUpdateUsers:n,user:o})),this.state.openTokenForm&&s.createElement(I,{onClose:this.handleCloseTokensForm,updateTokensCount:this.props.updateTokensCount,user:o}))},t}(s.PureComponent);function _e(e){var t=e.currentUser,n=e.identityProviders,r=e.onUpdateUsers,o=e.organizationsEnabled,a=e.updateTokensCount,c=e.users;return s.createElement("div",{className:"boxed-group boxed-group-inner"},s.createElement("table",{className:"data zebra",id:"users-list"},s.createElement("thead",null,s.createElement("tr",null,s.createElement("th",null),s.createElement("th",{className:"nowrap"}),s.createElement("th",{className:"nowrap"},Object(u.translate)("my_profile.scm_accounts")),s.createElement("th",{className:"nowrap"},Object(u.translate)("users.last_connection")),!o&&s.createElement("th",{className:"nowrap"},Object(u.translate)("my_profile.groups")),s.createElement("th",{className:"nowrap"},Object(u.translate)("users.tokens")),s.createElement("th",{className:"nowrap"}," "))),s.createElement("tbody",null,c.map((function(e){return s.createElement(be,{identityProvider:n.find((function(t){return e.externalProvider===t.key})),isCurrentUser:t.isLoggedIn&&t.login===e.login,key:e.login,onUpdateUsers:r,organizationsEnabled:o,updateTokensCount:a,user:e})})))))}var Oe=n(867),we=n.n(Oe),Ce=n(794),je=we()((function(e){return{search:Object(Ce.parseAsString)(e.search)}})),ke=we()((function(e){return Object(Ce.cleanQuery)({search:e.search?Object(Ce.serializeString)(e.search):void 0})})),Ne=function(){var e=function(t,n){return(e=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(t,n)};return function(t,n){function r(){this.constructor=t}e(t,n),t.prototype=null===n?Object.create(n):(r.prototype=n.prototype,new r)}}(),Pe=function(){return(Pe=Object.assign||function(e){for(var t,n=1,r=arguments.length;n<r;n++)for(var o in t=arguments[n])Object.prototype.hasOwnProperty.call(t,o)&&(e[o]=t[o]);return e}).apply(this,arguments)},Se=function(){for(var e=0,t=0,n=arguments.length;t<n;t++)e+=arguments[t].length;var r=Array(e),o=0;for(t=0;t<n;t++)for(var a=arguments[t],s=0,c=a.length;s<c;s++,o++)r[o]=a[s];return r},Ue=function(e){function t(){var t=null!==e&&e.apply(this,arguments)||this;return t.mounted=!1,t.state={identityProviders:[],loading:!0,users:[]},t.finishLoading=function(){t.mounted&&t.setState({loading:!1})},t.fetchIdentityProviders=function(){return Object(p.d)().then((function(e){var n=e.identityProviders;t.mounted&&t.setState({identityProviders:n})}),(function(){}))},t.fetchUsers=function(e){var n=(void 0===e?t.props:e).location;t.setState({loading:!0}),Object(p.f)({q:je(n.query).search}).then((function(e){var n=e.paging,r=e.users;t.mounted&&t.setState({loading:!1,paging:n,users:r})}),t.finishLoading)},t.fetchMoreUsers=function(){var e=t.state.paging;e&&(t.setState({loading:!0}),Object(p.f)({p:e.pageIndex+1,q:je(t.props.location.query).search}).then((function(e){var n=e.paging,r=e.users;t.mounted&&t.setState((function(e){return{loading:!1,users:Se(e.users,r),paging:n}}))}),t.finishLoading))},t.updateQuery=function(e){var n=ke(Pe(Pe({},je(t.props.location.query)),e));t.props.router.push(Pe(Pe({},t.props.location),{query:n}))},t.updateTokensCount=function(e,n){t.setState((function(t){return{users:t.users.map((function(t){return t.login===e?Pe(Pe({},t),{tokensCount:n}):t}))}}))},t}return Ne(t,e),t.prototype.componentDidMount=function(){this.mounted=!0,this.fetchIdentityProviders(),this.fetchUsers()},t.prototype.componentWillReceiveProps=function(e){e.location.query.search!==this.props.location.query.search&&this.fetchUsers(e)},t.prototype.componentWillUnmount=function(){this.mounted=!1},t.prototype.render=function(){var e=je(this.props.location.query),t=this.state,n=t.loading,r=t.paging,o=t.users;return s.createElement("div",{className:"page page-limited",id:"users-page"},s.createElement(m.a,{suggestions:"users"}),s.createElement(c.a,{defer:!1,title:Object(u.translate)("users.page")}),s.createElement(S,{loading:n,onUpdateUsers:this.fetchUsers}),s.createElement(F,{query:e,updateQuery:this.updateQuery}),s.createElement(_e,{currentUser:this.props.currentUser,identityProviders:this.state.identityProviders,onUpdateUsers:this.fetchUsers,organizationsEnabled:this.props.organizationsEnabled,updateTokensCount:this.updateTokensCount,users:o}),void 0!==r&&s.createElement(i.a,{count:o.length,loadMore:this.fetchMoreUsers,ready:!n,total:r.total}))},t}(s.PureComponent),Te=Object(d.a)(Ue);t.default=Object(o.connect)((function(e){return{currentUser:Object(a.getCurrentUser)(e),organizationsEnabled:Object(a.areThereCustomOrganizations)(e)}}))(Te)},682:function(e,t,n){"use strict";n.d(t,"a",(function(){return c}));var r,o=n(13),a=n(709),s=(r=function(e,t){return(r=Object.setPrototypeOf||{__proto__:[]}instanceof Array&&function(e,t){e.__proto__=t}||function(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n])})(e,t)},function(e,t){function n(){this.constructor=e}r(e,t),e.prototype=null===t?Object.create(t):(n.prototype=t.prototype,new n)});function c(e){var t=e.suggestions;return o.createElement(a.a.Consumer,null,(function(e){var n=e.addSuggestions,r=e.removeSuggestions;return o.createElement(l,{addSuggestions:n,removeSuggestions:r,suggestions:t})}))}var l=function(e){function t(){return null!==e&&e.apply(this,arguments)||this}return s(t,e),t.prototype.componentDidMount=function(){this.props.addSuggestions(this.props.suggestions)},t.prototype.componentDidUpdate=function(e){e.suggestions!==this.props.suggestions&&(this.props.removeSuggestions(this.props.suggestions),this.props.addSuggestions(e.suggestions))},t.prototype.componentWillUnmount=function(){this.props.removeSuggestions(this.props.suggestions)},t.prototype.render=function(){return null},t}(o.PureComponent)},769:function(e,t,n){"use strict";n.d(t,"a",(function(){return a}));var r=n(736),o=n(735);function a(e){Object(o.default)().dispatch(r.b(e))}},966:function(e,t,n){"use strict";n.d(t,"b",(function(){return a})),n.d(t,"a",(function(){return s})),n.d(t,"c",(function(){return c}));var r=n(49),o=n(677);function a(e){return Object(r.getJSON)("/api/user_tokens/search",{login:e}).then((function(e){return e.userTokens}),o.a)}function s(e){return Object(r.postJSON)("/api/user_tokens/generate",e).catch(o.a)}function c(e){return Object(r.post)("/api/user_tokens/revoke",e).catch(o.a)}}}]);
//# sourceMappingURL=332.1593679646964.chunk.js.map