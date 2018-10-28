/**
 * Copyright (C) 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import models.User;
import net.zileo.ninja.auth0.subject.Auth0;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.jpa.UnitOfWork;

@Singleton
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Inject
    private Provider<EntityManager> emProvider;

    @UnitOfWork
    public Result index(Context context, @Auth0 User user) {
        logger.info("account: " + context.getSession().get("account"));
        return Results.html().template("views/ApplicationController/index.ftl.html").render("User", user).render("Users", emProvider.get().createQuery("select u from User u", User.class).getResultList());

    }

    @UnitOfWork
    public Result session(Context context, @Auth0 User user) {
        context.getSession().put("account", "BIG UP");
        return index(context, user);
    }

    @UnitOfWork
    public Result helloWorldJson() {
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";
        return Results.json().render(simplePojo);
    }

    public static class SimplePojo {
        public String content;
    }
}
