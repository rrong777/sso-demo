package com.rrong.sso.server;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * @FrameworkEndpoint 和restController相似，这个注解注释的类上面可以去写RequestMapping去处理某个请求
 * WhitelabelApprovalEndpoint 这个是Oauth2 单点登录 从应用1跳转到应用2的时候，需要授权的这个controller
 * 我们可以改写 ，然后自动完成 确认授权这步
 * 我们可以写一个一模一样的类，不用FrameworkEndpoint 处理，而用RestController处理，如果@RestController和
 * @FrameworkEndpoint里面处理的url一样，就会使用@RestController里面的控制器处理，而不用FrameworkEndpoint里面的处理
 *
 * SpelView 这个类就不是public的 是不能用的，构造器是公有的没用，类就不是公有的
 * 在我们的代码是没办法用这个的
 * 那我们自己copy一份
 */
@RestController
@SessionAttributes("authorizationRequest")
public class SsoApprovalEndpoint {
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        String template = createTemplate(model, request);
        if (request.getAttribute("_csrf") != null) {
            model.put("_csrf", request.getAttribute("_csrf"));
        }
        // 应用1跳转到应用2 有一个确认授权的动作，确认授权是跳转到这里
        // 然后这里返回一个确认授权的页面，下面的ssoSpelView就是一个View视图，template就是一个html页面。
        // 我们改写一下这个View即可
        return new ModelAndView(new SsoSpelView(template), model);
    }

    protected String createTemplate(Map<String, Object> model, HttpServletRequest request) {
        String template = TEMPLATE;
        if (model.containsKey("scopes") || request.getAttribute("scopes") != null) {
            template = template.replace("%scopes%", createScopes(model, request)).replace("%denial%", "");
        }
        else {
            template = template.replace("%scopes%", "").replace("%denial%", DENIAL);
        }
        if (model.containsKey("_csrf") || request.getAttribute("_csrf") != null) {
            template = template.replace("%csrf%", CSRF);
        }
        else {
            template = template.replace("%csrf%", "");
        }
        return template;
    }

    private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("<ul>");
        @SuppressWarnings("unchecked")
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request
                .getAttribute("scopes"));
        for (String scope : scopes.keySet()) {
            String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
            String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";
            String value = SCOPE.replace("%scope%", scope).replace("%key%", scope).replace("%approved%", approved)
                    .replace("%denied%", denied);
            builder.append(value);
        }
        builder.append("</ul>");
        return builder.toString();
    }

    private static String CSRF = "<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}' />";

    private static String DENIAL = "<form id='denialForm' name='denialForm' action='${path}/oauth/authorize' method='post'><input name='user_oauth_approval' value='false' type='hidden'/>%csrf%<label><input name='deny' value='Deny' type='submit'/></label></form>";

    // 在body内容区域声明一个div display：none外界就看一片空白，然后程序自动点击确认.
    // 然后写个script 自动提交表单
    private static String TEMPLATE = "<html><body><div style='display:none'><h1>OAuth Approval</h1>"
            + "<p>Do you authorize '${authorizationRequest.clientId}' to access your protected resources?</p>"
            + "<form id='confirmationForm' name='confirmationForm' action='${path}/oauth/authorize' method='post'><input name='user_oauth_approval' value='true' type='hidden'/>%csrf%%scopes%<label><input name='authorize' value='Authorize' type='submit'/></label></form>"
            + "%denial%</div><script>document.getElementById('confirmationForm').submit()</script></body></html>";

    private static String SCOPE = "<li><div class='form-group'>%scope%: <input type='radio' name='%key%'"
            + " value='true'%approved%>Approve</input> <input type='radio' name='%key%' value='false'%denied%>Deny</input></div></li>";

}
