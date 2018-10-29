package services.user;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import models.User;
import net.zileo.ninja.auth0.handlers.Auth0EmailHandler;

public class UserAuth0TokenHandler extends Auth0EmailHandler<User> {

    private final static Logger logger = LoggerFactory.getLogger(UserAuth0TokenHandler.class);

    @Inject
    private Provider<EntityManager> emProvider;

    @Transactional
    @Override
    public User buildSubjectFromEmail(DecodedJWT jwt, String userId, String email) {

        EntityManager em = emProvider.get();

        logger.info("Claims = {}", jwt.getClaims());

        try {

            User user = em.createQuery("select u from User u where email = ?1", User.class).setParameter(1, email).getSingleResult();
            logger.info("Found user {} / {}", user.id, user.email);
            return user;

        } catch (NoResultException e) {

            logger.info("Creating user {} / {}", userId, email);
            User user = new User();
            user.email = email;
            user.auth0Id = userId;
            em.persist(user);
            return user;

        }

    }

}