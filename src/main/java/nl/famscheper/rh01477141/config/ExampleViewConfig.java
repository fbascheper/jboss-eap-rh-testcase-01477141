/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.famscheper.rh01477141.config;

import nl.famscheper.rh01477141.context.stereotype.SecuredByRole;
import org.apache.deltaspike.core.api.config.view.navigation.NavigationParameter;
import org.apache.deltaspike.jsf.api.config.view.Folder;
import org.apache.deltaspike.jsf.api.config.view.View;

import static nl.famscheper.rh01477141.context.stereotype.SecuredByRole.Role.ADMINISTRATOR;
import static nl.famscheper.rh01477141.context.stereotype.SecuredByRole.Role.USER;

/**
 * Static view configuration for the example folder.
 *
 * @author Erik-Berndt Scheper
 * @since 30-09-2015
 */
@Folder(name = "./example/")
public interface ExampleViewConfig extends WebAppRootViewConfig {

    class DemoPage implements ExampleViewConfig {
        // Define controller for demoPage.xhtml
    }

    @NavigationParameter.List({
            @NavigationParameter(key = "param1", value = "Hey, I come from a navigation parameter"),
            @NavigationParameter(key = "param2", value = "Hey, It's an interpolated value: #{myBean.aValue}")
    })
    @SecuredByRole(roles = {USER})
    @View(viewParams = View.ViewParameterMode.INCLUDE) class NavigationParameterPage implements ExampleViewConfig {
        // Define controller for navigationParameterPage.xhtml
    }

    @SecuredByRole(roles = {USER}) class SecuredUserPage implements ExampleViewConfig {
        // Define controller for securedUserPage.xhtml
    }

    @SecuredByRole(roles = {ADMINISTRATOR}) class SecuredAdminPage implements ExampleViewConfig {
        // Define controller for securedAdminPage.xhtml
    }

}
