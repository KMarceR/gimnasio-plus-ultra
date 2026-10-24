package com.sv.grupo7.gimnasio_plus_ultra.config;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String usuario;

    @Value("${spring.datasource.password}")
    private String contrasena;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    public javax.sql.DataSource dataSource() {
        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setUsername(usuario);
        p.setPassword(contrasena);
        p.setDriverClassName(driver);
        p.setName("PlusUltraPool");

        p.setValidationQuery("SELECT 1");           // consulta mínima para comprobar que la conexión sigue viva
        p.setInitialSize(2);                        // conexiones que se abren al arrancar
        p.setMinIdle(2);                            // mínimo de conexiones libres siempre listas
        p.setMaxIdle(5);                            // máximo de conexiones libres; las demás se cierran
        p.setMaxActive(10);                         // máximo total en uso a la vez (recepción, admin y tareas programadas)
        p.setMaxWait(10000);                        // ms que una petición espera una conexión libre antes de dar error
        p.setTestOnBorrow(true);                    // valida la conexión antes de prestarla
        p.setTestOnReturn(false);                   // no valida al devolverla, ya se validó al prestarla
        p.setTestWhileIdle(true);                   // valida las conexiones libres en segundo plano
        p.setTestOnConnect(true);                   // valida cada conexión nueva al crearla
        p.setTimeBetweenEvictionRunsMillis(30000);  // cada 30 s revisa las conexiones libres
        p.setMinEvictableIdleTimeMillis(60000);     // cierra las libres que llevan más de 60 s sin uso (respetando minIdle)
        p.setRemoveAbandoned(true);                 // recupera conexiones que el código olvidó devolver
        p.setRemoveAbandonedTimeout(60);            // segundos para considerar una conexión abandonada
        p.setValidationInterval(30000);             // no repite la validación de una misma conexión antes de 30 s
        p.setLogAbandoned(true);                    // muestra en consola qué código abandonó la conexión

        DataSource dataSource = new DataSource();
        dataSource.setPoolProperties(p);
        return dataSource;
    }
}
