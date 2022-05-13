package common;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.DefaultVertx;

import javax.annotation.PostConstruct;

public abstract class DefaultEmbeddableVerticle implements EmbeddableVerticle {

    @Autowired
    protected org.springframework.beans.factory.BeanFactory beanFactory;

    @PostConstruct
    public void runVerticle(){
        Vertx vertx = null;
        try {
            vertx = beanFactory.getBean(Vertx.class);
        } catch (NoSuchBeanDefinitionException e) {
            if(host() != null) {
                if(port() != -1) {
                    vertx = new DefaultVertx(port(), host());
                } else {
                    vertx = new DefaultVertx(host());
                }
            } else {
                vertx = new DefaultVertx();
            }
        }

        beanFactory.getBean(this.getClass()).start(vertx);
    }

    public String host(){
        return null;
    }

    public int port(){
        return -1;
    }

}
