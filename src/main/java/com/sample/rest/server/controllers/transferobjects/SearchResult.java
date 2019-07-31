package com.sample.rest.server.controllers.transferobjects;

import com.sample.rest.server.domain.valueobject.PricedExperienceVO;

import javax.json.bind.annotation.JsonbProperty;
import java.util.HashMap;
import java.util.List;

public class SearchResult {

    @JsonbProperty("search")
    private HashMap<String, Object> searchedParameters;
    private Result results;

    public SearchResult(HashMap<String, Object> searchedParameters, List<PricedExperienceVO> searchResult) {
        this.searchedParameters = searchedParameters;
        this.results = new Result(searchResult);
    }

    public HashMap<String, Object> getSearchedParameters() {
        return searchedParameters;
    }

    public Result getResults() {
        return results;
    }

    public class Result {

        @JsonbProperty("items")
        private List<PricedExperienceVO> searchResult;
        private Integer matching;

        public Result(List<PricedExperienceVO> searchResult) {
            this.searchResult = searchResult;
        }

        public List<PricedExperienceVO> getSearchResult() {
            return searchResult;
        }

        public Integer getMatching() {
            return searchResult.size();
        }
    }
}
