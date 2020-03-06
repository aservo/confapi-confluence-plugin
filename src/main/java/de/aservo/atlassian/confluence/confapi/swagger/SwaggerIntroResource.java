package de.aservo.atlassian.confluence.confapi.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(info =
@Info(
        title = "[![ASERVO Software GmbH](https://aservo.github.io/img/aservo_atlassian_banner.png)](https://www.aservo.com/en/atlassian)" +
                "ConfAPI for Confluence",
        license = @License(name = "Apache 2.0", url = "https://opensource.org/licenses/Apache-2.0"),
        contact = @Contact(url = "https://www.aservo.com/", name = "Kai Lehmann", email = "klehmann@aservo.com"),
        description = "[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.aservo.atlassian/confluence-confapi-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.aservo.atlassian/confluence-confapi-plugin)\n" +
                "[![Build Status](https://circleci.com/gh/aservo/confluence-confapi-plugin.svg?style=shield)](https://circleci.com/gh/aservo/confluence-confapi-plugin)\n" +
                "[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aservo_confluence-confapi-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=aservo_confluence-confapi-plugin)\n" +
                "[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aservo_confluence-confapi-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=aservo_confluence-confapi-plugin)\n" +
                "[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)\n\n" +
                "REST API for automated Confluence configuration.\n\n"
    )
)
public class SwaggerIntroResource {}
