(window.webpackJsonp=window.webpackJsonp||[]).push([[331],{1101:function(e,t,s){"use strict";s.d(t,"a",(function(){return O}));var n=s(2),a=s(312),r=s(318),o=s.n(r),l=s(31),i=s(618),c=s(319),m=s(375),d=s.n(m),u=s(316),h=s.n(u),p=s(356),g=s.n(p),E=s(816),b=s.n(E),f=s(396);class C extends n.PureComponent{constructor(){super(...arguments),this.mounted=!1,this.state={loading:!1,showConfirmation:!1},this.handleClick=()=>{this.state.showConfirmation?this.handleRevoke().then(()=>{this.mounted&&this.setState({showConfirmation:!1})}):this.setState({showConfirmation:!0})},this.handleRevoke=()=>(this.setState({loading:!0}),Object(i.c)({login:this.props.login,name:this.props.token.name}).then(()=>this.props.onRevokeToken(this.props.token),()=>{this.mounted&&this.setState({loading:!1})}))}componentDidMount(){this.mounted=!0}componentWillUnmount(){this.mounted=!1}render(){const{deleteConfirmation:e,token:t}=this.props,{loading:s,showConfirmation:r}=this.state;return n.createElement("tr",null,n.createElement("td",null,n.createElement(h.a,{overlay:t.name},n.createElement("span",null,Object(f.limitComponentName)(t.name)))),n.createElement("td",{className:"nowrap"},n.createElement(b.a,{date:t.lastConnectionDate})),n.createElement("td",{className:"thin nowrap text-right"},n.createElement(g.a,{date:t.createdAt,long:!0})),n.createElement("td",{className:"thin nowrap text-right"},n.createElement(o.a,{loading:s},n.createElement("i",{className:"deferred-spinner-placeholder"})),"modal"===e?n.createElement(d.a,{confirmButtonText:Object(l.translate)("users.tokens.revoke_token"),isDestructive:!0,modalBody:n.createElement(c.FormattedMessage,{defaultMessage:Object(l.translate)("users.tokens.sure_X"),id:"users.tokens.sure_X",values:{token:n.createElement("strong",null,t.name)}}),modalHeader:Object(l.translate)("users.tokens.revoke_token"),onConfirm:this.handleRevoke},({onClick:e})=>n.createElement(a.Button,{className:"spacer-left button-red input-small",disabled:s,onClick:e,title:Object(l.translate)("users.tokens.revoke_token")},Object(l.translate)("users.tokens.revoke"))):n.createElement(a.Button,{className:"button-red input-small spacer-left",disabled:s,onClick:this.handleClick},r?Object(l.translate)("users.tokens.sure"):Object(l.translate)("users.tokens.revoke"))))}}var v=s(373),k=s(325);function w({token:e}){return n.createElement("div",{className:"panel panel-white big-spacer-top"},n.createElement(k.Alert,{variant:"warning"},Object(l.translateWithParameters)("users.tokens.new_token_created",e.name)),n.createElement(v.ClipboardButton,{copyValue:e.token}),n.createElement("code",{className:"big-spacer-left text-success"},e.token))}class O extends n.PureComponent{constructor(){super(...arguments),this.mounted=!1,this.state={generating:!1,loading:!0,newTokenName:"",tokens:[]},this.fetchTokens=()=>{this.setState({loading:!0}),Object(i.b)(this.props.login).then(e=>{this.mounted&&this.setState({loading:!1,tokens:e})},()=>{this.mounted&&this.setState({loading:!1})})},this.updateTokensCount=()=>{this.props.updateTokensCount&&this.props.updateTokensCount(this.props.login,this.state.tokens.length)},this.handleGenerateToken=e=>{e.preventDefault(),this.state.newTokenName.length>0&&(this.setState({generating:!0}),Object(i.a)({name:this.state.newTokenName,login:this.props.login}).then(e=>{this.mounted&&this.setState(t=>{const s=[...t.tokens,{name:e.name,createdAt:e.createdAt}];return{generating:!1,newToken:e,newTokenName:"",tokens:s}},this.updateTokensCount)},()=>{this.mounted&&this.setState({generating:!1})}))},this.handleRevokeToken=e=>{this.setState(t=>({tokens:t.tokens.filter(t=>t.name!==e.name)}),this.updateTokensCount)},this.handleNewTokenChange=e=>this.setState({newTokenName:e.currentTarget.value})}componentDidMount(){this.mounted=!0,this.fetchTokens()}componentWillUnmount(){this.mounted=!1}renderItems(){const{tokens:e}=this.state;return e.length<=0?n.createElement("tr",null,n.createElement("td",{className:"note",colSpan:3},Object(l.translate)("users.no_tokens"))):e.map(e=>n.createElement(C,{deleteConfirmation:this.props.deleteConfirmation,key:e.name,login:this.props.login,onRevokeToken:this.handleRevokeToken,token:e}))}render(){const{generating:e,loading:t,newToken:s,newTokenName:r,tokens:i}=this.state,c=n.createElement("tr",null,n.createElement("td",null,n.createElement("i",{className:"spinner"})));return n.createElement(n.Fragment,null,n.createElement("h3",{className:"spacer-bottom"},Object(l.translate)("users.generate_tokens")),n.createElement("form",{autoComplete:"off",className:"display-flex-center",id:"generate-token-form",onSubmit:this.handleGenerateToken},n.createElement("input",{className:"input-large spacer-right",maxLength:100,onChange:this.handleNewTokenChange,placeholder:Object(l.translate)("users.enter_token_name"),required:!0,type:"text",value:r}),n.createElement(a.SubmitButton,{className:"js-generate-token",disabled:e||r.length<=0},Object(l.translate)("users.generate"))),s&&n.createElement(w,{token:s}),n.createElement("table",{className:"data zebra big-spacer-top"},n.createElement("thead",null,n.createElement("tr",null,n.createElement("th",null,Object(l.translate)("name")),n.createElement("th",null,Object(l.translate)("my_account.tokens_last_usage")),n.createElement("th",{className:"text-right"},Object(l.translate)("created")),n.createElement("th",null))),n.createElement("tbody",null,n.createElement(o.a,{customSpinner:c,loading:t&&i.length<=0},this.renderItems()))))}}},1876:function(e,t,s){"use strict";s.r(t);var n=s(321),a=s(323),r=s(2),o=s(332),l=s(347),i=s.n(l),c=s(31),m=s(436),d=s(333),u=s(359),h=s(312),p=s(318),g=s.n(p),E=s(426),b=s.n(E),f=s(335),C=s.n(f),v=s(325),k=s(9),w=s(326);class O extends r.PureComponent{constructor(){super(...arguments),this.handleChange=e=>this.props.onChange(this.props.idx,e.currentTarget.value),this.handleRemove=()=>this.props.onRemove(this.props.idx)}render(){return r.createElement("div",{className:"js-scm-account display-flex-row spacer-bottom"},r.createElement("input",{maxLength:255,onChange:this.handleChange,type:"text",value:this.props.scmAccount}),r.createElement(h.DeleteButton,{onClick:this.handleRemove}))}}class j extends r.PureComponent{constructor(e){super(e),this.mounted=!1,this.handleError=e=>this.mounted&&[400,500].includes(e.status)?Object(k.parseError)(e).then(e=>this.setState({error:e}),w.a):Object(w.a)(e),this.handleEmailChange=e=>this.setState({email:e.currentTarget.value}),this.handleLoginChange=e=>this.setState({login:e.currentTarget.value}),this.handleNameChange=e=>this.setState({name:e.currentTarget.value}),this.handlePasswordChange=e=>this.setState({password:e.currentTarget.value}),this.handleCreateUser=()=>Object(m.b)({email:this.state.email||void 0,login:this.state.login,name:this.state.name,password:this.state.password,scmAccount:b()(this.state.scmAccounts)}).then(()=>{this.props.onUpdateUsers(),this.props.onClose()},this.handleError),this.handleUpdateUser=()=>{const{user:e}=this.props;return Object(m.j)({email:e.local?this.state.email:void 0,login:this.state.login,name:e.local?this.state.name:void 0,scmAccount:b()(this.state.scmAccounts)}).then(()=>{this.props.onUpdateUsers(),this.props.onClose()},this.handleError)},this.handleAddScmAccount=()=>{this.setState(({scmAccounts:e})=>({scmAccounts:e.concat("")}))},this.handleUpdateScmAccount=(e,t)=>this.setState(({scmAccounts:s})=>{const n=s.slice();return n[e]=t,{scmAccounts:n}}),this.handleRemoveScmAccount=e=>this.setState(({scmAccounts:t})=>({scmAccounts:t.slice(0,e).concat(t.slice(e+1))}));const{user:t}=e;this.state=t?{email:t.email||"",login:t.login,name:t.name||"",password:"",scmAccounts:t.scmAccounts||[]}:{email:"",login:"",name:"",password:"",scmAccounts:[]}}componentDidMount(){this.mounted=!0}componentWillUnmount(){this.mounted=!1}render(){const{user:e}=this.props,{error:t}=this.state,s=e?Object(c.translate)("users.update_user"):Object(c.translate)("users.create_user");return r.createElement(C.a,{header:s,onClose:this.props.onClose,onSubmit:e?this.handleUpdateUser:this.handleCreateUser,size:"small"},({onCloseClick:n,onFormSubmit:a,submitting:o})=>r.createElement("form",{autoComplete:"off",id:"user-form",onSubmit:a},r.createElement("header",{className:"modal-head"},r.createElement("h2",null,s)),r.createElement("div",{className:"modal-body modal-container"},t&&r.createElement(v.Alert,{variant:"error"},t),!t&&e&&!e.local&&r.createElement(v.Alert,{variant:"warning"},Object(c.translate)("users.cannot_update_delegated_user")),!e&&r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"create-user-login"},Object(c.translate)("login"),r.createElement("em",{className:"mandatory"},"*")),r.createElement("input",{className:"hidden",name:"login-fake",type:"text"}),r.createElement("input",{autoFocus:!0,id:"create-user-login",maxLength:255,minLength:3,name:"login",onChange:this.handleLoginChange,required:!0,type:"text",value:this.state.login}),r.createElement("p",{className:"note"},Object(c.translateWithParameters)("users.minimum_x_characters",3))),r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"create-user-name"},Object(c.translate)("name"),r.createElement("em",{className:"mandatory"},"*")),r.createElement("input",{className:"hidden",name:"name-fake",type:"text"}),r.createElement("input",{autoFocus:!!e,disabled:e&&!e.local,id:"create-user-name",maxLength:200,name:"name",onChange:this.handleNameChange,required:!0,type:"text",value:this.state.name})),r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"create-user-email"},Object(c.translate)("users.email")),r.createElement("input",{className:"hidden",name:"email-fake",type:"email"}),r.createElement("input",{disabled:e&&!e.local,id:"create-user-email",maxLength:100,name:"email",onChange:this.handleEmailChange,type:"email",value:this.state.email})),!e&&r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"create-user-password"},Object(c.translate)("password"),r.createElement("em",{className:"mandatory"},"*")),r.createElement("input",{className:"hidden",name:"password-fake",type:"password"}),r.createElement("input",{id:"create-user-password",maxLength:50,name:"password",onChange:this.handlePasswordChange,required:!0,type:"password",value:this.state.password})),r.createElement("div",{className:"modal-field"},r.createElement("label",null,Object(c.translate)("my_profile.scm_accounts")),this.state.scmAccounts.map((e,t)=>r.createElement(O,{idx:t,key:t,onChange:this.handleUpdateScmAccount,onRemove:this.handleRemoveScmAccount,scmAccount:e})),r.createElement("div",{className:"spacer-bottom"},r.createElement(h.Button,{className:"js-scm-account-add",onClick:this.handleAddScmAccount},Object(c.translate)("add_verb"))),r.createElement("p",{className:"note"},Object(c.translate)("user.login_or_email_used_as_scm_account")))),r.createElement("footer",{className:"modal-foot"},o&&r.createElement("i",{className:"spinner spacer-right"}),r.createElement(h.SubmitButton,{disabled:o},e?Object(c.translate)("update_verb"):Object(c.translate)("create")),r.createElement(h.ResetButtonLink,{onClick:n},Object(c.translate)("cancel")))))}}class N extends r.PureComponent{constructor(){super(...arguments),this.state={openUserForm:!1},this.handleOpenUserForm=()=>{this.setState({openUserForm:!0})},this.handleCloseUserForm=()=>{this.setState({openUserForm:!1})}}render(){return r.createElement("header",{className:"page-header",id:"users-header"},r.createElement("h1",{className:"page-title"},Object(c.translate)("users.page")),r.createElement(g.a,{loading:this.props.loading}),r.createElement("div",{className:"page-actions"},r.createElement(h.Button,{id:"users-create",onClick:this.handleOpenUserForm},Object(c.translate)("users.create_user"))),r.createElement("p",{className:"page-description"},Object(c.translate)("users.page.description")),this.state.openUserForm&&r.createElement(j,{onClose:this.handleCloseUserForm,onUpdateUsers:this.props.onUpdateUsers}))}}var y=s(350),S=s.n(y);class U extends r.PureComponent{constructor(){super(...arguments),this.handleSearch=e=>{this.props.updateQuery({search:e})}}render(){const{query:e}=this.props;return r.createElement("div",{className:"panel panel-vertical bordered-bottom spacer-bottom",id:"users-search"},r.createElement(S.a,{minLength:2,onChange:this.handleSearch,placeholder:Object(c.translate)("search.search_by_login_or_name"),value:e.search}))}}var P=s(588),_=s.n(P),T=s(816),x=s.n(T),F=s(371),A=s(319),D=s(330),M=s.n(D),R=s(1101);function q(e){return r.createElement(M.a,{contentLabel:Object(c.translate)("users.tokens"),onRequestClose:e.onClose},r.createElement("header",{className:"modal-head"},r.createElement("h2",null,r.createElement(A.FormattedMessage,{defaultMessage:Object(c.translate)("users.user_X_tokens"),id:"users.user_X_tokens",values:{user:r.createElement("em",null,e.user.name)}}))),r.createElement("div",{className:"modal-body modal-container"},r.createElement(R.a,{deleteConfirmation:"inline",login:e.user.login,updateTokensCount:e.updateTokensCount})),r.createElement("footer",{className:"modal-foot"},r.createElement(h.ResetButtonLink,{onClick:e.onClose},Object(c.translate)("Done"))))}var L=s(380),B=s.n(L),z=s(337);class W extends r.PureComponent{constructor(){super(...arguments),this.mounted=!1,this.state={submitting:!1},this.handleDeactivate=e=>{e.preventDefault(),this.setState({submitting:!0}),Object(m.c)({login:this.props.user.login}).then(()=>{this.props.onUpdateUsers(),this.props.onClose()},()=>{this.mounted&&this.setState({submitting:!1})})}}componentDidMount(){this.mounted=!0}componentWillUnmount(){this.mounted=!1}render(){const{user:e}=this.props,{submitting:t}=this.state,s=Object(c.translate)("users.deactivate_user");return r.createElement(M.a,{contentLabel:s,onRequestClose:this.props.onClose},r.createElement("form",{autoComplete:"off",id:"deactivate-user-form",onSubmit:this.handleDeactivate},r.createElement("header",{className:"modal-head"},r.createElement("h2",null,s)),r.createElement("div",{className:"modal-body"},Object(c.translateWithParameters)("users.deactivate_user.confirmation",e.name,e.login)),r.createElement("footer",{className:"modal-foot"},t&&r.createElement("i",{className:"spinner spacer-right"}),r.createElement(h.SubmitButton,{className:"js-confirm button-red",disabled:t},Object(c.translate)("users.deactivate")),r.createElement(h.ResetButtonLink,{className:"js-modal-close",onClick:this.props.onClose},Object(c.translate)("cancel")))))}}var I=s(421);class G extends r.PureComponent{constructor(){super(...arguments),this.mounted=!1,this.state={confirmPassword:"",newPassword:"",oldPassword:"",submitting:!1},this.handleError=e=>this.mounted&&400===e.status?Object(k.parseError)(e).then(e=>this.setState({error:e,submitting:!1}),w.a):Object(w.a)(e),this.handleConfirmPasswordChange=e=>this.setState({confirmPassword:e.currentTarget.value}),this.handleNewPasswordChange=e=>this.setState({newPassword:e.currentTarget.value}),this.handleOldPasswordChange=e=>this.setState({oldPassword:e.currentTarget.value}),this.handleChangePassword=e=>{e.preventDefault(),this.state.newPassword.length>0&&this.state.newPassword===this.state.confirmPassword&&(this.setState({submitting:!0}),Object(m.a)({login:this.props.user.login,password:this.state.newPassword,previousPassword:this.state.oldPassword}).then(()=>{Object(I.a)(Object(c.translate)("my_profile.password.changed")),this.props.onClose()},this.handleError))}}componentDidMount(){this.mounted=!0}componentWillUnmount(){this.mounted=!1}render(){const{error:e,submitting:t,newPassword:s,confirmPassword:n}=this.state,a=Object(c.translate)("my_profile.password.title");return r.createElement(M.a,{contentLabel:a,onRequestClose:this.props.onClose,size:"small"},r.createElement("form",{autoComplete:"off",id:"user-password-form",onSubmit:this.handleChangePassword},r.createElement("header",{className:"modal-head"},r.createElement("h2",null,a)),r.createElement("div",{className:"modal-body"},e&&r.createElement(v.Alert,{variant:"error"},e),this.props.isCurrentUser&&r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"old-user-password"},Object(c.translate)("my_profile.password.old"),r.createElement("em",{className:"mandatory"},"*")),r.createElement("input",{className:"hidden",name:"old-password-fake",type:"password"}),r.createElement("input",{id:"old-user-password",maxLength:50,name:"old-password",onChange:this.handleOldPasswordChange,required:!0,type:"password",value:this.state.oldPassword})),r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"user-password"},Object(c.translate)("my_profile.password.new"),r.createElement("em",{className:"mandatory"},"*")),r.createElement("input",{className:"hidden",name:"password-fake",type:"password"}),r.createElement("input",{id:"user-password",maxLength:50,name:"password",onChange:this.handleNewPasswordChange,required:!0,type:"password",value:this.state.newPassword})),r.createElement("div",{className:"modal-field"},r.createElement("label",{htmlFor:"confirm-user-password"},Object(c.translate)("my_profile.password.confirm"),r.createElement("em",{className:"mandatory"},"*")),r.createElement("input",{className:"hidden",name:"confirm-password-fake",type:"password"}),r.createElement("input",{id:"confirm-user-password",maxLength:50,name:"confirm-password",onChange:this.handleConfirmPasswordChange,required:!0,type:"password",value:this.state.confirmPassword}))),r.createElement("footer",{className:"modal-foot"},t&&r.createElement("i",{className:"spinner spacer-right"}),r.createElement(h.SubmitButton,{disabled:t||!s||s!==n},Object(c.translate)("change_verb")),r.createElement(h.ResetButtonLink,{onClick:this.props.onClose},Object(c.translate)("cancel")))))}}class J extends r.PureComponent{constructor(){super(...arguments),this.state={},this.handleOpenDeactivateForm=()=>{this.setState({openForm:"deactivate"})},this.handleOpenPasswordForm=()=>{this.setState({openForm:"password"})},this.handleOpenUpdateForm=()=>{this.setState({openForm:"update"})},this.handleCloseForm=()=>{this.setState({openForm:void 0})},this.renderActions=()=>{const{user:e}=this.props;return r.createElement(B.a,null,r.createElement(L.ActionsDropdownItem,{className:"js-user-update",onClick:this.handleOpenUpdateForm},Object(c.translate)("update_details")),e.local&&r.createElement(L.ActionsDropdownItem,{className:"js-user-change-password",onClick:this.handleOpenPasswordForm},Object(c.translate)("my_profile.password.title")),r.createElement(L.ActionsDropdownDivider,null),Object(z.c)(e)&&r.createElement(L.ActionsDropdownItem,{className:"js-user-deactivate",destructive:!0,onClick:this.handleOpenDeactivateForm},Object(c.translate)("users.deactivate")))}}render(){const{openForm:e}=this.state,{isCurrentUser:t,onUpdateUsers:s,user:n}=this.props;return r.createElement(r.Fragment,null,this.renderActions(),"deactivate"===e&&Object(z.c)(n)&&r.createElement(W,{onClose:this.handleCloseForm,onUpdateUsers:s,user:n}),"password"===e&&r.createElement(G,{isCurrentUser:t,onClose:this.handleCloseForm,user:n}),"update"===e&&r.createElement(j,{onClose:this.handleCloseForm,onUpdateUsers:s,user:n}))}}var Q=s(358),X=s.n(Q),H=s(504),V=s.n(H),K=s(480),Y=s.n(K),Z=s(686);class $ extends r.PureComponent{constructor(e){super(e),this.mounted=!1,this.fetchUsers=e=>Object(m.e)({login:this.props.user.login,organization:void 0,p:e.page,ps:e.pageSize,q:""!==e.query?e.query:void 0,selected:e.filter}).then(t=>{this.mounted&&this.setState(s=>{const n=null!=e.page&&e.page>1,a=n?[...s.groups,...t.groups]:t.groups,r=t.groups.filter(e=>e.selected).map(e=>e.name),o=n?[...s.selectedGroups,...r]:r;return{lastSearchParams:e,needToReload:!1,groups:a,groupsTotalCount:t.paging.total,selectedGroups:o}})}),this.handleSelect=e=>Object(Z.a)({name:e,login:this.props.user.login}).then(()=>{this.mounted&&this.setState(t=>({needToReload:!0,selectedGroups:[...t.selectedGroups,e]}))}),this.handleUnselect=e=>Object(Z.e)({name:e,login:this.props.user.login}).then(()=>{this.mounted&&this.setState(t=>({needToReload:!0,selectedGroups:X()(t.selectedGroups,e)}))}),this.handleCloseClick=e=>{e.preventDefault(),this.handleClose()},this.handleClose=()=>{this.props.onUpdateUsers(),this.props.onClose()},this.renderElement=e=>{const t=V()(this.state.groups,{name:e});return r.createElement("div",{className:"select-list-list-item"},void 0===t?e:r.createElement(r.Fragment,null,t.name,r.createElement("br",null),r.createElement("span",{className:"note"},t.description)))},this.state={needToReload:!1,groups:[],selectedGroups:[]}}componentDidMount(){this.mounted=!0}componentWillUnmount(){this.mounted=!1}render(){const e=Object(c.translate)("users.update_groups");return r.createElement(M.a,{contentLabel:e,onRequestClose:this.handleClose},r.createElement("div",{className:"modal-head"},r.createElement("h2",null,e)),r.createElement("div",{className:"modal-body modal-container"},r.createElement(Y.a,{elements:this.state.groups.map(e=>e.name),elementsTotalCount:this.state.groupsTotalCount,needToReload:this.state.needToReload&&this.state.lastSearchParams&&this.state.lastSearchParams.filter!==K.SelectListFilter.All,onSearch:this.fetchUsers,onSelect:this.handleSelect,onUnselect:this.handleUnselect,renderElement:this.renderElement,selectedElements:this.state.selectedGroups,withPaging:!0})),r.createElement("footer",{className:"modal-foot"},r.createElement("a",{className:"js-modal-close",href:"#",onClick:this.handleCloseClick},Object(c.translate)("Done"))))}}class ee extends r.PureComponent{constructor(){super(...arguments),this.state={openForm:!1,showMore:!1},this.handleOpenForm=()=>this.setState({openForm:!0}),this.handleCloseForm=()=>this.setState({openForm:!1}),this.toggleShowMore=e=>{e.preventDefault(),this.setState(e=>({showMore:!e.showMore}))}}render(){const{groups:e}=this.props,t=e.length>3?2:3;return r.createElement("ul",null,e.slice(0,t).map(e=>r.createElement("li",{className:"little-spacer-bottom",key:e},e)),e.length>3&&this.state.showMore&&e.slice(t).map(e=>r.createElement("li",{className:"little-spacer-bottom",key:e},e)),r.createElement("li",{className:"little-spacer-bottom"},e.length>3&&!this.state.showMore&&r.createElement("a",{className:"js-user-more-groups spacer-right",href:"#",onClick:this.toggleShowMore},Object(c.translateWithParameters)("more_x",e.length-t)),r.createElement(h.ButtonIcon,{className:"js-user-groups button-small",onClick:this.handleOpenForm,tooltip:Object(c.translate)("users.update_groups")},r.createElement(_.a,null))),this.state.openForm&&r.createElement($,{onClose:this.handleCloseForm,onUpdateUsers:this.props.onUpdateUsers,user:this.props.user}))}}var te=s(668),se=s(338),ne=s(336);function ae({identityProvider:e,user:t}){return r.createElement("td",{className:"text-middle"},r.createElement("div",null,r.createElement("strong",{className:"js-user-name"},t.name),r.createElement("span",{className:"js-user-login note little-spacer-left"},t.login)),t.email&&r.createElement("div",{className:"js-user-email little-spacer-top"},t.email),!t.local&&"sonarqube"!==t.externalProvider&&r.createElement(re,{identityProvider:e,user:t}))}function re({identityProvider:e,user:t}){return e?r.createElement("div",{className:"js-user-identity-provider little-spacer-top"},r.createElement("div",{className:"identity-provider",style:{backgroundColor:e.backgroundColor,color:Object(te.getTextColor)(e.backgroundColor,ne.colors.secondFontColor)}},r.createElement("img",{alt:e.name,className:"little-spacer-right",height:"14",src:Object(se.getBaseUrl)()+e.iconPath,width:"14"}),t.externalIdentity)):r.createElement("div",{className:"js-user-identity-provider little-spacer-top"},r.createElement("span",null,t.externalProvider,": ",t.externalIdentity))}class oe extends r.PureComponent{constructor(){super(...arguments),this.state={showMore:!1},this.toggleShowMore=e=>{e.preventDefault(),this.setState(e=>({showMore:!e.showMore}))}}render(){const{scmAccounts:e}=this.props,t=e.length>3?2:3;return r.createElement("ul",{className:"js-scm-accounts"},e.slice(0,t).map((e,t)=>r.createElement("li",{className:"little-spacer-bottom",key:t},e)),e.length>3&&(this.state.showMore?e.slice(t).map((e,s)=>r.createElement("li",{className:"little-spacer-bottom",key:s+t},e)):r.createElement("li",{className:"little-spacer-bottom"},r.createElement("a",{className:"js-user-more-scm",href:"#",onClick:this.toggleShowMore},Object(c.translateWithParameters)("more_x",e.length-t)))))}}class le extends r.PureComponent{constructor(){super(...arguments),this.state={openTokenForm:!1},this.handleOpenTokensForm=()=>this.setState({openTokenForm:!0}),this.handleCloseTokensForm=()=>this.setState({openTokenForm:!1})}render(){const{identityProvider:e,onUpdateUsers:t,organizationsEnabled:s,user:n}=this.props;return r.createElement("tr",null,r.createElement("td",{className:"thin nowrap text-middle"},r.createElement(F.a,{hash:n.avatar,name:n.name,size:36})),r.createElement(ae,{identityProvider:e,user:n}),r.createElement("td",{className:"thin nowrap text-middle"},r.createElement(oe,{scmAccounts:n.scmAccounts||[]})),r.createElement("td",{className:"thin nowrap text-middle"},r.createElement(x.a,{date:n.lastConnectionDate})),!s&&r.createElement("td",{className:"thin nowrap text-middle"},r.createElement(ee,{groups:n.groups||[],onUpdateUsers:t,user:n})),r.createElement("td",{className:"thin nowrap text-middle"},n.tokensCount,r.createElement(h.ButtonIcon,{className:"js-user-tokens spacer-left button-small",onClick:this.handleOpenTokensForm,tooltip:Object(c.translate)("users.update_tokens")},r.createElement(_.a,null))),r.createElement("td",{className:"thin nowrap text-right text-middle"},r.createElement(J,{isCurrentUser:this.props.isCurrentUser,onUpdateUsers:t,user:n})),this.state.openTokenForm&&r.createElement(q,{onClose:this.handleCloseTokensForm,updateTokensCount:this.props.updateTokensCount,user:n}))}}function ie({currentUser:e,identityProviders:t,onUpdateUsers:s,organizationsEnabled:n,updateTokensCount:a,users:o}){return r.createElement("div",{className:"boxed-group boxed-group-inner"},r.createElement("table",{className:"data zebra",id:"users-list"},r.createElement("thead",null,r.createElement("tr",null,r.createElement("th",null),r.createElement("th",{className:"nowrap"}),r.createElement("th",{className:"nowrap"},Object(c.translate)("my_profile.scm_accounts")),r.createElement("th",{className:"nowrap"},Object(c.translate)("users.last_connection")),!n&&r.createElement("th",{className:"nowrap"},Object(c.translate)("my_profile.groups")),r.createElement("th",{className:"nowrap"},Object(c.translate)("users.tokens")),r.createElement("th",{className:"nowrap"}," "))),r.createElement("tbody",null,o.map(o=>r.createElement(le,{identityProvider:t.find(e=>o.externalProvider===e.key),isCurrentUser:e.isLoggedIn&&e.login===o.login,key:o.login,onUpdateUsers:s,organizationsEnabled:n,updateTokensCount:a,user:o})))))}var ce=s(520),me=s.n(ce),de=s(447);const ue=me()(e=>({search:Object(de.parseAsString)(e.search)})),he=me()(e=>Object(de.cleanQuery)({search:e.search?Object(de.serializeString)(e.search):void 0}));function pe(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),s.push.apply(s,n)}return s}function ge(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?pe(Object(s),!0).forEach((function(t){Ee(e,t,s[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):pe(Object(s)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))}))}return e}function Ee(e,t,s){return t in e?Object.defineProperty(e,t,{value:s,enumerable:!0,configurable:!0,writable:!0}):e[t]=s,e}class be extends r.PureComponent{constructor(){super(...arguments),this.mounted=!1,this.state={identityProviders:[],loading:!0,users:[]},this.finishLoading=()=>{this.mounted&&this.setState({loading:!1})},this.fetchIdentityProviders=()=>Object(m.d)().then(({identityProviders:e})=>{this.mounted&&this.setState({identityProviders:e})},()=>{}),this.fetchUsers=({location:e}=this.props)=>{this.setState({loading:!0}),Object(m.f)({q:ue(e.query).search}).then(({paging:e,users:t})=>{this.mounted&&this.setState({loading:!1,paging:e,users:t})},this.finishLoading)},this.fetchMoreUsers=()=>{const{paging:e}=this.state;e&&(this.setState({loading:!0}),Object(m.f)({p:e.pageIndex+1,q:ue(this.props.location.query).search}).then(({paging:e,users:t})=>{this.mounted&&this.setState(s=>({loading:!1,users:[...s.users,...t],paging:e}))},this.finishLoading))},this.updateQuery=e=>{const t=he(ge({},ue(this.props.location.query),{},e));this.props.router.push(ge({},this.props.location,{query:t}))},this.updateTokensCount=(e,t)=>{this.setState(s=>({users:s.users.map(s=>s.login===e?ge({},s,{tokensCount:t}):s)}))}}componentDidMount(){this.mounted=!0,this.fetchIdentityProviders(),this.fetchUsers()}componentWillReceiveProps(e){e.location.query.search!==this.props.location.query.search&&this.fetchUsers(e)}componentWillUnmount(){this.mounted=!1}render(){const e=ue(this.props.location.query),{loading:t,paging:s,users:n}=this.state;return r.createElement("div",{className:"page page-limited",id:"users-page"},r.createElement(d.a,{suggestions:"users"}),r.createElement(o.a,{defer:!1,title:Object(c.translate)("users.page")}),r.createElement(N,{loading:t,onUpdateUsers:this.fetchUsers}),r.createElement(U,{query:e,updateQuery:this.updateQuery}),r.createElement(ie,{currentUser:this.props.currentUser,identityProviders:this.state.identityProviders,onUpdateUsers:this.fetchUsers,organizationsEnabled:this.props.organizationsEnabled,updateTokensCount:this.updateTokensCount,users:n}),void 0!==s&&r.createElement(i.a,{count:n.length,loadMore:this.fetchMoreUsers,ready:!t,total:s.total}))}}var fe=Object(u.a)(be);t.default=Object(n.connect)(e=>({currentUser:Object(a.getCurrentUser)(e),organizationsEnabled:Object(a.areThereCustomOrganizations)(e)}))(fe)},333:function(e,t,s){"use strict";s.d(t,"a",(function(){return r}));var n=s(2),a=s(360);function r({suggestions:e}){return n.createElement(a.a.Consumer,null,({addSuggestions:t,removeSuggestions:s})=>n.createElement(o,{addSuggestions:t,removeSuggestions:s,suggestions:e}))}class o extends n.PureComponent{componentDidMount(){this.props.addSuggestions(this.props.suggestions)}componentDidUpdate(e){e.suggestions!==this.props.suggestions&&(this.props.removeSuggestions(this.props.suggestions),this.props.addSuggestions(e.suggestions))}componentWillUnmount(){this.props.removeSuggestions(this.props.suggestions)}render(){return null}}},421:function(e,t,s){"use strict";s.d(t,"a",(function(){return r}));var n=s(387),a=s(386);function r(e){Object(a.default)().dispatch(n.b(e))}},618:function(e,t,s){"use strict";s.d(t,"b",(function(){return r})),s.d(t,"a",(function(){return o})),s.d(t,"c",(function(){return l}));var n=s(9),a=s(326);function r(e){return Object(n.getJSON)("/api/user_tokens/search",{login:e}).then(e=>e.userTokens,a.a)}function o(e){return Object(n.postJSON)("/api/user_tokens/generate",e).catch(a.a)}function l(e){return Object(n.post)("/api/user_tokens/revoke",e).catch(a.a)}},686:function(e,t,s){"use strict";s.d(t,"f",(function(){return r})),s.d(t,"d",(function(){return o})),s.d(t,"a",(function(){return l})),s.d(t,"e",(function(){return i})),s.d(t,"b",(function(){return c})),s.d(t,"g",(function(){return m})),s.d(t,"c",(function(){return d}));var n=s(9),a=s(326);function r(e){return Object(n.getJSON)("/api/user_groups/search",e).catch(a.a)}function o(e){return Object(n.getJSON)("/api/user_groups/users",e).catch(a.a)}function l(e){return Object(n.post)("/api/user_groups/add_user",e).catch(a.a)}function i(e){return Object(n.post)("/api/user_groups/remove_user",e).catch(a.a)}function c(e){return Object(n.postJSON)("/api/user_groups/create",e).then(e=>e.group,a.a)}function m(e){return Object(n.post)("/api/user_groups/update",e).catch(a.a)}function d(e){return Object(n.post)("/api/user_groups/delete",e).catch(a.a)}}}]);
//# sourceMappingURL=331.m.21827866.chunk.js.map