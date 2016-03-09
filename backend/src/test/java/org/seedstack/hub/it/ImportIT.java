/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.it;

import org.junit.Test;
import org.seedstack.hub.application.importer.ImportService;
import org.seedstack.hub.domain.model.component.Component;
import org.seedstack.hub.domain.model.component.ComponentId;
import org.seedstack.hub.domain.model.component.Version;
import org.seedstack.hub.domain.model.component.VersionId;
import org.seedstack.hub.domain.model.document.Document;
import org.seedstack.hub.domain.model.document.DocumentId;
import org.seedstack.hub.domain.model.user.UserId;
import org.seedstack.seed.it.AbstractSeedIT;
import org.seedstack.seed.security.WithUser;

import javax.inject.Inject;
import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class ImportIT extends AbstractSeedIT {
    @Inject
    private ImportService importService;

    @Test
    @WithUser(id = "adrienlauer", password = "password")
    public void basic_import() throws Exception {
        Component component = importService.importComponent(new File("src/test/resources/components/component1"));
        assertThat(component.getComponentId().getName()).isEqualTo("component1");

        assertThat(component.getVersions()).containsExactly(new Version(new VersionId(1, 0, 0, null)));
        assertThat(component.getVersions().get(0).getPublicationDate()).isEqualTo(LocalDate.of(2016, 3, 8));
        assertThat(component.getVersions().get(0).getUrl()).isEqualTo("http://fake.url.org/component-1");

        assertThat(component.getOwner()).isEqualTo(new UserId("adrienlauer"));
        assertThat(component.getDescription().getName()).isEqualTo("component1");
        assertThat(component.getDescription().getIcon()).isEqualTo(buildDocumentId("component1", "images/icon.png"));
        assertThat(component.getDescription().getImages()).containsExactly(buildDocumentId("component1", "images/screenshot-1.png"), buildDocumentId("component1", "images/screenshot-2.png"), buildDocumentId("component1", "images/screenshot-3.png"));
        assertThat(component.getDescription().getMaintainers()).containsExactly(new UserId("pith"), new UserId("kavi87"));
        assertThat(component.getDescription().getReadme()).isEqualTo(buildDocumentId("component1", "README.md"));
        assertThat(component.getDocs()).containsExactly(buildDocumentId("component1", "docs/intro.md"), buildDocumentId("component1", "docs/integration.md"), buildDocumentId("component1", "docs/usage.md"));
    }


    @Test
    @WithUser(id = "adrienlauer", password = "password")
    public void stream_documents_from_imported_component() throws Exception {
        File directory = new File("src/test/resources/components/component1");
        Component component = importService.importComponent(directory);
        Map<DocumentId, Document> documents = importService.streamDocuments(component, directory).collect(toMap(Document::getDocumentId, Function.identity()));

        assertThat(documents.get(buildDocumentId("component1", "README.md"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "docs/intro.md"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "docs/integration.md"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "docs/usage.md"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "images/icon.png"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "images/screenshot-1.png"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "images/screenshot-2.png"))).isNotNull();
        assertThat(documents.get(buildDocumentId("component1", "images/screenshot-3.png"))).isNotNull();
    }


    private DocumentId buildDocumentId(String componentId, String path) {
        return new DocumentId(new ComponentId(componentId), path);
    }
}
