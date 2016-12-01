/*
 * Copyright 2016 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.pipeline.io.mappers;

import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.VariantAggregatedVcfFactory;
import org.opencb.biodata.models.variant.VariantSource;
import org.opencb.biodata.models.variant.VariantVcfEVSFactory;
import org.opencb.biodata.models.variant.VariantVcfExacFactory;
import org.opencb.biodata.models.variant.VariantVcfFactory;
import org.spockframework.util.Assert;
import org.springframework.batch.item.file.LineMapper;

import java.util.List;

/**
 * Maps a String (in VCF format, with aggregated samples) to a list of variants.
 * <p>
 * The actual implementation is reused from {@link VariantVcfFactory}.
 */
public class AggregatedVcfLineMapper implements LineMapper<List<Variant>> {
    private final VariantSource source;

    private VariantVcfFactory factory;

    public AggregatedVcfLineMapper(VariantSource source) {
        this.source = source;
        switch (source.getAggregation()) {
            case EVS:
                factory = new VariantVcfEVSFactory();
                break;
            case EXAC:
                factory = new VariantVcfExacFactory();
                break;
            case BASIC:
                factory = new VariantAggregatedVcfFactory();
                break;
            case NONE:
                factory = null;
                throw new IllegalArgumentException(
                        this.getClass().getSimpleName() + " should not take non-aggregated " +
                                "VCFs, but the VariantSource is marked as Aggregation.NONE");
        }
    }

    @Override
    public List<Variant> mapLine(String line, int lineNumber) throws Exception {
        Assert.notNull(factory, "It is not allowed to use " + this.getClass().getSimpleName()
                + " with non-aggregated VCFs (hint: do not set VariantSource.Aggregation to NONE");
        return factory.create(source, line);
    }
}
