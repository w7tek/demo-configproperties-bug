package us.w7tek.bug;

/*
    Copyright 2015 Tommy Knowlton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;

@EnableConfigurationProperties
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @ConfigurationProperties("someConfig")
    @Bean
    public ExternalizedConfig externalizedConfig() { return new ExternalizedConfig(); }

    public static class ExternalizedConfig extends LinkedHashMap<String, String[]> {}

    @Controller
    public static class ControllerThatConsumesConfig {
        private static final String A_KEY_THAT_COULDNT_BE_A_PROPERTY_NAME = "this == config cannot be expressed as a bean with properties, because the keys cannot be made into Java language identifiers for bean property setters and getters";

        @Autowired
        ExternalizedConfig config;

        @PostConstruct
        void init() {
            String[] strings = config.get(A_KEY_THAT_COULDNT_BE_A_PROPERTY_NAME);  //  ClassCastException occurs here

            // doesn't have to occur in @PostConstruct, that was just a convenient place for my demo.
        }
    }
}
