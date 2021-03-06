/*
 * Copyright 2015-2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.pipeline.parameters.validation.job;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.test.rules.PipelineTemporaryFolderRule;

import java.util.Map;
import java.util.TreeMap;

/**
 * Tests that the arguments necessary to run a {@link uk.ac.ebi.eva.pipeline.jobs.DropStudyJob} are
 * correctly validated
 */
public class DropStudyJobParametersValidatorTest {

    private DropStudyJobParametersValidator validator;

    @Rule
    public PipelineTemporaryFolderRule temporaryFolder = new PipelineTemporaryFolderRule();

    private Map<String, JobParameter> requiredParameters;

    private Map<String, JobParameter> optionalParameters;

    @Before
    public void setUp() throws Exception {
        validator = new DropStudyJobParametersValidator();

        requiredParameters = new TreeMap<>();
        requiredParameters.put(JobParametersNames.DB_NAME, new JobParameter("database"));
        requiredParameters.put(JobParametersNames.DB_COLLECTIONS_VARIANTS_NAME, new JobParameter("variants"));
        requiredParameters.put(JobParametersNames.DB_COLLECTIONS_FILES_NAME, new JobParameter("files"));
        requiredParameters.put(JobParametersNames.INPUT_STUDY_ID, new JobParameter("inputStudyId"));

        // optionals
        optionalParameters = new TreeMap<>();
        optionalParameters.put(JobParametersNames.CONFIG_CHUNK_SIZE, new JobParameter("100"));
    }

    // The next tests show behaviour about the required parameters

    @Test
    public void allJobParametersAreValid() throws JobParametersInvalidException {
        Map<String, JobParameter> parameters = new TreeMap<>();
        parameters.putAll(requiredParameters);
        parameters.putAll(optionalParameters);
        validator.validate(new JobParameters(parameters));
    }

    @Test
    public void allRequiredJobParametersAreValid() throws JobParametersInvalidException {
        Map<String, JobParameter> parameters = new TreeMap<>();
        parameters.putAll(requiredParameters);
        validator.validate(new JobParameters(parameters));
    }

    @Test(expected = JobParametersInvalidException.class)
    public void dbNameIsRequired() throws JobParametersInvalidException {
        Map<String, JobParameter> parameters = new TreeMap<>();
        parameters.putAll(requiredParameters);
        parameters.putAll(optionalParameters);
        parameters.remove(JobParametersNames.DB_NAME);
        validator.validate(new JobParameters(parameters));
    }

}
