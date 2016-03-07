/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.rest;

import org.seedstack.business.assembler.BaseAssembler;
import org.seedstack.hub.domain.model.component.Component;

public class CardAssembler extends BaseAssembler<Component, ComponentCard> {
    @Override
    protected void doAssembleDtoFromAggregate(ComponentCard componentCard, Component component) {
        componentCard.setName(component.getEntityId().getName());
    }

    @Override
    protected void doMergeAggregateWithDto(Component component, ComponentCard componentCard) {

    }
}
