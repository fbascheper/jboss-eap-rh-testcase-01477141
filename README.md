# Testcase for RedHat support issue 01477141 on JBoss EAP 6.4.3

This is a simple DeltaSpike 1.5.x JSF application to reproduce issue 01477141.
It requires the JBoss Maven repository for EAP 6.4.3 to build. 

Steps:

 * Build application and deploy WAR file (exploded).
 * Set a breakpoint on ExternalContext.java, line 1929 (in the isSecure() method). 
 * Login as user admin (password doesn't matter).
   * View the logged-in page.
   * Click logout.
 * Login as user abcd (password doesn't matter).
   * Breakpoint is hit.
   
Usually this works fine because the `com.sun.faces.context.InjectionFacesContextFactory` injects 
a value (`com.sun.faces.context.ExternalContextImpl`) in variable defaultExternalContext.

But sometimes the value of defaultExternalContext remains `null`. This scenario is usually preceded by the following message in the server log 

    Application was not properly initialized at startup, could not find Factory: javax.faces.application.ApplicationFactory. Attempting to find backup.
    
Since the JSF implementation can find a backup (e.g. primefaces application), processing continues.    

But in this scenario we are hit with the following (abbreviated) stacktrace:

      15:07:23,237 SEVERE [javax.faces] (ServerService Thread Pool -- 66) Application was not properly initialized at startup, could not find Factory: javax.faces.application.ApplicationFactory. Attempting to find backup.
      15:11:23,979 WARNING [javax.enterprise.resource.webcontainer.jsf.lifecycle] (http-/127.0.0.1:8080-1) #{loginController.loginWithUsernamePwd()}: java.lang.UnsupportedOperationException: javax.faces.FacesException: #{loginController.loginWithUsernamePwd()}: java.lang.UnsupportedOperationException
      	at com.sun.faces.application.ActionListenerImpl.processAction(ActionListenerImpl.java:117) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	at org.apache.deltaspike.jsf.impl.config.view.ViewControllerActionListener.processAction(ViewControllerActionListener.java:63) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.listener.action.DeltaSpikeActionListener.processAction(DeltaSpikeActionListener.java:51) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
        ....
      Caused by: javax.faces.el.EvaluationException: java.lang.UnsupportedOperationException
      	at javax.faces.component.MethodBindingMethodExpressionAdapter.invoke(MethodBindingMethodExpressionAdapter.java:101) [jboss-jsf-api_2.1_spec-2.1.28.Final-redhat-1.jar:2.1.28.Final-redhat-1]
      	at com.sun.faces.application.ActionListenerImpl.processAction(ActionListenerImpl.java:101) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	... 58 more
      Caused by: java.lang.UnsupportedOperationException
      	at javax.faces.context.ExternalContext.isSecure(ExternalContext.java:1932) [jboss-jsf-api_2.1_spec-2.1.28.Final-redhat-1.jar:2.1.28.Final-redhat-1]
      	at com.sun.faces.context.flash.ELFlash.setCookie(ELFlash.java:1002) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	at com.sun.faces.context.flash.ELFlash.doLastPhaseActions(ELFlash.java:639) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	at com.sun.faces.context.ExternalContextImpl.redirect(ExternalContextImpl.java:581) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	at org.apache.deltaspike.jsf.impl.listener.request.DeltaSpikeExternalContextWrapper.redirect(DeltaSpikeExternalContextWrapper.java:56) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at com.sun.faces.application.NavigationHandlerImpl.handleNavigation(NavigationHandlerImpl.java:180) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	at org.apache.deltaspike.jsf.impl.config.view.navigation.ViewConfigAwareNavigationHandler.handleNavigation(ViewConfigAwareNavigationHandler.java:146) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.scope.viewaccess.ViewAccessScopedAwareNavigationHandler.handleNavigation(ViewAccessScopedAwareNavigationHandler.java:51) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.navigation.DeltaSpikeNavigationHandler.handleNavigation(DeltaSpikeNavigationHandler.java:77) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.config.view.navigation.ViewConfigAwareNavigationHandler.handleNavigation(ViewConfigAwareNavigationHandler.java:146) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.scope.viewaccess.ViewAccessScopedAwareNavigationHandler.handleNavigation(ViewAccessScopedAwareNavigationHandler.java:51) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.navigation.DeltaSpikeNavigationHandler.handleNavigation(DeltaSpikeNavigationHandler.java:77) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.config.view.navigation.DefaultViewNavigationHandler.navigateTo(DefaultViewNavigationHandler.java:41) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
      	at org.apache.deltaspike.jsf.impl.config.view.navigation.DefaultViewNavigationHandler$Proxy$_$$_WeldClientProxy.navigateTo(DefaultViewNavigationHandler$Proxy$_$$_WeldClientProxy.java) [deltaspike-jsf-module-impl-ee6-1.5.0.jar:1.5.0]
        ...
      	at java.lang.reflect.Method.invoke(Unknown Source) [rt.jar:1.8.0_60]
      	at org.apache.el.parser.AstValue.invoke(AstValue.java:258) [jbossweb-7.5.10.Final-redhat-1.jar:7.5.10.Final-redhat-1]
      	at org.apache.el.MethodExpressionImpl.invoke(MethodExpressionImpl.java:278) [jbossweb-7.5.10.Final-redhat-1.jar:7.5.10.Final-redhat-1]
      	at org.jboss.weld.util.el.ForwardingMethodExpression.invoke(ForwardingMethodExpression.java:40) [weld-core-1.1.30.Final-redhat-1.jar:1.1.30.Final-redhat-1]
      	at org.jboss.weld.el.WeldMethodExpression.invoke(WeldMethodExpression.java:50) [weld-core-1.1.30.Final-redhat-1.jar:1.1.30.Final-redhat-1]
      	at com.sun.faces.facelets.el.TagMethodExpression.invoke(TagMethodExpression.java:105) [jsf-impl-2.1.28.redhat-10.jar:2.1.28.redhat-10]
      	at javax.faces.component.MethodBindingMethodExpressionAdapter.invoke(MethodBindingMethodExpressionAdapter.java:87) [jboss-jsf-api_2.1_spec-2.1.28.Final-redhat-1.jar:2.1.28.Final-redhat-1]

