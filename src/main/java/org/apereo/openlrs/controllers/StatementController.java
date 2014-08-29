/**
 * Copyright 2014 Unicon (R) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.apereo.openlrs.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apereo.openlrs.model.Statement;
import org.apereo.openlrs.model.StatementResult;
import org.apereo.openlrs.services.StatementService;
import org.apereo.openlrs.utils.StatementUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Controller to handle GET and POST calls
 * see https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#stmtapi
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 *
 */
@RestController
@RequestMapping("/xAPI/statements")
public class StatementController {
	
	private Logger log = Logger.getLogger(StatementController.class);
    private final StatementService statementService;

    @Autowired
    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8", params="statementId")
    public Statement getStatement(@RequestParam(value = "statementId", required = true) String statementId) {
    	return statementService.getStatement(statementId);
    }

    /**
     * Get statement objects for the specified criteria
     * 
     * @param actor the ID of the actor
     * @param activity the activity
     * @return JSON string of the statement objects matching the specified filter
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=utf-8", params="!statementId")
    public StatementResult getStatement(
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "activity", required = false) String activity) {
    	
    	if (log.isDebugEnabled()) {
    		log.debug(String.format("getStatement with actor: %s and activity: %s", actor, activity));
    	}
    	
        Map<String, String> filterMap = StatementUtils.createStatementFilterMap(null, actor, activity);

        return statementService.getStatement(filterMap);
    }

    /**
     * Post a statement
     * 
     * @param requestBody the JSON containing the statement data
     * @return JSON string of the statement object with the specified ID
     * @throws JsonProcessingException 
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
    public List<String> postStatement(@Valid @RequestBody Statement statement) {
        return statementService.postStatement(statement);
    }

}